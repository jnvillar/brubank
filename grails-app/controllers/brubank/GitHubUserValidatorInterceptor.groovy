package brubank

import cache.GitHubCache
import clients.Exception.BadResponse
import clients.GitHubClient
import grails.converters.JSON


class GitHubUserValidatorInterceptor {
    GitHubClient gitHubClient
    GitHubCache gitHubCache = GitHubCache.instance

    GitHubUserValidatorInterceptor() {
        matchAll()
                .excludes(controller: 'cache', action: '*')
                .excludes(controller: 'correlation', action: 'index')
    }

    boolean before() {
        String location
        try {
            def user = gitHubCache.get(params.user)
            if (user) return true

            user = gitHubClient.getUser(params.user as String)
            location = user.location
        } catch (BadResponse e) {
            render(status: e.status, ([msg: "${e.message}"] as JSON))
            return false
        }

        if (!location) {
            render(status: 400, ([msg: "GitHub user does not has a location set"] as JSON))
            return false
        }

        return true
    }

    boolean after() { true }

    void afterView() {
    }
}
