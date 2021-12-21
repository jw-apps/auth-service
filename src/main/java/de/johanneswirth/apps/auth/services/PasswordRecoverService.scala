package de.johanneswirth.apps.auth.services

import com.codahale.metrics.annotation.ExceptionMetered
import com.codahale.metrics.annotation.Timed
import de.johanneswirth.apps.auth.jdbi.AuthDAO
import de.johanneswirth.apps.auth.PasswordUtils
import de.johanneswirth.apps.common.IStatus
import org.jdbi.v3.core.Jdbi
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.ws.rs._
import javax.ws.rs.core.MediaType
import de.johanneswirth.apps.common.SuccessStatus.OK

@Path("request-pass")
class PasswordRecoverService(val jdbi: Jdbi, var utils: PasswordUtils) {
  private val dao = jdbi.onDemand(classOf[AuthDAO])

  @POST
  @Consumes(Array(Array(MediaType.APPLICATION_JSON)))
  @Produces(Array(Array(MediaType.APPLICATION_JSON)))
  @Valid
  @NotNull
  @Timed
  @ExceptionMetered def resetPass(request: PasswordRecoverService#ResetRequest): IStatus = OK

  private class ResetRequest {
    var email = null
  }

  @OPTIONS def options() = {
  }
}