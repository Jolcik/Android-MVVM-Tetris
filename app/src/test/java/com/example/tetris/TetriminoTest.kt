package com.example.tetris

import android.graphics.Color
import com.example.tetris.models.Tetrimino
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TetriminoTest {

    @Test
    fun constructor_test(){
        val tetrimino = getTetriminoSample()
        assertThat(tetrimino.shape).isEqualTo('I')
        assertThat(tetrimino.color).isEqualTo(Color.BLUE)
        assertThat(tetrimino.blocks).hasSize(4)
        assertPositions(tetrimino, arrayOf(3, 0, 4, 0, 5, 0, 6, 0))
    }

    @Test
    fun getNextTetrimino_test(){
        val tetrimino = getTetriminoSample()
        for(i in (0..4))
            assertThat(tetrimino.getNextTetrimino()).isIn(Tetrimino.shapes)
    }

    @Test
    fun getNextColor_test(){
        val tetrimino = getTetriminoSample()
        for(i in (0..4))
            assertThat(tetrimino.getNextColor()).isIn(Tetrimino.colors)
    }

    @Test
    fun moveDown_test(){
        val tetrimino = getTetriminoSample()
        var state = initStateBoard()

        // test zwyklego spadania
        assertThat(tetrimino.moveDown(state)).isTrue()
        assertPositions(tetrimino, arrayOf(3, 1, 4, 1, 5, 1, 6, 1))

        state[3][2] = 1 // nie mozna sie ruszyc
        assertThat(tetrimino.moveDown(state)).isFalse()
        assertPositions(tetrimino, arrayOf(3, 1, 4, 1, 5, 1, 6, 1))
    }

    @Test
    fun moveRight_test(){
        val tetrimino = getTetriminoSample()
        var state = initStateBoard()

        assertThat(tetrimino.moveRight(state)).isTrue()
        assertPositions(tetrimino, arrayOf(4, 0, 5, 0, 6, 0, 7, 0))

        // zajety blok
        state[8][0] = 1
        assertThat(tetrimino.moveRight(state)).isFalse()
        assertPositions(tetrimino, arrayOf(4, 0, 5, 0, 6, 0, 7, 0))

        // maksymalnie na prawo
        state[8][0] = 0
        assertThat(tetrimino.moveRight(state)).isTrue()
        assertThat(tetrimino.moveRight(state)).isTrue() // przesuwamy sie
        assertThat(tetrimino.moveRight(state)).isFalse() // tu juz nie mozna
        assertPositions(tetrimino, arrayOf(6, 0, 7, 0, 8, 0, 9, 0))
    }

    @Test
    fun moveLeft_test(){
        val tetrimino = getTetriminoSample()
        var state = initStateBoard()

        assertThat(tetrimino.moveLeft(state)).isTrue()
        assertPositions(tetrimino, arrayOf(2, 0, 3, 0, 4, 0, 5, 0))

        // zajety blok
        state[1][0] = 1
        assertThat(tetrimino.moveLeft(state)).isFalse()
        assertPositions(tetrimino, arrayOf(2, 0, 3, 0, 4, 0, 5, 0))

        // maksymalnie na lewo
        state[1][0] = 0
        assertThat(tetrimino.moveLeft(state)).isTrue() // przesuwamy sie
        assertThat(tetrimino.moveLeft(state)).isTrue()
        assertThat(tetrimino.moveLeft(state)).isFalse() // tu juz nie mozna
        assertPositions(tetrimino, arrayOf(0, 0, 1, 0, 2, 0, 3, 0))
    }

    @Test
    fun rotate_test(){
        val tetrimino = getTetriminoSample()
        var state = initStateBoard()

        assertThat(tetrimino.rotate(state)).isFalse() // nie mozna sie obrocic od razu po stworzeniu
        assertPositions(tetrimino, arrayOf(3, 0, 4, 0, 5, 0, 6, 0))

        // obracania
        assertThat(tetrimino.moveDown(state)).isTrue()
        assertThat(tetrimino.rotate(state)).isTrue()
        assertPositions(tetrimino, arrayOf(4, 0, 4, 1, 4, 2, 4, 3))

        assertThat(tetrimino.rotate(state)).isTrue()
        assertPositions(tetrimino, arrayOf(5, 1, 4, 1, 3, 1, 2, 1))

        assertThat(tetrimino.rotate(state)).isFalse()
        assertThat(tetrimino.moveDown(state)).isTrue()
        assertThat(tetrimino.rotate(state)).isTrue()
        assertPositions(tetrimino, arrayOf(4, 3, 4, 2, 4, 1, 4, 0))

        // obracanie po lewej stronie
        for( i in 0..3)
            assertThat(tetrimino.moveLeft(state)).isTrue()
        assertThat(tetrimino.rotate(state)).isFalse()
        assertPositions(tetrimino, arrayOf(0, 3, 0, 2, 0, 1, 0, 0))

        // obracanie po prawej stronie
        for( i in 0..8)
            assertThat(tetrimino.moveRight(state)).isTrue()
        assertThat(tetrimino.rotate(state)).isFalse()
        assertPositions(tetrimino, arrayOf(9, 3, 9, 2, 9, 1, 9, 0))

        // obracanie z klockiem na planszy
        assertThat(tetrimino.moveLeft(state)).isTrue()
        state[9][2] = 1
        assertThat(tetrimino.rotate(state)).isFalse()

    }

    // funkcje pomocnicze
    fun assertPositions(tetrimino: Tetrimino, positions: Array<Int>){
        assertThat(tetrimino.blocks[0].posX).isEqualTo(positions[0])
        assertThat(tetrimino.blocks[0].posY).isEqualTo(positions[1])
        assertThat(tetrimino.blocks[1].posX).isEqualTo(positions[2])
        assertThat(tetrimino.blocks[1].posY).isEqualTo(positions[3])
        assertThat(tetrimino.blocks[2].posX).isEqualTo(positions[4])
        assertThat(tetrimino.blocks[2].posY).isEqualTo(positions[5])
        assertThat(tetrimino.blocks[3].posX).isEqualTo(positions[6])
        assertThat(tetrimino.blocks[3].posY).isEqualTo(positions[7])
    }

    fun initStateBoard(): Array<Array<Int>>{
        var stateBoard: Array<Array<Int>> = arrayOf()
        for (i in (0 until GameController.PLAYGROUND_WIDTH)){
            var array = arrayOf<Int>()
            for (j in (0 until GameController.PLAYGROUND_HEIGHT)){
                array += 0
            }
            stateBoard += array
        }
        return stateBoard
    }

    fun getTetriminoSample(): Tetrimino{
        return Tetrimino('I', Color.BLUE)
    }
}