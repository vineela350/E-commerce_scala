package com.example.models

case class User(id: Option[Long] = None, username: String, email: String, password: String, role: String)