package de.johanneswirth.apps.authservice.services;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import de.johanneswirth.apps.authservice.AuthDAO;
import de.johanneswirth.apps.authservice.PasswordUtils;
import de.johanneswirth.apps.common.IStatus;
import de.johanneswirth.apps.common.Secured;
import org.jdbi.v3.core.Jdbi;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import static de.johanneswirth.apps.common.SuccessStatus.OK;

@Path("change-pass")
public class PasswordChangeService {

    private AuthDAO dao;
    private PasswordUtils utils;

    public PasswordChangeService(Jdbi jdbi, PasswordUtils utils) {
        this.dao = jdbi.onDemand(AuthDAO.class);
        this.utils = utils;
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Secured
    @Valid
    @NotNull
    @Timed
    @ExceptionMetered
    public IStatus register(@NotNull @Valid Password password, @Context SecurityContext securityContext) {
        String salt = utils.getSalt(30);
        String pass = utils.generateSecurePassword(password.password, salt);
        dao.setPassword(pass, salt, Integer.parseInt(securityContext.getUserPrincipal().getName()));
        return OK;
    }

    @OPTIONS
    public void options() {}

    private class Password {
        public String password;

        public Password() { }
    }
}
