import scala.concurrent.Await
import scala.concurrent.duration.Duration
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.example.api.ProductRoutes
import com.example.services.{ProductService, ProductServiceImpl}
import slick.jdbc.JdbcBackend.Database
import scala.concurrent.{ExecutionContextExecutor, Future, Promise, ExecutionContext}
import scala.util.{Failure, Success}
import slick.jdbc.PostgresProfile.api._
import com.example.services.{ShoppingCartService, ShoppingCartServiceImpl, OrderService, OrderServiceImpl}
import com.example.api.{ProductRoutes, ShoppingCartRoutes, OrderRoutes}

object MainApp extends App {
  implicit val system: ActorSystem = ActorSystem("ecommerceSystem")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  // Initialize the Database object
  implicit val db: Database = Database.forConfig("slick.dbs.default.db")

  // Initialize services
  val productService: ProductService = new ProductServiceImpl(db) // Pass db here
  val shoppingCartService: ShoppingCartService = new ShoppingCartServiceImpl()(executionContext, db) // Ensure the ShoppingCartServiceImpl also accepts db and executionContext
  val orderService: OrderService = new OrderServiceImpl()(executionContext, db) // Ensure the OrderServiceImpl also accepts db and executionContext


  // Initialize route handlers
  val productRoutes = new ProductRoutes(productService)
  val shoppingCartRoutes = new ShoppingCartRoutes(shoppingCartService)
  val orderRoutes = new OrderRoutes(orderService)

  // Combine all routes
  val combinedRoutes = productRoutes.routes ~ shoppingCartRoutes.routes ~ orderRoutes.routes

  // Asynchronously create the schema if it doesn't exist and start the server afterwards
  db.run(DBIO.seq(
    // Schema creation for CartItems, Orders, and OrderItems goes here
  )).onComplete {
    case Success(_) =>
      println("Database schema exists or has been successfully created.")
      
      // Start the HTTP server with combined routes
      val serverBindingFuture = Http().bindAndHandle(combinedRoutes, "localhost", 8080)
      
      serverBindingFuture.onComplete {
        case Success(binding) =>
          println(s"Server online at http://localhost:8080/")
          scala.sys.addShutdownHook {
            binding.unbind()
            system.terminate()
            println("Server is shutting down...")
          }

        case Failure(ex) =>
          println(s"Failed to bind to http://localhost:8080/: ${ex.getMessage}")
          system.terminate()
      }

    case Failure(ex) =>
      println(s"Failed to create database schema: ${ex.getMessage}")
      system.terminate()
  }

  // Await mechanism to keep the application running
  val promise = Promise[Unit]()
  scala.sys.addShutdownHook {
    promise.success(())
  }
  Await.ready(promise.future, Duration.Inf)
}
