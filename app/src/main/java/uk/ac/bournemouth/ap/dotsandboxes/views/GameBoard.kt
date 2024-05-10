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
    private var endCard: EndCard? = null


    /**
     * Sets the display board for the game.
     *
     * @param displayBoard The display board to be set.
     */
    fun setDisplayBoard(displayBoard: DisplayBoard) {
        this.displayBoard = displayBoard
    }

    /**
     * Sets the end card for the game.
     *
     * @param endCard The end card to be set.
     */
    fun setEndCard(endCard: EndCard){
        this.endCard = endCard
    }

    /**
     * Updates the display board with the current game state.
     *
     * @param game The StudentDotsBoxGame object representing the current game state.
     */
    private fun updateDisplayBoard(game: StudentDotsBoxGame?) {
        displayBoard?.game = game
        displayBoard?.invalidate()
    }

    /**
     * Displays the end card when the game ends.
     *
     * @param game The StudentDotsBoxGame object representing the final game state.
     */
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


    /**
     * Draws the grid for the game on the given canvas.
     * The grid consists of boxes and lines representing the boundaries of each box.
     * Circles are also drawn at the intersection points of the grid.
     *
     * @param canvas The Canvas object on which the grid will be drawn.
     */
    private fun drawGrid(canvas: Canvas) {
        val boxWidth = (width - (columns - 1) * boardOffset) / columns
        val boxHeight = (height - (rows - 1) * boardOffset) / rows
        val startXOffset = width / 12
        val startYOffset = height / 12

        // Iterate over each column and row to draw the grid lines and circles
        for (x in 0 until columns) {
            for (y in 0 until rows) {
                // Calculate the coordinates for drawing lines and circles
                val startX = startXOffset + x * (boxWidth + boardOffset)
                val endX = startX + boxWidth
                val startY = startYOffset + y * (boxHeight + boardOffset)
                val endY = startY + boxHeight

                // Draw horizontal lines for each box
                val xLine = game.lines[x, y * 2]
                val xLinePaint = if (xLine.isDrawn) drawnLinePaint else baseLinePaint
                if (x < columns - 1) {
                    canvas.drawLine(startX, startY, endX, startY, xLinePaint)
                }

                // Draw vertical lines for each box
                val yLine = game.lines[x, y * 2 + 1]
                val yLinePaint = if (yLine.isDrawn) drawnLinePaint else baseLinePaint
                if (y < rows - 1) {
                    canvas.drawLine(startX, startY, startX, endY, yLinePaint)
                }

                // Draw dots at the intersection points of the grid
                canvas.drawCircle(startX, startY, 10f, circlePaint)
            }
        }
    }


    /**
     * Draws the boxes on the game grid representing completed areas.
     * Each box is filled with color based on the owning player's type.
     *
     * @param canvas The Canvas object on which the boxes will be drawn.
     */
    private fun drawBoxes(canvas: Canvas) {
        val boxWidth = (width - (columns - 1) * boardOffset) / columns
        val boxHeight = (height - (rows - 1) * boardOffset) / rows
        val startXOffset = width / 12
        val startYOffset = height / 12

        // Iterate over each box and draw filled rectangles representing completed areas
        for (x in 0 until columns - 1) {
            for (y in 0 until rows - 1) {
                val box = game.boxes[x, y]
                if (box.owningPlayer != null) {
                    // Calculate the coordinates for drawing the box
                    val startX = startXOffset + box.boxX * (boxWidth + boardOffset)
                    val startY = startYOffset + box.boxY * (boxHeight + boardOffset)

                    // Adjust the start coordinates to fit within the canvas bounds
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


    /**
     * GestureDetector used for detecting touch gestures on the game grid.
     * It detects single taps to draw lines on the grid when a valid touch is detected.
     */
    private val gestureDetector = GestureDetectorCompat(context, object :
        GestureDetector.SimpleOnGestureListener() {

        /**
         * Called when a down motion event is detected.
         *
         * @param e The motion event.
         * @return true if the event is consumed, false otherwise.
         */
        override fun onDown(e: MotionEvent): Boolean = true

        /**
         * Called when a single-tap gesture is detected.
         *
         * @param e The motion event.
         * @return true if the event is consumed, false otherwise.
         */
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val boxWidth = (width - (columns - 1) * boardOffset) / columns
            val boxHeight = (height - (rows - 1) * boardOffset) / rows
            val maxDistance = boxHeight * 0.3f
            val startXOffset = width / 12
            val startYOffset = height / 12

            // Iterate over each line to check if the tap is close enough to it to draw
            for (line in game.lines) {
                if (!line.isDrawn) {
                    val lineX = startXOffset + line.lineX * boxWidth
                    var lineY = startYOffset + line.lineY * (boxHeight / 2)
                    var endX = lineX
                    var endY = lineY

                    // Adjust line coordinates based on orientation
                    if (line.lineY % 2 == 0) {
                        endX += boxHeight
                    } else {
                        lineY -= boxWidth / 2
                        endY = lineY + boxHeight
                        endX = lineX
                    }

                    // Check if the tap is within a certain distance of the line
                    if (e.x >= lineX - maxDistance && e.x <= endX + maxDistance &&
                        e.y >= lineY - maxDistance && e.y <= endY + maxDistance
                    ) {
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
