package eu.yegresom.parking;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ParkingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkingServiceApplication.class, args);
    }


    @Bean
    CommandLineRunner init(ParkingRepository repository) {
        return args -> {
            Parking parking = new Parking();
            parking.setNumber("12345");
            repository.save(parking);
        };
    }
}
