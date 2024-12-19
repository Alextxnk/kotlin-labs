import kotlin.math.sqrt
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.abs

/**
 * 7 вариант
 * Разработайте программу для работы с геометрическими фигурами на координатной плоскости. Программа должна:
 * 1. Функция ввода координат: Принимает от пользователя координаты вершин многоугольника.
 * 2. Функция вычисления периметра и площади: Рассчитывает периметр и площадь заданного многоугольника.
 * 3. Функция обнаружения пересечений: Вводит координаты двух многоугольников и проверяет, пересекаются ли они на плоскости.
 * Если многоугольники пересекаются, возвращает площадь области пересечения.
 * Для простоты считать, что все многоугольники выпуклые.
 */

// Функция ввода координат вершин многоугольника
fun inputPolygon(): List<Pair<Double, Double>> {
    var n: Int
    // Цикл для повторного ввода количества вершин, если введено меньше 3
    while (true) {
        println("Введите количество вершин многоугольника (целое число, не менее 3):")
        n = readlnOrNull()?.toIntOrNull() ?: continue

        if (n >= 3) break
        println("Ошибка: Количество вершин должно быть не менее трех!")
    }

    val points = mutableListOf<Pair<Double, Double>>()
    println("Введите координаты вершин в формате x y:")

    var i = 1
    // Цикл для повторного ввода координат вершин, если они введены неправильно
    while (i <= n) {
        val input = readlnOrNull()?.split(" ") ?: continue

        if (input.size != 2) {
            println("Ошибка: Введите два числа (x и y) для каждой вершины!")
            continue
        }

        val x = input[0].toDoubleOrNull()
        val y = input[1].toDoubleOrNull()

        if (x != null && y != null) {
            points.add(Pair(x, y))
            i++
        } else {
            println("Ошибка: Координаты должны быть числами!")
        }
    }
    return points
}

// Функция вычисления длины стороны многоугольника
fun distance(p1: Pair<Double, Double>, p2: Pair<Double, Double>): Double {
    return sqrt((p2.first - p1.first).pow(2) + (p2.second - p1.second).pow(2))
}

// Функция вычисления периметра многоугольника
fun calculatePerimeter(polygon: List<Pair<Double, Double>>): Double {
    return polygon.indices.sumOf { distance(polygon[it], polygon[(it + 1) % polygon.size]) }
}

// Функция вычисления площади многоугольника по формуле Гаусса
fun calculateArea(polygon: List<Pair<Double, Double>>): Double {
    return if (polygon.size < 3) 0.0 else
        abs(polygon.indices.sumOf { (polygon[it].first * polygon[(it + 1) % polygon.size].second) - (polygon[(it + 1) % polygon.size].first * polygon[it].second) }) / 2
}

// Функция проверки пересечения двух отрезков
fun edgesIntersect(p1: Pair<Double, Double>, p2: Pair<Double, Double>, q1: Pair<Double, Double>, q2: Pair<Double, Double>): Boolean {
    fun orientation(a: Pair<Double, Double>, b: Pair<Double, Double>, c: Pair<Double, Double>): Int {
        val value = (b.second - a.second) * (c.first - b.first) - (b.first - a.first) * (c.second - b.second)
        return when {
            value == 0.0 -> 0
            value > 0 -> 1
            else -> -1
        }
    }

    val o1 = orientation(p1, p2, q1)
    val o2 = orientation(p1, p2, q2)
    val o3 = orientation(q1, q2, p1)
    val o4 = orientation(q1, q2, p2)

    return o1 != o2 && o3 != o4
}

// Функция нахождения точки пересечения двух отрезков
fun lineSegmentIntersection(p1: Pair<Double, Double>, p2: Pair<Double, Double>, q1: Pair<Double, Double>, q2: Pair<Double, Double>): Pair<Double, Double>? {
    val a1 = p2.second - p1.second
    val b1 = p1.first - p2.first
    val c1 = a1 * p1.first + b1 * p1.second

    val a2 = q2.second - q1.second
    val b2 = q1.first - q2.first
    val c2 = a2 * q1.first + b2 * q1.second

    val determinant = a1 * b2 - a2 * b1
    if (determinant == 0.0) return null // Отрезки параллельны или совпадают

    val x = (b2 * c1 - b1 * c2) / determinant
    val y = (a1 * c2 - a2 * c1) / determinant

    return if (isOnSegment(p1, p2, Pair(x, y)) && isOnSegment(q1, q2, Pair(x, y))) Pair(x, y) else null
}

// Функция проверки, лежит ли точка на отрезке
fun isOnSegment(p1: Pair<Double, Double>, p2: Pair<Double, Double>, point: Pair<Double, Double>): Boolean {
    return point.first in minOf(p1.first, p2.first)..maxOf(p1.first, p2.first) &&
            point.second in minOf(p1.second, p2.second)..maxOf(p1.second, p2.second)
}

// Функция проверки, находится ли точка внутри многоугольника
fun pointInsidePolygon(point: Pair<Double, Double>, polygon: List<Pair<Double, Double>>): Boolean {
    val extreme = Pair(Double.MAX_VALUE, point.second)
    return polygon.indices.count { edgesIntersect(point, extreme, polygon[it], polygon[(it + 1) % polygon.size]) } % 2 == 1
}

// Функция вычисления площади пересечения многоугольников
fun calculateIntersectionArea(polygon1: List<Pair<Double, Double>>, polygon2: List<Pair<Double, Double>>): Double {
    val intersectionPoints = mutableSetOf<Pair<Double, Double>>()

    // Находим все точки пересечения
    for (i in polygon1.indices) {
        for (j in polygon2.indices) {
            lineSegmentIntersection(
                polygon1[i], polygon1[(i + 1) % polygon1.size],
                polygon2[j], polygon2[(j + 1) % polygon2.size]
            )?.let { intersectionPoints.add(it) }
        }
    }

    // Добавляем точки, которые лежат внутри другого многоугольника
    intersectionPoints.addAll(polygon1.filter { pointInsidePolygon(it, polygon2) })
    intersectionPoints.addAll(polygon2.filter { pointInsidePolygon(it, polygon1) })

    val uniquePoints = intersectionPoints.toList()
    println("Точки пересечения: $uniquePoints")
    println("Количество точек пересечения: ${uniquePoints.size}")

    if (uniquePoints.size < 3) return 0.0

    val center = Pair(uniquePoints.sumOf { it.first } / uniquePoints.size, uniquePoints.sumOf { it.second } / uniquePoints.size)
    val sortedPoints = uniquePoints.sortedBy { atan2(it.second - center.second, it.first - center.first) }

    return calculateArea(sortedPoints)
}

fun main() {
    println("Введите первый многоугольник:")
    val polygon1 = inputPolygon()
    println("Введите второй многоугольник:")
    val polygon2 = inputPolygon()

    val perimeter1 = calculatePerimeter(polygon1)
    val area1 = calculateArea(polygon1)
    val perimeter2 = calculatePerimeter(polygon2)
    val area2 = calculateArea(polygon2)

    println("Периметр первого многоугольника: ${"%.2f".format(perimeter1)}")
    println("Площадь первого многоугольника: ${"%.2f".format(area1)}")
    println("Периметр второго многоугольника: ${"%.2f".format(perimeter2)}")
    println("Площадь второго многоугольника: ${"%.2f".format(area2)}")

    val intersectionArea = calculateIntersectionArea(polygon1, polygon2)
    if (intersectionArea > 0) {
        println("Многоугольники пересекаются!")
        println("Площадь области пересечения: ${"%.2f".format(intersectionArea)}")
    } else {
        println("Многоугольники не пересекаются!")
    }
}
