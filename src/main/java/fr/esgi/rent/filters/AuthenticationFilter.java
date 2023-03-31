package fr.esgi.rent.filters;

import fr.esgi.rent.filters.response.CustomResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

import java.net.URI;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION;

@Provider
class AuthenticationFilter implements ContainerRequestFilter {

    private static final List<String> VALID_USERS = Arrays.asList("user1:password1", "user2:password2", "user3:password3");
    private static final String BASIC_PREFIX = "Basic ";
    private static final String RENT_API_OWNERS_URI = "/rent-api/api/owners";

    private final CustomResponse customResponse;

    @Inject
    public AuthenticationFilter(CustomResponse customResponse) {
        this.customResponse = customResponse;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        URI requestUri = requestContext.getUriInfo().getRequestUri();

        if (!requestUri.getPath().equals(RENT_API_OWNERS_URI)) {
            return;
        }

        String authorizationHeaderValue = requestContext.getHeaderString(AUTHORIZATION);

        if (authorizationHeaderValue == null || !authorizationHeaderValue.startsWith(BASIC_PREFIX)) {
            requestContext.abortWith(customResponse.getUnauthorizedResponse());
            return;
        }

        String token = authorizationHeaderValue.substring(BASIC_PREFIX.length());
        String decodedToken = new String(Base64.getDecoder().decode(token));

        if (!VALID_USERS.contains(decodedToken)) {
            requestContext.abortWith(customResponse.getUnauthorizedResponse());
        }
    }

}