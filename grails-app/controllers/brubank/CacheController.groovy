package brubank

import grails.converters.JSON

class CacheController {

    def flush() {
        def cache = session.cache
        cache.findAll().each { it.delete(flush: true, failOnError: true) }
        render(status: 200, "cache cleaned!")
    }

    def content() {
        def cache = session.cache
        render(([keys: cache.findAll()] as JSON))
    }
}
