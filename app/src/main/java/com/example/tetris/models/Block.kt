package com.example.tetris.models

class Block(var posX: Int, // pozycja X
            var posY: Int, // pozycja Y
            var color: Int // kolor ma wartosc z Paint. itd
) {

    fun moveDown(){
        if(posY > 0)
            --posY
    }

    fun setPosition(x: Int, y: Int){
        if (x in (0 until PLAYGROUND_WIDTH))
            posX = x

        if (y in 0 until PLAYGROUND_HEIGHT)
            posY = y
    }

    companion object{
        val PLAYGROUND_WIDTH = 10
        val PLAYGROUND_HEIGHT = 20
    }

}