package com.github.spacialcircumstances.universe

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch

class SpaceObject(radius: Float, val tex: Texture): CircleCollidingActor(radius) {
    var drawx = x - radius
    var drawy = y - radius
    var mass = (radius * radius * radius) * 3.2f
    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.draw(tex, drawx, drawy, 2 * radius, 2 * radius)
    }

    override fun setPosition(x: Float, y: Float) {
        super.setPosition(x, y)
        drawx = x - radius
        drawy = y - radius
    }
}