package uk.ac.bournemouth.ap.dotsandboxes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uk.ac.bournemouth.ap.dotsandboxes.views.DisplayBoard
import uk.ac.bournemouth.ap.dotsandboxes.views.EndCard
import uk.ac.bournemouth.ap.dotsandboxes.views.GameBoard


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gameBoard = findViewById<GameBoard>(R.id.gameBoard)
        val displayBoard = findViewById<DisplayBoard>(R.id.displayBoard)
        val endCard = findViewById<EndCard>(R.id.endCard)

        gameBoard.setDisplayBoard(displayBoard)
        gameBoard.setEndCard(endCard)
    }
}
