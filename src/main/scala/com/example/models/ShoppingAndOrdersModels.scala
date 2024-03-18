package com.example.models

case class CartItem(productId: Long, quantity: Int)

case class ShoppingCart(items: Seq[CartItem])

case class Order(id: Option[Long] = None, userId: Long, items: Seq[OrderItem], status: String, createdAt: java.time.LocalDateTime, totalPrice: BigDecimal)

case class OrderItem(id: Option[Long] = None, orderId: Long, productId: Long, quantity: Int, price: BigDecimal)
