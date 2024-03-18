package com.example.persistence.tables

import com.example.models.User
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

class UsersTable(tag: Tag) extends Table[User](tag, "users") {
  def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def username: Rep[String] = column[String]("username")
  def email: Rep[String] = column[String]("email")
  def password: Rep[String] = column[String]("password")
  // Add the role column if your User case class includes a role field
  def role: Rep[String] = column[String]("role")

  // Ensure the * projection includes all fields in the order they appear in the User case class constructor
  override def * : ProvenShape[User] = (id.?, username, email, password, role) <> ((User.apply _).tupled, User.unapply)
}

