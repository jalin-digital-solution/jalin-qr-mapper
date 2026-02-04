package co.id.jalin.qrmapper.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.function.Function;

@Log4j2
public class SortObjectComparator<T, U> implements Comparator<T> {

    private final Sort sort;
    private final Function<T, U> valueExtractor;

    public SortObjectComparator(Sort sort, Function<T, U> valueExtractor) {
        this.sort = sort;
        this.valueExtractor = valueExtractor;
    }

    @Override
    public int compare(T o1, T o2) {
        return sort.stream()
                .findFirst()
                .map(order -> compareField(o1, o2, order.getProperty(), order.getDirection()))
                .orElse(0);
    }

    private int compareField(T o1, T o2, String fieldName, Sort.Direction direction) {
        try {
            Comparable<Object> fieldValue1 = getFieldValue(valueExtractor.apply(o1), fieldName);
            Comparable<Object> fieldValue2 = getFieldValue(valueExtractor.apply(o2), fieldName);

            if (fieldValue1 == null || fieldValue2 == null) {
                return 0;
            }

            int result = fieldValue1.compareTo(fieldValue2);
            return direction == Sort.Direction.ASC ? result : -result;
        } catch (Exception e) {
            log.error(e);
            return 0;
        }
    }

    private Comparable<Object> getFieldValue(U obj, String fieldName) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        if (obj == null) {
            return null;
        }

        PropertyDescriptor pd = new PropertyDescriptor(fieldName, obj.getClass());
        Method getter = pd.getReadMethod();

        if (getter == null) {
            return null;
        }

        return (Comparable<Object>) getter.invoke(obj);
    }
}
