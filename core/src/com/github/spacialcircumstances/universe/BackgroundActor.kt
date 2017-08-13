package com.github.spacialcircumstances.universe

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch

class BackgroundActor(var tex: Texture): Actor() {
    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.draw(tex, 0f, 0f)
    }
}