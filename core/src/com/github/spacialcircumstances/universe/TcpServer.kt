package com.github.spacialcircumstances.universe

import java.io.BufferedReader
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class TcpServer(var port: Int) {
    var socket: ServerSocket = ServerSocket(port)
    var events: MutableList<TcpEvent> = mutableListOf()
    var socketMap: MutableMap<Int, Socket> = mutableMapOf()
    var lastId = 0
    var serverThread = thread {
        connectionLoop(socket)
    }
    fun start() {
        lastId = 0
        events.clear()
        socketMap.clear()
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
        socketMap[id] = s
        var event = TcpEvent(id, TcpEventTypes.PlayerJoined)
        events.add(event)
    }

    private fun removePlayer(id: Int) {
        socketMap.remove(id)
        var event = TcpEvent(id, TcpEventTypes.PlayerLeft)
        events.add(event)
    }

    private fun writeSocket(id: Int, message: String) {
        val s = socketMap[id]!!
        if (s != null) {
            s.getOutputStream().write(message.toByteArray())
            s.getOutputStream().flush()
        }
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
            writeSocket(it, message)
        })
    }

    fun sendMessage(id: Int, message: String) {
        writeSocket(id, message)
    }
}