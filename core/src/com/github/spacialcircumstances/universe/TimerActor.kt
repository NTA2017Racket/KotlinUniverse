package com.github.spacialcircumstances.universe

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Actor

class TimerActor(var font: BitmapFont): Actor() {
    private var gameTime: Float = 0f
    override fun act(delta: Float) {
        super.act(delta)
        gameTime -= delta
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        font.color = Color.CYAN
        font.draw(batch, formatTime(gameTime), 20f, 20f)
        font.color = Color.WHITE
    }

    fun resetTimer(to: Float) {
        gameTime = to
    }

    fun isFinished(): Boolean {
        return gameTime < 0
    }

    private fun formatTime(time: Float): String {
        val min = (time / 60).toString()
        val minString = min.substring(0, min.indexOf("."))
        val sec = (time % 60).toString()
        val secString = sec.substring(0, sec.indexOf("."))
        return minString + ":" + secString
    }
}