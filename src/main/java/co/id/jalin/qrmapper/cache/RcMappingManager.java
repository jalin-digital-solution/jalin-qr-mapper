package co.id.jalin.qrmapper.cache;

import co.id.jalin.qrmapper.entity.RcMapping;
import co.id.jalin.qrmapper.repository.RcMappingRepository;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import static co.id.jalin.qrmapper.util.constant.GeneralConstant.*;

@Log4j2
@Component
@RequiredArgsConstructor
public class RcMappingManager {

    private final HazelcastInstance hazelcastInstance;
    private final RcMappingRepository rcMappingRepository;

    @Getter
    private IMap<String, RcMapping> rcMappingIMapJalinToDana;
    @Getter
    private IMap<String, RcMapping> rcMappingIMapDanaToJalin;

    @PostConstruct
    public void loadCache() {
        log.info("Initializing RcMapping cache");

        this.rcMappingIMapJalinToDana = hazelcastInstance.getMap(VAR_RC_MAPPING_JALIN_TO_DANA);
        this.rcMappingIMapDanaToJalin = hazelcastInstance.getMap(VAR_RC_MAPPING_DANA_TO_JALIN);

        reload();

        log.info("rcMappingIMapJalinToDana cache loaded: {} entries", rcMappingIMapJalinToDana.size());
        log.info("rcMappingIMapDanaToJalin cache loaded: {} entries", rcMappingIMapDanaToJalin.size());
    }

    public void reload() {
        rcMappingIMapJalinToDana.clear();
        rcMappingIMapDanaToJalin.clear();

        rcMappingRepository.findAll().forEach(rc -> {
            switch (rc.getServiceCode()) {
                case QR_DOM_MPM_ESB_TO_DANA ->
                        rcMappingIMapJalinToDana.put(rc.getInputRc(), rc);
                case QR_DOM_MPM_DANA_TO_ESB ->
                        rcMappingIMapDanaToJalin.put(rc.getInputRc(), rc);
                default -> log.info("Service code not implemented {}",rc.getServiceCode());
            }
        });
    }


}
