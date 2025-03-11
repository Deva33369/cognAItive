package visual.camp.sample.app.activity

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import camp.visual.gazetracker.GazeTracker
import camp.visual.gazetracker.callback.GazeCallback
import camp.visual.gazetracker.filter.OneEuroFilterManager
import camp.visual.gazetracker.state.EyeMovementState
import camp.visual.gazetracker.util.ViewLayoutChecker
import visual.camp.sample.app.GazeTrackerManager
import visual.camp.sample.app.R
import visual.camp.sample.view.GazePathView
import java.io.IOException
import java.io.InputStream

class DemoActivity : AppCompatActivity() {
    private val viewLayoutChecker = ViewLayoutChecker()
    private var gazePathView: GazePathView? = null
    private var gazeTrackerManager: GazeTrackerManager? = null
    private val oneEuroFilterManager = OneEuroFilterManager(
        2, 30f, 0.5f, 0.001f, 1.0f
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        gazeTrackerManager = GazeTrackerManager.getInstance(applicationContext) // Use getInstance
        Log.i(TAG, "gazeTracker version: ${GazeTracker.getVersionName()}")
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart")
        gazeTrackerManager!!.setGazeTrackerCallbacks(gazeCallback)
        initView()
    }

    override fun onResume() {
        super.onResume()
        gazeTrackerManager!!.startGazeTracking()
        setOffsetOfView()
        Log.i(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        gazeTrackerManager!!.stopGazeTracking()
        Log.i(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        gazeTrackerManager!!.removeCallbacks(gazeCallback)
        Log.i(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun initView() {
        gazePathView = findViewById(R.id.gazePathView)

        val am = resources.assets
        var `is`: InputStream? = null

        try {
            `is` = am.open("palace_seoul.jpg")
            val bm = BitmapFactory.decodeStream(`is`)
            val catView = findViewById<ImageView>(R.id.catImage)
            catView.setImageBitmap(bm)
            `is`.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setOffsetOfView() {
        viewLayoutChecker.setOverlayView(
            gazePathView!!
        ) { x, y ->
            gazePathView!!.setOffset(
                x,
                y
            )
        }
    }

    private val gazeCallback =
        GazeCallback { gazeInfo ->
            if (oneEuroFilterManager.filterValues(gazeInfo.timestamp, gazeInfo.x, gazeInfo.y)) {
                val filtered = oneEuroFilterManager.filteredValues
                gazePathView!!.onGaze(
                    filtered[0],
                    filtered[1],
                    gazeInfo.eyeMovementState == EyeMovementState.FIXATION
                )
            }
        }

    companion object {
        private const val TAG = "DemoActivity"  // Simplified declaration
    }
}
