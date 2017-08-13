package com.github.spacialcircumstances.universe

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import java.security.SecureRandom

class MyGame : ApplicationAdapter() {
    lateinit var stage: Stage

    lateinit var background: BackgroundActor
    var objectsList: MutableList<SpaceObject> = mutableListOf()

    val random = SecureRandom()

    lateinit var MoonTexture: Texture
    lateinit var PlanetTexture: Texture
    lateinit var SunTexture: Texture

    override fun create() {
        stage = Stage()
        background = BackgroundActor(Texture("Background.png"))
        stage.addActor(background)
        MoonTexture = Texture("Moon.png")
        PlanetTexture = Texture("Planet.png")
        SunTexture = Texture("Sun.png")
        createMap()
    }

    override fun render() {
        var delta = Gdx.graphics.deltaTime
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(delta)
        stage.draw()
    }

    fun createMap() {
        for (i in 0..20) {
            var rad = random.nextInt(40) + 15f
            var posx = random.nextInt(1600)
            var posy = random.nextInt(900)
            var obj = SpaceObject(rad, getObjectTexture(rad))
            obj.setPosition(posx.toFloat(), posy.toFloat())
            objectsList.add(obj)
            stage.addActor(obj)
        }
    }

    fun getObjectTexture(radius: Float): Texture {
        if(radius < 25) {
            return MoonTexture
        } else if (radius < 40) {
            return PlanetTexture
        } else {
            return SunTexture
        }

    }

    override fun dispose() {
        stage.dispose()
    }
}
