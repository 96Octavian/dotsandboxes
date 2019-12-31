class Player(val Number: Int) {
    var score: Int = 0
    var Name: String? = null
        get() = if (field == null) Number.toString() else field
        set(value) {
            field = value
        }
}

class ScoreBoard(val board: Board, somePlayers: List<Player>) {

    private val players: List<Player> = somePlayers
    private var currentPlayer: Int = 0

    companion object {
        fun setup(board: Board): ScoreBoard {
            var num: Int? = null
            while (num == null) {
                print("Player number: ")
                num = readLine()?.trim()?.toIntOrNull()
            }
            return ScoreBoard(board, List(num) { i -> Player(i) })
        }
    }

    private fun checkClosedSquare() {
        // TODO: Implement
    }

    fun play() {

        val coordinates: MutableList<Int?> = mutableListOf()

        // Ask every player if he wants to set a custom name
        for (player in players) {
            var answer: String? = null
            while (answer == null) {
                println("Player ${player.Name}, do you want to change your name (y/n)?")
                val tmp = readLine()?.trim()
                if (tmp != null && (tmp == "y" || tmp == "n"))
                    answer = tmp
            }
            if (answer == "y") {
                answer = null
                while (answer == null) {
                    print("Insert name: ")
                    answer = readLine()?.trim()
                }
                player.Name = answer
            }
        }

        while (true) {
            println(players.get(0).Name)
            coordinates.clear()
            println("Insert the coordinates:")
            readLine()?.let { line ->
                line.trim().split("\\s+".toRegex()).map {
                    coordinates.add(it.trim().toIntOrNull())
                }
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
                val result: Pair<Boolean, String> = board.setLineEasy(coordinates[0]!!, coordinates[1]!!)
                println(result.second)
                if (result.first)
                    println(board.getMap())
            }

            checkClosedSquare()

            currentPlayer++
        }

    }
}