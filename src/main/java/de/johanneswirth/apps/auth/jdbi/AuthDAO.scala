package de.johanneswirth.apps.auth.jdbi

import org.apache.commons.lang3.tuple.Pair
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

trait AuthDAO {
  @SqlQuery("select id from users where username = :username")
  def getID(username: String): Long

  @SqlQuery("select password, salt from users where username = :username")
  def getPasswordHash(username: String): Pair[String, String]

  @SqlQuery("select count(*) from users where username = :username")
  def userExists(username: String): Int

  @SqlUpdate("insert into users (username, email, password, salt) values (:username, :email, :password, :salt)")
  def registerUser(username: String, email: String, password: String, salt: String): Unit

  @SqlUpdate("update users set password = :password, salt = :salt where id = :id")
  def setPassword(password: String, salt: String, id: Int): Unit
}