import java.lang.Exception

enum class Directions {
    VERTICAL,
    HORIZONTAL
}

class IndexOutOfBoardException(message: String) : Exception(message)

// Public interface for cell containing coordinates and lines drawn
interface IBoardCell {
    val x: Int
    val y: Int
    val vertical: Boolean
    val horizontal: Boolean
}

// Holds coordinates and lines drawn for a cell, and provides representation
private class BoardCell(override val x: Int, override val y: Int) : IBoardCell {
    override var vertical: Boolean = false
    override var horizontal: Boolean = false

    // Provide indication if the line is drawn
    fun toString(direction: Directions): String {
        var symbol = "   "
        if (direction == Directions.HORIZONTAL && horizontal)
            symbol = " - "
        else if (direction == Directions.VERTICAL && vertical)
            symbol = " | "
        return symbol
    }

    override fun equals(other: Any?): Boolean {
        return other is BoardCell && other.x == x && other.y == y
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}

// Handles every operation on the gaem board
class Board(val columns: Int, val rows: Int) {

    // Setup handler
    companion object {
        fun setup(): Board {
            // Ask for columns number
            var col: Int? = null
            while (col == null) {
                print("Columns number: ")
                col = readLine()?.trim()?.toIntOrNull()
                if (col != null && col < 2) {
                    println("Minimum is 2")
                    col = null
                }
            }
            // Ask for rows number
            var row: Int? = null
            while (row == null) {
                print("Rows number: ")
                row = readLine()?.trim()?.toIntOrNull()
                if (row != null && row < 2) {
                    println("Minimum is 2")
                    row = null
                }
            }
            return Board(col, row)
        }
    }

    /*
     * The matrix holds only vertical/horizontal line pairs. It has an extra row and column which can hold respectively
     * only horizontal and vertical lines: this is because every cell holds a vertical line and the horizontal line to
     * its right, therefore in the rightmost column the horizontal line must not have a correspoding vertical one. The
     * same holds conversely for the lowest row, as cells hold an horizontal line and the vertical one under that. If we
     * used every row/column, a 3x3 map would result in this:
     * • - • - • -
     * |   |   |
     * • - • - • -
     * |   |   |
     * • - • - • -
     * |   |   |
     * instead of the (correct) 3x3 map
     * • - • - •
     * |   |   |
     * • - • - •
     * |   |   |
     * • - • - •
     */
    // Matrix containing cells
    private val board: Array<Array<BoardCell>> = Array(this.columns) { i ->
        Array(this.rows) { j ->
            BoardCell(i, j)
        }
    }

    // Retrieve a cell using visual coordinates
    fun getCellEasy(x: Int, y: Int): IBoardCell {
        val actual = translate(x, y)
        return getCell(actual.first, actual.second)
    }

    // Retrieve a cell using actual coordinates (internal representation)
    fun getCell(x: Int, y: Int): IBoardCell {
        if (x < 0 || y < 0 || x >= columns || y >= rows)
            throw IndexOutOfBoardException("Cell index out of board")
        return board[x][y]
    }

    // From visual coordinates retrieves actual coordinates and direction (internal representation)
    fun translate(x: Int, y: Int): Triple<Int, Int, Directions> {
        return if (y % 2 == 0) {
            Triple(x / 2, (y - 1) / 2, Directions.VERTICAL)
        } else {
            Triple((x - 1) / 2, y / 2, Directions.HORIZONTAL)
        }
    }

    // Draw a line using visual coordinates
    fun setLineEasy(x: Int, y: Int): Pair<Boolean, String> {
        if ((y + x) % 2 == 0)
            return Pair(false, "Can't draw line on dots")
        val translated = translate(x, y)
        return setLine(translated.first, translated.second, translated.third)
    }

    // Draws a line using actual coordinates (internal representation)
    private fun setLine(x: Int, y: Int, direction: Directions): Pair<Boolean, String> {

        if (x < 0 || y < 0 || x >= columns || y >= rows)    // Indexes must not exceed the matrix's dimensions
            return Pair(false, "Coordinates out of map")
        when (direction) {
            Directions.HORIZONTAL -> {
                return if (x < 0 || x >= columns - 1)       // The last column cannot have horizontal lines
                    Pair(false, "Coordinates out of map")
                else if (board[x][y].horizontal)
                    Pair(false, "Line already drawn")
                else {
                    board[x][y].horizontal = true
                    Pair(true, "Line drawn")
                }
            }
            Directions.VERTICAL -> {
                return if (y < 0 || y >= rows - 1)          // The last row cannot have vertical lines
                    Pair(false, "Coordinates out of map")
                else if (board[x][y].vertical)
                    Pair(false, "Line already drawn")
                else {
                    board[x][y].vertical = true
                    Pair(true, "Line drawn")
                }
            }
        }
    }

    // TODO: Column headers with more than a single digit
    fun getMap(): String {
        var stringMap = " "

        // Print x headers
        var crappyMap = false
        for (i in 0 until columns * 2 - 1) {
            stringMap += " ${i + 1}"
            if (i + 1 > 10)
                crappyMap = true
        }
        stringMap += "\n"
        if (crappyMap) stringMap = "Map is too large, it's gonna look all crappy ¯\\_(ツ)_/¯\n\n$stringMap"

        var rowIndex = 1
        for (y in 0 until rows) {
            stringMap += "$rowIndex "
            rowIndex++
            for (x in 0 until columns - 1)
                stringMap += "•" + board[x][y].toString(Directions.HORIZONTAL)
            stringMap += "•\n"
            if (y >= rows - 1)
            /* I'm one step closer to the edge, and I'm about to... */ break
            stringMap += rowIndex
            rowIndex++
            for (x in 0 until columns)
                stringMap += board[x][y].toString(Directions.VERTICAL) + " "
            stringMap += "\n"
        }
        return stringMap
    }
}