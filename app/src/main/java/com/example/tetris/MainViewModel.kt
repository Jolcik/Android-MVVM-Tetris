package com.example.tetris

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tetris.GameController.Companion.MOVE_DOWN
import com.example.tetris.GameController.Companion.MOVE_LEFT
import com.example.tetris.GameController.Companion.MOVE_RIGHT
import com.example.tetris.GameController.Companion.ROTATE
import com.example.tetris.interfaces.AudioInterface
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

    var isPaused: Boolean = false

    var audioManager: AudioInterface? = null // przygotowanie menadzera audio, acitivity jest ustawia

    lateinit var gameController: GameController // klasa sterujaca gra

    var longPressingButton: Pair<Boolean, Int> // Boolean - czy trwa jakas akcja dlugiego trzymanie
                                                // Int - jaka akcja

    init {
        longPressingButton = Pair(false, 0)
    }

    fun startButtonClicked(){ // nowa gra
        score.value = 0 // ustawiamy znaczniki na poczatkowe
        gameOver.value = false
        blockList.value = listOf()
        isPaused = false
        gameController = GameController(this) // tworzymy nowa gre
        nextTetrimino.value = gameController.nextTetrimino
        nextColor = gameController.nextColor
        tickTime = START_TICK_TIME
        timer = Timer()
        timer.scheduleAtFixedRate(tickTime, tickTime){ onTimerTick() } // ustawiamy timer

        // AUDIO
        audioManager?.onStartGame()
    }

    fun pauseButtonPressed(){
        if(!isPaused){
            isPaused = true // zaznacz ze pauza
            timer.cancel() // odwolaj timer
            audioManager?.onPause() // wykonaj odpowiednie dzialania co do dzwieku
        }
        else{
            isPaused = false // ustawiamy znowu
            timer = Timer() // tworzymy nowy timer z mniejszym delayem, nie da sie spauzowac timera
            timer.scheduleAtFixedRate(tickTime/2, tickTime){ onTimerTick() }
            audioManager?.onResume()
        }
    }

    override fun gameOverCallback() { // koniec gry
        timer.cancel()
        gameOver.postValue(true)

        // AUDIO
        audioManager?.onEndGame()
    }

    fun onTimerTick(){
        gameController.makeTick() // wykonaj tykniecie
        var allBlocks: ArrayList<Block> = arrayListOf() // tworzymy liste ktora polaczy te dwie
        allBlocks.addAll(gameController.blocks)
        allBlocks.addAll(gameController.tetrimino.blocks)
        blockList.postValue(allBlocks) // ustawiamy nowa wartosc, co zalacza obserwatora

        // AUDIO
        audioManager?.onBlockFall()
    }

    fun moveButtonPressed(whichOption: Int){
        Log.d("VM", longPressingButton.first.toString())
        if(!longPressingButton.first && !gameController.isTheGameOver && !isPaused) // jak nie trzymamy jakiegos przycisku
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
        if(!longPressingButton.first && !gameController.isTheGameOver && !isPaused){
            if(whichButton == MOVE_DOWN) {
                timer.cancel()
                timer = Timer()
                timer.scheduleAtFixedRate(SHORT_TICK_TIME, SHORT_TICK_TIME){ onTimerTick() }
                onTimerTick()
            }
            else{
                longPressedTimer = Timer()
                longPressedTimer.scheduleAtFixedRate(SHORT_TICK_TIME, SHORT_TICK_TIME) {
                    gameController.moveTetrimino(whichButton)
                }
                longPressingButton = Pair(true, whichButton)
            }
        }
    }

    fun moveButtonReleased(whichButton: Int){ // puszczamy przycisk
        if(whichButton == longPressingButton.second && !gameController.isTheGameOver && !isPaused) {
            if(whichButton == MOVE_DOWN) // jak w dol to tylko resetujemy timer
                resetTimer()
            else longPressedTimer.cancel() // w przypadku bocznych przyciskow odwolujemy timer
            longPressingButton = Pair(false, 0) // przestawiamy pare
        }
    }

    private fun resetTimer(){ // resetuje timer
        timer.cancel()
        timer = Timer()
        timer.scheduleAtFixedRate(tickTime, tickTime){ onTimerTick() }
    }

    override fun onNewTetriminoCallback() {
        Log.d("VM", "Nowe tetrimino!")
        if(tickTime > MIN_TICK_TIME) // szybsza gra
            tickTime -= TICK_TIME_STEP
        score.postValue(gameController.score) // poprawiamy wynik
        nextColor = gameController.nextColor //ustawiamy nastepny kolor
        nextTetrimino.postValue(gameController.nextTetrimino) // i nastepne tetrimino

        // AUDIO
        audioManager?.onBlockLockdown()
    }

    override fun tetriminoMovedCallback() {
        var allBlocks: ArrayList<Block> = arrayListOf() // tworzymy liste ktora polaczy te dwie
        allBlocks.addAll(gameController.blocks)
        allBlocks.addAll(gameController.tetrimino.blocks)
        blockList.postValue(allBlocks) // ustawiamy nowa wartosc, co zalacza obserwatora

        // AUDIO
        audioManager?.onMove()
    }

    override fun moveFailed() {
        audioManager?.onRotateFail()
        if(longPressingButton.first){
            longPressedTimer.cancel()
            longPressedTimer = Timer()
            longPressingButton = Pair(false, 0)
        }
    }

    override fun manyRowsDeleted(howMany: Int) {
        // tylko audio
        audioManager?.onManyRowsDeleted(howMany)
    }

    companion object{
        const val START_TICK_TIME: Long = 700
        const val SHORT_TICK_TIME: Long = 100
        const val MIN_TICK_TIME: Long = 100
        const val TICK_TIME_STEP = 4
    }

}