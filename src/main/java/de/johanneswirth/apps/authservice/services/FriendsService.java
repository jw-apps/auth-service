package de.johanneswirth.apps.authservice.services;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import de.johanneswirth.apps.authservice.FriendsDAO;
import de.johanneswirth.apps.authservice.User;
import de.johanneswirth.apps.common.IStatus;
import de.johanneswirth.apps.common.Secured;
import org.jdbi.v3.core.Jdbi;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import java.util.List;
import java.util.Optional;

import static de.johanneswirth.apps.authservice.ErrorStatus.ALREADY_FRIENDS;
import static de.johanneswirth.apps.authservice.ErrorStatus.FRIENDREQUEST_EXISTS;
import static de.johanneswirth.apps.common.SuccessStatus.OK;
import static de.johanneswirth.apps.common.Utils.userID;

@Path("friends")
public class FriendsService {

    private FriendsDAO dao;

    public FriendsService(Jdbi jdbi) {
        this.dao = jdbi.onDemand(FriendsDAO.class);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    @Valid
    @NotNull
    @Timed
    @ExceptionMetered
    @Path("request")
    public IStatus createFriendRequest(Long other_id, @Context SecurityContext securityContext) {
        long user_id = userID(securityContext);
        Optional<Long> request = dao.existsFriendRequest(other_id, user_id);
        if (request.isPresent()) {
            dao.addFriend(user_id, other_id);
            dao.deleteFriendRequest(other_id, user_id);
            return OK;
        }
        request = dao.existsFriendRequest(user_id, other_id);
        if (request.isPresent()) {
            return FRIENDREQUEST_EXISTS;
        }
        if (dao.areFriends(user_id, other_id)) {
            return ALREADY_FRIENDS;
        }
        dao.addFriendRequest(user_id, other_id);
        return OK;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    @Valid
    @NotNull
    @Timed
    @ExceptionMetered
    public IStatus<List<User>> getFriends(@Context SecurityContext securityContext) {
        long user_id = userID(securityContext);
        return OK(dao.getFriends(user_id));
    }
}
