package uk.ac.bournemouth.ap.dotsandboxes.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet.Motion
import androidx.core.view.GestureDetectorCompat
import org.example.student.dotsboxgame.SimpleHumanPlayer
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
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt
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
        color = Color.DKGRAY
        strokeWidth = 10f
    }

    private val circlePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 13f
    }


    private var boardOffset: Float = 10f
    private var game: StudentDotsBoxGame =
        StudentDotsBoxGame(6, 6, listOf(SimpleHumanPlayer(), SimpleRobotPlayer()))
    private val columns get() = game.columns
    private val rows get() = game.rows


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBoxes(canvas)
        drawGrid(canvas)

    }


    private fun drawGrid(canvas: Canvas) {

        val boxWidth = (width - (columns - 1) * boardOffset) / columns
        val boxHeight = (height - (rows - 1) * boardOffset) / rows

        val startXOffset = width / 12
        val startYOffset = height / 12

        for (x in 0 until columns) {
            for (y in 0 until rows) {
                val startX = startXOffset + x * (boxWidth + boardOffset)
                val endX = startX + boxWidth
                val startY = startYOffset + y * (boxHeight + boardOffset)
                val endY = startY + boxHeight

                val xLine = game.lines[x, y * 2]
                val xLinePaint = if (xLine.isDrawn) drawnLinePaint else baseLinePaint
                if (x < columns - 1) {
                    canvas.drawLine(startX, startY, endX, startY, xLinePaint)
                }

                val yLine = game.lines[x, y * 2 + 1]
                val yLinePaint = if (yLine.isDrawn) drawnLinePaint else baseLinePaint
                if (y < rows - 1) {
                    canvas.drawLine(startX, startY, startX, endY, yLinePaint)
                }
                canvas.drawCircle(startX, startY, 10f, circlePaint)
            }
        }
    }


    private fun drawBoxes(canvas: Canvas) {
        val boxWidth = (width - (columns - 1) * boardOffset) / columns
        val boxHeight = (height - (rows - 1) * boardOffset) / rows

        val startXOffset = width / 12
        val startYOffset = height / 12

        for (box in game.boxes) {
            if (box.owningPlayer != null) {
                val startX = startXOffset + box.boxX * (boxWidth + boardOffset)
                val startY = startYOffset + box.boxY.coerceAtMost(rows - 1) * (boxHeight + boardOffset)

                val boxFill = Paint().apply {
                    color = when (box.owningPlayer) {
                        is SimpleRobotPlayer -> Color.CYAN
                        is SimpleHumanPlayer -> Color.GREEN
                        else -> Color.TRANSPARENT
                    }
                    style = Paint.Style.FILL
                }
                canvas.drawRect(startX, startY, startX + boxWidth + 5, startY + boxHeight + 5, boxFill)
            }
        }
    }

    private val gestureDetector = GestureDetectorCompat(context, object:
        GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean = true

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val boxWidth = (width - (columns - 1) * boardOffset) / columns
            val boxHeight = (height - (rows - 1) * boardOffset) / rows
            val maxDistance = boxHeight * 0.3f
            val startXOffset = width / 12
            val startYOffset = height / 12

            for (line in game.lines) {
                if (!line.isDrawn) {
                    val lineX = startXOffset + line.lineX * boxWidth
                    var lineY = startYOffset + line.lineY * (boxHeight / 2)
                    var endX = lineX
                    var endY = lineY

                    if (line.lineY % 2 == 0) {
                        endX += boxHeight
                    } else {
                        lineY -= boxWidth / 2
                        endY = lineY + boxHeight
                        endX = lineX
                    }

                    if (e.x >= lineX - maxDistance && e.x <= endX + maxDistance && e.y >= lineY - maxDistance && e.y <= endY + maxDistance) {
                        line.drawLine()
                        invalidate()
                        return true
                    }
                }
            }
            return false
        }
    })

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }
}
