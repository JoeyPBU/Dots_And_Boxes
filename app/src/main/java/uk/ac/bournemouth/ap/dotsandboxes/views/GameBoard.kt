package uk.ac.bournemouth.ap.dotsandboxes.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import org.example.student.dotsboxgame.SimpleRobotPlayer
import org.example.student.dotsboxgame.StudentDotsBoxGame
import uk.ac.bournemouth.ap.dotsandboxeslib.Player
import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix
import uk.ac.bournemouth.ap.dotsandboxeslib.AbstractDotsAndBoxesGame.AbstractBox
import uk.ac.bournemouth.ap.dotsandboxeslib.AbstractDotsAndBoxesGame.AbstractLine
import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame.Box
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame.Line
import uk.ac.bournemouth.ap.dotsandboxeslib.HumanPlayer
import uk.ac.bournemouth.ap.lib.matrix.ext.Coordinate
import kotlin.random.Random

class GameBoard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val baseLinePaint = Paint().apply {
        color = Color.GRAY
        strokeWidth = 5f
    }

    private val drawnLinePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 8f
    }

    private val circlePaint = Paint().apply{
        color = Color.RED
        strokeWidth = 10f
    }



    private var boardOffset: Float = 10f
    private lateinit var game: StudentDotsBoxGame
    class Player() : HumanPlayer

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBoard(canvas)
    }


    private fun drawBoard(canvas: Canvas) {
        drawGrid(canvas)
        drawBoxes(canvas, 5F)
    }

    private fun drawGrid(canvas: Canvas) {
        val columns = 6
        val rows = 6

        val boxWidth = (width - (columns + 1) * boardOffset) / columns
        val boxHeight = (height - (rows + 1) * boardOffset) / rows

        for (x in 0..columns) {
            for (y in 0..rows) {
                val startX = boardOffset + x * (boxWidth + boardOffset)
                val endX = startX + boxWidth
                val startY = boardOffset + y * (boxHeight + boardOffset)
                val endY = startY + boxHeight

                val line = game.lines[Coordinate(x, y)]
                val linePaint = if(line.isDrawn) drawnLinePaint else baseLinePaint
                canvas.drawLine(startX, startY, endX, startY, linePaint)
                canvas.drawLine(startX, startY, startX, endY, linePaint)
                canvas.drawCircle(startX, startY, 10f, circlePaint)
            }
        }
    }

    private fun drawBoxes(canvas: Canvas, gridSize: Float) {

    }
}

