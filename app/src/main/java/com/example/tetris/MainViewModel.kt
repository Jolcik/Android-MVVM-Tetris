package com.example.tetris

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tetris.interfaces.NewTetriminoCallback
import com.example.tetris.models.Block
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.scheduleAtFixedRate

class MainViewModel: ViewModel(), NewTetriminoCallback {

    val blockList: MutableLiveData<List<Block>> = MutableLiveData()
    var timer: Timer = Timer()

    var gameController: GameController

    init {
        // inicjalizacja zmiennych
        blockList.value = listOf<Block>()
        gameController = GameController(this)

        // ustawiamy timer
        timer.scheduleAtFixedRate(1000, 1000){ onTimerTick() }
    }

    fun onTimerTick(){
        gameController.makeTick() // wykonaj tykniecie
        var allBlocks: ArrayList<Block> = arrayListOf() // tworzymi liste ktora polaczy te dwie
        allBlocks.addAll(gameController.blocks)
        allBlocks.addAll(gameController.tetrimino.blocks)
        Log.d("VM", gameController.tetrimino.blocks.toString())
        blockList.postValue(allBlocks) // ustawiamy nowa wartosc, co zalacza obserwatora
    }


    override fun onNewTetriminoCallback() {
        Log.d("VM", "Nowe tetrimino!")
    }

}