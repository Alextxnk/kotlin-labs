import java.time.LocalDate

/**
 * 6 вариант
 * Анализ данных о погоде.
 * 1) Разработайте следующие data-классы:
 * - Запись о погоде: дата, город, температура, влажность, осадки.
 * - Город: название, страна, координаты, список записей погоды.
 * 2) Используя коллекции и лямбда-выражения, создайте функции для:
 * - Сбора и хранения данных о погоде.
 * - Анализа погодных тенденций.
 * - Вычисления средних значений температуры и осадков за период.
 * - Поиска экстремальных значений.
 * - Сравнения погодных условий между городами.
 * 3) Создайте приложение, представляющее наполнять базу данными о погоде, создавать
 * и выводить отчёты по погодным данным, позволяя пользователям изучать исторические
 * данные и тенденции.
 */

// Класс для записи о погоде
data class WeatherRecord(
    val date: LocalDate,
    val temperature: Double,  // температура в градусах Цельсия
    val humidity: Double,     // влажность в процентах
    val precipitation: Double // осадки в миллиметрах
)

// Класс для города
data class City(
    val name: String,
    val country: String,
    val coordinates: Pair<Double, Double>, // Широта и долгота
    val weatherRecords: MutableList<WeatherRecord> = mutableListOf()
) {
    // Функция для добавления записи о погоде
    fun addWeatherRecord(record: WeatherRecord) {
        weatherRecords.add(record)
    }

    // Функция для вычисления средней температуры за период
    fun averageTemperature(): Double {
        return weatherRecords.map { it.temperature }.average()
    }

    // Функция для вычисления средних осадков за период
    fun averagePrecipitation(): Double {
        return weatherRecords.map { it.precipitation }.average()
    }

    // Функция для поиска экстремальных температур
    fun extremeTemperatures(): Pair<Double?, Double?> {
        return weatherRecords.map { it.temperature }
            .let { temperatures -> temperatures.minOrNull() to temperatures.maxOrNull() }
    }

    // Функция для поиска экстремальных осадков
    fun extremePrecipitation(): Pair<Double?, Double?> {
        return weatherRecords.map { it.precipitation }
            .let { precipitations -> precipitations.minOrNull() to precipitations.maxOrNull() }
    }

    // Функция для сравнения погодных условий между двумя городами
    fun compareWeatherWith(otherCity: City): String {
        val avgTempThisCity = averageTemperature()
        val avgTempOtherCity = otherCity.averageTemperature()

        return when {
            avgTempThisCity > avgTempOtherCity -> "${this.name} теплее, чем ${otherCity.name}."
            avgTempThisCity < avgTempOtherCity -> "${this.name} холоднее, чем ${otherCity.name}."
            else -> "${this.name} и ${otherCity.name} имеют одинаковую среднюю температуру."
        }
    }
}

// Функция для сбора данных о погоде
fun collectWeatherData(city: City, record: WeatherRecord) {
    city.addWeatherRecord(record)
}

// Функция для анализа тенденций, например, выявление повышения или понижения температуры
fun analyzeTemperatureTrends(city: City): String {
    return city.weatherRecords.map { it.temperature }
        .let { temperatures ->
            when {
                temperatures.all { it > temperatures.first() } -> "Температура растет."
                temperatures.all { it < temperatures.first() } -> "Температура падает."
                else -> "Температура колеблется."
            }
        }
}

fun main() {
    // Создание города и добавление погодных записей
    val city1 = City("Москва", "Россия", Pair(55.7558, 37.6173))
    collectWeatherData(city1, WeatherRecord(LocalDate.of(2024, 12, 1), -5.0, 80.0, 0.0))
    collectWeatherData(city1, WeatherRecord(LocalDate.of(2024, 12, 2), -3.0, 85.0, 1.0))
    collectWeatherData(city1, WeatherRecord(LocalDate.of(2024, 12, 3), 0.0, 75.0, 0.0))

    val city2 = City("Санкт-Петербург", "Россия", Pair(59.9343, 30.3351))
    collectWeatherData(city2, WeatherRecord(LocalDate.of(2024, 12, 1), -3.0, 78.0, 0.0))
    collectWeatherData(city2, WeatherRecord(LocalDate.of(2024, 12, 2), -2.0, 80.0, 0.5))
    collectWeatherData(city2, WeatherRecord(LocalDate.of(2024, 12, 3), 1.0, 70.0, 0.0))

    // Анализ тенденций
    println(analyzeTemperatureTrends(city1))

    // Сравнение температуры между городами
    println(city1.compareWeatherWith(city2))

    // Вывод средней температуры и осадков для города
    println("Средняя температура в г. ${city1.name}: ${city1.averageTemperature()}")

    println("Средние осадки в г. ${city1.name}: ${city1.averagePrecipitation()}")

    // Поиск экстремальных температур и осадков
    val extremeTemps = city1.extremeTemperatures()
    println("Экстремальные температуры в г. ${city1.name}: ${extremeTemps.first} до ${extremeTemps.second}")

    val extremePrecip = city1.extremePrecipitation()
    println("Экстремальные осадки в г. ${city1.name}: ${extremePrecip.first} до ${extremePrecip.second}")
}
