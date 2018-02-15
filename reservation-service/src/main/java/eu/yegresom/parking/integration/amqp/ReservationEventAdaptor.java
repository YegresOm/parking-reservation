package eu.yegresom.parking.integration.amqp;

import eu.yegresom.parking.Reservation;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.rest.core.support.SelfLinkProvider;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

/**
 * Created by Sergii Motynga.
 */
@AllArgsConstructor
public class ReservationEventAdaptor extends AbstractMongoEventListener<Reservation> {

    private FanoutExchange exchange;

    private RabbitTemplate rabbitTemplate;

    private SelfLinkProvider linkProvider;

    @Override
    public void onAfterSave(AfterSaveEvent<Reservation> event) {
        Link reservation = linkProvider.createSelfLinkFor(event.getSource()).withSelfRel();
        rabbitTemplate.convertAndSend(exchange.getName(), "parking.reservation.created", reservation);
    }
}
