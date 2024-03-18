package com.example.models

case class Product(id: Option[Long] = None, name: String, description: String, price: BigDecimal, categoryId: Long)
