package com.github.spacialcircumstances.universe

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch

class SpaceObject(radius: Float, val tex: Texture): CircleCollidingActor(radius) {
    var drawx = x - tex.width / 2
    var drawy = y - tex.height / 2
    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.draw(tex, drawx, drawy, radius, radius)
    }

    override fun setPosition(x: Float, y: Float) {
        super.setPosition(x, y)
        drawx = x - tex.width / 2
        drawy = y - tex.height / 2
    }
}