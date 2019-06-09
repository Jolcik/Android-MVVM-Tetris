package com.example.tetris

import android.graphics.Color
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tetris.models.Block
import com.example.tetris.models.Tetrimino
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

class ViewModelTest {

    @get:Rule //  bo dzialamy na watkach
    val rule = InstantTaskExecutorRule()

    @Test
    fun constructor_test(){
        val model = MainViewModel()
        assertThat(model.score.value).isNull()
        assertThat(model.gameOver.value).isNull()
        assertThat(model.blockList.value).isNull()
        assertThat(model.isPaused).isFalse()
        assertThat(model.nextTetrimino.value).isNull()
        assertThat(model.nextColor).isEqualTo(0)
        assertThat(model.tickTime).isEqualTo(MainViewModel.START_TICK_TIME)
    }

    @Test
    fun startButtonClicked(){
        val model = MainViewModel()
        model.startButtonClicked()
        assertThat(model.score.value).isEqualTo(0)
        assertThat(model.gameOver.value).isFalse()
        assertThat(model.blockList.value).isEmpty()
        assertThat(model.isPaused).isFalse()
        assertThat(model.nextTetrimino.value).isIn(Tetrimino.shapes)
        assertThat(model.nextColor).isIn(Tetrimino.colors)
        assertThat(model.tickTime).isEqualTo(MainViewModel.START_TICK_TIME)
    }

    @Test
    fun pauseButtonPressed_test(){
        val model = MainViewModel()
        model.startButtonClicked()
        model.pauseButtonPressed()
        assertThat(model.isPaused).isTrue()
        model.pauseButtonPressed()
        assertThat(model.isPaused).isFalse()
    }

    @Test
    fun gameOverCallback_test(){
        val model = MainViewModel()
        model.startButtonClicked()
        model.gameOverCallback()
        assertThat(model.gameOver.value).isTrue()
    }

    @Test
    fun onTimerTick_test(){
        val model = MainViewModel()
        model.startButtonClicked()
        model.onTimerTick()
        assertThat(model.blockList.value).hasSize(4)

        for(i in 0..18) // zeby doszedl na sam dol
            model.onTimerTick()
        assertThat(model.blockList.value).hasSize(8) // spadlo i stworzylo nowe tetrimino
    }

    @Test
    fun moveButtonPressed_test(){
        val model = MainViewModel()
        model.startButtonClicked()

        model.moveButtonPressed(GameController.MOVE_RIGHT) // prawo
        assertPositions(model.gameController.tetrimino, arrayOf(4, 0, 5, 0, 6, 0, 7, 0))

        model.moveButtonPressed(GameController.MOVE_LEFT) // lewo
        assertPositions(model.gameController.tetrimino, arrayOf(3, 0, 4, 0, 5, 0, 6, 0))

        model.moveButtonPressed(GameController.MOVE_DOWN) // dol
        assertPositions(model.gameController.tetrimino, arrayOf(3, 1, 4, 1, 5, 1, 6, 1))

        model.moveButtonPressed(GameController.ROTATE) // rotacja
        assertPositions(model.gameController.tetrimino, arrayOf(4, 0, 4, 1, 4, 2, 4, 3))

        // gra spauzowana
        model.pauseButtonPressed()
        model.moveButtonPressed(GameController.MOVE_DOWN)
        assertPositions(model.gameController.tetrimino, arrayOf(4, 0, 4, 1, 4, 2, 4, 3))

        // trzymamy inny przycisk
        model.pauseButtonPressed() // odpauzowanie
        model.longPressingButton = Pair(true, 0)
        model.moveButtonPressed(GameController.MOVE_DOWN)
        assertPositions(model.gameController.tetrimino, arrayOf(4, 0, 4, 1, 4, 2, 4, 3))
    }

    @Test
    fun longPressAndRelease_test(){
        val model = MainViewModel()
        model.startButtonClicked()

        model.moveButtonLongPressed(GameController.MOVE_LEFT) // wciskamy
        assertThat(model.longPressingButton.first).isTrue()
        assertThat(model.longPressingButton.second).isEqualTo(GameController.MOVE_LEFT)

        model.moveButtonReleased(GameController.MOVE_LEFT) // puszczamy
        assertThat(model.longPressingButton.first).isFalse()
        assertThat(model.longPressingButton.second).isEqualTo(0)
    }

    @Test
    fun onNewTetriminoCallback_test(){
        val model = MainViewModel()
        model.startButtonClicked()

        model.gameController.score = 50
        model.gameController.nextColor = Color.GRAY // takie wartosci nigdy naprawde nie wystapia
        model.gameController.nextTetrimino = 'K'
        model.onNewTetriminoCallback()

        assertThat(model.score.value).isEqualTo(50)
        assertThat(model.nextColor).isEqualTo(Color.GRAY)
        assertThat(model.nextTetrimino.value).isEqualTo('K')
    }

    @Test
    fun tetriminoMovedCallback_test(){
        val model = MainViewModel()
        model.startButtonClicked()

        model.gameController.blocks.add(Block(0,0, Color.RED)) // dodajemy blok
        model.tetriminoMovedCallback()
        assertThat(model.blockList.value).hasSize(5) // czy jest sumowane
    }

    @Test
    fun moveFailed_test(){
        val model = MainViewModel()
        model.startButtonClicked()
        model.moveButtonPressed(GameController.MOVE_LEFT)
        model.moveButtonPressed(GameController.MOVE_LEFT)
        model.moveButtonPressed(GameController.MOVE_LEFT)
        assertThat(model.blockList.value!!.first().posX).isEqualTo(0)

        model.moveButtonLongPressed(GameController.MOVE_LEFT)
        assertThat(model.longPressingButton.first).isTrue()

        model.moveFailed()
        assertThat(model.longPressingButton.first).isFalse()
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