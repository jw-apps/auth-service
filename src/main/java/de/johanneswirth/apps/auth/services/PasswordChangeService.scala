package de.johanneswirth.apps.auth.services

import com.codahale.metrics.annotation.{ExceptionMetered, Timed}
import de.johanneswirth.apps.auth.PasswordUtils
import de.johanneswirth.apps.auth.jdbi.AuthDAO
import de.johanneswirth.apps.common.{IStatus, Secured}
import de.johanneswirth.apps.common.SuccessStatus.OK
import org.jdbi.v3.core.Jdbi

import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.ws.rs._
import javax.ws.rs.core.{Context, MediaType, SecurityContext}

@Path("change-pass")
class PasswordChangeService(val jdbi: Jdbi, var utils: PasswordUtils) {
  private val dao = jdbi.onDemand(classOf[AuthDAO])

  @POST
  @Consumes(Array(Array(MediaType.APPLICATION_JSON)))
  @Produces(Array(Array(MediaType.APPLICATION_JSON)))
  @Secured
  @Valid
  @NotNull
  @Timed
  @ExceptionMetered
  def register(@NotNull @Valid password: PasswordChangeService#Password, @Context securityContext: SecurityContext): IStatus = {
    val salt = utils.getSalt(30)
    val pass = utils.generateSecurePassword(password.password, salt)
    dao.setPassword(pass, salt, securityContext.getUserPrincipal.getName.toInt)
    OK
  }

  @OPTIONS def options() = {
  }

  private case class Password(password: String)
}