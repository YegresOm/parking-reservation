package eu.yegresom.parking.integration;

import eu.yegresom.parking.integration.resource.ParkingResource;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.MethodParameter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.UriTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.NativeWebRequest;

import static eu.yegresom.parking.integration.ParkingArgumentResolver.*;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.Resources.wrap;
import static org.springframework.hateoas.TemplateVariable.VariableType.REQUEST_PARAM;

/**
 * Created by Sergii Motynga.
 */
@RunWith(SpringRunner.class)
@RestClientTest(ParkingArgumentResolver.class)
public class ParkingArgumentResolverTest extends ArgumentResolverTestHelper {

    @TestConfiguration
    static class Config {
        @Bean
        ParkingArgumentResolver resolver(RestTemplateBuilder builder) {
            return new ParkingArgumentResolver(builder.build());
        }
    }

    @Autowired
    private ParkingArgumentResolver argumentResolver;

    @Test
    public void shouldCorrectResolveParkingResource() throws Exception {
        //given path variable mock
        String pathVariableValue = "parking number";
        ImmutablePair<NativeWebRequest, MethodParameter> mocks = super.mockPathVariable(pathVariableValue);

        //given first hop response
        Link parkingLink = new Link("http://parking", PARKING_REL);
        super.mockResponse(PARKING_SERVICE_URI.toString(), super.emptyResourceWithLink(parkingLink));

        //given second hop response
        Link searchLink = new Link("http://search", SEARCH_REL);
        super.mockResponse(parkingLink.getHref(), super.emptyResourceWithLink(searchLink));

        //given third hop response
        Link byNumberLink = new Link(new UriTemplate("http://by-number")
                .with(NUMBER_REQUEST_PARAM_NAME, REQUEST_PARAM), BY_NUMBER_LIKE_REL);
        super.mockResponse(searchLink.getHref(), super.emptyResourceWithLink(byNumberLink));

        //given ParkingResource
        ParkingResource parkingResource = new ParkingResource();
        parkingResource.setNumber(pathVariableValue);

        super.mockResponse(byNumberLink.expand(pathVariableValue).getHref(), wrap(singleton(parkingResource)));

        //when
        ParkingResource result = argumentResolver.resolveArgument(mocks.getRight(), null, mocks.getLeft(), null);

        //then
        assertThat(result).isEqualTo(parkingResource);

    }


}
