package models

import javax.inject.Inject

import anorm.SqlParser._
import anorm._
import play.api.db.Database

/**
  * @author antonpp
  * @since 09/07/2017
  */
class PartyDao @Inject()(db: Database) {

    /**
      * Create a Party.
      */
    def create(description: String) = {
        db.withConnection { implicit connection =>
            SQL(""" insert into parties values ( {description} ) """).
                    on('description -> description).executeUpdate()
        }
    }

    /**
      * Find all party members.
      */
    def findAllPartyMembers(partyId: Int): Seq[Account] = {
        db.withConnection { implicit connection =>
            SQL(
                """select * from accounts, party_members
                  where party_members.member_id = accounts.id
                  and party_members.party_id = {partyId}""")
                    .on('partyId -> partyId)
                    .as(AccountDao.simple *)
        }
    }
}

object PartyDao {
    /**
      * Parse a Party from a ResultSet
      */
    val simple: RowParser[Party] = {
        get[Int]("party.id") ~ get[String]("party.description") map {
            case id ~ description => Party(id, description)
        }
    }
}
