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

// Класс для записей о погоде
data class WeatherRecord(
    val date: LocalDate,      // Дата записи о погоде
    val city: String,         // Город
    val temperature: Double,  // Температура в градусах Цельсия
    val humidity: Double,     // Влажность в процентах
    val precipitation: Double // Осадки в миллиметрах
)

// Класс для города
data class City(
    val name: String,                                                // Название города
    val country: String,                                             // Страна, в которой находится город
    val coordinates: Pair<Double, Double>,                           // Широта и долгота
    val weatherRecords: MutableList<WeatherRecord> = mutableListOf() // Список записей о погоде
) {
    // Функция для добавления записи о погоде
    fun addWeatherRecord(record: WeatherRecord) {
        weatherRecords.add(record)
    }

    // Функция для вычисления средней температуры за период
    fun averageTemperature(): Double {
        return weatherRecords.map { it.temperature }.average()
    }

    // Функция для вычисления средней влажности за период
    fun averageHumidity(): Double {
        return weatherRecords.map { it.humidity }.average()
    }

    // Функция для вычисления среднего количества осадков за период
    fun averagePrecipitation(): Double {
        return weatherRecords.map { it.precipitation }.average()
    }

    // Функция для поиска экстремальной температуры (минимальной и максимальной)
    fun extremeTemperatures(): Pair<Double?, Double?> {
        return weatherRecords.map { it.temperature } // Извлекаем температуру из каждой записи и создаем список температур
            .let { it.minOrNull() to it.maxOrNull() }
    }

    // Функция для поиска экстремальной влажности (минимальной и максимальной)
    fun extremeHumidity(): Pair<Double?, Double?> {
        return weatherRecords.map { it.humidity } // Извлекаем влажность из каждой записи и создаем список влажностей
            .let { it.minOrNull() to it.maxOrNull() }
    }

    // Функция для поиска экстремальных осадков (минимальных и максимальных)
    fun extremePrecipitation(): Pair<Double?, Double?> {
        return weatherRecords.map { it.precipitation } // Извлекаем осадки из каждой записи и создаем список осадков
            .let { it.minOrNull() to it.maxOrNull() }
    }

    // Функция для сравнения погодных условий между двумя городами
    fun compareWeatherWith(otherCity: City): String {
        return listOf(averageTemperature() to otherCity.averageTemperature())  // Создаем список с парой средних температур
            .let {
                when {
                    it[0].first > it[0].second -> "${this.name} теплее, чем ${otherCity.name}"
                    it[0].first < it[0].second -> "${this.name} холоднее, чем ${otherCity.name}"
                    else -> "${this.name} и ${otherCity.name} имеют одинаковую среднюю температуру"
                }
            }
    }
}

// Функция для сбора данных о погоде
fun collectWeatherData(city: City, record: WeatherRecord) {
    city.addWeatherRecord(record)
}

// Функция для анализа тенденций, например, выявление повышения или понижения температуры
fun analyzeTemperatureTrends(city: City): String {
    return city.weatherRecords.map { it.temperature } // Извлекаем температуру из каждой записи и создаем список температур
        .zipWithNext { a, b -> b - a }  // Список разниц температур между соседними днями
        .let { temperatureChanges ->  // Внутри let доступен список разниц температур
            when {
                temperatureChanges.all { it > 0 } -> "Температура в г. ${city.name} растет" // Все изменения положительные -> температура растет
                temperatureChanges.all { it < 0 } -> "Температура в г. ${city.name} падает" // Все изменения отрицательные -> температура падает
                else -> "Температура в г. ${city.name} колеблется" // Есть как рост, так и падение температуры -> колеблется
            }
        }
}

fun main() {
    // Создание городов и добавление погодных записей
    val city1 = City("Москва", "Россия", Pair(55.7558, 37.6173))
    collectWeatherData(city1, WeatherRecord(LocalDate.of(2024, 12, 1), "Москва", -5.0, 80.0, 0.0))
    collectWeatherData(city1, WeatherRecord(LocalDate.of(2024, 12, 2), "Москва", -3.0, 85.0, 1.0))
    collectWeatherData(city1, WeatherRecord(LocalDate.of(2024, 12, 3), "Москва", 0.0, 75.0, 0.0))

    val city2 = City("Санкт-Петербург", "Россия", Pair(59.9343, 30.3351))
    collectWeatherData(city2, WeatherRecord(LocalDate.of(2024, 12, 1), "Санкт-Петербург", 1.0, 78.0, 0.0))
    collectWeatherData(city2, WeatherRecord(LocalDate.of(2024, 12, 2), "Санкт-Петербург", 0.0, 80.0, 0.5))
    collectWeatherData(city2, WeatherRecord(LocalDate.of(2024, 12, 3), "Санкт-Петербург", -2.0, 70.0, 0.0))

    // Анализ температурных тенденций
    println(analyzeTemperatureTrends(city1))
    println(analyzeTemperatureTrends(city2))

    // Сравнение температуры между городами
    println(city1.compareWeatherWith(city2))

    // Вывод средних значений температуры и осадков для города Москва
    println("\nСредняя температура в г. ${city1.name}: ${String.format(Locale.US, "%.2f", city1.averageTemperature())}")
    println("Средняя влажность в г. ${city1.name}: ${String.format(Locale.US, "%.2f", city1.averageHumidity())}")
    println("Средние осадки в г. ${city1.name}: ${String.format(Locale.US, "%.2f", city1.averagePrecipitation())}")

    // Поиск экстремальных температур, влажностей и осадков для города Москва
    val extremeTempsCity1 = city1.extremeTemperatures()
    println("Экстремальные температуры в г. ${city1.name}: от ${extremeTempsCity1.first} до ${extremeTempsCity1.second}")
    val extremeHumidityCity1 = city1.extremeHumidity()
    println("Экстремальная влажность в г. ${city1.name}: от ${extremeHumidityCity1.first} до ${extremeHumidityCity1.second}")
    val extremePrecipCity1 = city1.extremePrecipitation()
    println("Экстремальные осадки в г. ${city1.name}: от ${extremePrecipCity1.first} до ${extremePrecipCity1.second}")

    // Вывод средних значений температуры и осадков для города Санкт-Петербург
    println("\nСредняя температура в г. ${city2.name}: ${String.format(Locale.US, "%.2f", city2.averageTemperature())}")
    println("Средняя влажность в г. ${city2.name}: ${String.format(Locale.US, "%.2f", city2.averageHumidity())}")
    println("Средние осадки в г. ${city2.name}: ${String.format(Locale.US, "%.2f", city2.averagePrecipitation())}")

    // Поиск экстремальных температур, влажностей и осадков для города Санкт-Петербург
    val extremeTempsCity2 = city2.extremeTemperatures()
    println("Экстремальные температуры в г. ${city2.name}: от ${extremeTempsCity2.first} до ${extremeTempsCity2.second}")
    val extremeHumidityCity2 = city2.extremeHumidity()
    println("Экстремальная влажность в г. ${city2.name}: от ${extremeHumidityCity2.first} до ${extremeHumidityCity2.second}")
    val extremePrecipCity2 = city2.extremePrecipitation()
    println("Экстремальные осадки в г. ${city2.name}: от ${extremePrecipCity2.first} до ${extremePrecipCity2.second}")
}
