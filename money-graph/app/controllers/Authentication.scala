package controllers

import javax.inject.Inject

import models.{Account, AccountDao}
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import views._

class Authentication @Inject() (cc: ControllerComponents, dao : AccountDao) extends AbstractController(cc) {

    val loginForm = Form(
        tuple(
            "email" -> email,
            "password" -> nonEmptyText
        ) verifying ("Invalid email or password", {
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
        Redirect(routes.Authentication.login()).withNewSession.flashing(
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

}