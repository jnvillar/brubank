package brubank

import cache.GitHubCache
import clients.GitHubClient

class GitHubService {
    GitHubClient gitHubClient
    GitHubCache gitHubCache = GitHubCache.instance

    def getUser(String name) {
        User user = gitHubCache.get(name)
        if (user) return user

        def userGithubJson = gitHubClient.getUser(name)
        List<Date> reposCreationDates = gitHubClient.getUserRepositoriesCreationDates(name)
        LinkedHashMap userInfo = userInfo(userGithubJson.location as String, reposCreationDates)

        gitHubCache.put(name, userInfo)
    }

    def userInfo(String location, List<Date> reposCreationDates) {
        [
                location          : location,
                reposCreationDates: reposCreationDates
        ]
    }
}

