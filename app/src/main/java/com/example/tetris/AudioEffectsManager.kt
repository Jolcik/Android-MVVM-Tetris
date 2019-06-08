package com.example.tetris

import android.content.Context
import android.media.MediaPlayer
import android.media.SoundPool
import com.example.tetris.interfaces.AudioInterface

class AudioEffectsManager(context: Context): AudioInterface {

    val mediaPlayer = MediaPlayer.create(context, R.raw.main_theme)
    val audioSoundPool = SoundPool.Builder().setMaxStreams(9).build()
    var sounds: Array<Int>

    init{
        mediaPlayer.setVolume(0.07f, 0.07f)
        mediaPlayer.isLooping = true
        sounds = arrayOf(
            audioSoundPool.load(context, R.raw.block_fall, 1),
            audioSoundPool.load(context, R.raw.block_move_lr, 1),
            audioSoundPool.load(context, R.raw.block_rotate, 1),
            audioSoundPool.load(context, R.raw.block_rotate_fail, 1),
            audioSoundPool.load(context, R.raw.block_lockdown, 1),
            audioSoundPool.load(context, R.raw.noice_sound, 1),
            audioSoundPool.load(context, R.raw.skamieliny_sound, 1),
            audioSoundPool.load(context, R.raw.aleurwal_sound, 1),
            audioSoundPool.load(context, R.raw.game_over, 1)
        )
    }

    override fun onStartGame() {
        mediaPlayer.start()
        audioSoundPool.stop(sounds[GAME_OVER])
    }

    override fun onEndGame() {
        audioSoundPool.play(sounds[GAME_OVER],0.4f,0.4f,1,0,1f)
        mediaPlayer.stop()
        mediaPlayer.prepare()
    }

    override fun onBlockFall() {
        audioSoundPool.stop(sounds[BLOCK_FALL])
        audioSoundPool.play(sounds[BLOCK_FALL],1f,1f,1,0,1f)
    }

    override fun onMove() {
        audioSoundPool.stop(sounds[MOVE])
        audioSoundPool.play(sounds[MOVE],1f,1f,1,0,1f)
    }

    override fun onRotate() {
        audioSoundPool.stop(sounds[ROTATE])
        audioSoundPool.play(sounds[ROTATE],1f,1f,1,0,1f)
    }

    override fun onRotateFail() {
        audioSoundPool.stop(sounds[ROTATE_FAIL])
        audioSoundPool.play(sounds[ROTATE_FAIL],0.5f,0.5f,1,0,1f)
    }

    override fun onBlockLockdown() {
        audioSoundPool.stop(sounds[BLOCK_LOCKDOWN])
        audioSoundPool.play(sounds[BLOCK_LOCKDOWN],0.8f,0.8f,1,0,1f)
    }

    override fun onManyRowsDeleted(howMany: Int) {
        when(howMany){
            2 -> audioSoundPool.play(sounds[TWO_ROWS_DELETED],0.8f,0.8f,1,0,1f)
            3 -> audioSoundPool.play(sounds[THREE_ROWS_DELETED],0.8f,0.8f,1,0,1f)
            4 -> audioSoundPool.play(sounds[FOUR_ROWS_DELETED],0.5f,0.5f,1,0,1f)
        }
    }

    override fun onPause() {
        mediaPlayer.pause()
        audioSoundPool.autoPause()
    }

    override fun onResume() {
        mediaPlayer.start()
        audioSoundPool.autoResume()
    }

    companion object{
        const val BLOCK_FALL = 0
        const val MOVE = 1
        const val ROTATE = 2
        const val ROTATE_FAIL = 3
        const val BLOCK_LOCKDOWN = 4
        const val TWO_ROWS_DELETED = 5
        const val THREE_ROWS_DELETED = 6
        const val FOUR_ROWS_DELETED = 7
        const val GAME_OVER = 8
    }
}