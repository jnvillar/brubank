package brubank

class User {
    String name
    Date creation = new Date()
    List<Date> reposCreationDates
    String location

    static constraints = {
        name(nullable: false)
        location(nullable: false)
    }
}
