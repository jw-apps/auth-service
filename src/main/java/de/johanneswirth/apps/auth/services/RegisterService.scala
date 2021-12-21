package de.johanneswirth.apps.auth.services

import com.codahale.metrics.annotation.{ExceptionMetered, Timed}
import de.johanneswirth.apps.auth.AuthError.{INCORRECT_INVITATION, USER_EXISTS}
import de.johanneswirth.apps.auth._
import de.johanneswirth.apps.auth.jdbi.{AuthDAO, InvitationDAO}
import de.johanneswirth.apps.common.IStatus
import de.johanneswirth.apps.common.SuccessStatus.OK
import org.jdbi.v3.core.Jdbi

import javax.validation.Valid
import javax.validation.constraints.{NotEmpty, NotNull}
import javax.ws.rs._
import javax.ws.rs.core.MediaType


@Path("register") class RegisterService(val jdbi: Jdbi, var utils: PasswordUtils, var config: AuthConfiguration) {
  private var authDAO = jdbi.onDemand(classOf[AuthDAO])
  private var invitationDAO = jdbi.onDemand(classOf[InvitationDAO])

  @POST
  @Consumes(Array(Array(MediaType.APPLICATION_JSON)))
  @Produces(Array(Array(MediaType.APPLICATION_JSON)))
  @Valid
  @NotNull
  @Timed
  @ExceptionMetered
  def register(@Valid @NotNull user: User): IStatus = {
    if (authDAO.userExists(user.username) != 0) USER_EXISTS
    else {
      val inviter = invitationDAO.checkInvitation(user.invitation)
      if (inviter.isPresent) {
        invitationDAO.deleteInvitation(user.invitation)
        val salt = utils.getSalt(30)
        val password = utils.generateSecurePassword(user.password, salt)
        authDAO.registerUser(user.username, user.email, password, salt)
        val user_id = authDAO.getID(user.username)
        //TODO: friendsDAO.addFriend(user_id, inviter.get)
        OK(utils.generateJWTToken(user_id))
      }
      else if (!config.isRequireInvitation) {
        val salt = utils.getSalt(30)
        val password = utils.generateSecurePassword(user.password, salt)
        authDAO.registerUser(user.username, user.email, password, salt)
        val user_id = authDAO.getID(user.username)
        OK(utils.generateJWTToken(user_id))
      }
      else INCORRECT_INVITATION
    }
  }

  @OPTIONS def options(): Unit = {
  }

  private case class User(@NotEmpty username: String, @NotEmpty email: String, @NotEmpty password: String, @NotEmpty invitation: String)
}