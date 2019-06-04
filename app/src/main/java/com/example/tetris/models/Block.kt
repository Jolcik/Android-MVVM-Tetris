package com.example.tetris.models

import com.example.tetris.GameController

class Block(var posX: Int, // pozycja X
            var posY: Int, // pozycja Y
            var color: Int // kolor ma wartosc z Paint. itd
) {

    fun moveDown(){
        if(posY < GameController.PLAYGROUND_HEIGHT)
            ++posY
    }

    fun setPosition(x: Int, y: Int){
        if (x in (0 until GameController.PLAYGROUND_WIDTH))
            posX = x

        if (y in 0 until GameController.PLAYGROUND_HEIGHT)
            posY = y
    }

    override fun toString(): String {
        return "[$posX, $posY]"
    }

}