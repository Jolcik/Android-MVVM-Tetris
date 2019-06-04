package com.example.tetris.models

import android.graphics.Color
import android.util.Log
import com.example.tetris.GameController

class Tetrimino {

    var blocks: List<Block> = listOf()

    init {
        var randomChar = shapes.get((0..6).shuffled().first())
        Log.d("RC", randomChar.toString())
        var positions: Array<Int>
        positions = when(randomChar){
            'I' -> arrayOf(3, 0, 4, 0, 5, 0, 6, 0)
            'T' -> arrayOf(3, 0, 4, 0, 5, 0, 4, 1)
            'O' -> arrayOf(4, 0, 5, 0, 4, 1, 5, 1)
            'L' -> arrayOf(4, 1, 4, 0, 5, 0, 6, 0)
            'J' -> arrayOf(4, 0, 5, 0, 6, 0, 6, 1)
            'S' -> arrayOf(4, 1, 5, 1, 5, 0, 6, 0)
            'Z' -> arrayOf(4, 0, 5, 0, 5, 1, 6, 1)
            else -> arrayOf()
        }
        Log.d("RC", positions.toString())

        // losowanie koloru
        val color = colors.get((0 until colors.size).shuffled().first())

        blocks = listOf( // tworzymy bloki i nadajemy im pozycje oraz kolor
            Block(positions[0], positions[1], color),
            Block(positions[2], positions[3], color),
            Block(positions[4], positions[5], color),
            Block(positions[6], positions[7], color)
        )
    }

    fun moveDown(state: Array<Array<Int>>): Boolean {
        blocks.forEach { block -> // sprawdz czy mozna isc na dol
            if(block.posY+1 == GameController.PLAYGROUND_HEIGHT
                || state[block.posX][block.posY+1] == 1)
                return false
        }
        blocks.forEach { block -> block.moveDown() } // idz na dol
        return true
    }

    fun moveRight(state: Array<Array<Int>>): Boolean{
        blocks.forEach { block -> // sprawdz czy mozna isc w prawo
            if(block.posX+1 == GameController.PLAYGROUND_WIDTH
                || state[block.posX+1][block.posY] == 1)
                return false // jak nie to trudno
        }
        blocks.forEach { block -> block.moveRight() } // idz w prawo
        return true
    }
    fun moveLeft(state: Array<Array<Int>>): Boolean{
        blocks.forEach { block -> // sprawdz czy mozna isc w lewo
            if(block.posX == 0
                || state[block.posX-1][block.posY] == 1)
                return false // jak nie to trudno
        }
        blocks.forEach { block -> block.moveLeft() } // idz w lewo
        return true
    }

    fun rotateRight(){

    }

    fun rotateLeft(){

    }

    companion object{
        val shapes: List<Char> = listOf( // mozliwe ksztalty tetrimino jako litery
            'I', 'T', 'O', 'L', 'J', 'S', 'Z')

        val colors: List<Int> = listOf( // mozliwe kolory do wylosowania
            Color.BLUE, Color.RED, Color.GREEN,
            Color.YELLOW, Color.CYAN, Color.MAGENTA
        )
    }

}
