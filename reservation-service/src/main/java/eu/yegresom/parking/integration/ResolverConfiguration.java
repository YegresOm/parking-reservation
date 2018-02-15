package eu.yegresom.parking.integration;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Created by Sergii Motynga.
 */
@AllArgsConstructor
@Configuration
public class ResolverConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(new UserArgumentResolver(restTemplate));
        argumentResolvers.add(new ParkingArgumentResolver(restTemplate));
    }
}
