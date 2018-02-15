package eu.yegresom.parking.integration;

import eu.yegresom.parking.integration.resource.UserResource;
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

import static eu.yegresom.parking.integration.UserArgumentResolver.*;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.Resources.wrap;
import static org.springframework.hateoas.TemplateVariable.VariableType.REQUEST_PARAM;

/**
 * Created by Sergii Motynga.
 */
@RunWith(SpringRunner.class)
@RestClientTest(UserArgumentResolver.class)
public class UserArgumentResolverTest extends ArgumentResolverTestHelper {

    @TestConfiguration
    static class Config {
        @Bean
        UserArgumentResolver resolver(RestTemplateBuilder builder) {
            return new UserArgumentResolver(builder.build());
        }
    }

    @Autowired
    private UserArgumentResolver argumentResolver;

    @Test
    public void shouldCorrectResolveParkingResource() throws Exception {
        //given path variable mock
        String pathVariableValue = "user name";
        ImmutablePair<NativeWebRequest, MethodParameter> mocks = super.mockPathVariable(pathVariableValue);

        //given first hop response
        Link userLink = new Link("http://user", USER_REL);
        super.mockResponse(USER_SERVICE_URL.toString(), super.emptyResourceWithLink(userLink));

        //given second hop response
        Link searchLink = new Link("http://search", SEARCH_REL);
        super.mockResponse(userLink.getHref(), super.emptyResourceWithLink(searchLink));

        //given third hop response
        Link byNameLink = new Link(new UriTemplate("http://by-name")
                .with(NAME_REQUEST_PARAM_NAME, REQUEST_PARAM), BY_NAME_LIKE_REL);
        super.mockResponse(searchLink.getHref(), super.emptyResourceWithLink(byNameLink));

        //given UserResource
        UserResource userResource = new UserResource();
        userResource.setName(pathVariableValue);

        super.mockResponse(byNameLink.expand(pathVariableValue).getHref(), wrap(singleton(userResource)));

        //when
        UserResource result = argumentResolver.resolveArgument(mocks.getRight(), null, mocks.getLeft(), null);

        //then
        assertThat(result).isEqualTo(userResource);

    }


}
