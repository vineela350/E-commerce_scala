package models

case class Order(id: Option[Long] = None, userId: Long, status: String, createdAt: java.time.LocalDateTime)
