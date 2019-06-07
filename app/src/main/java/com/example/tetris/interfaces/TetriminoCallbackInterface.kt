package com.example.tetris.interfaces

interface TetriminoCallback {
    fun onNewTetriminoCallback() // wywolywane gdy tworzymy nowe tetrimino
    fun tetriminoMovedCallback() // asynchroniczne ruszenie tetrmino, callback dla aktualnosci planszy
    fun gameOverCallback()
    fun moveFailed()
    fun manyRowsDeleted(howMany: Int)
}