package com.example.tetris.models

import android.graphics.Color
import android.util.Log

class Tetrimino() {

    var blocks: List<Block> = listOf()

    init {
        var randomChar = shapes.get((0..6).shuffled()[2])
        Log.d("RC", randomChar.toString())
        var positions: Array<Int>
        positions = when(randomChar){
            'I' -> arrayOf(3, 0, 4, 0, 5, 0, 6, 0)
            'T' -> arrayOf(3, 0, 4, 0, 5, 0, 4, 1)
            'O' -> arrayOf(3, 0, 4, 0, 5, 0, 4, 1)
            'L' -> arrayOf(3, 0, 4, 0, 5, 0, 4, 1)
            'J' -> arrayOf(3, 0, 4, 0, 5, 0, 4, 1)
            'S' -> arrayOf(3, 0, 4, 0, 5, 0, 4, 1)
            'Z' -> arrayOf(3, 0, 4, 0, 5, 0, 4, 1)
            else -> arrayOf()
        }
        Log.d("RC", positions.toString())

        val color = Color.RED

        blocks = listOf(
            Block(positions[0], positions[1], color),
            Block(positions[2], positions[3], color),
            Block(positions[4], positions[5], color),
            Block(positions[6], positions[7], color)
        )
    }

    fun moveRight() = blocks.forEach { ++it.posX }
    fun moveLeft() = blocks.forEach { --it.posX }
    fun moveDown() = blocks.forEach { it.moveDown() }

    fun rotateRight(){

    }

    fun rotateLeft(){

    }

    companion object{
        val shapes: List<Char> = listOf(
            'I', 'T', 'O', 'L', 'J', 'S', 'Z')
    }
}