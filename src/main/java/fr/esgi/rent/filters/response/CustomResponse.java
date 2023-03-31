package fr.esgi.rent.filters.response;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;
import static jakarta.ws.rs.core.Response.status;

@ApplicationScoped
public class CustomResponse {

    public Response getUnauthorizedResponse() {
        return status(UNAUTHORIZED)
                .entity("Unauthorized Access")
                .type(APPLICATION_JSON).build();
    }

}
