package com.example.tetris.interfaces

interface AudioInterface {
    fun onBlockFall()
    fun onMove()
    fun onRotate()
    fun onRotateFail()
    fun onBlockLockdown()
    fun onStartGame()
    fun onEndGame()
    fun onManyRowsDeleted(howMany: Int)
}