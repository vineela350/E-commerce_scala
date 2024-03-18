package com.example.services

import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import com.example.models.{CartItem, ShoppingCart}
import com.example.services.ShoppingCartService

class ShoppingCartServiceImpl(implicit val executionContext: ExecutionContext, val db: Database) extends ShoppingCartService {
  private val cartItems = TableQuery[CartItems]

  override def addToCart(userId: Long, productId: Long, quantity: Int): Future[ShoppingCart] = {
    val action = (cartItems returning cartItems.map(_.id)) += CartItem(None, userId, productId, quantity)
    db.run(action).flatMap(_ => getCart(userId)) // After adding, retrieve the updated cart
  }

  override def removeFromCart(userId: Long, productId: Long): Future[ShoppingCart] = {
    val query = cartItems.filter(item => item.userId === userId && item.productId === productId)
    val action = query.delete
    db.run(action).flatMap(_ => getCart(userId)) // After removal, retrieve the updated cart
  }

  override def getCart(userId: Long): Future[ShoppingCart] = {
    val query = cartItems.filter(_.userId === userId).result
    db.run(query).map(items => ShoppingCart(items))
  }
}
