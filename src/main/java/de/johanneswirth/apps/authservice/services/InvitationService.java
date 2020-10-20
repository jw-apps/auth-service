package de.johanneswirth.apps.authservice.services;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import de.johanneswirth.apps.authservice.AuthDAO;
import de.johanneswirth.apps.authservice.InvitationDAO;
import de.johanneswirth.apps.authservice.PasswordUtils;
import de.johanneswirth.apps.common.IStatus;
import de.johanneswirth.apps.common.Secured;
import org.jdbi.v3.core.Jdbi;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static de.johanneswirth.apps.common.SuccessStatus.OK;

@Path("invitation")
public class InvitationService {

    private InvitationDAO dao;

    public InvitationService(Jdbi jdbi) {
        this.dao = jdbi.onDemand(InvitationDAO.class);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    @Valid
    @NotNull
    @Timed
    @ExceptionMetered
    public IStatus<String> createInvitation(@Context SecurityContext securityContext) {
        byte[] array = new byte[48];
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);
        dao.createInvitation(generatedString, Long.parseLong(securityContext.getUserPrincipal().getName()));
        return OK(generatedString, System.currentTimeMillis());
    }
}
