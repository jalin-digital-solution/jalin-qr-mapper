package co.id.jalin.qrmapper.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Log4j2
@Configuration
public class ObservationConfiguration {

    @Bean
    public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
        return new ObservedAspect(observationRegistry);
    }

    @Bean
    public ObservationHandler<Observation.Context> observationTextPublisher(MeterRegistry meterRegistry) {
        return new ObservationHandler<>() {

            @Override
            public void onStart(Observation.Context context) {
                Timer.Sample sample = Timer.start(meterRegistry);
                context.put("timer.sample", sample);
//                log.info("START - {} - {}", context.getName(), getSimpleName(context));
            }

            @Override
            public void onStop(Observation.Context context) {
                Timer.Sample sample = context.get("timer.sample");
                long durationMs = 0;

                if (sample != null) {
                    Timer timer = Timer.builder(context.getName() + ".duration")
                            .description("Duration of " + context.getName())
                            .register(meterRegistry);

                    long durationNanos = sample.stop(timer);
                    durationMs = TimeUnit.NANOSECONDS.toMillis(durationNanos);
                }
                log.info("{} - {} - Duration: {} ms", context.getName(), getSimpleName(context), durationMs);

//                log.info("STOP - {} - {} - Duration: {} ms", context.getName(), getSimpleName(context), durationMs);
            }

            @Override
            public boolean supportsContext(Observation.Context context) {
                return true;
            }

            private String getSimpleName(Observation.Context context) {
                var classValue = context.getLowCardinalityKeyValue("class");
                var methodValue = context.getLowCardinalityKeyValue("method");

                if (classValue != null && methodValue != null) {
                    return String.format("%s.%s", classValue.getValue(), methodValue.getValue());
                }
                return context.getName();
            }
        };
    }
}
