package uk.ac.bournemouth.ap.dotsandboxes.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import org.example.student.dotsboxgame.StudentDotsBoxGame
import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix


class GameBoard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val linePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
    }

    var boardOffset: Float = 10f
    lateinit var game: StudentDotsBoxGame


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBoard(canvas)
    }


    fun drawBoard(canvas: Canvas) {
        drawGrid(canvas)
        drawBoxes(canvas, 5F)
    }

    fun drawGrid(canvas: Canvas) {
        val columns = game.lines.maxWidth - 1
        val rows = (game.lines.maxHeight - 1) / 2

        val boxWidth = (width - 2 * boardOffset) / columns
        val boxHeight = (height - 2 * boardOffset) / rows

        for (x in 0 until columns + 1) {
            val startX = boardOffset + x * boxWidth
            val startY = boardOffset
            val endX = startX
            val endY = boardOffset + rows * boxHeight
            canvas.drawLine(startX, startY, endX, endY, linePaint)
        }

        for (y in 0 until rows + 1){
            val startX = boardOffset
            val startY = boardOffset + y * boxHeight
            val endX = boardOffset + columns * boxWidth
            val endY = startY
            canvas.drawLine(startX, startY, endX, endY, linePaint)
        }

    }

    fun drawBoxes(canvas: Canvas, gridSize: Float) {

    }
}

