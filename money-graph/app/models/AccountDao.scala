package models

import javax.inject.Inject

import anorm.SqlParser.get
import anorm.{RowParser, SQL, ~}
import play.api.db.Database

/**
  * @author Anton Mordberg
  * @since 02.07.17
  */
class AccountDao @Inject()(db: Database) {
    /**
      * Parse a User from a ResultSet
      */
    val simple: RowParser[Account] = {
        get[String]("user.email") ~
            get[String]("user.name") ~
            get[String]("user.password") map {
            case email ~ name ~ password => Account(email, name, password)
        }
    }

    /**
      * Retrieve a User from email.
      */
    def findByEmail(email: String): Option[Account] = {
        db.withConnection { implicit connection =>
            SQL("select * from user where email = {email}").on(
                'email -> email).as(simple.singleOpt)
        }
    }

    /**
      * Retrieve all users.
      */
    def findAll: Seq[Account] = {
        db.withConnection { implicit connection =>
            SQL("select * from user").as(simple *)
        }
    }

    /**
      * Authenticate a User.
      */
    def authenticate(email: String, password: String): Option[Account] = {
        db.withConnection { implicit connection =>
            SQL(
                """
         select * from user where
         email = {email} and password = {password}
        """).on(
                'email -> email,
                'password -> password).as(simple.singleOpt)
        }
    }

    /**
      * Create a User.
      */
    def create(user: Account): Account = {
        db.withConnection { implicit connection =>
            SQL(
                """
          insert into user values (
            {email}, {name}, {password}
          )
        """).on(
                'email -> user.email,
                'name -> user.name,
                'password -> user.password).executeUpdate()

            user

        }
    }

}
