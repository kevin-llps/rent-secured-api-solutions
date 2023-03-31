package fr.esgi.rent.filters;

import fr.esgi.rent.filters.response.CustomResponse;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;

import static jakarta.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationFilterTest {

    private static final String RENT_API_OWNERS_URI = "/rent-api/api/owners";

    @Mock
    private ContainerRequestContext requestContext;

    @Mock
    private CustomResponse customResponse;

    @InjectMocks
    private AuthenticationFilter authenticationFilter;

    @Test
    void shouldFilterOnProtectedURI() {
        String authorizationHeaderValue = "Basic dXNlcjE6cGFzc3dvcmQx";

        UriInfo uriInfo = mock(UriInfo.class);
        URI uri = mock(URI.class);

        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(uriInfo.getRequestUri()).thenReturn(uri);
        when(uri.getPath()).thenReturn(RENT_API_OWNERS_URI);
        when(requestContext.getHeaderString(AUTHORIZATION)).thenReturn(authorizationHeaderValue);

        authenticationFilter.filter(requestContext);

        verify(requestContext).getUriInfo();
        verify(requestContext).getHeaderString(AUTHORIZATION);

        verifyNoMoreInteractions(requestContext);
        verifyNoInteractions(customResponse);
    }

    @Test
    void givenUriNotMatchingWithOwners_shouldFilter() {
        UriInfo uriInfo = mock(UriInfo.class);
        URI uri = mock(URI.class);

        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(uriInfo.getRequestUri()).thenReturn(uri);
        when(uri.getPath()).thenReturn("/rent-api/api/rental-properties");

        authenticationFilter.filter(requestContext);

        verify(requestContext).getUriInfo();

        verifyNoMoreInteractions(requestContext);
        verifyNoInteractions(customResponse);
    }

    @Test
    void whenAuthorizationHeaderIsMissing_shouldAbortWithMockedResponse() {
        Response mockedResponse = mock(Response.class);
        UriInfo uriInfo = mock(UriInfo.class);
        URI uri = mock(URI.class);

        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(uriInfo.getRequestUri()).thenReturn(uri);
        when(uri.getPath()).thenReturn(RENT_API_OWNERS_URI);
        when(customResponse.getUnauthorizedResponse()).thenReturn(mockedResponse);

        authenticationFilter.filter(requestContext);

        verify(requestContext).getUriInfo();
        verify(requestContext).getHeaderString(AUTHORIZATION);
        verify(requestContext).abortWith(mockedResponse);
        verify(customResponse).getUnauthorizedResponse();

        verifyNoMoreInteractions(requestContext, customResponse);
    }

    @ParameterizedTest
    @ValueSource(strings = {"dXNlcjE6cGFzc3dvcmQx", "Bearer dXNlcjE6cGFzc3dvcmQx"})
    void whenAuthorizationHeaderHasNotBasicPrefix_shouldAbortWithMockedResponse(String authorizationHeaderValue) {
        Response mockedResponse = mock(Response.class);
        UriInfo uriInfo = mock(UriInfo.class);
        URI uri = mock(URI.class);

        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(uriInfo.getRequestUri()).thenReturn(uri);
        when(uri.getPath()).thenReturn(RENT_API_OWNERS_URI);
        when(requestContext.getHeaderString(AUTHORIZATION)).thenReturn(authorizationHeaderValue);
        when(customResponse.getUnauthorizedResponse()).thenReturn(mockedResponse);

        authenticationFilter.filter(requestContext);

        verify(requestContext).getUriInfo();
        verify(requestContext).getHeaderString(AUTHORIZATION);
        verify(requestContext).abortWith(mockedResponse);
        verify(customResponse).getUnauthorizedResponse();

        verifyNoMoreInteractions(requestContext, customResponse);
    }

    @Test
    void whenUserIsNotValid_shouldAbortWithMockedResponse() {
        String authorizationHeaderValue = "Basic dXNlck5vdFZhbGlkOnBhc3N3b3Jk";

        Response mockedResponse = mock(Response.class);
        UriInfo uriInfo = mock(UriInfo.class);
        URI uri = mock(URI.class);

        when(requestContext.getUriInfo()).thenReturn(uriInfo);
        when(uriInfo.getRequestUri()).thenReturn(uri);
        when(uri.getPath()).thenReturn(RENT_API_OWNERS_URI);
        when(requestContext.getHeaderString(AUTHORIZATION)).thenReturn(authorizationHeaderValue);
        when(customResponse.getUnauthorizedResponse()).thenReturn(mockedResponse);

        authenticationFilter.filter(requestContext);

        verify(requestContext).getUriInfo();
        verify(requestContext).getHeaderString(AUTHORIZATION);
        verify(requestContext).abortWith(mockedResponse);
        verify(customResponse).getUnauthorizedResponse();

        verifyNoMoreInteractions(requestContext, customResponse);
    }

}