import java.time.LocalDate
import java.util.*

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
            .let { it.minOrNull() to it.maxOrNull() }
    }

    // Функция для поиска экстремальных осадков
    fun extremePrecipitation(): Pair<Double?, Double?> {
        return weatherRecords.map { it.precipitation }
            .let { it.minOrNull() to it.maxOrNull() }
    }

    // Функция для сравнения погодных условий между двумя городами
    fun compareWeatherWith(otherCity: City): String {
        val avgTempThisCity = averageTemperature()
        val avgTempOtherCity = otherCity.averageTemperature()

        return when {
            avgTempThisCity > avgTempOtherCity -> "${this.name} теплее, чем ${otherCity.name}"
            avgTempThisCity < avgTempOtherCity -> "${this.name} холоднее, чем ${otherCity.name}"
            else -> "${this.name} и ${otherCity.name} имеют одинаковую среднюю температуру"
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
        .zipWithNext { a, b -> b - a }  // Список разниц температур между соседними днями
        .let { temperatureChanges ->  // Внутри let доступен список разниц температур
            when {
                temperatureChanges.all { it > 0 } -> "Температура в г. ${city.name} растет"
                temperatureChanges.all { it < 0 } -> "Температура в г. ${city.name} падает"
                else -> "Температура в г. ${city.name} колеблется"
            }
        }
}

fun main() {
    // Создание городов и добавление погодных записей
    val city1 = City("Москва", "Россия", Pair(55.7558, 37.6173))
    collectWeatherData(city1, WeatherRecord(LocalDate.of(2024, 12, 1), -5.0, 80.0, 0.0))
    collectWeatherData(city1, WeatherRecord(LocalDate.of(2024, 12, 2), -3.0, 85.0, 1.0))
    collectWeatherData(city1, WeatherRecord(LocalDate.of(2024, 12, 3), 0.0, 75.0, 0.0))

    val city2 = City("Санкт-Петербург", "Россия", Pair(59.9343, 30.3351))
    collectWeatherData(city2, WeatherRecord(LocalDate.of(2024, 12, 1), 1.0, 78.0, 0.0))
    collectWeatherData(city2, WeatherRecord(LocalDate.of(2024, 12, 2), 0.0, 80.0, 0.5))
    collectWeatherData(city2, WeatherRecord(LocalDate.of(2024, 12, 3), -2.0, 70.0, 0.0))

    // Анализ температурных тенденций
    println(analyzeTemperatureTrends(city1))
    println(analyzeTemperatureTrends(city2))

    // Сравнение температуры между городами
    println(city1.compareWeatherWith(city2))

    // Вывод средних значений температуры и осадков для города Москва
    println("\nСредняя температура в г. ${city1.name}: ${String.format(Locale.US, "%.2f", city1.averageTemperature())}")
    println("Средние осадки в г. ${city1.name}: ${String.format(Locale.US, "%.2f", city1.averagePrecipitation())}")

    // Поиск экстремальных температур и осадков
    val extremeTempsCity1 = city1.extremeTemperatures()
    println("Экстремальные температуры в г. ${city1.name}: ${extremeTempsCity1.first} до ${extremeTempsCity1.second}")

    val extremePrecipCity1 = city1.extremePrecipitation()
    println("Экстремальные осадки в г. ${city1.name}: ${extremePrecipCity1.first} до ${extremePrecipCity1.second}")

    // Вывод средних значений температуры и осадков для города Санкт-Петербург
    println("\nСредняя температура в г. ${city2.name}: ${String.format(Locale.US, "%.2f", city2.averageTemperature())}")
    println("Средние осадки в г. ${city2.name}: ${String.format(Locale.US, "%.2f", city2.averagePrecipitation())}")

    // Поиск экстремальных температур и осадков
    val extremeTempsCity2 = city2.extremeTemperatures()
    println("Экстремальные температуры в г. ${city2.name}: ${extremeTempsCity2.first} до ${extremeTempsCity2.second}")

    val extremePrecipCity2 = city2.extremePrecipitation()
    println("Экстремальные осадки в г. ${city2.name}: ${extremePrecipCity2.first} до ${extremePrecipCity2.second}")
}
