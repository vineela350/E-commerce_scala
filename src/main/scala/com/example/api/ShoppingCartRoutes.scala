package com.example.api

import akka.http.scaladsl.server.Directives._
import JsonFormats._ // Import your JSON formats

class ShoppingCartRoutes(shoppingCartService: ShoppingCartService)(implicit executionContext: ExecutionContext) {
  val routes = pathPrefix("cart") {
    concat(
      path("add") {
        post {
          entity(as[CartItem]) { cartItem =>
            onComplete(shoppingCartService.addToCart(cartItem.userId, cartItem.productId, cartItem.quantity)) {
              case Success(cart) => complete(cart)
              case Failure(ex) => complete(StatusCodes.InternalServerError, ex.getMessage)
            }
          }
        }
      },
      path("remove") {
        post {
          entity(as[CartItem]) { cartItem =>
            onComplete(shoppingCartService.removeFromCart(cartItem.userId, cartItem.productId)) {
              case Success(cart) => complete(cart)
              case Failure(ex) => complete(StatusCodes.InternalServerError, ex.getMessage)
            }
          }
        }
      },
      path("view") {
        get {
          parameter("userId".as[Long]) { userId =>
            onComplete(shoppingCartService.getCart(userId)) {
              case Success(cart) => complete(cart)
              case Failure(ex) => complete(StatusCodes.InternalServerError, ex.getMessage)
            }
          }
        }
      }
    )
  }
}
