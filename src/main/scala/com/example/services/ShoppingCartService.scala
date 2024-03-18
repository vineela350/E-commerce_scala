package com.example.services

import scala.concurrent.Future
import com.example.models.ShoppingCart

trait ShoppingCartService {
  def addToCart(userId: Long, productId: Long, quantity: Int): Future[ShoppingCart]
  def removeFromCart(userId: Long, productId: Long): Future[ShoppingCart]
  def getCart(userId: Long): Future[ShoppingCart]
}
