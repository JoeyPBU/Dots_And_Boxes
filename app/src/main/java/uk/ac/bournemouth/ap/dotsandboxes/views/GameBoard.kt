package uk.ac.bournemouth.ap.dotsandboxes.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View


class GameBoard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val linePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 5f
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(0f, 0f, width.toFloat(), height.toFloat(), linePaint)

        drawStartCard(canvas)
        drawBoard(canvas)
        drawEndCard(canvas)

    }


    fun drawStartCard(canvas: Canvas){
        // Draw a box in the middle of the screen with two Buttons
        // Two Player and Versus Bot
        // Start Button once mode selected
    }


    fun drawBoard(canvas: Canvas){
        drawBoardDisplay(canvas)
        drawGrid(canvas)
        drawBoxes(canvas, 5F)
    }


    fun drawBoardDisplay(canvas: Canvas){
        // Draw a four textBoxes in a grid
        // Two for the Players
        // Two for their respective Scores
    }


    fun drawGrid(canvas: Canvas){

    }


    fun drawBoxes(canvas: Canvas, gridSize: Float){

    }


    fun drawEndCard(canvas: Canvas){
        // Draw a box in the middle of the screen with a textBox and Button
        // The textBox will display the total scores of the Players, highlighting the Winner
        // The Button will restart the View
    }
}