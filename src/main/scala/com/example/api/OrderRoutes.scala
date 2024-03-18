package com.example.api

import akka.http.scaladsl.server.Directives._
import JsonFormats._ // Import your JSON formats

class OrderRoutes(orderService: OrderService)(implicit executionContext: ExecutionContext) {
  val routes = pathPrefix("orders") {
    concat(
      path("create") {
        post {
          entity(as[Long]) { userId =>
            onComplete(orderService.createOrder(userId)) {
              case Success(order) => complete(StatusCodes.Created, order)
              case Failure(ex) => complete(StatusCodes.InternalServerError, ex.getMessage)
            }
          }
        }
      },
      path(LongNumber) { orderId =>
        get {
          onComplete(orderService.getOrderById(orderId)) {
            case Success(Some(order)) => complete(order)
            case Success(None) => complete(StatusCodes.NotFound)
            case Failure(ex) => complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      },
      path("user" / LongNumber) { userId =>
        get {
          onComplete(orderService.getUserOrders(userId)) {
            case Success(orders) => complete(orders)
            case Failure(ex) => complete(StatusCodes.InternalServerError, ex.getMessage)
          }
        }
      }
    )
  }
}
