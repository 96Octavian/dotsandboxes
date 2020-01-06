class OutputManager : IOutputManager {
    override fun output(message: String) {
        println("INFO - $message")
    }

    override fun error(message: String) {
        println("Error - $message")
    }
}