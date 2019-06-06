package com.example.tetris

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
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
    lateinit var rotateButton: Button

    // wyswietla gre tzn. wszystkie bloki itd
    private lateinit var gameDisplayer: GameSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // pelny ekran
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide() // schowaj action bar
        setContentView(R.layout.activity_main) // ustaw layout

        // USTAW VIEWMODEL
        model = ViewModelProviders.of(this).get(MainViewModel::class.java)
        setupViews() // przygotuj wszystkie views
        setupClickListeners()

        // ustaw obserwatora
        model.blockList.observe(this, Observer {
            if (gameDisplayer.wasSurfaceCreated.value == true)
                gameDisplayer.drawBlock(it)
        })

        gameDisplayer.wasSurfaceCreated.observe(this, Observer {
            if(it == true)
                gameDisplayer.drawBlock(model.blockList.value!!)
        })


    }

    fun displayState(){

    }

    fun setupViews(){
        gameDisplayer = findViewById(R.id.surfaceView)
        moveRightButton = findViewById(R.id.moveRight_button)
        moveLeftButton = findViewById(R.id.moveLeft_button)
        moveDownButton = findViewById(R.id.moveDown_button)
        rotateButton = findViewById(R.id.rotate_button)
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


    fun setupClickListeners(){
        moveRightButton.setOnClickListener {
            if(!model.longPressingButton.first) model.moveButtonPressed(GameController.MOVE_RIGHT)
            else model.moveButtonReleased(GameController.MOVE_RIGHT)
        }
        moveLeftButton.setOnClickListener {
            if(!model.longPressingButton.first) model.moveButtonPressed(GameController.MOVE_LEFT)
            else model.moveButtonReleased(GameController.MOVE_LEFT)
        }
        moveDownButton.setOnClickListener { model.moveButtonPressed(GameController.MOVE_DOWN) }
        rotateButton.setOnClickListener { model.moveButtonPressed(GameController.ROTATE) }

        moveRightButton.setOnLongClickListener { model.moveButtonLongPressed(GameController.MOVE_RIGHT); false } // false oznacza ze nie skonsumowalismy eventu
        moveLeftButton.setOnLongClickListener { model.moveButtonLongPressed(GameController.MOVE_LEFT); false } // jest to dlatego ze jak nie skonsumujemy to wywoluje sie zwykly click listener
        moveDownButton.setOnLongClickListener { model.moveButtonLongPressed(GameController.MOVE_DOWN); false }

    }

}
