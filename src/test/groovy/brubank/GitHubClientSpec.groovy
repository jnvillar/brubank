package brubank

import clients.Exception.BadResponse
import clients.GitHubClient
import grails.plugins.rest.client.RestBuilder
import org.springframework.http.HttpStatus
import org.springframework.test.web.client.MockRestServiceServer
import spock.lang.Specification
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import static org.springframework.http.HttpMethod.GET
import static grails.web.http.HttpHeaders.ACCEPT
import static org.springframework.http.MediaType.APPLICATION_JSON


class GitHubClientSpec extends Specification {
    GitHubClient gitHubClient = new GitHubClient()
    MockRestServiceServer mockServer


    def setup() {
        gitHubClient.restClient = new RestBuilder()
        mockServer = MockRestServiceServer.createServer(gitHubClient.restClient.restTemplate)
    }

    void "get repositories creation dates"() {
        given:

        mockServer.expect(requestTo("https://api.github.com/users/juan/repos"))
                .andExpect(method(GET))
                .andExpect(header(ACCEPT, "application/json"))
                .andRespond(withSuccess('[{"created_at":"2017-10-30T20:33:24Z"},{"created_at":"2017-10-30T20:33:24Z"},{"created_at":"2017-10-30T20:33:24Z"}]', APPLICATION_JSON))

        Date date = new Date().parse("yyyy-MM-dd-HH:mm:ss", "2017-10-30-20:33:24")

        when:
        List<Date> response = gitHubClient.getUserRepositoriesCreationDates("juan")

        then:
        response == [date, date, date]
    }

    void "get user"() {
        given:
        mockServer.expect(requestTo("https://api.github.com/users/juan"))
                .andExpect(method(GET))
                .andExpect(header(ACCEPT, "application/json"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND))

        when:
        gitHubClient.getUser("juan")

        then:
        thrown(BadResponse)
    }
}
