package com.github.spacialcircumstances.universe

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor

class ProjectileActor(var player: PlayerActor): CircleCollidingActor(5f) {
    var velocityX = 0f
    var velocityY = 0f
    var accelerationX = 0f
    var accelerationY = 0f
    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
    }

    override fun act(delta: Float) {
        super.act(delta)
    }
}