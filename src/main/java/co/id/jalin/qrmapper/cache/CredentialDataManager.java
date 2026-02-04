package co.id.jalin.qrmapper.cache;

import co.id.jalin.qrmapper.entity.CredentialData;
import co.id.jalin.qrmapper.repository.CredentialDataRepository;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static co.id.jalin.qrmapper.util.StringUtil.buildCredDataByCredIdKey;
import static co.id.jalin.qrmapper.util.StringUtil.buildCredDataByUserPassKey;
import static co.id.jalin.qrmapper.util.constant.GeneralConstant.VAR_CRED_DATA_BY_CRED_ID;
import static co.id.jalin.qrmapper.util.constant.GeneralConstant.VAR_CRED_DATA_BY_USER_PASS;

@Log4j2
@Component
@RequiredArgsConstructor
public class CredentialDataManager {

    private final HazelcastInstance hazelcastInstance;
    private final CredentialDataRepository credentialDataRepository;

    @Getter
    private IMap<String, CredentialData> credentialDataIMapByUserPass;
    @Getter
    private IMap<String, CredentialData> credentialDataIMapByCredId;

    @PostConstruct
    public void loadCache() {
        log.info("Initializing CredentialData cache");

        this.credentialDataIMapByUserPass = hazelcastInstance.getMap(VAR_CRED_DATA_BY_USER_PASS);
        this.credentialDataIMapByCredId = hazelcastInstance.getMap(VAR_CRED_DATA_BY_CRED_ID);

        reload();

        log.info("credentialDataIMapByUserPass cache loaded: {} entries", credentialDataIMapByUserPass.size());
        log.info("credentialDataIMapByCredId cache loaded: {} entries", credentialDataIMapByCredId.size());
    }

    public void reload() {
        credentialDataIMapByUserPass.clear();
        credentialDataIMapByCredId.clear();

        credentialDataRepository.findAll().forEach(cred -> {
            credentialDataIMapByUserPass.put(buildCredDataByUserPassKey(cred.getUsername(),cred.getPassword(),cred.getCredentialTarget()), cred);
            credentialDataIMapByCredId.put(buildCredDataByCredIdKey(cred.getCredentialIdentifier(),cred.getCredentialTarget()), cred);
        });
    }

    public Optional<CredentialData> getCredDataByUserPass(String key) {
        return Optional.ofNullable(credentialDataIMapByUserPass.get(key));
    }

    public Optional<CredentialData> getCredDataByCredId(String key) {
        return Optional.ofNullable(credentialDataIMapByCredId.get(key));
    }
}
