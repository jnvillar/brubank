import clients.GitHubClient
import clients.WorldWeatherClient
import grails.plugins.rest.client.RestBuilder

beans = {
    gitHubClient(GitHubClient){
        restClient = ref('restBuilder')
    }

    weatherClient(WorldWeatherClient){
        restClient = ref('restBuilder')
    }

    restBuilder(RestBuilder)
}
