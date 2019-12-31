enum class directions {
    VERTICAL,
    HORIZONTAL
}

class BoardCell(val x: Int, val y: Int) {
    var vertical: Boolean = false
    var horizontal: Boolean = false

    override fun toString(): String {
        val symbol: String
        if (vertical && horizontal)
            symbol = " + "
        else if (vertical)
            symbol = " | "
        else if (horizontal)
            symbol = " - "
        else
            symbol = "   "
        return symbol
    }

    fun toString(direction: directions): String {
        var symbol = "   "
        if (direction == directions.HORIZONTAL && horizontal)
            symbol = " - "
        else if (direction == directions.VERTICAL && vertical)
            symbol = " | "
        return symbol
    }
}

class Board(private val columns: Int, private val rows: Int) {

    companion object {
        fun setup():Board{
            var col:Int? = null
            while(col == null) {
                print("Column number: ")
                col = readLine()?.trim()?.toIntOrNull()
            }
            var row:Int? = null
            while(row == null) {
                print ("Row number: ")
                row = readLine()?.trim()?.toIntOrNull()
            }
            return Board(col, row)
        }
    }

    private val board: Array<Array<BoardCell>> = Array(this.columns) { i ->
        Array(this.rows) { j ->
            BoardCell(i, j)
        }
    }

    fun setLineEasy(x: Int, y: Int): Pair<Boolean, String> {
        if ((y + x) % 2 == 0)
            return Pair(false, "Can't draw line on dots")
        if (y % 2 == 0) {
            return setLine(x / 2, (y - 1) / 2, directions.VERTICAL)
        } else {
            return setLine((x - 1) / 2, y / 2, directions.HORIZONTAL)
        }
    }

    private fun setLine(x: Int, y: Int, direction: directions): Pair<Boolean, String> {
        if (x < 0 || y < 0 || x >= columns || y >= rows)
            return Pair(false, "Coordinates out of map")
        when (direction) {
            directions.HORIZONTAL -> {
                if (x < 0 || x >= columns - 2)
                    return Pair(false, "Coordinates out of map")
                if (board[x][y].horizontal)
                    return Pair(false, "Line already drawn")
                else {
                    board[x][y].horizontal = true
                    return Pair(true, "Line drawn")
                }
            }
            directions.VERTICAL -> {
                if (y < 0 || y >= rows - 2)
                    return Pair(false, "Coordinates out of map")
                if (board[x][y].vertical)
                    return Pair(false, "Line already drawn")
                else {
                    board[x][y].vertical = true
                    return Pair(true, "Line drawn")
                }
            }
        }
    }

    // TODO: Numeri a più di una cifra
    fun getMap(): String {
        var stringMap = " "

        // Print x headers
        for (i in 0 until columns * 2 - 1) {
            stringMap += " ${i + 1}"
        }
        stringMap += "\n"

        var rowIndex = 1
        for (y in 0 until rows) {
            stringMap += "${rowIndex} "
            rowIndex++
            for (x in 0 until columns - 1)
                stringMap += "•" + board[x][y].toString(directions.HORIZONTAL)
            stringMap += "•\n"
            if (y >= rows - 1)
                break
            stringMap += rowIndex
            rowIndex++
            for (x in 0 until columns)
                stringMap += board[x][y].toString(directions.VERTICAL) + " "
            stringMap += "\n"
        }
        return stringMap
    }
}