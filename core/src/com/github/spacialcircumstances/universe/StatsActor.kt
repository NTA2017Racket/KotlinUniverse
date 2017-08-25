package com.github.spacialcircumstances.universe

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Actor

class StatsActor(var font: BitmapFont) : Actor() {
    var statsDisplay: MutableList<String> = mutableListOf()
    fun initialize(players: Collection<PlayerActor>) {
        players.sortedWith(compareByDescending({
            it.killStat
        })
        ).forEach({
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
        for (i in 0..(statsDisplay.size - 1)) {
            val p = i % 3
            if (p < 3) {
                font.draw(batch, statsDisplay[i], 100f, 800f - (i * 40f))
            } else {
                font.draw(batch, statsDisplay[i], 400f, 800f - ((i - 9) * 40f))
            }
        }
    }
}