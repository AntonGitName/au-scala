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
      * Parse an Account from a ResultSet
      */
    val simple: RowParser[Account] = {
        get[String]("accounts.email") ~
                get[String]("accounts.name") ~
                get[String]("accounts.password") map {
            case email ~ name ~ password => Account(email, name, password)
        }
    }

    /**
      * Retrieve an Account from email.
      */
    def findByEmail(email: String): Option[Account] = {
        db.withConnection { implicit connection =>
            SQL("select * from accounts where email = {email}").on(
                'email -> email).as(simple.singleOpt)
        }
    }

    /**
      * Retrieve all accounts.
      */
    def findAll: Seq[Account] = {
        db.withConnection { implicit connection =>
            SQL("select * from accounts").as(simple *)
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
                'password -> password).as(simple.singleOpt)
        }
    }

    /**
      * Create an Account.
      */
    def create(accounts: Account): Account = {
        db.withConnection { implicit connection =>
            SQL(
                """
          insert into accounts values (
            {email}, {name}, {password}
          )
                """).on(
                'email -> accounts.email,
                'name -> accounts.name,
                'password -> accounts.password).executeUpdate()

            accounts

        }
    }

}
