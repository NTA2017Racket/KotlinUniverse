package com.github.spacialcircumstances.universe

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Actor

class PlayerActor(var tex: Texture, var font: BitmapFont, playerID: Int, var playerColor: Color): CircleCollidingActor(10f) {
    var drawx = x - 10
    var drawy = y - 10
    var playerName: String = "player"
    var playerEnergy: Float = 0f
    var playerId: Int = playerID
    var deathStat: Int = 0
    var killStat: Int = 0
    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.color = playerColor
        font.color = playerColor
        batch.draw(tex, drawx, drawy, 20f, 20f)
        font.draw(batch, playerName, playerId * 150f - 50f, 800f)
        val en = playerEnergy.toString()
        font.draw(batch, en.substring(0, en.indexOf(".")), playerId * 150f - 50f, 850f)
        batch.color = Color.WHITE
        font.color = Color.WHITE
    }

    override fun setPosition(x: Float, y: Float) {
        super.setPosition(x, y)
        drawx = x - 10
        drawy = y - 10
    }

    override fun act(delta: Float) {
        playerEnergy += delta * 1.6f
    }
}