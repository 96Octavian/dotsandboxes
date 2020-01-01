// Holds a player's data
class Player(private val Number: Int) {
    var score: Int = 0
    var name: String? = null
        get() = if (field == null) Number.toString() else field
}

// Handles the game flow
class ScoreBoard(private val board: Board, somePlayers: List<Player>) {

    private val players: List<Player> = somePlayers
    private var currentPlayer: Int = 0

    // Setup handler
    companion object {
        fun setup(board: Board): ScoreBoard {
            // Ask how many participating players
            var num: Int? = null
            while (num == null) {
                print("Player number: ")
                num = readLine()?.trim()?.toIntOrNull()
            }
            return ScoreBoard(board, List(num) { i -> Player(i) })
        }
    }

    // Provides a list of players ordered by score
    private fun getScore(): List<Player> {
        return players.sortedBy { player -> player.score }
    }

    // Check if the given line closes a square
    private fun checkClosedSquareAround(starting: IBoardCell, direction: Directions): Boolean {

        // Check if the scquare is closed with the cell at its right and under (for both vertical and horizontal lines)
        val closed: Boolean = try {
            val one = board.getCell(starting.x + 1, starting.y)
            val two = board.getCell(starting.x, starting.y + 1)
            one.vertical && two.horizontal && starting.vertical
        } catch (e: IndexOutOfBoardException) {
            false
        }
        val closedOver: Boolean = if (direction == Directions.HORIZONTAL) {
            // If the line is horizontal we have to check if the square is closed with the cells above
            try {
                val three = board.getCell(starting.x, starting.y - 1)
                val four = board.getCell(three.x + 1, three.y)
                three.horizontal && three.vertical && four.vertical
            } catch (e: IndexOutOfBoardException) {
                false
            }
        } else {
            // If the line is vertical we have to check if the square is closed with the cells to the left
            try {
                val three = board.getCell(starting.x - 1, starting.y)
                val four = board.getCell(three.x, three.y + 1)
                three.horizontal && three.vertical && four.vertical
            } catch (e: IndexOutOfBoardException) {
                false
            }
        }
        return closed || closedOver
    }

    // Retrieve the drawn line and check if it closes a square
    private fun checkClosedSquare(column: Int, row: Int): Boolean {

        val (x, y, dir) = board.translate(column, row)
        val original = board.getCellEasy(x, y)

        return checkClosedSquareAround(original, dir)

    }

    // Main loop
    fun play() {

        val coordinates: MutableList<Int?> = mutableListOf()

        // Ask every player if he wants to set a custom name
        for (player in players) {
            var answer: String? = null
            while (answer == null) {
                println("Player ${player.name}, do you want to change your name (y/n)?")
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
                player.name = answer
            }
        }

        // Every board has a maximum number of drawable lines. End the game after this many lines are drawn
        var availableMoves: Int = 2 * (board.columns - 1) * (board.rows - 1) + board.columns - 1 + board.rows + -1
        while (availableMoves > 0) {
            coordinates.clear()
            println(board.getMap())
            println("--> Player " + players[currentPlayer].name)

            // Ask the player where does he wants to draw a line
            println("Insert the coordinates:")
            readLine()?.let { line ->
                line.trim().split("\\s+".toRegex()).map {
                    coordinates.add(it.trim().toIntOrNull())
                }
            }
            // Sanity check the input
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
                // Check if coordinates are valid
                if (result.first) {

                    // A move has been done
                    availableMoves--

                    // Check if a square has been closed and in that case, assign a point and play again
                    if (checkClosedSquare(coordinates[0]!!, coordinates[1]!!)) {
                        players[currentPlayer].score++
                        println("Player ${players[currentPlayer].name} just scored!")
                        println("Score:")
                        for (player in getScore()) {
                            println(player.name + ":\t" + player.score)
                        }
                        continue
                    }
                } else
                    continue
            }

            // Change player
            currentPlayer++
            if (currentPlayer == players.size)
                currentPlayer = 0
        }

        // Game ended, print scoreboard and goodbye
        println("Score:")
        for (player in getScore()) {
            println(player.name + ":\t" + player.score)
        }
        println("#################\n### Game Over ###\n#################")

    }
}