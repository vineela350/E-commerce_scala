package com.example.services

import com.example.models.{User, UserCredentials}
import com.example.persistence.tables.UsersTable
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{Future, ExecutionContext}

class UserService(db: Database)(implicit ec: ExecutionContext) {
  val users = TableQuery[UsersTable]

  // Add an additional parameter for the role, with a default value for regular users
  def createUser(userCredentials: UserCredentials, role: String = "user"): Future[User] = {
    val hashedPassword = hashPassword(userCredentials.password)
    val newUser = User(None, userCredentials.username, userCredentials.email, hashedPassword, role)
    val action = (users returning users.map(_.id) into ((_, id) => newUser.copy(id = Some(id)))) += newUser
    db.run(action)
  }

  def authenticateUser(userCredentials: UserCredentials): Future[Option[User]] = {
    val action = users.filter(u => u.username === userCredentials.username && u.password === hashPassword(userCredentials.password)).result.headOption
    db.run(action)
  }

  private def hashPassword(password: String): String = password // Implement proper hashing in production
}
