package eu.yegresom.parking;

import eu.yegresom.parking.integration.resource.ParkingResource;
import eu.yegresom.parking.integration.resource.UserResource;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Created by Sergii Motynga.
 */
@Component
@AllArgsConstructor
public class ReservationService {

    private ReservationRepository repository;

    public Reservation createReservation(LocalDate date, UserResource user, ParkingResource parking) {
        // TODO sergii motynga: throw forbidden if exist with same date and parking
        return repository.save(new Reservation(date, user.getId(), parking.getId()));
    }
}
