package com.github.spacialcircumstances.universe

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Actor

class PlayerActor(var tex: Texture, var font: BitmapFont, playerID: Int): Actor() {
    var drawx = x - 50
    var drawy = y - 50
    var playerName: String = "player"
    var playerEnergy: Int = 0
    var playerId: Int = playerID
    override fun draw(batch: Batch?, parentAlpha: Float) {
        batch!!.draw(tex, drawx, drawy)
        font.draw(batch, playerName, playerId * 150f, 700f)
        font.draw(batch, playerEnergy.toString(), playerId * 150f, 800f)
    }

    override fun setPosition(x: Float, y: Float) {
        super.setPosition(x, y)
        drawx = x - 50
        drawy = y - 50
    }
}