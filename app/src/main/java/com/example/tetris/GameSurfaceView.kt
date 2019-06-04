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

        blocks.forEach{ block ->
            paint.color = block.color
            canvas?.drawRect(createRectangleFromBlock(block), paint)
        }

        /*
        when(how){
            1 -> {
                paint.color = Color.YELLOW
                canvas?.drawRect(0f, 0f, 200f, height.toFloat(), paint)
            }
            2 -> {
                paint.color = Color.GREEN
                canvas?.drawRect(50f, 0f, 200f, height.toFloat(), paint)
            }
            3 -> {
                paint.color = Color.RED
                canvas?.drawRect(100f, 0f, 200f, height.toFloat(), paint)
            }
        }
        */

        Log.d("GSV", height.toString())
        Log.d("GSV", width.toString())
        holder.unlockCanvasAndPost(canvas)

    }

    fun createRectangleFromBlock(block: Block): Rect {

        val blockWidth = width/Block.PLAYGROUND_WIDTH
        val blockHeight = height/Block.PLAYGROUND_HEIGHT

        val left = blockWidth * block.posX
        val right = blockWidth * (block.posX+1)
        val top = blockHeight * block.posY
        val bottom = blockHeight * (block.posY+1)

        return Rect(left, right, top, bottom)
    }

}

