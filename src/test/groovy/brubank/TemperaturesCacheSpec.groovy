package brubank

import cache.TemperaturesCache
import grails.testing.gorm.DataTest
import spock.lang.Specification

class TemperaturesCacheSpec extends Specification implements DataTest {
    TemperaturesCache temperaturesCache = TemperaturesCache.instance

    def setupSpec() {
        mockDomain AverageTemperature
    }

    def cleanup() {
        AverageTemperature.findAll().each { it.delete(flush: true, failOnError: true) }
    }

    void "returns AverageTemperature if it's not older than 4 days"() {
        given:
        Date date = new Date()
        AverageTemperature averageTemperature = new AverageTemperature(location: "quilmes", averageTemperature: 20, date: date)
        averageTemperature.save(flush: true, failOnError: true)

        when:
        def res = temperaturesCache.get([location: "quilmes", date: date])

        then:
        res == averageTemperature
    }

    void "returns null if AverageTemperature it's not in the cache"() {
        when:
        def res = temperaturesCache.get([location: "quilmes", date: new Date()])

        then:
        res == null
    }
}
