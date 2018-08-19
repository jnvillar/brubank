package brubank

import cache.GitHubCache
import grails.converters.JSON
import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class CacheControllerSpec extends Specification implements ControllerUnitTest<CacheController>, DataTest {

    def setupSpec() {
        mockDomain User
    }

    def setup() {
        session.cache = User
    }

    def cleanup() {
        User.findAll().each { it.delete(flush: true, failOnError: true) }
    }

    void "info"() {
        given:
        Date date = new Date()
        User user = new User(name: "juan", location: "quilmes", reposCreationDates: [date])
        user.save(flush: true, failOnError: true)

        when:
        controller.content()

        then:
        response.status == 200
        response.json.keys[0].name == "juan"
    }

    void "flush"() {
        given:
        User user = new User(name: "juan", location: "quilmes", reposCreationDates: [new Date()])
        user.save(flush: true, failOnError: true)

        when:
        controller.flush()

        then:
        response.status == 200
        response.text == "cache cleaned!"
        User.count() == 0
    }
}
