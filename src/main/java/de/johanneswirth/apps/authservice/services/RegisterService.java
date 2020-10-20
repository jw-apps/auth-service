package de.johanneswirth.apps.authservice.services;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import de.johanneswirth.apps.authservice.AuthDAO;
import de.johanneswirth.apps.authservice.FriendsDAO;
import de.johanneswirth.apps.authservice.InvitationDAO;
import de.johanneswirth.apps.authservice.PasswordUtils;
import de.johanneswirth.apps.common.IStatus;
import org.jdbi.v3.core.Jdbi;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.util.Optional;

import static de.johanneswirth.apps.authservice.ErrorStatus.INCORRECT_INVITATION;
import static de.johanneswirth.apps.authservice.ErrorStatus.USER_EXISTS;
import static de.johanneswirth.apps.common.SuccessStatus.OK;

@Path("register")
public class RegisterService {

    private AuthDAO dao;
    private InvitationDAO invitationDAO;
    private FriendsDAO friendsDAO;
    private PasswordUtils utils;

    public RegisterService(Jdbi jdbi, PasswordUtils utils) {
        this.dao = jdbi.onDemand(AuthDAO.class);
        this.invitationDAO = jdbi.onDemand(InvitationDAO.class);
        this.friendsDAO = jdbi.onDemand(FriendsDAO.class);
        this.utils = utils;
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Valid
    @NotNull
    @Timed
    @ExceptionMetered
    public IStatus<String> register(@Valid @NotNull User user) {
        if (dao.userExists(user.username) != 0) {
            return USER_EXISTS;
        } else {
            Optional<Long> inviter = invitationDAO.checkInvitation(user.invitation);
            if (inviter.isPresent()) {
                invitationDAO.deleteInvitation(user.invitation);
                String salt = utils.getSalt(30);
                String password = utils.generateSecurePassword(user.password, salt);
                dao.registerUser(user.username, user.email, password, salt);
                long user_id = dao.getID(user.username);
                friendsDAO.addFriend(user_id, inviter.get());
                return OK(utils.generateJWTToken(user_id));
            } else {
                return INCORRECT_INVITATION;
            }
        }
    }

    @OPTIONS
    public void options() {}

    private static class User {
        @NotEmpty
        public String username;
        @NotNull
        public String email;
        @NotEmpty
        public String password;
        @NotEmpty
        public String invitation;

        public User() { }
    }

}
