package eu.yegresom.parking;

import eu.yegresom.parking.integration.ResolverConfiguration;
import eu.yegresom.parking.integration.resource.ParkingResource;
import eu.yegresom.parking.integration.resource.UserResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.rest.core.support.SelfLinkProvider;
import org.springframework.hateoas.Link;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.time.LocalDate;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ReservationController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ResolverConfiguration.class))
public class ReservationControllerTest {


    @Autowired
    private MockMvc mvc;

    @Autowired
    RequestMappingHandlerMapping handlerMapping;

    @MockBean
    private ReservationService service;

    @MockBean
    private SelfLinkProvider linkProvider;

    @Test
    public void shouldReturnCorrectLocationHeader() throws Exception {
        //given
        Reservation reservation = new Reservation();
        Link reservationLink = new Link("http://service/reservation/id");

        given(service.createReservation(any(LocalDate.class), any(UserResource.class), any(ParkingResource.class)))
                .willReturn(reservation);
        given(linkProvider.createSelfLinkFor(reservation)).willReturn(reservationLink);

        //then
        mvc.perform(put("/reservation/user/{name}/parking/{number}", "User", "12345"))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, reservationLink.getHref()));

    }

}
