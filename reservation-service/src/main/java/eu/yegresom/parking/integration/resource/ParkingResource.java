package eu.yegresom.parking.integration.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

/**
 * Created by Sergii Motynga.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ParkingResource extends ResourceSupport {
    private String number;
}
