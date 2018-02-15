package eu.yegresom.parking.integration.amqp;

import lombok.Setter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.rest.core.support.SelfLinkProvider;

/**
 * Created by Sergii Motynga.
 */
@Configuration
@ConfigurationProperties(prefix = "reservation.amqp")
@Profile("amqp")
public class AmqpConfiguration {

    @Setter
    private String exchange;

    @Bean
    public FanoutExchange reservationExchange() {
        return new FanoutExchange(exchange);
    }

    @Bean
    public ReservationEventAdaptor reservationEventAdaptor(FanoutExchange exchange, RabbitTemplate rabbitTemplate,
                                                           SelfLinkProvider linkProvider) {
        return new ReservationEventAdaptor(exchange, rabbitTemplate, linkProvider);
    }
}
