package abobus.game

import abobus.game.good.GoodGame
import com.badlogic.gdx.Files
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

fun main() {

    val config = Lwjgl3ApplicationConfiguration()

    config.setForegroundFPS(60 * 2)
    config.setIdleFPS(30)
    config.useVsync(false)
    config.setTitle("Tank game")
    config.setResizable(true)
    config.setWindowedMode(1920,1100)
    config.setWindowIcon(Files.FileType.Local, "src/main/resources/icons/tank.png")

    Lwjgl3Application(GoodGame(), config)
}