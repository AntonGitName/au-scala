package controllers

import javax.inject.Inject

import models.{User, UserDao}
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import views._

class Authentication @Inject() (cc: ControllerComponents, dao : UserDao) extends AbstractController(cc) {

    val loginForm = Form(
        tuple(
            "email" -> text,
            "password" -> text
        ) verifying ("Invalid email or password", result => result match {
            case (email, password) => dao.authenticate(email, password).isDefined
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
            user => Redirect(routes.Restricted.index()).withSession("email" -> user._1)
        )
    }

}