package com.example.tetris

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tetris.models.Block

class MainViewModel: ViewModel() {

    val blockList: MutableLiveData<ArrayList<Block>> = MutableLiveData()

}