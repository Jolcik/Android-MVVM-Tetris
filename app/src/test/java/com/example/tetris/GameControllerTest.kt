package com.example.tetris

import android.graphics.Color
import com.example.tetris.models.Block
import com.example.tetris.models.Tetrimino
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class GameControllerTest {

    @Test
    fun constructor_test(){
        val controller = GameController(null) // bez callbackow

        assertThat(controller.blocks).isEmpty()
        for( i in 0 until GameController.PLAYGROUND_WIDTH)
            for( j in 0 until GameController.PLAYGROUND_HEIGHT)
                assertThat(controller.stateOfBoard[i][j]).isEqualTo(0)

        assertThat(controller.isTheGameOver).isFalse()
        assertThat(controller.score).isEqualTo(0)
        assertThat(controller.howManyRowsRemoved).isEqualTo(0)

        assertThat(controller.nextTetrimino).isIn(Tetrimino.shapes)
        assertThat(controller.nextColor).isIn(Tetrimino.colors)
    }


    @Test
    fun moveTetrimino_test(){
        val controller = GameController(null)

        controller.moveTetrimino(GameController.MOVE_RIGHT) // prawo
        assertPositions(controller.tetrimino, arrayOf(4, 0, 5, 0, 6, 0, 7, 0))

        controller.moveTetrimino(GameController.MOVE_LEFT) // lewo
        assertPositions(controller.tetrimino, arrayOf(3, 0, 4, 0, 5, 0, 6, 0))

        controller.tetrimino.moveDown(controller.stateOfBoard) // idziemy w dol
        controller.moveTetrimino(GameController.ROTATE) // rotacja
        assertPositions(controller.tetrimino, arrayOf(4, 0, 4, 1, 4, 2, 4, 3))
    }

    @Test
    fun makeTick_test(){
        val controller = GameController(null)
        controller.makeTick()

        assertThat(controller.blocks).isEmpty()

        for(i in 0..17)
            controller.makeTick() // niech dojdzie na sam dol i sie zalaczy

        assertThat(controller.blocks).isEmpty()
        assertThat(controller.isTheGameOver).isFalse() // zeby gra sie zakonczyla
        controller.blocks.add(Block(4,0, Color.RED))
        controller.makeTick()
        assertThat(controller.isTheGameOver).isTrue()

        assertThat(controller.blocks).isNotEmpty() // czy dodano bloki
        assertThat(controller.stateOfBoard[2][19]).isEqualTo(0) // czy stany sie zgadzaja
        assertThat(controller.stateOfBoard[3][19]).isEqualTo(1)
        assertThat(controller.stateOfBoard[4][19]).isEqualTo(1)
        assertThat(controller.stateOfBoard[5][19]).isEqualTo(1)
        assertThat(controller.stateOfBoard[6][19]).isEqualTo(1)
        assertThat(controller.stateOfBoard[7][19]).isEqualTo(0)
        assertThat(controller.stateOfBoard[3][18]).isEqualTo(0)
        assertThat(controller.stateOfBoard[4][18]).isEqualTo(0)
        assertThat(controller.stateOfBoard[5][18]).isEqualTo(0)
        assertThat(controller.stateOfBoard[6][18]).isEqualTo(0)
    }

    @Test
    fun removingRows_test(){
        // dla jednego rzedu
        var controller = GameController(null)
        for(i in 0..18) // zeby doszedl na sam dol
            controller.makeTick()

        //dodajemy bloki z boku i dwa na gorze
        controller.blocks.addAll(listOf(
            Block(0, 19, Color.RED),
            Block(1, 19, Color.RED),
            Block(2, 19, Color.RED),
            Block(7, 19, Color.RED),
            Block(8, 19, Color.RED),
            Block(9, 19, Color.RED),
            Block(0, 18, Color.RED),
            Block(1, 18, Color.RED)
        ))
        controller.makeTick() // teraz powinno sie usunac
        assertThat(controller.blocks).hasSize(2) // rozmiar listy
        assertThat(controller.score).isEqualTo(40) // wynik

        // dwa rzedy
        controller = GameController(null)
        for(i in 0..18) // zeby doszedl na sam dol
            controller.makeTick()

        //dodajemy bloki z boku i na gorze
        controller.blocks.addAll(listOf(
            Block(0, 19, Color.RED), Block(1, 19, Color.RED),
            Block(2, 19, Color.RED), Block(7, 19, Color.RED),
            Block(8, 19, Color.RED), Block(9, 19, Color.RED),
            Block(0, 18, Color.RED), Block(1, 18, Color.RED),
            Block(2, 18, Color.RED), Block(3, 18, Color.RED),
            Block(4, 18, Color.RED), Block(5, 18, Color.RED),
            Block(6, 18, Color.RED), Block(7, 18, Color.RED),
            Block(8, 18, Color.RED), Block(9, 18, Color.RED),
            Block(9, 17, Color.RED)
        ))
        controller.makeTick()
        assertThat(controller.blocks).hasSize(1)
        assertThat(controller.score).isEqualTo(164)

        // trzy rzedy
        controller = GameController(null)
        for(i in 0..18) // zeby doszedl na sam dol
            controller.makeTick()

        //dodajemy bloki z boku i na gorze
        var blocks = arrayListOf<Block>()
        for( i in 17..18) // dwa rzedy
            for(j in 0..9)
                blocks.add(Block(j, i, Color.RED))

        controller.blocks.addAll(blocks)
        controller.blocks.addAll(listOf(
            Block(0, 19, Color.RED), Block(1, 19, Color.RED),
            Block(2, 19, Color.RED), Block(7, 19, Color.RED),
            Block(8, 19, Color.RED), Block(9, 19, Color.RED),
            Block(0, 16, Color.RED)
        ))
        controller.makeTick()
        assertThat(controller.blocks).hasSize(1)
        assertThat(controller.score).isEqualTo(504)


        // 4 rzedy
        controller = GameController(null)
        for(i in 0..18) // zeby doszedl na sam dol
            controller.makeTick()

        //dodajemy bloki z boku i na gorze
        var blocks2 = arrayListOf<Block>()
        for( i in 16..18) // dwa rzedy
            for(j in 0..9)
                blocks2.add(Block(j, i, Color.RED))

        controller.blocks.addAll(blocks2)
        controller.blocks.addAll(listOf(
            Block(0, 19, Color.RED), Block(1, 19, Color.RED),
            Block(2, 19, Color.RED), Block(7, 19, Color.RED),
            Block(8, 19, Color.RED), Block(9, 19, Color.RED),
            Block(0, 15, Color.RED)
        ))
        controller.makeTick()
        assertThat(controller.blocks).hasSize(1)
        assertThat(controller.score).isEqualTo(1376)
    }



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

}