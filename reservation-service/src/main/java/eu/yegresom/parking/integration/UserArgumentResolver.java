package eu.yegresom.parking.integration;

import eu.yegresom.parking.integration.resource.UserResource;
import lombok.AllArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.mvc.TypeReferences;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.singletonMap;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;
import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

/**
 * Created by Sergii Motynga.
 */
@AllArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    public static final URI USER_SERVICE_URL = URI.create("http://user-service");

    public static final String USER_REL = "user";
    public static final String SEARCH_REL = "search";
    public static final String BY_NAME_LIKE_REL = "by-name-like";
    public static final String NAME_REQUEST_PARAM_NAME = "name";

    private RestTemplate restTemplate;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(ResolvedVariable.class)
                && methodParameter.getParameterType().equals(UserResource.class);
    }

    @Override
    public UserResource resolveArgument(MethodParameter methodParameter,
                                        ModelAndViewContainer modelAndViewContainer,
                                        NativeWebRequest nativeWebRequest,
                                        WebDataBinderFactory webDataBinderFactory) throws Exception {

        Map attributes = (Map) nativeWebRequest.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE, SCOPE_REQUEST);
        Object pathVariable = attributes.get(methodParameter.getParameterAnnotation(ResolvedVariable.class).value());

        Optional<UserResource> user = new Traverson(USER_SERVICE_URL, MediaTypes.HAL_JSON)
                .setRestOperations(restTemplate)
                .follow("user", "search", "by-name-like")
                .withTemplateParameters(singletonMap("name", pathVariable))
                .toObject(new TypeReferences.PagedResourcesType<UserResource>() {
                }).getContent().stream()
                .filter(u -> u.getName().equalsIgnoreCase(pathVariable.toString())).findAny();

        return user.orElseThrow(ResourceNotFoundException::new);
    }
}
