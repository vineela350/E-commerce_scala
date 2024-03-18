package persistence.tables

import models.OrderItem
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

class OrderItemsTable(tag: Tag) extends Table[OrderItem](tag, "order_items") {
  def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def orderId: Rep[Long] = column[Long]("order_id")
  def productId: Rep[Long] = column[Long]("product_id")
  def quantity: Rep[Int] = column[Int]("quantity")
  def price: Rep[BigDecimal] = column[BigDecimal]("price")

  override def * : ProvenShape[OrderItem] = (id.?, orderId, productId, quantity, price) <> ((OrderItem.apply _).tupled, OrderItem.unapply)
}
