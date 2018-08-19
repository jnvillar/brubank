package brubank

import clients.Exception.BadResponse
import clients.WorldWeatherClient
import grails.plugins.rest.client.RestBuilder
import org.springframework.http.HttpStatus
import org.springframework.test.web.client.MockRestServiceServer
import spock.lang.Specification
import static grails.web.http.HttpHeaders.ACCEPT
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess

class WorldWeatherClientSpec extends Specification {
    WorldWeatherClient weatherClient = new WorldWeatherClient()
    MockRestServiceServer mockServer


    def setup() {
        weatherClient.restClient = new RestBuilder()
        mockServer = MockRestServiceServer.createServer(weatherClient.restClient.restTemplate)
    }

    void "get hourly temperature samples"() {
        given:

        Date date = new Date().parse("yyyy-MM-dd-HH:mm:ss", "2017-10-30-20:33:24")
        mockServer.expect(requestTo("https://api.worldweatheronline.com/premium/v1/past-weather.ashx?key=3479616aa74241fe8f8195526181808&q=location&format=json&date=2017-10-30"))
                .andExpect(method(GET))
                .andExpect(header(ACCEPT, "application/json"))
                .andRespond(withSuccess('{"data":{"weather":{"hourly":[{"tempC":"1"},{"tempC":"2"},{"tempC":"3"}]}}}', APPLICATION_JSON))

        when:
        List<Integer> response = weatherClient.getHourlyTemperatureSamples([location: "location", date: date])

        then:
        response == [1, 2, 3]
    }

    void "get hourly temperature samples unexpexted response"() {
        given:

        Date date = new Date().parse("yyyy-MM-dd-HH:mm:ss", "2017-10-30-20:33:24")
        mockServer.expect(requestTo("https://api.worldweatheronline.com/premium/v1/past-weather.ashx?key=3479616aa74241fe8f8195526181808&q=location&format=json&date=2017-10-30"))
                .andExpect(method(GET))
                .andExpect(header(ACCEPT, "application/json"))
                .andRespond(withSuccess('{"data":{"error":[{"msg":"error"}]}}', APPLICATION_JSON))

        when:
        weatherClient.getHourlyTemperatureSamples([location: "location", date: date])

        then:
        thrown(BadResponse)
    }

    void "get hourly temperature samples location not found"() {
        given:

        Date date = new Date().parse("yyyy-MM-dd-HH:mm:ss", "2017-10-30-20:33:24")
        mockServer.expect(requestTo("https://api.worldweatheronline.com/premium/v1/past-weather.ashx?key=3479616aa74241fe8f8195526181808&q=location&format=json&date=2017-10-30"))
                .andExpect(method(GET))
                .andExpect(header(ACCEPT, "application/json"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND))

        when:
        weatherClient.getHourlyTemperatureSamples([location: "location", date: date])

        then:
        thrown(BadResponse)
    }

}
