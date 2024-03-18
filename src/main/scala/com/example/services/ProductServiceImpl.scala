// ProductServiceImpl.scala
package com.example.services

import com.example.models.Product
import com.example.persistence.tables.ProductsTable
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{Future, ExecutionContext}

class ProductServiceImpl(db: Database)(implicit ec: ExecutionContext) extends ProductService {
  val products = TableQuery[ProductsTable]

  override def createProduct(product: Product): Future[Product] = db.run {
    (products returning products.map(_.id)
      into ((product, id) => product.copy(id=Some(id)))
    ) += product
  }

  override def getProduct(id: Long): Future[Option[Product]] = db.run {
    products.filter(_.id === id).result.headOption
  }

  override def updateProduct(product: Product): Future[Option[Product]] = db.run {
    products.filter(_.id === product.id).update(product).map {
      case 0 => None
      case _ => Some(product)
    }
  }

  override def deleteProduct(id: Long): Future[Boolean] = db.run {
    products.filter(_.id === id).delete.map(_ > 0)
  }

  override def listProducts(): Future[Seq[Product]] = db.run {
    products.result
  }
}
