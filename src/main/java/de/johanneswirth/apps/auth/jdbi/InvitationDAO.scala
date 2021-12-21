package de.johanneswirth.apps.auth.jdbi

import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import java.util.Optional

trait InvitationDAO {
  @SqlQuery("select user_id from invitations where invitation = :invitation")
  def checkInvitation(invitation: String): Optional[Long]

  @SqlUpdate("delete from invitations where invitation = :invitation")
  def deleteInvitation(invitation: String): Unit

  @SqlUpdate("insert into invitations (invitation, user_id) values (:invitation, :user_id)")
  def createInvitation(invitation: String, user_id: Long): Unit
}