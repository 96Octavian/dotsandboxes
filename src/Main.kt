fun main() {

    val board = Board(4, 3)

    println(board.getMap())

    val coordinates: MutableList<Int?> = mutableListOf()

    while (true) {
        coordinates.clear()
        println("Insert the coordinates:")
        readLine()?.let { line ->
            line.trim().split("\\s".toRegex()).map {
                coordinates.add(it.trim().toIntOrNull())
            } //TODO: Accetta molti spazi
        }
        if (coordinates.contains(null)) {
            println("Please input only numbers")
            continue
        } else if (coordinates.size == 1 && coordinates[0] == 0) {
            println("Bye!")
            break
        } else if (coordinates.size != 2) {
            println("Please input only two numbers")
            continue
        } else {
            val result : Pair<Boolean, String> = board.setLineEasy(coordinates[0]!!, coordinates[1]!!)
            println(result.second)
            if (result.first)
                println(board.getMap())
        }

    }

}