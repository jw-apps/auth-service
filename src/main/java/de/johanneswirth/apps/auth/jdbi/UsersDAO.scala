package de.johanneswirth.apps.auth.jdbi

import org.jdbi.v3.sqlobject.statement.SqlQuery
import java.util

trait UsersDAO {
  @SqlQuery("select username from users where username like CONCAT('%', :username, '%')")
  def findUsers(username: String): List[String]
}