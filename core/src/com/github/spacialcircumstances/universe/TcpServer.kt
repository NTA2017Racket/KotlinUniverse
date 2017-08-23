package com.github.spacialcircumstances.universe

import java.io.BufferedReader
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class TcpServer(var port: Int) {
    var socket: ServerSocket = ServerSocket(port)
    var events: MutableList<TcpEvent> = mutableListOf()
    var socketMap: MutableMap<Int, Socket> = mutableMapOf()
    var playerCount = 0
    var unusedIds: MutableList<Int> = mutableListOf()
    var serverThread = thread {
        connectionLoop(socket)
    }

    fun start() {
        unusedIds = mutableListOf(1, 2, 3, 4, 5, 6)
        events.clear()
        socketMap.clear()
    }

    private fun getSocket(id: Int): Socket {
        return socketMap[id]!!
    }

    private fun connectionLoop(server: ServerSocket) {
        while (true) {
            acceptConnections(server)
        }
    }

    private fun acceptConnections(server: ServerSocket) {
        var socket = server.accept()
        thread {
            handle(socket)
            socket.close()
        }
    }

    private fun handle(socket: Socket) {
        if (playerCount < 6) {
            val id = retrievePlayerId()
            addPlayer(id, socket)
            playerCount++
            println("Player " + id.toString() + " added!")
            writeSocket(getSocket(id), "Welcome Player!")
            inOutLoop(id, socket)
        } else {
            println("Game is full!")
            writeSocket(socket, "Game is already full, sorry!")
        }
    }

    private fun retrievePlayerId(): Int {
        return unusedIds.removeAt(0)
    }

    private fun addPlayer(id: Int, s: Socket) {
        socketMap[id] = s
        var event = TcpEvent(id, TcpEventTypes.PlayerJoined)
        events.add(event)
    }

    private fun removePlayer(id: Int) {
        socketMap.remove(id)
        playerCount--
        unusedIds.add(id)
        unusedIds.sort()
        var event = TcpEvent(id, TcpEventTypes.PlayerLeft)
        events.add(event)
    }

    private fun writeSocket(s: Socket, message: String) {
        s.getOutputStream().write(message.toByteArray())
        s.getOutputStream().flush()

    }

    private fun inOutLoop(id: Int, socket: Socket) {
        var reader = BufferedReader(socket.getInputStream().reader())
        var isRunning = true
        while (isRunning) {
            var input = reader.readLine()
            if (input != null) {
                handleClientInput(id, socket, input)
            } else {
                isRunning = false
                removePlayer(id)
            }
        }
    }

    private fun handleClientInput(id: Int, socket: Socket, input: String) {
        if (input.startsWith("c ")) {
            changePlayerName(id, input.substring(2))
        } else if (input.toFloatOrNull() != null) {
            shootProjectile(id, input)
        }
    }

    private fun changePlayerName(id: Int, name: String) {
        println("Attempting to change name")
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
        socketMap.keys.forEach({
            writeSocket(getSocket(it), message)
        })
    }

    fun sendMessage(id: Int, message: String) {
        writeSocket(getSocket(id), message)
    }
}