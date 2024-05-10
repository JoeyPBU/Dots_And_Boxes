package uk.ac.bournemouth.ap.dotsandboxes.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import org.example.student.dotsboxgame.StudentDotsBoxGame

class DisplayBoard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    var game: StudentDotsBoxGame? = null
        set(value) {
            field = value
            invalidate()
        }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawPlayerTextBox(canvas)
        drawRobotTextBox(canvas)
    }

    private fun drawPlayerTextBox(canvas: Canvas) {
        val playerBoxWidth = width / 2
        val playerBoxHeight = height / 4
        val playerBoxX = 0
        val playerBoxY = 0

        val playerBoxPaint = Paint().apply {
            color = Color.WHITE
            style = Paint.Style.FILL
        }
        canvas.drawRect(
            playerBoxX.toFloat(),
            playerBoxY.toFloat(),
            (playerBoxX + playerBoxWidth).toFloat(),
            (playerBoxY + playerBoxHeight).toFloat(),
            playerBoxPaint
        )

        val playerTextPaint = Paint().apply {
            color = Color.BLACK
            textSize = 50f
        }
        val playerScore = game?.countBoxes()?.get(0) ?: 0
        val playerText = "Player: $playerScore boxes"
        canvas.drawText(
            playerText,
            playerBoxX.toFloat(),
            playerBoxY.toFloat() + playerBoxHeight / 2,
            playerTextPaint
        )
    }

    private fun drawRobotTextBox(canvas: Canvas) {
        val robotBoxWidth = width / 2
        val robotBoxHeight = height / 4
        val robotBoxX = 0
        val robotBoxY = height / 2

        val robotBoxPaint = Paint().apply {
            color = Color.WHITE
            style = Paint.Style.FILL
        }
        canvas.drawRect(
            robotBoxX.toFloat(),
            robotBoxY.toFloat(),
            (robotBoxX + robotBoxWidth).toFloat(),
            (robotBoxY + robotBoxHeight).toFloat(),
            robotBoxPaint
        )

        val robotTextPaint = Paint().apply {
            color = Color.BLACK
            textSize = 50f
        }
        val robotScore = game?.countBoxes()?.get(1) ?: 0
        val robotText = "Robot: $robotScore boxes"
        canvas.drawText(
            robotText,
            robotBoxX.toFloat(),
            robotBoxY.toFloat() + robotBoxHeight / 2,
            robotTextPaint
        )
    }
}