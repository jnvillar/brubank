package brubank

import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class CacheInterceptorSpec extends Specification implements InterceptorUnitTest<CacheInterceptor> {

    void "sets the cache"() {
        when:
        params["cache"] = "user"
        withRequest(controller: "cache")

        then:
        interceptor.before()
        session.cache == User
    }

    void "handles invalid cache names"() {
        when:
        params["cache"] = "other"
        withRequest(controller: "cache")

        then:
        !interceptor.before()
        session.cache == null
        response.status == 404
    }
}
