package com.example.tetris

import android.util.Log
import com.example.tetris.interfaces.NewTetriminoCallback
import com.example.tetris.models.Block
import com.example.tetris.models.Tetrimino

class GameController(blockCallback: NewTetriminoCallback) {

    var blocks: List<Block> = listOf()
    var tetrimino: Tetrimino = Tetrimino()

    var stateOfBoard: Array<Array<Int>> = arrayOf()



    init {

        clearState()
    }

    fun makeTick(){
        tetrimino.moveDown()
    }

    fun moveTetrimino(option: Int){

    }

    fun clearState(){/*
        for(i in (0 until PLAYGROUND_WIDTH))
            for(j in (0 until PLAYGROUND_HEIGHT))
                stateOfBoard[i][j] = 0
                */
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
        val MOVE_DOWN = 1002
        val ROTATE_RIGHT = 1003
        val ROTATE_LEFT = 1004
    }

}