package eu.yegresom.parking.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.core.MethodParameter;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.context.request.NativeWebRequest;

import static java.util.Collections.singletonMap;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;
import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

/**
 * Created by Sergii Motynga.
 */
@Import(RepositoryRestMvcConfiguration.class)
public abstract class ArgumentResolverTestHelper {

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    @Qualifier("halObjectMapper")
    private ObjectMapper mapper;

    protected ImmutablePair<NativeWebRequest, MethodParameter> mockPathVariable(String value) {
        NativeWebRequest request = mock(NativeWebRequest.class);
        String name = "variableName";
        given(request.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE, SCOPE_REQUEST))
                .willReturn(singletonMap(name, value));

        MethodParameter parameters = mock(MethodParameter.class);
        ResolvedVariable annotation = mock(ResolvedVariable.class);

        given(parameters.getParameterAnnotation(ResolvedVariable.class)).willReturn(annotation);
        given(annotation.value()).willReturn(name);

        return ImmutablePair.of(request, parameters);

    }

    protected void mockResponse(String href, Object response) throws JsonProcessingException {
        String json = mapper.writeValueAsString(response);
        server.expect(requestTo(href)).andRespond(withSuccess(json, HAL_JSON));
    }

    protected Resource<String> emptyResourceWithLink(Link link) {
        return new Resource<>("", link);
    }
}
