package com.github.spacialcircumstances.universe

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import java.util.*

class MyGame : ApplicationAdapter() {
    lateinit var stage: Stage
    lateinit var endStage: Stage

    lateinit var background: BackgroundActor
    lateinit var endBackground: BackgroundActor
    lateinit var timer: TimerActor
    lateinit var statsActor: StatsActor
    var objectsList: MutableList<SpaceObject> = mutableListOf()
    var playerList: MutableMap<Int, PlayerActor> = mutableMapOf()
    var projectilesList: MutableList<ProjectileActor> = mutableListOf()

    val random = Random()

    var endInit = false

    lateinit var moonTexture: Texture
    lateinit var planetTexture: Texture
    lateinit var sunTexture: Texture
    lateinit var playerTexture: Texture
    lateinit var projectileTexture: Texture
    lateinit var font: BitmapFont
    lateinit var server: TcpServer

    override fun create() {
        stage = Stage()
        stage.addListener(object : InputListener() {
            override fun keyDown(event: InputEvent?, keycode: Int): Boolean {
                if (keycode == Input.Keys.ESCAPE) {
                    Gdx.app.exit()
                }
                return true
            }
        })
        Gdx.input.inputProcessor = stage
        endStage = Stage()
        endStage.addListener(object: InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                startGame()
                return true
            }
        })
        background = BackgroundActor(Texture("Background.png"))
        stage.addActor(background)
        endBackground = BackgroundActor(Texture("GameOver.png"))
        endStage.addActor(endBackground)
        moonTexture = Texture("Moon.png")
        planetTexture = Texture("Planet.png")
        sunTexture = Texture("Sun.png")
        playerTexture = Texture("Player.png")
        projectileTexture = Texture("Projectile.png")
        font = BitmapFont()
        timer = TimerActor(font)
        stage.addActor(timer)
        statsActor = StatsActor(font)
        endStage.addActor(statsActor)
        server = TcpServer(8080)
        startGame()
    }

    fun startGame() {
        projectilesList.forEach({ it.remove() })
        playerList.values.forEach({ it.remove() })
        objectsList.forEach({ it.remove() })
        projectilesList.clear()
        playerList.clear()
        objectsList.clear()
        endInit = false
        createMap()
        timer.resetTimer(120f)
        server.start()
        statsActor.reset()
    }

    override fun render() {
        var delta = Gdx.graphics.deltaTime
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        if(timer.isFinished()) {
            if(!endInit) {
                Gdx.input.inputProcessor = endStage
                endInit = true
                statsActor.initialize(playerList.values)
                server.broadcastMessage("Match ended!")
            }
            //Show end screen
            endStage.act(delta)
            endStage.draw()
        } else {
            handleServerEvents()
            applyGravity()
            stage.act(delta * 0.5f)
            handleCollisions()
            stage.act(delta * 0.5f)
            handleCollisions()
            stage.draw()
        }
    }

    fun createMap() {
        for (i in 0..10) {
            var rad = random.nextInt(40) + 15f
            var posx = random.nextInt(1600)
            var posy = random.nextInt(900)
            var obj = SpaceObject(rad, getObjectTexture(rad))
            obj.setPosition(posx.toFloat(), posy.toFloat())
            objectsList.add(obj)
            stage.addActor(obj)
        }
    }

    fun getObjectTexture(radius: Float): Texture {
        if (radius < 25) {
            return moonTexture
        } else if (radius < 40) {
            return planetTexture
        } else {
            return sunTexture
        }

    }

    override fun dispose() {
        stage.dispose()
    }

    fun placePlayer(p: PlayerActor) {
        p.setPosition(random.nextInt(1400).toFloat() + 100, random.nextInt(700).toFloat() + 100)
        p.playerEnergy += 10
    }

    fun getPlayerColor(id: Int): Color {
        return when (id) {
            0 -> Color.WHITE
            1 -> Color.RED
            2 -> Color.BLUE
            3 -> Color.GREEN
            4 -> Color.YELLOW
            5 -> Color.VIOLET
            6 -> Color.LIGHT_GRAY
            else -> Color.BLACK
        }
    }

    fun handleCollisions() {
        var toRemove = projectilesList.filter {
            collidesWithObjects(it) || it.distanceSquaredTo(it.player) > 5000000
        }
        projectilesList.removeAll(toRemove)
        toRemove.forEach({
            it.remove()
        })
    }

    fun collidesWithObjects(pr: ProjectileActor): Boolean {
        for (pl in playerList.values) {
            if (pr.collides(pl) && pr.player != pl) {
                handleKill(pr, pl)
                return true
            }
        }
        for (obj in objectsList) {
            if (pr.collides(obj)) {
                return true
            }
        }

        return false
    }

    fun applyGravity() {
        projectilesList.forEach({ pr ->
            pr.accelerationX =
            objectsList.map {
                var vx = it.x - pr.x
                val l = it.distanceTo(pr).toFloat()
                vx /= l
                vx *= it.mass / (l * l)

                vx
            }.sum()
            pr.accelerationY = objectsList.map {
                var vy = it.y - pr.y
                val l = it.distanceTo(pr).toFloat()
                vy /= l
                vy *= it.mass / (l * l)
                vy
            }.sum()
        })
    }

    fun spawnProjectile(id: Int, angle: Double) {
        val player = playerList[id]!!
        if (player.playerEnergy > 20) {
            player.playerEnergy -= 20
            var pr = ProjectileActor(player, playerTexture)
            pr.setPosition(player.x, player.y)
            pr.velocityY = (100 * Math.cos(Math.toRadians(angle))).toFloat()
            pr.velocityX = (100 * Math.sin(Math.toRadians(angle))).toFloat()
            stage.addActor(pr)
            projectilesList.add(pr)
        } else {
            server.sendMessage(id, "Not enough energy")
        }
    }

    fun handleServerEvents() {
        val events = server.retrieveEvents()
        events.forEach({
            val id = it.playerId
            println(it.type)
            when (it.type) {
                TcpEventTypes.PlayerJoined -> {
                    createPlayer(id)
                }
                TcpEventTypes.PlayerLeft -> {
                    removePlayer(id)
                }
                TcpEventTypes.PlayerNameChange -> {
                    val p = playerList[id]!!
                    p.playerName = it.data
                    println("player changed name to " + it.data)
                }
                TcpEventTypes.PlayerShoot -> {
                    spawnProjectile(id, it.data.toDouble())
                }
            }
        })
    }

    fun createPlayer(id: Int) {
        val p = PlayerActor(playerTexture, font, id, getPlayerColor(id))
        p.playerEnergy = 20f
        placePlayer(p)
        stage.addActor(p)
        playerList[id] = p
    }

    fun removePlayer(id: Int) {
        playerList.remove(id)!!.remove()
    }

    fun handleKill(pr: ProjectileActor, killed: PlayerActor) {
        val killer = pr.player
        killer.killStat++
        killed.deathStat++
        placePlayer(killed)
        server.broadcastMessage(killer.name + " killed " + killed.name + "!")
    }
}
