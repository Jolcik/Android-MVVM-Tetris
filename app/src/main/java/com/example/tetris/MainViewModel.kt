package com.example.tetris

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tetris.models.Block

class MainViewModel: ViewModel() {

    val blockList: MutableLiveData<List<Block>> = MutableLiveData()

    init {
        blockList.value = listOf<Block>(
            Block(1, 2, Color.GREEN),
            Block(3, 4, Color.YELLOW))
    }

}