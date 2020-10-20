package de.johanneswirth.apps.authservice;

import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlScript;

import java.util.List;

public interface FriendsDAO {

    @SqlScript("insert into friends (user_id_a, user_id_b) VALUES (:user_a, :user_b)")
    @SqlScript("insert into friends (user_id_a, user_id_b) VALUES (:user_b, :user_a)")
    void addFriend(long user_a, long user_b);

    @SqlQuery("select user_id_b from friends where user_id_a = :user_id")
    List<Long> getFriends(long user_id);

    @SqlScript("insert into friendRequests (user_id_a, user_id_b) VALUES (:user_a, :user_b)")
    void addFriendRequest(long user_a, long user_b);
}
