package de.johanneswirth.apps.auth.services

import com.codahale.metrics.annotation.{ExceptionMetered, Timed}
import de.johanneswirth.apps.auth.jdbi.UsersDAO
import de.johanneswirth.apps.common.{IStatus, Secured}
import de.johanneswirth.apps.common.SuccessStatus.OK
import org.jdbi.v3.core.Jdbi

import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.ws.rs._
import javax.ws.rs.core.{Context, MediaType, SecurityContext}

@Path("users") class UsersService(val jdbi: Jdbi) {
  private val dao = jdbi.onDemand(classOf[UsersDAO])

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Secured
  @Valid
  @NotNull
  @Timed
  @ExceptionMetered def findUser(@QueryParam("username") @NotNull username: String, @Context securityContext: SecurityContext): IStatus = {
    val list = dao.findUsers(username)
    OK(list)
  }

  @OPTIONS def options(): Unit = {
  }
}