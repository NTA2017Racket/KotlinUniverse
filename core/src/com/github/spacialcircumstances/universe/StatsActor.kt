package com.github.spacialcircumstances.universe

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Actor

class StatsActor(var font: BitmapFont): Actor() {
    var statsDisplay: MutableList<String> = mutableListOf()
    fun initialize(players: Collection<PlayerActor>) {
        players.forEach({
            statsDisplay.add(it.playerName)
            statsDisplay.add("Kills: " + it.killStat.toString())
            statsDisplay.add("Deaths: " + it.deathStat.toString())
        })
    }

    fun reset() {
        statsDisplay.clear()
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        statsDisplay.forEach({
            font.draw(batch, it, 100f, 800f - (statsDisplay.indexOf(it) * 40))
        })
    }
}