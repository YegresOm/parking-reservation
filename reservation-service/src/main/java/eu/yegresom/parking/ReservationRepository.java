package eu.yegresom.parking;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by Sergii Motynga.
 */
@RepositoryRestResource(path = "reservation", collectionResourceRel = "reservation")
public interface ReservationRepository extends MongoRepository<Reservation, String> {
}
