package brubank

import cache.GitHubCache
import grails.testing.gorm.DataTest
import spock.lang.Specification

class GitHubCacheSpec extends Specification implements DataTest {
    GitHubCache gitHubCache = GitHubCache.instance

    def setupSpec() {
        mockDomain User
    }

    def cleanup() {
        User.findAll().each { it.delete(flush: true, failOnError: true) }
    }

    void "returns user if it's not older than 4 days"() {
        given:
        User user = new User(name: "juan", reposCreationDates: [], location: "location")
        user.save(flush: true, failOnError: true)

        when:
        def res = gitHubCache.get("juan")

        then:
        res == user
    }

    void "returns null if user it's older than 4 days"() {
        given:
        Date fourDaysAgo = new Date((new Date().getTime()) - (1000 * 60 * 60 * 24 * gitHubCache.daysOld))
        User user = new User(name: "juan", reposCreationDates: [], location: "location", creation: fourDaysAgo)
        user.save(flush: true, failOnError: true)

        when:
        def res = gitHubCache.get("juan")

        then:
        res == null
    }

    void "returns null if user it's not in the cache"() {
        when:
        def res = gitHubCache.get("juan")

        then:
        res == null
    }
}
