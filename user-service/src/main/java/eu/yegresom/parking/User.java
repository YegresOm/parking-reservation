package eu.yegresom.parking;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.remoting.support.RemotingSupport;

/**
 * Created by Sergii Motynga.
 */
@Document
@Data
@NoArgsConstructor
public class User {
    private String id, name;
}
