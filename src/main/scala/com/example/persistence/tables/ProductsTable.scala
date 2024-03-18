package com.example.persistence.tables

import com.example.models.Product
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

class ProductsTable(tag: Tag) extends Table[Product](tag, "products") {
  def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name: Rep[String] = column[String]("name")
  def description: Rep[String] = column[String]("description")
  def price: Rep[BigDecimal] = column[BigDecimal]("price")
  def categoryId: Rep[Long] = column[Long]("category_id")

  override def * : ProvenShape[Product] = (id.?, name, description, price, categoryId) <> ((Product.apply _).tupled, Product.unapply)
}
