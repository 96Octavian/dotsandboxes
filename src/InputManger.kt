class InputManger : IInputManger {
    override fun input(): String {
        val line = readLine()?.trim()
        while (line == null) readLine()?.trim()
        return line
    }
}