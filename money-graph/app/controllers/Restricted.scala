package controllers

import javax.inject.Inject

import play.api.mvc.{AbstractController, Controller, ControllerComponents}
import models.{Account, AccountDao}
import play.api.db.Database
import views._

class Restricted @Inject() (cc: ControllerComponents, dao : AccountDao) extends AbstractController(cc)  with Secured {

    /**
      * Display restricted area only if user is logged in.
      */
    def index = IsAuthenticated { username =>
        _ =>
            dao.findByEmail(username).map { user =>
                Ok(
                    html.restricted(user)
                )
            }.getOrElse(Forbidden)
    }

}