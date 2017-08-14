package com.github.spacialcircumstances.universe

class TcpEvent(var playerId: Int, var type: TcpEventTypes) {
    var data: String = "0"
}