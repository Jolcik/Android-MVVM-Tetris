package com.example.tetris

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.lifecycle.MutableLiveData
import com.example.tetris.models.Block

class NextTetriminoSurfaceView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes), SurfaceHolder.Callback {
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
        wasSurfaceCreated.value = false
    }


    fun drawBlocks(blocks: List<Block>){
        var canvas = holder.lockCanvas()
        var paint = Paint()

        paint.strokeWidth = 5f
        paint.style = Paint.Style.FILL

        // Tlo
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        // siatka linii
        var linePaint = Paint()
        linePaint.color = Color.DKGRAY

        for( i in 0..NEXT_TETRIMINO_WIDTH) // pionowe
            canvas?.drawLine((i.toFloat()/NEXT_TETRIMINO_WIDTH)*width, 0f,
                (i.toFloat()/NEXT_TETRIMINO_WIDTH)*width, height.toFloat(),
                linePaint)

        for( i in 0..NEXT_TETRIMINO_HEIGHT) // poziome
            canvas?.drawLine(0f, (i.toFloat()/NEXT_TETRIMINO_HEIGHT)*height,
                width.toFloat(), (i.toFloat()/NEXT_TETRIMINO_HEIGHT)*height,
                linePaint)

        // rysowanie blokow
        blocks.forEach{ block ->
            paint.color = block.color
            var position = createPositionFromBlock(block)
            canvas?.drawRect(position.left, position.top, position.right, position.bottom, paint)
        }

        // obramowanie
        linePaint.color = Color.WHITE
        canvas?.drawLine(0f, 0f, 0f, height.toFloat(), linePaint)
        canvas?.drawLine(0f, 0f, width.toFloat(), 0f, linePaint)
        canvas?.drawLine(0f, height.toFloat()-1, width.toFloat(), height.toFloat()-1, linePaint)
        canvas?.drawLine(width.toFloat()-1, 0f, width.toFloat()-1, height.toFloat(), linePaint)

        holder.unlockCanvasAndPost(canvas) // zaktualizowanie view
    }

    fun createPositionFromBlock(block: Block): BlockPosition {

        val blockWidth = width/NEXT_TETRIMINO_WIDTH.toFloat()
        val blockHeight = height/ NEXT_TETRIMINO_HEIGHT.toFloat()

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

    companion object{
        const val NEXT_TETRIMINO_WIDTH = 6
        const val NEXT_TETRIMINO_HEIGHT = 4
    }
}