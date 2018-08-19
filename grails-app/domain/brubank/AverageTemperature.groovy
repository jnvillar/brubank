package brubank

class AverageTemperature {
    Date creation = new Date()
    BigDecimal averageTemperature
    String location
    Date date

    static constraints = {
        location(nullable: false)
        date(nullable: false)
        averageTemperature(nullable: false)
    }
}
