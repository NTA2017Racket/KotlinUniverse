package com.github.spacialcircumstances.universe

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport

class MyGame : ApplicationAdapter() {
    lateinit var stage: Stage

    lateinit var background: BackgroundActor

    override fun create() {
        stage = Stage()
        background = BackgroundActor(Texture("Background.png"))
        stage.addActor(background)
    }

    override fun render() {
        var delta = Gdx.graphics.deltaTime
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(delta)
        stage.draw()
    }

    override fun dispose() {
    }
}
