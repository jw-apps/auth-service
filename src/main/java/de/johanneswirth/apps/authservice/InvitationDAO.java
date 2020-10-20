package de.johanneswirth.apps.authservice;

import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;

public interface InvitationDAO {

    @SqlQuery("select user_id from invitations where invitation = :invitation")
    Optional<Long> checkInvitation(String invitation);

    @SqlUpdate("delete from invitations where invitation = :invitation")
    void deleteInvitation(String invitation);

    @SqlUpdate("insert into invitations (invitation, user_id) values (:invitation, :user_id)")
    void createInvitation(String invitation, long user_id);
}
