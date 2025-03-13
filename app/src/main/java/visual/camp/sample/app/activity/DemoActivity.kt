package visual.camp.sample.app.activity

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import camp.visual.gazetracker.GazeTracker
import camp.visual.gazetracker.callback.GazeCallback
import camp.visual.gazetracker.filter.OneEuroFilterManager
import visual.camp.sample.app.GazeTrackerManager
import kotlin.random.Random

class DemoActivity : AppCompatActivity() {
    private lateinit var mazeView: MazeView
    private val oneEuroFilterManager = OneEuroFilterManager(2, 30f, 0.5f, 0.001f, 1.0f)
    private lateinit var gazeTrackerManager: GazeTrackerManager
    private var playerX = 1
    private var playerY = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gazeTrackerManager = GazeTrackerManager.getInstance(applicationContext)
        mazeView = MazeView(this)
        setContentView(mazeView)
    }

    override fun onStart() {
        super.onStart()
        gazeTrackerManager.setGazeTrackerCallbacks(gazeCallback)
    }

    override fun onResume() {
        super.onResume()
        gazeTrackerManager.startGazeTracking()
    }

    override fun onPause() {
        super.onPause()
        gazeTrackerManager.stopGazeTracking()
    }

    override fun onStop() {
        super.onStop()
        gazeTrackerManager.removeCallbacks(gazeCallback)
    }

    private val gazeCallback = GazeCallback { gazeInfo ->
        if (oneEuroFilterManager.filterValues(gazeInfo.timestamp, gazeInfo.x, gazeInfo.y)) {
            val filtered = oneEuroFilterManager.filteredValues  // Get filtered gaze coordinates
            val gazeX = (filtered[0] / mazeView.cellSize).toInt()
            val gazeY = (filtered[1] / mazeView.cellSize).toInt()

            // Check if the move is valid
            if (mazeView.isPath(gazeX, gazeY)) {
                playerX = gazeX
                playerY = gazeY
                mazeView.setPlayerPosition(playerX, playerY)
            }

            // Check if the player reached the goal
            if (playerX == mazeView.exitX && playerY == mazeView.exitY) {
                runOnUiThread {
                    toast("You reached the goal!")
                }
            }
        }
    }


    private fun toast(message: String) {
        runOnUiThread {
            android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    // 🟢 INNER CLASS FOR THE MAZE VIEW 🟢
    inner class MazeView(context: Context) : View(context) {
        private val cols = 25
        private val rows = 25
        var cellSize: Float = 0f
        private val maze = Array(rows) { IntArray(cols) { 1 } } // 1 = Wall, 0 = Path
        private val paint = Paint()
        var exitX = cols - 2
        var exitY = rows - 2
        private var playerX = 1
        private var playerY = 1

        init {
            generateMaze()
        }

        fun setPlayerPosition(x: Int, y: Int) {
            playerX = x
            playerY = y
            invalidate()
        }

        fun isPath(x: Int, y: Int): Boolean {
            return x in 0 until cols && y in 0 until rows && maze[y][x] == 0
        }

        private fun generateMaze() {
            val startX = 1
            val startY = 1
            val stack = mutableListOf(Pair(startX, startY))
            maze[startY][startX] = 0

            val directions = listOf(Pair(2, 0), Pair(-2, 0), Pair(0, 2), Pair(0, -2))

            while (stack.isNotEmpty()) {
                val (x, y) = stack.removeLast()
                directions.shuffled().forEach { (dx, dy) ->
                    val nx = x + dx
                    val ny = y + dy
                    if (nx in 1 until cols - 1 && ny in 1 until rows - 1 && maze[ny][nx] == 1) {
                        maze[ny][nx] = 0
                        maze[y + dy / 2][x + dx / 2] = 0
                        stack.add(Pair(nx, ny))
                    }
                }
            }
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            cellSize = width / cols.toFloat()

            for (row in 0 until rows) {
                for (col in 0 until cols) {
                    paint.color = if (maze[row][col] == 1) Color.BLACK else Color.WHITE
                    canvas.drawRect(
                        col * cellSize, row * cellSize,
                        (col + 1) * cellSize, (row + 1) * cellSize, paint
                    )
                }
            }

            // Draw Player
            paint.color = Color.RED
            canvas.drawCircle(
                (playerX + 0.5f) * cellSize,
                (playerY + 0.5f) * cellSize,
                cellSize / 3,
                paint
            )

            // Draw Exit
            paint.color = Color.GREEN
            canvas.drawCircle(
                (exitX + 0.5f) * cellSize,
                (exitY + 0.5f) * cellSize,
                cellSize / 3,
                paint
            )
        }
    }
}
