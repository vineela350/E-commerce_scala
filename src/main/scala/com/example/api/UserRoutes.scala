package api

import akka.http.scaladsl.server.Directives._
import com.example.models.{UserCredentials, AuthToken}
import akka.http.scaladsl.model.StatusCodes
import com.example.services.UserService
import com.example.utils.{JsonSupport, JwtUtils}
import scala.concurrent.ExecutionContext
import scala.util.{Success, Failure}

trait UserRoutes extends JsonSupport {
  implicit val executionContext: ExecutionContext
  val userService: UserService

  val userRoutes = pathPrefix("auth") {
    concat(
      path("signup") {
        post {
          entity(as[UserCredentials]) { userCredentials =>
            onComplete(userService.createUser(userCredentials)) {
              case Success(user) =>
                val token = JwtUtils.createToken(user.id.toString)
                complete(StatusCodes.Created, AuthToken(token))
              case Failure(_) =>
                complete(StatusCodes.InternalServerError)
            }
          }
        }
      },
      path("login") {
        post {
          entity(as[UserCredentials]) { userCredentials =>
            onComplete(userService.authenticateUser(userCredentials)) {
              case Success(Some(user)) =>
                val token = JwtUtils.createToken(user.id.toString)
                complete(AuthToken(token))
              case Success(None) =>
                complete(StatusCodes.Unauthorized)
              case Failure(_) =>
                complete(StatusCodes.InternalServerError)
            }
          }
        }
      }
    )
  }
}
