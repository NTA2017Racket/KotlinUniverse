# KotlinUniverse
A port of our UniverseHost to Kotlin

This is a gravity-based shooter party-game inspired by [NewtonWars](https://github.com/Draradech/NewtonWars).
This is the server and display version of the game. All players use this as the display. The players play the game using some kind of TCP Client.
Goal of the game is to kill the most other players on a field that includes many gravity wells, such as moons and planets, that make aiming more difficult.

![Screenshot version 0.1.1](https://raw.githubusercontent.com/NTA2017Racket/KotlinUniverse/master/screenshots/screenshot-011-1.png)

The first version of this was written in Racket and can be found [here](https://github.com/NTA2017Racket/UniverseHost)

## NTA 2017 - Racket course

Group Project in the NTA 2017 course "Spieleprogrammierung in Racket" by Felix N., Tom S. and Nils B.

## Commands
` c Name ` change your name to "Name"

` 20.0 ` shoot a projectile in this angle

## Technical
This game is written in Kotlin using LibGDX.
To build, run ` gradlew dist ` after cloning this repository

## License
This project is licensed under the MIT License that can be found in the LICENSE file.

Copyright (c) 2017  Felix N., Tom S. and Nils B.
