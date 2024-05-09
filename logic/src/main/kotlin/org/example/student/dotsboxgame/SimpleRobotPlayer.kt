package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame

class SimpleRobotPlayer : ComputerPlayer {
    override fun makeMove(game: DotsAndBoxesGame) {
        val availableLines = game.lines.filter { !it.isDrawn }
        val linesToCompleteBox = availableLines.filter { line ->
            val (box1, box2) = line.adjacentBoxes
            box1 != null && box2 != null && box1.boundingLines.count { it.isDrawn } == 3
        }

        if (linesToCompleteBox.isNotEmpty()) {
            val winningLine = linesToCompleteBox.first()
            winningLine.drawLine()
        } else {
            val randomLine = availableLines.random()
            randomLine.drawLine()
        }
    }
}
