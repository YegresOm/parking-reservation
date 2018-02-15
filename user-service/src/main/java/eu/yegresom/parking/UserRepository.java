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
@RepositoryRestResource(path = "user", collectionResourceRel = "user")
public interface UserRepository extends MongoRepository<User, String> {

    @RestResource(path = "name", rel = "by-name-like")
    Page<User> findByNameLike(@Param("name") String name, Pageable pageable);
}
