package eu.yegresom.parking.integration.resource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

/**
 * Created by Sergii Motynga.
 */
@Getter
@Setter
public class UserResource extends ResourceSupport {
    private String name;
}
