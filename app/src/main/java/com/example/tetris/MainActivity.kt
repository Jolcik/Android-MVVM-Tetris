package com.example.tetris

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.tetris.models.Block
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), LifecycleOwner {

    // viewmodel
    lateinit var model: MainViewModel

    // przyciski sterujace
    lateinit var moveDownButton: Button
    lateinit var moveRightButton: Button
    lateinit var moveLeftButton: Button
    lateinit var turnRightButton: Button
    lateinit var turnLeftButton: Button

    // wyswietla gre tzn. wszystkie bloki itd
    private lateinit var gameDisplayer: GameSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // pelny ekran
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        // schowaj action bar
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        // USTAW VIEWMODEL
        model = ViewModelProviders.of(this).get(MainViewModel::class.java)

        // przygotuj wszystkie views
        setupViews()

        // ustaw obserwatora
        model.blockList.observe(this, Observer {
            if (gameDisplayer.wasSurfaceCreated.value == true)
                gameDisplayer.drawBlock(it)
        })

        gameDisplayer.wasSurfaceCreated.observe(this, Observer {
            if(gameDisplayer.wasSurfaceCreated.value == true)
                gameDisplayer.drawBlock(model.blockList.value!!)
        })

        button10.setOnClickListener { model.blockList.value = arrayListOf(Block(5, 6, Color.RED)) }
    }

    fun displayState(){

    }

    fun setupViews(){
        gameDisplayer = findViewById(R.id.surfaceView)


        /*
        //gameDisplayer = GameSurfaceView(this.applicationContext )
        var canvas = Canvas()
        var paint = Paint()
        paint.color = Color.RED

        canvas.drawRect(200f, 100f, 200f, 200f, paint)

        surfaceView.setBackgroundColor(Color.BLUE)

        //val surfaceLayout: LinearLayout = findViewById(R.id.surfaceView)
        //surfaceLayout.addView(gameDisplayer)

        findViewById<Button>(R.id.button10).setOnClickListener {
            (surfaceView as GameSurfaceView).drawBlock(howM)
            howM++
        }

        //surfaceView.drawBlock(1)
        */
    }

}
