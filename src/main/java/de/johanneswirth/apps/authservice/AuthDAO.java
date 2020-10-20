package de.johanneswirth.apps.authservice;

import org.apache.commons.lang3.tuple.Pair;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface AuthDAO {

    @SqlQuery("select id from users where username = :username")
    long getID(String username);

    @SqlQuery("select password, salt from users where username = :username")
    Pair<String, String> getPasswordHash(String username);

    @SqlQuery("select count(*) from users where username = :username")
    int userExists(String username);

    @SqlUpdate("insert into users (username, email, password, salt) values (:username, :email, :password, :salt)")
    void registerUser(String username, String email, String password, String salt);

    @SqlUpdate("update users set password = :password, salt = :salt where id = :id")
    void setPassword(String password, String salt, int id);
}
