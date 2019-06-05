package com.example.tetris

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tetris.GameController.Companion.MOVE_DOWN
import com.example.tetris.GameController.Companion.MOVE_LEFT
import com.example.tetris.GameController.Companion.MOVE_RIGHT
import com.example.tetris.GameController.Companion.ROTATE
import com.example.tetris.interfaces.TetriminoCallback
import com.example.tetris.models.Block
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.scheduleAtFixedRate

class MainViewModel: ViewModel(), TetriminoCallback {

    val blockList: MutableLiveData<List<Block>> = MutableLiveData() // lista blokow do obserowania przez view
    var timer: Timer = Timer() // timer do wywolywania kolejnych tykniec

    var gameController: GameController // klasa sterujaca gra

    init {
        // inicjalizacja zmiennych
        blockList.value = listOf<Block>()
        gameController = GameController(this)

        // ustawiamy timer
        timer.scheduleAtFixedRate(1000, 500){ onTimerTick() }
    }

    fun onTimerTick(){
        gameController.makeTick() // wykonaj tykniecie
        var allBlocks: ArrayList<Block> = arrayListOf() // tworzymi liste ktora polaczy te dwie
        allBlocks.addAll(gameController.blocks)
        allBlocks.addAll(gameController.tetrimino.blocks)
        blockList.postValue(allBlocks) // ustawiamy nowa wartosc, co zalacza obserwatora
    }

    fun moveButtonPressed(whichOption: Int){
        when (whichOption) { // informujemy o wcisnieciu przycisku
            MOVE_RIGHT -> gameController.moveTetrimino(MOVE_RIGHT)
            MOVE_LEFT -> gameController.moveTetrimino(MOVE_LEFT)
            MOVE_DOWN -> {
                resetTimer()
                onTimerTick()
            }
            ROTATE -> gameController.moveTetrimino(ROTATE)
        }
    }

    fun moveButtonLongPressed(whichOption: Int){
        when (whichOption) { // informujemy o wcisnieciu przycisku
            MOVE_RIGHT -> gameController.moveTetrimino(MOVE_RIGHT)
            MOVE_LEFT -> gameController.moveTetrimino(MOVE_LEFT)
            MOVE_DOWN -> gameController.moveTetrimino(MOVE_DOWN)
            ROTATE -> gameController.moveTetrimino(ROTATE)
        }
    }

    fun resetTimer(){
        timer.cancel()
        timer = Timer()
        timer.scheduleAtFixedRate(500, 500){ onTimerTick() }
    }

    override fun onNewTetriminoCallback() { // zeby mozna bylo zwolnic timer po stworzeniu nowego tetrimino
        // chodzi o to ze gdy trzymamy przycisk do poruszania sie w dol, to w trakcie calej akcji tetrimino moze sie
        // ustawic na dole i kolejne zostanie stworzone
        // trzymajac dalej przycisk timer dalej bedzoe wykonywal szybkie tykniecia, co przy slabym refleksie
        // moze prowadzic do produkcji brzydkiego slowa (a nawet kilku), zatem jestesmy user friendly


        Log.d("VM", "Nowe tetrimino!")
    }

    override fun tetriminoMovedCallback() {
        var allBlocks: ArrayList<Block> = arrayListOf() // tworzymi liste ktora polaczy te dwie
        allBlocks.addAll(gameController.blocks)
        allBlocks.addAll(gameController.tetrimino.blocks)
        blockList.postValue(allBlocks) // ustawiamy nowa wartosc, co zalacza obserwatora
    }


}