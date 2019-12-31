fun main() {

    val board = Board.setup()
    val score = ScoreBoard.setup(board)

    println(board.getMap())

    score.play()

}