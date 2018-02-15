package eu.yegresom.parking;

import lombok.*;
import org.springframework.hateoas.Link;

import java.time.LocalDate;

/**
 * Created by Sergii Motynga.
 */
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Reservation {

    private String id;

    @NonNull
    private LocalDate date;

    @NonNull
    private Link user;

    @NonNull
    private Link parking;
}
