package co.id.jalin.qrmapper.cache;

import co.id.jalin.qrmapper.entity.ApplicationParameter;
import co.id.jalin.qrmapper.repository.ApplicationParameterRepository;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static co.id.jalin.qrmapper.util.constant.GeneralConstant.VAR_APP_PARAM;
import static co.id.jalin.qrmapper.util.constant.GeneralConstant.VAR_APP_PARAM_VALUE;

@Log4j2
@Component
@RequiredArgsConstructor
public class ApplicationParameterManager {

    private final HazelcastInstance hazelcastInstance;
    private final ApplicationParameterRepository applicationParameterRepository;

    @Getter
    private IMap<String, ApplicationParameter> applicationParameterMap;
    @Getter
    private IMap<String, String> applicationParameterValueMap;

    @PostConstruct
    public void loadCache() {
        log.info("Initializing ApplicationParameter cache");

        this.applicationParameterMap = hazelcastInstance.getMap(VAR_APP_PARAM);
        this.applicationParameterValueMap = hazelcastInstance.getMap(VAR_APP_PARAM_VALUE);

        reload();

        log.info("applicationParameterMap cache loaded: {} entries", applicationParameterMap.size());
        log.info("applicationParameterValueMap cache loaded: {} entries", applicationParameterValueMap.size());
    }

    public void reload() {
        applicationParameterMap.clear();
        applicationParameterValueMap.clear();

        applicationParameterRepository.findAll().forEach(param -> {
            applicationParameterMap.put(param.getName(), param);
            applicationParameterValueMap.put(param.getName(), param.getValue());
        });
    }

    public Optional<ApplicationParameter> getApplicationParameterByName(String nameCode) {
        return Optional.ofNullable(applicationParameterMap.get(nameCode));
    }

    public Optional<String> getApplicationParameterValueByName(String nameCode) {
        return Optional.ofNullable(applicationParameterValueMap.get(nameCode));
    }
}
