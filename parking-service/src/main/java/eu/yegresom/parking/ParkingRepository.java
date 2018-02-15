package eu.yegresom.parking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by Sergii Motynga.
 */
@RepositoryRestResource(path = "parking", collectionResourceRel = "parking")
public interface ParkingRepository extends MongoRepository<Parking, String> {

    @RestResource(path = "number", rel = "by-number-like")
    Page<Parking> findByNumberLike(@Param("number") String number, Pageable pageable);
}
