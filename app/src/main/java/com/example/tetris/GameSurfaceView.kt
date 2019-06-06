package com.example.tetris

import android.content.Context
import android.graphics.*
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.lifecycle.MutableLiveData
import com.example.tetris.models.Block

class GameSurfaceView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes), SurfaceHolder.Callback {

    var surfaceHolder  = holder

    var wasSurfaceCreated: MutableLiveData<Boolean> = MutableLiveData()

    init {
        surfaceHolder.addCallback(this)
        setZOrderOnTop(true)
        holder.setFormat(PixelFormat.TRANSLUCENT)

        wasSurfaceCreated.value = false
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        Log.d("GSV", "CHANGED")
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        wasSurfaceCreated.value = true
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {

    }


    fun drawBlock(blocks: List<Block>){
        var canvas = holder.lockCanvas()
        var paint = Paint()

        paint.strokeWidth = 5f
        paint.style = Paint.Style.FILL

        // Tlo
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        // siatka linii
        var linePaint = Paint()
        linePaint.color = Color.DKGRAY

        for( i in 0..GameController.PLAYGROUND_WIDTH) // pionowe
            canvas?.drawLine((i.toFloat()/GameController.PLAYGROUND_WIDTH)*width, 0f,
                (i.toFloat()/GameController.PLAYGROUND_WIDTH)*width, height.toFloat(),
                linePaint)

        for( i in 0..GameController.PLAYGROUND_HEIGHT) // poziome
            canvas?.drawLine(0f, (i.toFloat()/GameController.PLAYGROUND_HEIGHT)*height,
                width.toFloat(), (i.toFloat()/GameController.PLAYGROUND_HEIGHT)*height,
                linePaint)

        // rysowanie blokow
        blocks.forEach{ block ->
            paint.color = block.color
            var position = createPositionFromBlock(block)
            canvas?.drawRect(position.left, position.top, position.right, position.bottom, paint)
        }

        holder.unlockCanvasAndPost(canvas) // zaktualizowanie view
    }

    fun createPositionFromBlock(block: Block): BlockPosition {

        val blockWidth = width/GameController.PLAYGROUND_WIDTH.toFloat()
        val blockHeight = height/GameController.PLAYGROUND_HEIGHT.toFloat()

        val left = blockWidth * block.posX
        val right = blockWidth * (block.posX+1)
        val top = blockHeight * block.posY
        val bottom = blockHeight * (block.posY+1)

        return BlockPosition(left, right, top, bottom)
    }

    data class BlockPosition(val left: Float,
                             val right: Float,
                             val top: Float,
                             val bottom: Float)

}

