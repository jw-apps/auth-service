package de.johanneswirth.apps.auth.services

import com.codahale.metrics.annotation.{ExceptionMetered, Timed}
import de.johanneswirth.apps.auth.jdbi.InvitationDAO
import de.johanneswirth.apps.common.{IStatus, Secured}
import de.johanneswirth.apps.common.SuccessStatus.OK
import de.johanneswirth.apps.common.VerificationHelper.userID
import org.jdbi.v3.core.Jdbi

import java.nio.charset.StandardCharsets
import java.util.Random
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.ws.rs.{Consumes, POST, Path, Produces}
import javax.ws.rs.core.{Context, MediaType, SecurityContext}

@Path("invitation")
class InvitationService(jdbi: Jdbi) {
  private val dao = jdbi.onDemand(classOf[InvitationDAO])

  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  @Produces(Array(MediaType.APPLICATION_JSON))
  @Secured
  @Valid
  @NotNull
  @Timed
  @ExceptionMetered def createInvitation(@Context securityContext: SecurityContext): IStatus = {
    val array = new Array[Byte](48)
    new Random().nextBytes(array)
    val generatedString = new String(array, StandardCharsets.UTF_8)
    dao.createInvitation(generatedString, userID(securityContext))
    OK(generatedString)
  }
}