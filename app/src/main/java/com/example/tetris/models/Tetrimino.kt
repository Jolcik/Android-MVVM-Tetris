package com.example.tetris.models

import android.graphics.Color
import android.util.Log
import com.example.tetris.GameController

class Tetrimino(whichShape: Char, whichColor: Int) {

    var blocks: List<Block> = listOf()
    var shape: Char = whichShape //przchowuje informacje jakiego rodzaju jest dane tetrimino
    var color: Int = whichColor

    init {
        Log.d("RC", whichShape.toString())
        var positions: Array<Int> = when(whichShape){
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

        blocks = listOf( // tworzymy bloki i nadajemy im pozycje oraz kolor
            Block(positions[0], positions[1], color),
            Block(positions[2], positions[3], color),
            Block(positions[4], positions[5], color),
            Block(positions[6], positions[7], color)
        )
    }

    fun getNextTetrimino(): Char{
        return shapes[(0 until shapes.size).shuffled().first()]
    }

    fun getNextColor(): Int{
        return colors[(0 until colors.size).shuffled().first()]
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

    fun rotate(state: Array<Array<Int>>): Boolean{
        val center = when(shape){ // na podstawie ksztaltu wyznaczamy srodek figury
            'I' -> Pair(blocks[1].posX, blocks[1].posY)
            'T' -> Pair(blocks[1].posX, blocks[1].posY)
            'O' -> return true // kwadrat nie ma jak sie obrocic
            'L' -> Pair(blocks[2].posX, blocks[2].posY)
            'J' -> Pair(blocks[1].posX, blocks[1].posY)
            'S' -> Pair(blocks[2].posX, blocks[2].posY)
            'Z' -> Pair(blocks[1].posX, blocks[1].posY)
            else -> Pair(0,0)
        }

        Log.d("ROT CENTER", center.toString())

        blocks.forEach {
            var x = it.posX - center.first
            var y = center.second - it.posY // bo uklad wspolrzednych jest obrocony

            // na dole to jest przekształcenie liniowe, tak wyznaczam punkty
            if( center.first + y !in (0 until GameController.PLAYGROUND_WIDTH) || // czy w planszy
                center.second + x !in (0 until GameController.PLAYGROUND_HEIGHT) || // uklad obrocony
                state[center.first + y][center.second + x] == 1) // jak ktorykolwiek zajety
                return false
        }
        Log.d("ROT BEFORE", blocks.toString())

        // jak sie udalo to obroc te klocki
        blocks.forEach {
            var x = it.posX - center.first
            var y = center.second - it.posY // bo uklad wspolrzednych jest obrocony
            it.posX = center.first + y
            it.posY = center.second + x // uklad obrocony
        }
        Log.d("ROT AFTER", blocks.toString())
        return true
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
