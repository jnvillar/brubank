package cache

import brubank.AverageTemperature
import dateUtils.DateUtils

@Singleton
class TemperaturesCache implements CacheIO<LinkedHashMap, BigDecimal, AverageTemperature> {

    AverageTemperature get(LinkedHashMap searchParams) {
        AverageTemperature.findByLocationAndDate(searchParams.location, searchParams.date)
    }

    AverageTemperature put(LinkedHashMap creationParams, BigDecimal averageTemp) {
        AverageTemperature avgLocation = AverageTemperature.findOrCreateByLocationAndDate(creationParams.location, creationParams.date)
        avgLocation.averageTemperature = averageTemp
        avgLocation.save(flush: true, failOnError: true)
        avgLocation
    }
}
