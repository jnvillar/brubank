package clients

import clients.Exception.BadResponse
import grails.plugins.rest.client.RestBuilder

class ApiClient {
    protected String baseUrl
    RestBuilder restClient

    def get(String url, Map params = [:]) {
        url = params ? addUrlParams(url, params) : url

        def response = restClient.get(baseUrl + url) {
            accept("application/json")
        }

        checkResponseStatus(response)
        response
    }

    static checkResponseStatus(response) {
        if (response.status != 200) throw new BadResponse("Unexpected response status (${response.status})", response.status as Integer)
    }

    static addUrlParams(String url, Map params) {
        url += "?"
        params.each { k, v ->
            url += "${k}=${v}&"
        }
        url[0..-1]
    }
}
