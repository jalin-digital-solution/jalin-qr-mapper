package co.id.jalin.qrmapper.util;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class LoggingUtil {

    private LoggingUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static void logResponseTime(long startTime, String className, String methodName){
        long duration = System.currentTimeMillis() - startTime;
        log.info("ResponseTime {}.{} {}ms",className,methodName,duration);
    }
}
