package com.github.spacialcircumstances.universe

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import java.security.SecureRandom

class MyGame : ApplicationAdapter() {
    lateinit var stage: Stage

    lateinit var background: BackgroundActor
    var objectsList: MutableList<SpaceObject> = mutableListOf()
    var playerList: MutableMap<Int, PlayerActor> = mutableMapOf()

    val random = SecureRandom()

    lateinit var moonTexture: Texture
    lateinit var planetTexture: Texture
    lateinit var sunTexture: Texture
    lateinit var playerTexture: Texture
    lateinit var font: BitmapFont

    override fun create() {
        stage = Stage()
        background = BackgroundActor(Texture("Background.png"))
        stage.addActor(background)
        moonTexture = Texture("Moon.png")
        planetTexture = Texture("Planet.png")
        sunTexture = Texture("Sun.png")
        playerTexture = Texture("Player.png")
        font = BitmapFont()
        createMap()
        createPlayers()
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
            return moonTexture
        } else if (radius < 40) {
            return planetTexture
        } else {
            return sunTexture
        }

    }

    override fun dispose() {
        stage.dispose()
    }

    fun placePlayer(p: PlayerActor) {
        p.setPosition(random.nextInt(1600).toFloat(), random.nextInt(900).toFloat())
    }

    //Methods for testing. Remove at the end.

    fun createPlayers() {
        for(i in 0..4) {
            var p = PlayerActor(playerTexture, font, i)
            placePlayer(p)
            stage.addActor(p)
            playerList[i] = p
        }
    }
}
