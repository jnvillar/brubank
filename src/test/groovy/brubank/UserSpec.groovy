package brubank

import grails.testing.gorm.DataTest
import spock.lang.Specification

class UserSpec extends Specification implements DataTest {

    def setupSpec() {
        mockDomain User
    }

    void "user creation"() {
        given:
        User user = new User(name: name, reposCreationDates: reposCreationDates, location: locations)

        when:
        user.save(flush: true, failOnError: true)

        then:
        user.name == name
        user.location == locations
        user.reposCreationDates == reposCreationDates
        user.creation != null

        where:
        name << ["juan", "pedro", "facundo"]
        reposCreationDates << [[new Date()], [], [new Date(), new Date()]]
        locations << ["quilmes", "palermo", "avellaneda"]
    }

    void "user name and location can't be null"() {
        given:
        User user = new User(creationParams)

        when:
        user.save(flush: true, failOnError: true)

        then:
        thrown(Exception)

        where:
        creationParams << [[location: "location"], [name: "name"], [reposCreationDates: []]]
    }
}
