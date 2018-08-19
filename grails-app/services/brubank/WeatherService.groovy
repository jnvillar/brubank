package brubank

import cache.TemperaturesCache
import clients.WorldWeatherClient

class WeatherService {
    WorldWeatherClient weatherClient
    TemperaturesCache temperaturesCache = TemperaturesCache.instance

    def getAverageTemperaturesOfLocationOn(String location, List<Date> dates) {
        List<BigDecimal> averageTemperatures = []

        dates.each { Date date ->
            averageTemperatures.add(getAverageTemperatureForLocationOn([location: location, date: date]))
        }

        averageTemperatures
    }

    def getAverageTemperatureForLocationOn(LinkedHashMap searchParams) {
        AverageTemperature averageTemperature = temperaturesCache.get(searchParams)
        if (averageTemperature) return averageTemperature.averageTemperature

        List<Integer> temperatures = weatherClient.getHourlyTemperatureSamples(searchParams)
        BigDecimal averageTemp = temperatures.sum() / ([temperatures.size(), 1].max())
        temperaturesCache.put(searchParams, averageTemp)
        averageTemp
    }
}
