package com.github.spacialcircumstances.universe.desktop

import com.badlogic.gdx.Files
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.github.spacialcircumstances.universe.MyGame

object DesktopLauncher {
    @JvmStatic fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.width = 1600
        config.height = 900
        config.resizable = false
        config.title = "UniverseHost"
        config.addIcon("Icon.png", Files.FileType.Internal)
        LwjglApplication(MyGame(), config)
    }
}
