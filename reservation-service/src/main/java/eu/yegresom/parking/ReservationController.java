package eu.yegresom.parking;

import eu.yegresom.parking.integration.ResolvedVariable;
import eu.yegresom.parking.integration.resource.ParkingResource;
import eu.yegresom.parking.integration.resource.UserResource;
import lombok.AllArgsConstructor;
import org.springframework.data.rest.core.support.SelfLinkProvider;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static java.time.LocalDate.now;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

/**
 * Created by Sergii Motynga.
 */
@AllArgsConstructor
@RestController
@RequestMapping("reservation")
@ExposesResourceFor(Reservation.class)
public class ReservationController {

    private ReservationService reservationService;

    private SelfLinkProvider linkProvider;

    @PutMapping("user/{userName}/parking/{parkingNumber}")
    public ResponseEntity<?> putReservation(@ResolvedVariable("userName") UserResource user,
                                            @ResolvedVariable("parkingNumber") ParkingResource parking) {

        Reservation reservation = reservationService.createReservation(now(), user, parking);

        return status(CREATED).location(URI.create(linkProvider.createSelfLinkFor(reservation).getHref())).build();
    }
}
