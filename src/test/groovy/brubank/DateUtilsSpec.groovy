package brubank

import dateUtils.DateUtils
import spock.lang.Specification

class DateUtilsSpec extends Specification {

    void "new date is not more than a day old"() {
        when:
        def isNotMoreThanNthDaysOld = DateUtils.isNotMoreThanNthDaysOld(new Date(), days)

        then:
        isNotMoreThanNthDaysOld

        where:
        days << [1, 2, 3]
    }

    void "given date is older than given days old"() {
        when:
        def isNotMoreThanNthDaysOld = DateUtils.isNotMoreThanNthDaysOld(date, dayOld)

        then:
        !isNotMoreThanNthDaysOld

        where:
        date << [new Date((new Date().getTime()) - (1000 * 60 * 60 * 24 * 1)),
                 new Date((new Date().getTime()) - (1000 * 60 * 60 * 24 * 2)),
                 new Date((new Date().getTime()) - (1000 * 60 * 60 * 24 * 2)),
                 new Date((new Date().getTime()) - (1000 * 60 * 60 * 24 * 3)),
                 new Date((new Date().getTime()) - (1000 * 60 * 60 * 24 * 3)),
                 new Date((new Date().getTime()) - (1000 * 60 * 60 * 24 * 3))]
        dayOld << [1, 1, 2, 1, 2, 3]
    }

    void "parse dates parses a date correctly"(){
        when:
        List<Date> parsedDates = DateUtils.parseDates(["2017-08-30T09:10:12Z"])

        then:
        parsedDates == [new Date().parse("yyyy-MM-dd-HH:mm:ss", "2017-08-30-09:10:12")]
    }

}
