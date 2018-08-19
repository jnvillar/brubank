package clients

import cache.GitHubCache
import dateUtils.DateUtils

class GitHubClient extends ApiClient{

    public GitHubClient(){
        baseUrl = "https://api.github.com"
    }

    def getUser(String name){
       get("/users/${name}").json
    }

    def getUserRepositoriesCreationDates(String name){
        List<String> reposCreationDatesAsString = get("/users/${name}/repos").json.created_at
        List<Date> repositoriesCreationDates = DateUtils.parseDates(reposCreationDatesAsString)
        repositoriesCreationDates
    }
}
