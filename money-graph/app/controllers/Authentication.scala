package controllers

import javax.inject.Inject

import models.{Account, AccountDao}
import play.api.Logger
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import views._

class Authentication @Inject() (cc: ControllerComponents, dao : AccountDao) extends AbstractController(cc) {

    val logger: Logger = Logger(this.getClass())

    val loginForm = Form(
        tuple(
            "email" -> email,
            "password" -> nonEmptyText
        ) verifying ("Invalid email or password", result => result match {
            case (mail, password) => dao.authenticate(mail, password).isDefined
        })
    )

    /**
      * Login page.
      */
    def login = Action { implicit request =>
        Ok(html.login(loginForm))
    }

    /**
      * Logout and clean the session.
      */
    def logout = Action {
        Redirect(routes.Authentication.login).withNewSession.flashing(
            "success" -> "You've been logged out"
        )
    }

    /**
      * Handle login form submission.
      */
    def authenticate = Action { implicit request =>
        loginForm.bindFromRequest.fold(
            formWithErrors => BadRequest(html.login(formWithErrors)),
            account => Redirect(routes.Restricted.index()).withSession("email" -> account._1)
        )
    }

    val registerForm = Form(
        tuple(
            "email" -> email,
            "name" -> nonEmptyText,
            "password" -> nonEmptyText
        ) verifying ("Account with specified email already exists", result => result match {
            case (mail, name, password) => dao.findByEmail(mail).isEmpty
        })
    )

    /**
      * Register page.
      */
    def register = Action { implicit request =>
        Ok(html.register(registerForm))
    }

    /**
      * Handle register form submission.
      */
    def createAccount = Action { implicit request =>
        registerForm.bindFromRequest.fold(
            formWithErrors => {
                logger.debug("Account creation aborted")
                BadRequest(html.register(formWithErrors))
            },
            account => {
                logger.debug("Creating new account for " + account._1)
                dao.create(account._1, account._2, account._3)
                Redirect(routes.Restricted.index()).withSession("email" -> account._1)
            }
        )
    }

}