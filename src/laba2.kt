/**
 * 5 вариант
 * Разработайте иерархию классов для транспортных средств.
 * Базовый класс Vehicle:
 * - Свойства: make, model, year.
 * - Методы: start(), stop(), displayInfo().
 * Наследники:
 * - Car: свойства numberOfDoors, fuelType.
 * - Bicycle: свойства type (горный, шоссейный), hasBell.
 * - Bus: свойства capacity, routeNumber.
 * Класс FleetManager управляет парком транспортных средств, предоставляет методы
 * для добавления, обслуживания, назначения на маршруты.
 * Консольное приложение должно позволять:
 * - Регистрировать транспортные средства.
 * - Планировать обслуживание.
 * - Назначать транспорт на маршруты.
 * - Отображать состояние парка
 */

// Перечисление для типа велосипеда
enum class BicycleType(val typeName: String) {
    MOUNTAIN("Горный"),
    ROAD("Шоссейный")
}

// Класс Vehicle (Транспортное средство)
open class Vehicle(val make: String, val model: String, private val year: Int) {

    // Метод для запуска транспортного средства
    open fun start() {
        println("Транспортное средство $make $model запускается.")
    }

    // Метод для остановки транспортного средства
    open fun stop() {
        println("Транспортное средство $make $model останавливается.")
    }

    // Метод для отображения информации о транспортном средстве
    open fun displayInfo() {
        println("Марка: $make, Модель: $model, Год выпуска: $year")
    }
}

// Класс Car (Автомобиль)
class Car(make: String, model: String, year: Int, private val numberOfDoors: Int, private val fuelType: String) : Vehicle(make, model, year) {

    // Переопределение метода для запуска автомобиля
    override fun start() {
        println("Автомобиль $make $model запускается с топливом $fuelType.")
    }

    // Переопределение метода для остановки автомобиля
    override fun stop() {
        println("Автомобиль $make $model останавливается.")
    }

    // Переопределение метода для отображения информации об автомобиле
    override fun displayInfo() {
        super.displayInfo()
        println("Количество дверей: $numberOfDoors, Тип топлива: $fuelType")
    }
}

// Класс Bicycle (Велосипед)
class Bicycle(make: String, model: String, year: Int, private val type: BicycleType, private val hasBell: Boolean) : Vehicle(make, model, year) {

    // Переопределение метода для запуска велосипеда
    override fun start() {
        println("Велосипед $make $model запускается.")
    }

    // Переопределение метода для остановки велосипеда
    override fun stop() {
        println("Велосипед $make $model останавливается.")
    }

    // Переопределение метода для отображения информации о велосипеде
    override fun displayInfo() {
        super.displayInfo()
        println("Тип: ${type.typeName}, Есть звонок: $hasBell")
    }
}

// Класс Bus (Автобус)
class Bus(make: String, model: String, year: Int, private val capacity: Int, val routeNumber: String) : Vehicle(make, model, year) {

    // Переопределение метода для запуска автобуса
    override fun start() {
        println("Автобус $make $model запускается на маршруте $routeNumber.")
    }

    // Переопределение метода для остановки автобуса
    override fun stop() {
        println("Автобус $make $model останавливается.")
    }

    // Переопределение метода для отображения информации об автобусе
    override fun displayInfo() {
        super.displayInfo()
        println("Вместимость: $capacity, Номер маршрута: $routeNumber")
    }
}

// Класс FleetManager для управления парком транспортных средств
class FleetManager {
    private val fleet = mutableListOf<Vehicle>() // Список всех транспортных средств в парке

    // Метод для регистрации нового транспортного средства в парке
    fun registerVehicle(vehicle: Vehicle) {
        fleet.add(vehicle)
        println("Транспортное средство зарегистрировано: ${vehicle.make} ${vehicle.model}")
    }

    // Метод для планирования обслуживания всех транспортных средств
    fun planMaintenance() {
        println("\nПланирование обслуживания для всех транспортных средств в парке.")
        for (vehicle in fleet) {
            println("Запланировано обслуживание для ${vehicle.make} ${vehicle.model}")
        }
    }

    // Метод для назначения автобуса на маршрут
    fun assignVehicleToRoute(vehicle: Bus) {
        println("\nНазначение автобуса ${vehicle.make} ${vehicle.model} на маршрут ${vehicle.routeNumber}")
    }

    // Метод для отображения состояния всего парка
    fun displayFleetStatus() {
        println("\nСостояние парка:")
        for (vehicle in fleet) {
            vehicle.displayInfo()
        }
    }
}

fun main() {
    val fleetManager = FleetManager() // Создание менеджера парка

    // Регистрируем транспортные средства
    val car = Car("Toyota", "Camry", 2020, 4, "Бензин")
    val bicycle = Bicycle("Giant", "XTC", 2022, BicycleType.MOUNTAIN, true)
    val bus = Bus("Mercedes", "Sprinter", 2018, 50, "101")

    fleetManager.registerVehicle(car)
    fleetManager.registerVehicle(bicycle)
    fleetManager.registerVehicle(bus)

    // Планируем обслуживание
    fleetManager.planMaintenance()

    // Назначаем автобус на маршрут
    fleetManager.assignVehicleToRoute(bus)

    // Отображаем состояние парка
    fleetManager.displayFleetStatus()
}
