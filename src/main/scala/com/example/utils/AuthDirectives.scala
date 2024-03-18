package com.example.utils

import com.example.models.User
import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives._
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}
import scala.util.{Success, Failure} // Import Success and Failure

object AuthDirectives {
  def authenticateUser(token: String): Option[User] = {
    Jwt.decode(token, JwtUtils.secretKey, Seq(JwtAlgorithm.HS256)) match {
      case Success(claim) =>
        // Assuming the claim's subject is the username. You need to adjust according to your JWT structure.
        // Also assuming some default values for email and password, adjust as necessary.
        Some(User(id = None, username = claim.subject.getOrElse(""), email = "default@example.com", password = "hashed-default", role = "admin"))
      case Failure(_) => None
    }
  }

  def authenticated: Directive1[User] = {
    optionalHeaderValueByName("Authorization").flatMap {
      case Some(token) => authenticateUser(token) match {
        case Some(user) => provide(user)
        case None => reject
      }
      case None => reject
    }
  }

  def authorized(role: String): Directive1[User] = authenticated.flatMap { user =>
    // Make sure the User case class has a `role` field for this to work.
    if (user.role == role) provide(user) else reject
  }
}
