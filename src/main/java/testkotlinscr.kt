import scripts.FilesScripts

class testkotlinscr : FilesScripts() {
    override fun start() {
        println("starting kotlin script")
    }

    override fun update(dt: Float) {
        println("updating kotlin script")
    }
}