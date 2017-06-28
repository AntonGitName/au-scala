import play.api._
import models.User

object Global extends GlobalSettings {

    override def onStart(app: Application) {
        InitialData.insert()
    }
}

/**
  * Initialize test data
  */
object InitialData {

    def insert() = {
        if (User.findAll.isEmpty) {
            Seq(
                User("user1@company.com", "User One", "secret"),
                User("user2@company.com", "User Two", "secret"),
                User("user3@company.com", "User Three", "secret")
            ).foreach(User.create)
        }
    }

}