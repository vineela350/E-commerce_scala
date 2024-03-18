package com.example.services

import scala.concurrent.Future
import com.example.models.Order

trait OrderService {
  def createOrder(userId: Long): Future[Order]
  def getOrderById(orderId: Long): Future[Option[Order]]
  def getUserOrders(userId: Long): Future[Seq[Order]]
}
