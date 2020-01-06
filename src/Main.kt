fun main() {

    // Use I/O Managers
    val oManager = OutputManager()
    val iManager = InputManger()

    // Setup board and players
    val board = Board.setup(oManager, iManager)
    val score = ScoreBoard.setup(board, oManager, iManager)

    println(board.getMap())

    // Main loop
    score.play()

}