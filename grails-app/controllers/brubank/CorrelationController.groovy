package brubank

import clients.Exception.BadResponse
import grails.converters.JSON

class CorrelationController {
    GitHubService gitHubService
    WeatherService weatherService

    def index() {
        render(([msg: "Brubank Challenge", urls: [
                "https://brubank.herokuapp.com/cache/correlation/jnvillar",
                "https://brubank.herokuapp.com/cache/user",
                "https://brubank.herokuapp.com/cache/user/flush",
                "https://brubank.herokuapp.com/cache/averageTemperature",
                "https://brubank.herokuapp.com/cache/averageTemperature/flush"]] as JSON))
    }

    def getCorrelation() {
        try {
            User user = gitHubService.getUser(params.user as String)
            List<BigDecimal> averageTemps = weatherService.getAverageTemperaturesOfLocationOn(user.location, user.reposCreationDates)

            render(([
                    number_of_repositories: user.reposCreationDates.size(),
                    average_temperatures  : averageTemps.collect { it.toString() + " ºC" }
            ] as JSON))

        } catch (BadResponse e) {
            render(status: e.status, ([status: e.status, msg: "${e.message}"] as JSON))
        } catch (Exception e) {
            render(status: 500, ([status: 500, msg: "${e.message}"] as JSON))
        }
    }
}
