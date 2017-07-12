package controllers

import javax.inject.Inject
import play.api.mvc._
import play.api.Logger

class Application @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

    val logger: Logger = Logger(this.getClass)

    def index = Action {
        logger.info("index requested")
        Ok(views.html.index())
    }

}