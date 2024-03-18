// ProductService.scala
package com.example.services

import com.example.models.Product
import scala.concurrent.Future

trait ProductService {
  def createProduct(product: Product): Future[Product]
  def getProduct(id: Long): Future[Option[Product]]
  def updateProduct(product: Product): Future[Option[Product]]
  def deleteProduct(id: Long): Future[Boolean]
  def listProducts(): Future[Seq[Product]]
}
