package persistence.tables

import models.Category
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

class CategoriesTable(tag: Tag) extends Table[Category](tag, "categories") {
  def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name: Rep[String] = column[String]("name")

  override def * : ProvenShape[Category] = (id.?, name) <> ((Category.apply _).tupled, Category.unapply)
}
