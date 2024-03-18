package models

case class OrderItem(id: Option[Long] = None, orderId: Long, productId: Long, quantity: Int, price: BigDecimal)
