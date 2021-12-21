package de.johanneswirth.apps.auth.services

import com.codahale.metrics.annotation.{ExceptionMetered, Timed}
import de.johanneswirth.apps.auth.PasswordUtils
import de.johanneswirth.apps.auth.jdbi.AuthDAO
import de.johanneswirth.apps.common.CommonError.AUTHENTICATION_ERROR
import de.johanneswirth.apps.common.IStatus
import de.johanneswirth.apps.common.SuccessStatus.OK
import org.jdbi.v3.core.Jdbi

import javax.validation.Valid
import javax.validation.constraints.{NotEmpty, NotNull}
import javax.ws.rs._
import javax.ws.rs.core.MediaType

@Path("login")
class LoginService(val jdbi: Jdbi, var utils: PasswordUtils) {
  private val dao = jdbi.onDemand(classOf[AuthDAO])

  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Valid
  @NotNull
  @Timed
  @ExceptionMetered
  def login(@NotNull @Valid user: User): IStatus = {
    if (dao.userExists(user.username) == 0) AUTHENTICATION_ERROR
    else {
      val password = dao.getPasswordHash(user.username)
      if (utils.verifyUserPassword(user.password, password.getLeft, password.getRight)) OK(utils.generateJWTToken(dao.getID(user.username)))
      else AUTHENTICATION_ERROR
    }
  }

  @OPTIONS def options() = {
  }

  private case class User(@NotEmpty username: String, @NotEmpty password: String)
}