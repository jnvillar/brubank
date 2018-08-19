package brubank

import cache.TemperaturesCache
import clients.WorldWeatherClient
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class WeatherServiceSpec extends Specification implements ServiceUnitTest<WeatherService>{

    def setup() {
        service.temperaturesCache = Mock(TemperaturesCache)
        service.weatherClient = Mock(WorldWeatherClient)
    }

    void "getAverageTemperaturesOfLocationOn using the cache"() {
        given:
        Date date = new Date()
        service.temperaturesCache.get(_) >> { arg -> return ([averageTemperature:1] as AverageTemperature) }

        when:
        List<BigDecimal> result = service.getAverageTemperaturesOfLocationOn("quilmes", [date])

        then:
        result == [1]
    }

    void "getAverageTemperaturesOfLocationOn not using the cache"() {
        given:
        Date date = new Date()
        service.temperaturesCache.get(_) >> { arg -> return null}
        service.weatherClient.getHourlyTemperatureSamples(_) >> { arg -> return [1, 2, 3]}

        when:
        List<BigDecimal> result = service.getAverageTemperaturesOfLocationOn("quilmes", [date])

        then:
        result == [2]
    }
}
