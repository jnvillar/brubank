package clients

import clients.Exception.BadResponse

class WorldWeatherClient extends ApiClient {
    private String apiKey = "3479616aa74241fe8f8195526181808"
    private String dateFormat = "yyyy-MM-dd"

    public WorldWeatherClient() {
        baseUrl = "https://api.worldweatheronline.com"
    }

    def getHourlyTemperatureSamples(LinkedHashMap searchParams) {

        def response = get("/premium/v1/past-weather.ashx", [
                key   : apiKey,
                q     : searchParams.location,
                format: "json",
                date  : searchParams.date.format(dateFormat)])

        checkResponseStatus(response)

        List tempC = response.json.data?.weather?.hourly?.tempC?.flatten()
        tempC.collect { String temp -> temp.toInteger() }
    }

    static checkResponseStatus(response) {
        if (response.status != 200) throw new BadResponse("Unexpected response status (${response.status})", response.status as Integer)
        if (response.json.data.error) throw new BadResponse(response.json.data.error[0].msg as String, response.status as Integer)
    }
}
