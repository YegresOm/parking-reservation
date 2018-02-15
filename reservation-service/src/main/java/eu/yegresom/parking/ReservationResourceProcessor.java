package eu.yegresom.parking;

import lombok.AllArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Enumeration;

/**
 * If request was done from outside client doesn't have Ribbon and can't resolve service id
 * Therefore we need to replace id with host for every resource {@link Link}
 * Because this service is behind proxy and all routes will be handled by Zuul
 * we could just replace service id with 'Host' header value.
 * <p/>
 * Be sure that Zuul has <b>zuul.add-host-header: true</b>
 * <p/>
 * Created by Sergii Motynga.
 */
@Component
@AllArgsConstructor
public class ReservationResourceProcessor implements ResourceProcessor<Resource<Reservation>> {

    public static final String HOST = "Host";

    private Provider<HttpServletRequest> requestProvider;

    @Override
    public Resource<Reservation> process(Resource<Reservation> resource) {
        Enumeration<String> headers = requestProvider.get().getHeaders(HOST);

        if (headers.hasMoreElements()) {

            Reservation reservation = resource.getContent();
            String host = headers.nextElement();

            Link user = reservation.getUser();
            reservation.setUser(replaceHost(user, host));

            Link parking = reservation.getParking();
            reservation.setParking(replaceHost(parking, host));

        }
        return resource;
    }

    private Link replaceHost(Link link, String host) {
        String href = link.getHref();
        return new Link(href.replaceFirst(URI.create(href).getHost(), host)).withRel(link.getRel());
    }
}
