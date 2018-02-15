package eu.yegresom.parking.integration;

import eu.yegresom.parking.integration.resource.ParkingResource;
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
public class ParkingArgumentResolver implements HandlerMethodArgumentResolver {

    public static final URI PARKING_SERVICE_URI = URI.create("http://parking-service");

    public static final String PARKING_REL = "parking";
    public static final String SEARCH_REL = "search";
    public static final String BY_NUMBER_LIKE_REL = "by-number-like";
    public static final String NUMBER_REQUEST_PARAM_NAME = "number";

    private RestTemplate restTemplate;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(ResolvedVariable.class)
                && methodParameter.getParameterType().equals(ParkingResource.class);
    }

    @Override
    public ParkingResource resolveArgument(MethodParameter methodParameter,
                                           ModelAndViewContainer mavContainer,
                                           NativeWebRequest nativeWebRequest,
                                           WebDataBinderFactory binderFactory) throws Exception {

        Map attributes = (Map) nativeWebRequest.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE, SCOPE_REQUEST);
        Object pathVariable = attributes.get(methodParameter.getParameterAnnotation(ResolvedVariable.class).value());

        Optional<ParkingResource> parking = new Traverson(PARKING_SERVICE_URI, MediaTypes.HAL_JSON)
                .setRestOperations(restTemplate)
                .follow(PARKING_REL, SEARCH_REL, BY_NUMBER_LIKE_REL)
                .withTemplateParameters(singletonMap(NUMBER_REQUEST_PARAM_NAME, pathVariable))
                .toObject(new TypeReferences.PagedResourcesType<ParkingResource>() {
                }).getContent().stream()
                .filter(p -> p.getNumber().equalsIgnoreCase(pathVariable.toString())).findAny();

        return parking.orElseThrow(ResourceNotFoundException::new);
    }
}
