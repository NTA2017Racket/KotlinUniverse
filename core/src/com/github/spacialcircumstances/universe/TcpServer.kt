package com.github.spacialcircumstances.universe

import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class TcpServer(var port: Int) {
    var socket: ServerSocket = ServerSocket(port)
    var events: MutableList<TcpEvent> = mutableListOf()
    var playerMap: MutableMap<Int, TcpPlayer> = mutableMapOf()
    var lastId = 0
    fun start() {
        thread {
            connectionLoop(socket)
        }
    }

    private fun connectionLoop(server: ServerSocket) {
        acceptConnections(server)
        connectionLoop(server)
    }

    private fun acceptConnections(server: ServerSocket) {
        var socket = server.accept()
        thread {
            handle(socket)
            socket.close()
        }
    }

    private fun handle(socket: Socket) {
        var id = retrievePlayerId()
        addPlayer(id, socket)
        println("Player " + id.toString() + " added!")
        writeSocket(id, "Welcome Player!")
        inOutLoop(id, socket)
    }

    private fun retrievePlayerId(): Int {
        lastId++
        return lastId
    }

    private fun addPlayer(id: Int, s: Socket) {
        var player = TcpPlayer(id, s)
        playerMap[id] = player
        var event = TcpEvent(id, TcpEventTypes.PlayerJoined)
        events.add(event)
    }

    private fun removePlayer(id: Int) {
        playerMap.remove(id)
        var event = TcpEvent(id, TcpEventTypes.PlayerLeft)
        events.add(event)
    }

    private fun writeSocket(id: Int, message: String) {
        val s = playerMap[id]!!.socket
        if (s != null) {
            s.getOutputStream().write(message.toByteArray())
            s.getOutputStream().flush()
        }
    }

    private fun inOutLoop(id: Int, socket: Socket) {
        var reader = socket.getInputStream().reader()
        var input = reader.readText()
        if (!socket.isInputShutdown) {
            handleClientInput(id, socket, input)
        } else {
            socket.close()
            removePlayer(id)
        }
    }

    private fun handleClientInput(id: Int, socket: Socket, input: String) {
        if (input.isNotEmpty()) {
            if (input.startsWith("c ")) {
                changePlayerName(id, input.substring(2))
            } else if (input.toIntOrNull() != null) {
                shootProjectile(id, input)
            }
        }
        inOutLoop(id, socket)
    }

    private fun changePlayerName(id: Int, name: String) {
        playerMap[id]!!.name = name
        var event = TcpEvent(id, TcpEventTypes.PlayerNameChange)
        event.data = name
        events.add(event)
    }

    private fun shootProjectile(id: Int, data: String) {
        var event = TcpEvent(id, TcpEventTypes.PlayerShoot)
        event.data = data
        events.add(event)
    }

    fun retrieveEvents(): List<TcpEvent> {
        val queue = events.toList()
        events.clear()
        return queue
    }

    fun broadcastMessage(message: String) {
        playerMap.keys.forEach({
            writeSocket(it, message)
        })
    }

    fun sendMessage(id: Int, message: String) {
        writeSocket(id, message)
    }
}