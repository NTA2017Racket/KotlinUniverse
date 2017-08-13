package com.github.spacialcircumstances.universe

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch

class ProjectileActor(var player: PlayerActor, var tex: Texture): CircleCollidingActor(5f) {
    var velocityX = 0f
    var velocityY = 0f
    var accelerationX = 0f
    var accelerationY = 0f
    init {
        x = player.x
        y = player.y
    }
    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = player.playerColor
        batch!!.draw(tex, x - radius , y - radius, radius * 2, radius * 2)
        batch!!.color = Color.WHITE
    }

    override fun act(delta: Float) {
        velocityX += accelerationX * delta
        velocityY += accelerationY * delta
        x += velocityX * delta
        y += velocityY * delta
    }
}