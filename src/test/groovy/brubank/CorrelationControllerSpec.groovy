package brubank

import clients.Exception.BadResponse
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class CorrelationControllerSpec extends Specification implements ControllerUnitTest<CorrelationController> {

    void "user repositories"() {
        given:
        Date date = new Date()
        controller.gitHubService = Stub(GitHubService) {
            getUser(_) >> [location: "location", reposCreationDates: [date]]
        }
        controller.weatherService = Stub(WeatherService) {
            getAverageTemperaturesOfLocationOn("location", [date]) >> [20]
        }

        when:
        controller.getCorrelation()

        then:
        response.status == 200
        response.json == [number_of_repositories: 1,
                          average_temperatures  : ["20 ºC"]
        ]
    }

    void "user repositories raises BadResponse exception"() {
        given:
        controller.gitHubService = Stub(GitHubService) {
            getUser(_) >> { throw new BadResponse("bad reponse", 404) }
        }

        when:
        controller.getCorrelation()

        then:
        response.status == 404
        response.json == [msg: "bad reponse", status:404]
    }

    void "user repositories raises exception"() {
        given:
        controller.gitHubService = Stub(GitHubService) {
            getUser(_) >> { throw new Exception("Exception") }
        }

        when:
        controller.getCorrelation()

        then:
        response.status == 500
        response.json == [msg: "Exception", status:500]
    }

    void "get weather raises BadResponse exception"() {
        given:
        controller.gitHubService = Stub(GitHubService) {
            getUser(_) >> [:]
        }

        controller.weatherService = Stub(WeatherService) {
            getAverageTemperaturesOfLocationOn(_, _) >> { throw new BadResponse("bad reponse", 404) }
        }

        when:
        controller.getCorrelation()

        then:
        response.status == 404
        response.json == [msg:"bad reponse", status:404]
    }

    void "get weather raises exception"() {
        given:
        controller.gitHubService = Stub(GitHubService) {
            getUser(_) >> [:]
        }

        controller.weatherService = Stub(WeatherService) {
            getAverageTemperaturesOfLocationOn(_, _) >> { throw new Exception("Exception") }
        }

        when:
        controller.getCorrelation()

        then:
        response.status == 500
        response.json ==  [msg:"Exception", status:500]
    }
}
