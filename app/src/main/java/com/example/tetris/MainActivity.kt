package com.example.tetris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.tetris.models.Block


class MainActivity : AppCompatActivity(), LifecycleOwner {

    // viewmodel
    lateinit var model: MainViewModel

    // przyciski sterujace
    lateinit var moveDownButton: Button
    lateinit var moveRightButton: Button
    lateinit var moveLeftButton: Button
    lateinit var rotateButton: Button
    lateinit var pauseButton: Button

    // wyswietla gre tzn. wszystkie bloki itd
    lateinit var gameDisplayer: GameSurfaceView
    lateinit var nextTetriminoDisplayer: NextTetriminoSurfaceView
    lateinit var scoreText: TextView

    // start screen
    lateinit var startScreenButton: Button
    lateinit var startScreenText: TextView
    lateinit var endScoreText: TextView

    // manager dzwiekow, wywolywany tylko przez viewmodel przez interfejs
    lateinit var audioManager: AudioEffectsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // pelny ekran
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide() // schowaj action bar
        setContentView(R.layout.activity_main) // ustaw layout

        // ustaw viewmodel
        model = ViewModelProviders.of(this).get(MainViewModel::class.java)
        setupViews() // przygotuj wszystkie views
        setupClickListeners() // przygotuj click listenery
        setupObservers()// ustaw obserwatorow

    }

    fun onStartGame(){
        // chowamy te rzeczy
        startScreenText.visibility = View.GONE
        startScreenText.text = " "
        startScreenButton.visibility = View.GONE
        startScreenButton.text = " "
        endScoreText.visibility = View.GONE
        endScoreText.text = " "

        //a teraz sami ustawiamy wszystkie rzeczy na widoczne
        moveDownButton.visibility = View.VISIBLE
        moveRightButton.visibility = View.VISIBLE
        moveLeftButton.visibility = View.VISIBLE
        rotateButton.visibility = View.VISIBLE
        gameDisplayer.visibility = View.VISIBLE
        nextTetriminoDisplayer.visibility = View.VISIBLE
        scoreText.visibility = View.VISIBLE
        pauseButton.visibility = View.VISIBLE

        model.startButtonClicked() // mowimy
    }

    fun onGameOver(){
        // chowamy UI
        moveDownButton.visibility = View.GONE
        moveRightButton.visibility = View.GONE
        moveLeftButton.visibility = View.GONE
        rotateButton.visibility = View.GONE
        gameDisplayer.visibility = View.GONE
        nextTetriminoDisplayer.visibility = View.GONE
        scoreText.visibility = View.GONE
        pauseButton.visibility = View.GONE

        // wyswietlamy koncowe napisy
        startScreenText.text = "GAME OVER!"
        startScreenText.visibility = View.VISIBLE
        startScreenButton.text = "PLAY AGAIN"
        startScreenButton.visibility = View.VISIBLE
        endScoreText.text = "SCORE: ${model.score.value}"
        endScoreText.visibility = View.VISIBLE
    }

    fun onSurfaceCreated(){
        gameDisplayer.drawBlock(model.blockList.value!!)
        scoreText.text = "SCORE: ${model.score.value}"
    }

    fun setNextTetriminoView(){
        // w zaleznosci od tego jakie tetrimino jest nastepne, ustawiamy widok
        val positions = when(model.nextTetrimino.value){
            'I' -> arrayOf(1, 1, 2, 1, 3, 1, 4, 1)
            'T' -> arrayOf(2, 1, 3, 1, 4, 1, 3, 2)
            'O' -> arrayOf(2, 1, 3, 1, 2, 2, 3, 2)
            'L' -> arrayOf(2, 1, 3, 1, 4, 1, 2, 2)
            'J' -> arrayOf(2, 1, 3, 1, 4, 1, 4, 2)
            'S' -> arrayOf(2, 2, 3, 2, 3, 1, 4, 1)
            'Z' -> arrayOf(2, 1, 3, 1, 3, 2, 4, 2)
            else -> arrayOf()
        }
        val blocksToShow = listOf( // tworzymy bloki i nadajemy im pozycje oraz kolor
            Block(positions[0], positions[1], model.nextColor),
            Block(positions[2], positions[3], model.nextColor),
            Block(positions[4], positions[5], model.nextColor),
            Block(positions[6], positions[7], model.nextColor)
        )
        nextTetriminoDisplayer.drawBlocks(blocksToShow)
    }

    fun setupViews(){ // ladujemy wszystkie view
        gameDisplayer = findViewById(R.id.surfaceView)
        nextTetriminoDisplayer = findViewById(R.id.nextTetrimino_view)
        scoreText = findViewById(R.id.score_text)
        moveRightButton = findViewById(R.id.moveRight_button)
        moveLeftButton = findViewById(R.id.moveLeft_button)
        moveDownButton = findViewById(R.id.moveDown_button)
        rotateButton = findViewById(R.id.rotate_button)
        pauseButton = findViewById(R.id.pause_button)

        startScreenButton = findViewById(R.id.startScreen_button)
        startScreenText = findViewById(R.id.startScreen_text)
        endScoreText = findViewById(R.id.startScreenScore_text)

        audioManager = AudioEffectsManager(this.applicationContext) // tworzymy managera audio
        model.audioManager = audioManager // mowimy modelowi ze to jest nasz audio manager
    }

    fun setupClickListeners(){
        startScreenButton.setOnClickListener { onStartGame() }

        moveRightButton.setOnClickListener {
            if(!model.longPressingButton.first) model.moveButtonPressed(GameController.MOVE_RIGHT)
            else model.moveButtonReleased(GameController.MOVE_RIGHT)
        }
        moveLeftButton.setOnClickListener {
            if(!model.longPressingButton.first) model.moveButtonPressed(GameController.MOVE_LEFT)
        }
        moveDownButton.setOnClickListener { model.moveButtonPressed(GameController.MOVE_DOWN) }
        rotateButton.setOnClickListener {
            model.moveButtonPressed(GameController.ROTATE)
        }

        moveRightButton.setOnLongClickListener { model.moveButtonLongPressed(GameController.MOVE_RIGHT); false } // false oznacza ze nie skonsumowalismy eventu
        moveLeftButton.setOnLongClickListener { model.moveButtonLongPressed(GameController.MOVE_LEFT); false } // jest to dlatego ze jak nie skonsumujemy to wywoluje sie zwykly click listener
        moveDownButton.setOnLongClickListener { model.moveButtonLongPressed(GameController.MOVE_DOWN); false }

        pauseButton.setOnClickListener {
            if(model.isPaused) // zmienia obrazek i pauzuje gre
                pauseButton.setBackgroundResource(R.drawable.pause)
            else pauseButton.setBackgroundResource(R.drawable.play)
            model.pauseButtonPressed()
        }
    }

    fun setupObservers(){
        // obserwuje liste blokow i na biezaco wyswietla je na ekranie
        model.blockList.observe(this, Observer {blocks ->
            if (gameDisplayer.wasSurfaceCreated.value == true)
                gameDisplayer.drawBlock(blocks)
        })

        // wyswietlamy tylko jesli powierzchnia jest gotowa
        gameDisplayer.wasSurfaceCreated.observe(this, Observer {surfaceWasCreated ->
            if(surfaceWasCreated)
                onSurfaceCreated()
        })

        // analogicznie w przypadku ekranu nastepnego klocka
        model.nextTetrimino.observe(this, Observer {
            if(nextTetriminoDisplayer.wasSurfaceCreated.value == true)
                setNextTetriminoView()
        })

        nextTetriminoDisplayer.wasSurfaceCreated.observe(this, Observer{surfaceWasCreated ->
            if(surfaceWasCreated)
                setNextTetriminoView()
        })

        // obserwuje moment kiedy gra sie zakonczy
        model.gameOver.observe(this, Observer{gameOver ->
            if(gameOver)
                onGameOver()
        })

        // obserwuje wynik i wyseitla go na biezaco
        model.score.observe(this, Observer{
            scoreText.text = "SCORE: $it"
        })


    }


    override fun onPause() { // wyjscie z aplikacji ja pauzuje
        super.onPause()

        if(gameDisplayer.isVisible && !model.isPaused)
            pauseButton.callOnClick()
            //model.pauseButtonPressed()
    }

    override fun onResume(){ // powrot odpauzowuje
        super.onResume()

        if(gameDisplayer.isVisible && model.isPaused)
            pauseButton.callOnClick()
            //model.pauseButtonPressed()
    }

}
