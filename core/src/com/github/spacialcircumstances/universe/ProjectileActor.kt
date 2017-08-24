package com.github.spacialcircumstances.universe

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite

class ProjectileActor(var player: PlayerActor, var tex: Texture): CircleCollidingActor(5f) {
    var velocityX = 0f
    var velocityY = 0f
    var accelerationX = 0f
    var accelerationY = 0f
    val sprite = Sprite(tex, 0, 0, 10, 10)
    init {
        x = player.x
        y = player.y
        sprite.setPosition(x, y)
        sprite.texture = tex
        sprite.color = player.playerColor
    }
    override fun draw(batch: Batch?, parentAlpha: Float) {
        sprite.draw(batch)
    }

    override fun act(delta: Float) {
        velocityX += accelerationX * delta
        velocityY += accelerationY * delta
        x += velocityX * delta
        y += velocityY * delta
        sprite.setPosition(x, y)
        rotation = ((Math.atan((velocityY / velocityX).toDouble()) / (2 * Math.PI)).toFloat() * 360) - 90
        sprite.rotation = rotation
    }
}