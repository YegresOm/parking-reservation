package eu.yegresom.parking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import static java.util.Collections.singletonList;

@SpringBootApplication
@EnableEurekaClient
public class ReservationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationServiceApplication.class, args);
    }

    /**
     * This RestTemplate is prepared to use HATEOAS together with Ribbon and Eureka.
     * X-Forwarded-Host header is used for set correct domain within HAL links
     * for {@link org.springframework.hateoas.client.Traverson}
     *
     * The problem is that after first request generated links will have service real host but not Ribbon service id
     * and next traverson call will fail because Ribbon won't be possible to resolve it
     */
    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(singletonList((ClientHttpRequestInterceptor) (request, body, execution) -> {
            request.getHeaders().add("X-Forwarded-Host", request.getURI().getHost());
            return execution.execute(request, body);

        }));
        return restTemplate;
    }
}
