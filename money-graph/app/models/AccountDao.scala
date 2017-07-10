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
      * Retrieve an Account from email.
      */
    def findByEmail(email: String): Option[Account] = {
        db.withConnection { implicit connection =>
            SQL("select * from accounts where email = {email}").on(
                'email -> email).as(AccountDao.simple.singleOpt)
        }
    }

    /**
      * Retrieve all accounts.
      */
    def findAll: Seq[Account] = {
        db.withConnection { implicit connection =>
            SQL("select * from accounts").as(AccountDao.simple *)
        }
    }

    /**
      * Authenticate an Account.
      */
    def authenticate(email: String, password: String): Option[Account] = {
        db.withConnection { implicit connection =>
            SQL(
                """
         select * from accounts where
         email = {email} and password = {password}
                """).on(
                'email -> email,
                'password -> password).as(AccountDao.simple.singleOpt)
        }
    }

    /**
      * Create an Account.
      */
    def create(email: String, name: String, password: String) = {
        db.withConnection { implicit connection =>
            SQL(
                """
          insert into accounts (email, name, password)
          values ( {email}, {name}, {password} )
                """).on(
                'email -> email,
                'name -> name,
                'password -> password).executeUpdate()

        }
    }
}

object AccountDao {
    /**
      * Parse an Account from a ResultSet
      */
    val simple: RowParser[Account] = {
        get[Int]("accounts.id") ~
                get[String]("accounts.email") ~
                get[String]("accounts.name") ~
                get[String]("accounts.password") map {
            case id ~ email ~ name ~ password => Account(id, email, name, password)
        }
    }
}
