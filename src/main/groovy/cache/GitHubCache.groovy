package cache

import brubank.User
import dateUtils.DateUtils

@Singleton
class GitHubCache implements CacheIO<String, LinkedHashMap, User> {
    final int daysOld = 4

    User get(String name) {
        User user = User.findByName(name)
        if (user && DateUtils.isNotMoreThanNthDaysOld(user.creation, daysOld)) return user
        return null
    }

    User put(String userName, LinkedHashMap userInfo) {
        User user = User.findOrCreateByName(userName)
        user.reposCreationDates = userInfo.reposCreationDates
        user.location = userInfo.location
        user.creation = new Date()
        user.save(flush: true, failOnError: true)
        user
    }
}
