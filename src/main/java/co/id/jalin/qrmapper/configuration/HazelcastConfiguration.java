package co.id.jalin.qrmapper.configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static co.id.jalin.qrmapper.util.constant.GeneralConstant.*;

@Configuration
@EnableCaching
@Log4j2
public class HazelcastConfiguration {

    @Value(value = "${hazelcast.cluster.name}")
    private String clusterName;
    @Value(value = "${hazelcast.host.pair}")
    private String hostPair;

    @Bean
    public Config hazelcastConfig() {

        Config config = new Config();
        config.setClusterName(clusterName);

        config.addMapConfig(new MapConfig(VAR_APP_PARAM));
        config.addMapConfig(new MapConfig(VAR_APP_PARAM_VALUE));
        config.addMapConfig(new MapConfig(VAR_CRED_DATA_BY_USER_PASS));
        config.addMapConfig(new MapConfig(VAR_CRED_DATA_BY_CRED_ID));
        config.addMapConfig(new MapConfig(VAR_RC_MAPPING_JALIN_TO_DANA));
        config.addMapConfig(new MapConfig(VAR_RC_MAPPING_DANA_TO_JALIN));

        NetworkConfig network = config.getNetworkConfig();
        JoinConfig join = network.getJoin();

        join.getMulticastConfig().setEnabled(false);
        join.getTcpIpConfig().setEnabled(true);

        for (String host : hostPair.split(SEMICOLON_SEPARATOR)) {
            join.getTcpIpConfig().addMember(host);
            log.info("Hazelcast member added: {}", host);
        }

        return config;
    }

    @Bean
    public HazelcastInstance hazelcastInstance(Config config) {
        return Hazelcast.newHazelcastInstance(config);
    }

    @Bean
    public CacheManager cacheManager(HazelcastInstance instance) {
        return new HazelcastCacheManager(instance);
    }
}
