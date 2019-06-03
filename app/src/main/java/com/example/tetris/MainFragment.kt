package com.example.tetris


import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.tetris.models.Block


class MainFragment : Fragment() {

    // viewmodel
    lateinit var model: MainViewModel

    // przyciski sterujace
    lateinit var moveDownButton: Button
    lateinit var moveRightButton: Button
    lateinit var moveLeftButton: Button
    lateinit var turnRightButton: Button
    lateinit var turnLeftButton: Button

    // wyswietla gre tzn. wszystkie bloki itd
    private lateinit var gameDisplayer: Canvas


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // USTAW VIEWMODEL
        model = ViewModelProviders.of(this).get(MainViewModel::class.java)

        // przygotuj wszystkie views
        setupViews()

        // ustaw obserwatora
        model.blockList.observe(this, Observer {
            refreshGameDisplayer(it)
        })


        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    fun refreshGameDisplayer( blocks: ArrayList<Block> ){

    }

    fun setupViews(){

    }


}
