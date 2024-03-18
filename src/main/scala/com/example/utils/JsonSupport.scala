package com.example.utils

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
// Import everything from spray.json to include deserializationError and other utilities
import spray.json._
import com.example.models.{Product, UserCredentials, AuthToken} 

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val userCredentialsFormat = jsonFormat3(UserCredentials)
  implicit val authTokenFormat = jsonFormat1(AuthToken)

  // Assuming Product class has the following fields: id, name, description, price, categoryId
  implicit val productFormat = jsonFormat5(Product)

  implicit val cartItemFormat = jsonFormat3(CartItem)
  implicit val shoppingCartFormat = jsonFormat1(ShoppingCart)
  implicit val orderFormat = jsonFormat5(Order) // Update the numbers according to your case classes
  // Add more formats as needed

  // Custom format for a Seq[Product]
  implicit object productsFormat extends RootJsonFormat[Seq[Product]] {
    def write(products: Seq[Product]): JsValue = JsArray(products.map(productFormat.write).toVector)
    def read(value: JsValue): Seq[Product] = value match {
      case JsArray(elements) => elements.map(productFormat.read)
      case _ => deserializationError("Expected a JsArray of products")
    }
  }
}
