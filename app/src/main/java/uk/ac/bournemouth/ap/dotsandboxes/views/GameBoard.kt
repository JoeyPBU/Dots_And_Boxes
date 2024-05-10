package uk.ac.bournemouth.ap.dotsandboxes.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import org.example.student.dotsboxgame.SimpleHumanPlayer
import org.example.student.dotsboxgame.SimpleRobotPlayer
import org.example.student.dotsboxgame.StudentDotsBoxGame


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

    private var displayBoard: DisplayBoard? = null

    fun setDisplayBoard(displayBoard: DisplayBoard) {
        this.displayBoard = displayBoard
    }

    fun setEndCard(endCard: EndCard){
        this.endCard = endCard
    }

    private fun updateDisplayBoard(game: StudentDotsBoxGame?) {
        displayBoard?.game = game
        displayBoard?.invalidate()
    }

    private var endCard: EndCard? = null


    private fun displayEndCard(game: StudentDotsBoxGame) {
        endCard?.game = game
        visibility = GONE
        endCard?.visibility = VISIBLE
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBoxes(canvas)
        drawGrid(canvas)

        if ((0 until columns - 1).all { x ->
                (0 until rows - 1).all { y ->
                    game.boxes[x, y].owningPlayer != null
                }
            })  {
            displayEndCard(game)
            return
        }
        updateDisplayBoard(game)
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

        for (x in 0 until columns - 1) {
            for (y in 0 until rows - 1) {
                val box = game.boxes[x, y]
                if (box.owningPlayer != null) {
                    val startX = startXOffset + box.boxX * (boxWidth + boardOffset)
                    val startY = startYOffset + box.boxY * (boxHeight + boardOffset)

                    val adjustedStartX = startX.coerceAtMost(width - boxWidth)
                    val adjustedStartY = startY.coerceAtMost(height - boxHeight)

                    val boxFill = Paint().apply {
                        color = when (box.owningPlayer) {
                            is SimpleRobotPlayer -> Color.CYAN
                            is SimpleHumanPlayer -> Color.GREEN
                            else -> Color.TRANSPARENT
                        }
                        style = Paint.Style.FILL
                    }
                    canvas.drawRect(
                        adjustedStartX, adjustedStartY,
                        adjustedStartX + boxWidth + 5, adjustedStartY + boxHeight + 5, boxFill
                    )
                }
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
