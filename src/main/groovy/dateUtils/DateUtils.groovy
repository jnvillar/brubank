package dateUtils

class DateUtils {

    static isNotMoreThanNthDaysOld(Date date, int days){
        Date now = new Date()
        Long differenceInMillis = now.getTime() - date.getTime()
        return differenceInMillis < (days * 24 * 60 * 60 * 1000)
    }

    static parseDates(List<String> datesAsString){
        List<Date> parsedDates = []
        datesAsString.each{ String date ->
            date = date.replace("T","-").replace("Z","")
            parsedDates.add(new Date().parse("yyyy-MM-dd-HH:mm:ss", date))
        }
        parsedDates
    }
}
