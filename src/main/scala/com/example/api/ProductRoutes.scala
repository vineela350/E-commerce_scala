// ProductRoutes.scala
package com.example.api

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes
import com.example.models.Product
import com.example.services.ProductService
import com.example.utils.JsonSupport
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class ProductRoutes(val productService: ProductService)(implicit val executionContext: ExecutionContext) extends JsonSupport {
  import akka.http.scaladsl.server.Directives._
  import com.example.utils.AuthDirectives._

  val productRoutes: Route = pathPrefix("products") {
    concat(
      pathEnd {
        concat(
          get {
            onComplete(productService.listProducts()) {
              case Success(products) => complete(products)
              case Failure(ex) => complete(StatusCodes.InternalServerError -> ex.getMessage)
            }
          },
          post {
            authorized("admin") { _ =>
              entity(as[Product]) { product =>
                onComplete(productService.createProduct(product)) {
                  case Success(createdProduct) => complete(StatusCodes.Created -> createdProduct)
                  case Failure(ex) => complete(StatusCodes.InternalServerError -> ex.getMessage)
                }
              }
            }
          }
        )
      },
      path(LongNumber) { id =>
        concat(
          get {
            onComplete(productService.getProduct(id)) {
              case Success(Some(product)) => complete(product)
              case Success(None) => complete(StatusCodes.NotFound)
              case Failure(ex) => complete(StatusCodes.InternalServerError -> ex.getMessage)
            }
          },
          put {
            authorized("admin") { _ =>
              entity(as[Product]) { product =>
                // Assuming the updateProduct method now correctly expects a product with an ID
                onComplete(productService.updateProduct(product.copy(id = Some(id)))) {
                  case Success(Some(updatedProduct)) => complete(StatusCodes.OK -> updatedProduct)
                  case Success(None) => complete(StatusCodes.NotFound)
                  case Failure(ex) => complete(StatusCodes.InternalServerError -> ex.getMessage)
                }
              }
            }
          },
          delete {
            authorized("admin") { _ =>
              onComplete(productService.deleteProduct(id)) {
                case Success(true) => complete(StatusCodes.NoContent)
                case Success(false) => complete(StatusCodes.NotFound)
                case Failure(ex) => complete(StatusCodes.InternalServerError -> ex.getMessage)
              }
            }
          }
        )
      }
    )
  }
}
