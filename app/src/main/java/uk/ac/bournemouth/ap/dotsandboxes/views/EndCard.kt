package uk.ac.bournemouth.ap.dotsandboxes.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import org.example.student.dotsboxgame.StudentDotsBoxGame

class EndCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawGoodbye(canvas)
        visibility = VISIBLE
    }

    var game: StudentDotsBoxGame? = null
        set(value) {
            field = value
            invalidate()
        }

    private fun drawGoodbye(canvas: Canvas) {
        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 50f
            textAlign = Paint.Align.CENTER
        }

        val centreX = width / 2f
        val centreY = height / 2f

        val robotScore = game?.countBoxes()?.get(1) ?: 0
        val playerScore = game?.countBoxes()?.get(0) ?: 0

        canvas.drawText("Game Over", centreX, centreY - 50, paint)

        val winnerText = when {
            robotScore > playerScore -> "Robot wins!"
            playerScore > robotScore -> "Player wins!"
            else -> "It's a draw!"
        }

        canvas.drawText(winnerText, centreX, centreY + 50, paint)
    }
}
