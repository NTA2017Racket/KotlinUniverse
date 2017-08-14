package com.github.spacialcircumstances.universe

import java.net.Socket

class TcpPlayer(var id: Int, var socket: Socket) {
    var name: String = "default"
}