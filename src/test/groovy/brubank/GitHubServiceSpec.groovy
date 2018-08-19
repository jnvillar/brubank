package brubank

import cache.GitHubCache
import clients.GitHubClient
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class GitHubServiceSpec extends Specification implements ServiceUnitTest<GitHubService>, DataTest {

    def setupSpec(){
        mockDomain User
    }

    def setup() {
        service.gitHubCache = Mock(GitHubCache)
        service.gitHubClient = Mock(GitHubClient)
    }

    def cleanup() {
    }

    void "looks first in cache"() {
        when:
        service.gitHubCache.get(_) >> { arg -> return ([name:"juan", reposCreationDates:[], location:"location"] as User) }
        User user = service.getUser("juan")

        then:
        user.name == "juan"
        user.reposCreationDates == []
        user.location == "location"
    }

    void "if data is not in cache it uses the client"() {
        given:
        Date date = new Date()

        when:
        service.gitHubCache.get(_) >> { arg -> return null }
        service.gitHubClient.getUser(_) >> { arg -> return [name:"juan", location:"location"] }
        service.gitHubClient.getUserRepositoriesCreationDates(_) >> { arg -> return [date] }
        service.gitHubCache.put("juan", [
                location          : "location",
                reposCreationDates: [date]
        ]) >> { name, paramsInfo -> return ([name:"juan", reposCreationDates:[date], location:"location"] as User) }
        User user = service.getUser("juan")

        then:
        user.name == "juan"
        user.reposCreationDates == [date]
        user.location == "location"
    }
}
