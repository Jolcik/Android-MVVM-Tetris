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

    val score: MutableLiveData<Int> = MutableLiveData()
    val gameOver: MutableLiveData<Boolean> = MutableLiveData()
    val blockList: MutableLiveData<List<Block>> = MutableLiveData() // lista blokow do obserowania przez view
    val nextTetrimino: MutableLiveData<Char> = MutableLiveData()
    var nextColor: Int = 0

    var timer: Timer = Timer() // timer do wywolywania kolejnych tykniec
    var tickTime: Long = START_TICK_TIME
    var longPressedTimer: Timer = Timer()

    lateinit var gameController: GameController // klasa sterujaca gra

    var longPressingButton: Pair<Boolean, Int> // Boolean - czy trwa jakas akcja dlugiego trzymanie
                                                // Int - jaka akcja

    init {
        longPressingButton = Pair(false, 0)
    }

    fun startButtonClicked(){ // nowa gra
        score.value = 0
        gameOver.value = false
        blockList.value = listOf()
        gameController = GameController(this) // tworzymy nowa gre
        nextTetrimino.value = gameController.nextTetrimino
        nextColor = gameController.nextColor
        tickTime = START_TICK_TIME
        timer = Timer()
        timer.scheduleAtFixedRate(tickTime, tickTime){ onTimerTick() } // ustawiamy timer
    }

    fun onTimerTick(){
        gameController.makeTick() // wykonaj tykniecie
        var allBlocks: ArrayList<Block> = arrayListOf() // tworzymi liste ktora polaczy te dwie
        allBlocks.addAll(gameController.blocks)
        allBlocks.addAll(gameController.tetrimino.blocks)
        blockList.postValue(allBlocks) // ustawiamy nowa wartosc, co zalacza obserwatora
    }

    fun moveButtonPressed(whichOption: Int){
        Log.d("VM", longPressingButton.first.toString())
        if(longPressingButton.first == false && !gameController.isTheGameOver) // jak nie trzymamy jakiegos przycisku
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

    fun moveButtonLongPressed(whichButton: Int){
        if(!longPressingButton.first && !gameController.isTheGameOver){
            if(whichButton == MOVE_DOWN) {
                timer.cancel()
                timer = Timer()
                timer.scheduleAtFixedRate(SHORT_TICK_TIME, SHORT_TICK_TIME){ onTimerTick() }
                onTimerTick()
            }
            else {
                longPressedTimer = Timer()
                longPressedTimer.scheduleAtFixedRate(SHORT_TICK_TIME, SHORT_TICK_TIME) {
                    gameController.moveTetrimino(whichButton)
                }
                longPressingButton = Pair(true, whichButton)
            }
        }
    }

    fun moveButtonReleased(whichButton: Int){
        if(whichButton == longPressingButton.second && !gameController.isTheGameOver) {
            if(whichButton == MOVE_DOWN)
                resetTimer()
            else longPressedTimer.cancel()
            longPressingButton = Pair(false, 0)
        }
    }

    fun resetTimer(){
        timer.cancel()
        timer = Timer()
        timer.scheduleAtFixedRate(tickTime, tickTime){ onTimerTick() }
    }

    override fun onNewTetriminoCallback() { // zeby mozna bylo zwolnic timer po stworzeniu nowego tetrimino
        // chodzi o to ze gdy trzymamy przycisk do poruszania sie w dol, to w trakcie calej akcji tetrimino moze sie
        // ustawic na dole i kolejne zostanie stworzone
        // trzymajac dalej przycisk timer dalej bedzoe wykonywal szybkie tykniecia, co przy slabym refleksie
        // moze prowadzic do produkcji brzydkiego slowa (a nawet kilku!), zatem jestesmy user friendly
        Log.d("VM", "Nowe tetrimino!")
        /*
        if(longPressingButton.first && longPressingButton.second == MOVE_DOWN) {
            resetTimer()
            longPressingButton = Pair(false, 0)
        }
        */
        if(tickTime > MIN_TICK_TIME) // szybsza gra
            tickTime -= TICK_TIME_STEP
        score.postValue(gameController.score)
        nextColor = gameController.nextColor
        nextTetrimino.postValue(gameController.nextTetrimino)
    }

    override fun tetriminoMovedCallback() {
        var allBlocks: ArrayList<Block> = arrayListOf() // tworzymi liste ktora polaczy te dwie
        allBlocks.addAll(gameController.blocks)
        allBlocks.addAll(gameController.tetrimino.blocks)
        blockList.postValue(allBlocks) // ustawiamy nowa wartosc, co zalacza obserwatora
    }

    override fun gameOverCallback() {
        timer.cancel()
        gameOver.postValue(true)
    }

    companion object{
        const val START_TICK_TIME: Long = 700
        const val SHORT_TICK_TIME: Long = 100
        const val MIN_TICK_TIME: Long = 300
        const val TICK_TIME_STEP = 10
    }

}