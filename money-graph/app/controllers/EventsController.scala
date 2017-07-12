package controllers

import javax.inject.Inject

import models.PartyDao
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{AbstractController, ControllerComponents}
import views.html

/**
  * @author antonpp
  * @since 10/07/2017
  */
class EventsController  @Inject() (cc: ControllerComponents, dao : PartyDao) extends AbstractController(cc) {

    val logger: Logger = Logger(this.getClass)

    val createForm = Form(
        single("description" -> text)
    )

    /**
      * Create event page.
      */
    def create = Action { implicit request =>
        Ok(html.create_party(createForm))
    }

    /**
      * Handle create event form submission.
      */
    def createEvent = Action { implicit request =>
        createForm.bindFromRequest.fold(
            formWithErrors => {
                logger.debug("Event creation aborted")
                BadRequest(html.create_party(formWithErrors))
            },
            party => {
                logger.debug("Creating new event " + party)
                dao.create(party)
                Redirect(routes.Restricted.index())
            }
        )
    }
}
