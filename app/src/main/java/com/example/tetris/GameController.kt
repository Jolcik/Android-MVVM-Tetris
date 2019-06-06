package com.example.tetris

import android.util.Log
import com.example.tetris.interfaces.TetriminoCallback
import com.example.tetris.models.Block
import com.example.tetris.models.Tetrimino

class GameController(val tetriminoCallback: TetriminoCallback) {

    var blocks: ArrayList<Block> = arrayListOf()
    var tetrimino: Tetrimino = Tetrimino()

    var stateOfBoard: Array<Array<Int>> = arrayOf()

    var isTheGameOver: Boolean = false

    init {
        initStateOfBoard() // tablice ze stanami trzeba przygotowac, bo
    }                    // kotlin nie obsluguje domyslnie tablic 2D

    fun makeTick(){
        if(!tetrimino.moveDown(stateOfBoard)){ // jak nie moze sie ruszyc
            blocks.addAll(tetrimino.blocks)
            refreshStateOfBoard() // odswiez stan
            checkIfColumnCanBeRemoved()
            tetriminoCallback.onNewTetriminoCallback() // powiedz ze jest nowy
                            // jest to po to zeby zwolnic timer, jak juz dojdzie
            tetrimino = Tetrimino()
            tetrimino.blocks.forEach { // jezeli w miejscu nowego tetrimino juz cos jest to koniec gry
                if(stateOfBoard[it.posX][it.posY] == 1) {
                    isTheGameOver = true
                    tetriminoCallback.gameOverCallback()
                }
            }
        }
    }

    fun moveTetrimino(option: Int){
        when (option) {
            MOVE_RIGHT -> tetrimino.moveRight(stateOfBoard)
            MOVE_LEFT -> tetrimino.moveLeft(stateOfBoard)
            ROTATE -> tetrimino.rotate(stateOfBoard)
        }
        tetriminoCallback.tetriminoMovedCallback() // powiedz ze sie ruszyl
    }

    fun refreshStateOfBoard(){
        for(i in (0 until PLAYGROUND_WIDTH)) // wyzeruj stany
            for(j in (0 until PLAYGROUND_HEIGHT))
                stateOfBoard[i][j] = 0
        blocks.forEach{ block -> stateOfBoard[block.posX][block.posY] = 1 } // ustaw dla danego bloku jeden
    }

    fun checkIfColumnCanBeRemoved(){
0
        for(j in (0 until PLAYGROUND_HEIGHT)){
            for(i in (0 until PLAYGROUND_WIDTH)) {
                if (stateOfBoard[i][j] == 0) // jezeli jest pusty blok to ten rzad odpada
                    break

                if (i == PLAYGROUND_WIDTH - 1) { // jak doszlismy do konca to usuwamy rzad
                    deleteRow(j)
                    moveDownUpperBlocks(j)
                }
            }
        }
    }

    fun deleteRow(whichRow: Int){
        blocks.filter { it.posY == whichRow }.forEach {
            blocks.remove(it)
        }
        refreshStateOfBoard()
    }

    fun moveDownUpperBlocks(whichRow: Int){
        blocks.filter { it.posY < whichRow }.forEach {
            it.moveDown()
        }
        refreshStateOfBoard()
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
        const val PLAYGROUND_WIDTH = 100
        const val PLAYGROUND_HEIGHT = 102

        const val MOVE_RIGHT = 1000
        const val MOVE_LEFT = 1001
        const val MOVE_DOWN = 1002
        const val ROTATE = 1003
    }

}