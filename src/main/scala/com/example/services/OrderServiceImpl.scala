package com.example.services

import com.example.models.{Order, OrderItem}
import com.example.persistence.{Orders, OrderItems} // Make sure to import your table mappings
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{ExecutionContext, Future}

class OrderServiceImpl(implicit val db: Database, val executionContext: ExecutionContext) extends OrderService {

  private val orders = TableQuery[Orders]
  private val orderItems = TableQuery[OrderItems]

  override def createOrder(userId: Long): Future[Order] = {
    val action = for {
      // Create a new order entry
      orderId <- (orders returning orders.map(_.id)) += Order(None, userId, "New", java.time.LocalDateTime.now())
      
      // Assuming you have a mechanism to get the cart items for the user
      // and convert them to order items. This step is simplified here.
      // You should replace it with actual logic to move items from cart to order.
      _ <- DBIO.sequence(Seq(
        // Example of adding order items
        orderItems += OrderItem(None, orderId, productId = 1L, quantity = 2, price = 100.00)
        // Add other items here
      ))
    } yield Order(Some(orderId), userId, "New", java.time.LocalDateTime.now())

    db.run(action.transactionally)
  }

  override def getOrderById(orderId: Long): Future[Option[Order]] = {
    db.run(orders.filter(_.id === orderId).result.headOption)
  }

  override def getUserOrders(userId: Long): Future[Seq[Order]] = {
    db.run(orders.filter(_.userId === userId).result)
  }
}
