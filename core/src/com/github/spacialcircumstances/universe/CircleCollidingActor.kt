package com.github.spacialcircumstances.universe

import com.badlogic.gdx.scenes.scene2d.Actor

open class CircleCollidingActor(var radius: Float): Actor() {
    var radiussq = Math.pow(radius.toDouble(), 2f.toDouble())
    fun contains(x: Float, y: Float): Boolean {
        return distsq(x, y, this.x, this.y) < radiussq
    }

    fun collides(other: CircleCollidingActor): Boolean {
        return dist(x, y, other.x, other.y) < radius + other.radius
    }

    fun distsq(x1: Float, y1: Float, x2: Float, y2: Float): Double {
        return (Math.pow((x1 - x2).toDouble(), 2f.toDouble()) + Math.pow((y1 - y2).toDouble(), 2f.toDouble()))
    }

    fun dist(x1: Float, y1: Float, x2: Float, y2: Float): Double {
        return Math.sqrt(distsq(x1, y1, x2, y2))
    }

    fun distanceSquaredTo(other: Actor): Double {
        return distsq(x, y, other.x, other.y)
    }

    fun distanceTo(other: Actor): Double {
        return dist(x, y, other.x, other.y)
    }
}