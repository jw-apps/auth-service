package de.johanneswirth.apps.authservice;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlScript;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

public interface FriendsDAO {

    @SqlScript("insert into friends (user_id_a, user_id_b) values (:user_a, :user_b)")
    @SqlScript("insert into friends (user_id_a, user_id_b) values (:user_b, :user_a)")
    void addFriend(long user_a, long user_b);

    @SqlQuery("select users.id, users.username from friends join users on users.id = friends.user_id_b where user_id_a = :user_id")
    @RegisterConstructorMapper(User.class)
    List<User> getFriends(long user_id);

    @SqlUpdate("insert into friendRequests (user_id_a, user_id_b) values (:user_a, :user_b)")
    void addFriendRequest(long user_a, long user_b);

    @SqlUpdate("select user_id_b from friendRequests where user_id_a = :user_id_a and user_id_b = :user_id_b")
    Optional<Long> existsFriendRequest(long user_id_a, long user_id_b);

    @SqlUpdate("delete from friendRequests where user_id_a = :user_id_a and user_id_b = :user_id_b")
    void deleteFriendRequest(long user_id_a, long user_id_b);

    @SqlQuery("select * from friends where user_id_a = :user_id_a and user_id_b = :user_id_b")
    boolean areFriends(long user_id_a, long user_id_b);
}
