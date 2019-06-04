package com.example.tetris

import android.util.Log
import com.example.tetris.interfaces.TetriminoCallback
import com.example.tetris.models.Block
import com.example.tetris.models.Tetrimino

class GameController(val tetriminoCallback: TetriminoCallback) {

    var blocks: ArrayList<Block> = arrayListOf()
    var tetrimino: Tetrimino = Tetrimino()

    var stateOfBoard: Array<Array<Int>> = arrayOf()



    init {
        initStateOfBoard() // tablice ze stanami trzeba przygotowac, bo
    }                    // kotlin nie obsluguje domyslnie tablic 2D

    fun makeTick(){
        if(!tetrimino.moveDown(stateOfBoard)){ // jak nie moze sie ruszyc
            blocks.addAll(tetrimino.blocks)
            refreshStateOfBoard() // odswiez stan
            tetriminoCallback.onNewTetriminoCallback() // powiedz ze jest nowy
                            // jest to po to zeby zwolnic timer, jak juz dojdzie
            tetrimino = Tetrimino()
        }
    }

    fun moveTetrimino(option: Int){
        when (option) {
            MOVE_RIGHT -> tetrimino.moveRight(stateOfBoard)
            MOVE_LEFT -> tetrimino.moveLeft(stateOfBoard)
            ROTATE_RIGHT -> tetrimino.rotateRight()
            ROTATE_LEFT -> tetrimino.rotateLeft()
        }
        tetriminoCallback.tetriminoMovedCallback() // powiedz ze sie ruszyl
    }

    fun refreshStateOfBoard(){
        for(i in (0 until PLAYGROUND_WIDTH)) // wyzeruj stany
            for(j in (0 until PLAYGROUND_HEIGHT))
                stateOfBoard[i][j] = 0
        blocks.forEach{ block -> stateOfBoard[block.posX][block.posY] = 1 } // ustaw dla danego bloku jeden
    }

    fun initStateOfBoard(){
        for (i in (0 until PLAYGROUND_WIDTH)){
            var array = arrayOf<Int>()
            for (j in (0 until PLAYGROUND_HEIGHT)){
                array += 0
            }
            stateOfBoard += array
        }
        Log.d("GC", stateOfBoard.toString())
    }


    companion object{
        val PLAYGROUND_WIDTH = 10
        val PLAYGROUND_HEIGHT = 20

        val MOVE_RIGHT = 1000
        val MOVE_LEFT = 1001
        val ROTATE_RIGHT = 1002
        val ROTATE_LEFT = 1003
    }

}