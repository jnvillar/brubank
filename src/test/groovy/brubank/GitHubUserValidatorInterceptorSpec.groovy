package brubank

import cache.GitHubCache
import clients.Exception.BadResponse
import clients.GitHubClient
import grails.testing.gorm.DataTest
import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class GitHubUserValidatorInterceptorSpec extends Specification implements InterceptorUnitTest<GitHubUserValidatorInterceptor>, DataTest {

    private user = [name: "juan", reposCreationDates: [], location: "location"]

    void setupSpec() {
        mockDomain User
    }

    void setup() {
        interceptor.gitHubCache = Mock(GitHubCache)
    }

    void "github user exists in the cache"() {
        given:
        interceptor.gitHubCache.get(_) >> { arg -> return (user as User) }

        when:
        withRequest(controller: "correlation")

        then:
        interceptor.before()
    }

    void "github user exists but is not in the cache"() {
        given:
        interceptor.gitHubCache.get(_) >> { arg -> return null }
        interceptor.gitHubClient = Stub(GitHubClient) { getUser(_) >> (user as User) }

        when:
        withRequest(controller: "correlation")

        then:
        interceptor.before()
    }

    void "github user does not exist"() {
        given:
        interceptor.gitHubCache.get(_) >> { arg -> return null }
        interceptor.gitHubClient = Stub(GitHubClient) { getUser(_) >> { throw new BadResponse("msg", 404) } }

        when:
        withRequest(controller: "correlation")

        then:
        !interceptor.before()
        response.status == 404
        response.json == [msg: "msg"]
    }

    void "github user has no location set"() {
        given:
        interceptor.gitHubCache.get(_) >> { arg -> return null }
        interceptor.gitHubClient = Stub(GitHubClient) {
            getUser(_) >> { ([name: "juan", reposCreationDates: []] as User) }
        }

        when:
        withRequest(controller: "correlation")

        then:
        !interceptor.before()
        response.status == 400
        response.json == [msg: "GitHub user does not has a location set"]
    }

    void "check for user raises exception"() {
        given:
        interceptor.gitHubClient = Stub(GitHubClient) { getUser(_) >> { throw new BadResponse("msg", 403) } }

        when:
        withRequest(controller: "correlation")

        then:
        !interceptor.before()
        response.status == 403
        response.json == [msg: "msg"]
    }
}
