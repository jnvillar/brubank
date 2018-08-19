package brubank

class CacheInterceptor {

    CacheInterceptor() {
        match controller: 'cache'
    }

    boolean before() {

        try {
            session.cache = Class.forName("brubank." + params.cache.capitalize())
        } catch (ClassNotFoundException e) {
            render(status: 404, "cache not found")
            return false
        }

        return true
    }

    boolean after() { true }

    void afterView() {
    }
}
