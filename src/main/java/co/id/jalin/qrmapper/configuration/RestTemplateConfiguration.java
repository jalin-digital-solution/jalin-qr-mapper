package co.id.jalin.qrmapper.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    @Value("${esb.http.outgoing.readTimeout}")
    private int readTimeout;
    @Value("${esb.http.outgoing.connectionTimeout}")
    private int connectionTimeout;

    @Bean(name = "restTemplateEsb")
    public RestTemplate restTemplateEsb() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectionTimeout);
        factory.setReadTimeout(readTimeout);
        return new RestTemplate(factory);
    }
}
