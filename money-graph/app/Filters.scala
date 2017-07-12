import javax.inject.Inject

import play.api.http.DefaultHttpFilters
import play.filters.csrf.CSRFFilter

/**
  * @author antonpp
  * @since 12/07/2017
  */
class Filters @Inject()(csrfFilter: CSRFFilter) extends DefaultHttpFilters(csrfFilter)