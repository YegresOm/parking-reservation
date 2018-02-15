package eu.yegresom.parking;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Sergii Motynga.
 */
@Document
@Data
@NoArgsConstructor
public class Parking {

    private String id, number;

}
