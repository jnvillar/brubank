package brubank

import grails.testing.gorm.DataTest
import spock.lang.Specification

class AverageTemperatureSpec extends Specification implements DataTest {

    def setupSpec() {
        mockDomain AverageTemperature
    }

    void "creation"() {
        given:
        Date date = new Date()
        AverageTemperature averageTemperature = new AverageTemperature(date: date, averageTemperature: avgTemp, location: location)

        when:
        averageTemperature.save(flush: true, failOnError: true)

        then:
        averageTemperature.date == date
        averageTemperature.location == location
        averageTemperature.averageTemperature == avgTemp

        where:
        avgTemp << [0, -5, 10, 100]
        location << ["quilmes", "palermo", "avellaneda", "lanus"]
    }

    void "creation: location, date and average temp cant be null"() {
        given:
        AverageTemperature averageTemperature = new AverageTemperature(creationParams)

        when:
        averageTemperature.save(flush: true, failOnError: true)

        then:
        thrown(Exception)

        where:
        creationParams << [[location: "location", averageTemperature: 1],
                           [averageTemperature: 1, date: new Date()],
                           [location: "location", date: new Date()]]
    }
}
