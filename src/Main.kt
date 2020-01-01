fun main() {

    // Setup board and players
    val board = Board.setup()
    val score = ScoreBoard.setup(board)

    println(board.getMap())

    // Main loop
    score.play()

}