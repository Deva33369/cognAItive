package visual.camp.sample.app

import android.content.Context
import android.view.TextureView
import camp.visual.gazetracker.GazeTracker
import camp.visual.gazetracker.callback.CalibrationCallback
import camp.visual.gazetracker.callback.GazeCallback
import camp.visual.gazetracker.callback.GazeTrackerCallback
import camp.visual.gazetracker.callback.ImageCallback
import camp.visual.gazetracker.callback.InitializationCallback
import camp.visual.gazetracker.callback.StatusCallback
import camp.visual.gazetracker.callback.UserStatusCallback
import camp.visual.gazetracker.constant.AccuracyCriteria
import camp.visual.gazetracker.constant.CalibrationModeType
import camp.visual.gazetracker.constant.StatusErrorType
import camp.visual.gazetracker.constant.UserStatusOption
import visual.camp.sample.app.calibration.CalibrationDataStorage.loadCalibrationData
import visual.camp.sample.app.calibration.CalibrationDataStorage.saveCalibrationData
import java.lang.ref.WeakReference

class GazeTrackerManager private constructor(context: Context) {
    private val initializationCallbacks: MutableList<InitializationCallback> = ArrayList()
    private val gazeCallbacks: MutableList<GazeCallback> = ArrayList()
    private val calibrationCallbacks: MutableList<CalibrationCallback> = ArrayList()
    private val statusCallbacks: MutableList<StatusCallback> = ArrayList()
    private val imageCallbacks: MutableList<ImageCallback> = ArrayList()
    private val userStatusCallbacks: MutableList<UserStatusCallback> = ArrayList()

    private var cameraPreview: WeakReference<TextureView>? = null
    private val mContext =
        WeakReference(context)

    var gazeTracker: GazeTracker? = null

    // TODO: change licence key
    var SEESO_LICENSE_KEY: String = "dev_vuy52cjc3dq6il50izp1oc41j7e9kde3jj2ldzck"

    fun hasGazeTracker(): Boolean {
        return gazeTracker != null
    }

    fun initGazeTracker(callback: InitializationCallback, option: UserStatusOption?) {
        initializationCallbacks.add(callback)

        GazeTracker.initGazeTracker(
            mContext.get(),
            SEESO_LICENSE_KEY,
            initializationCallback,
            option
        )
    }

    fun deinitGazeTracker() {
        if (hasGazeTracker()) {
            GazeTracker.deinitGazeTracker(gazeTracker)
            gazeTracker = null
        }
    }

    fun setGazeTrackerCallbacks(vararg callbacks: GazeTrackerCallback?) {
        for (callback in callbacks) {
            if (callback is GazeCallback) {
                if (!gazeCallbacks.contains(callback)) {
                    gazeCallbacks.add(callback)
                }
            } else if (callback is CalibrationCallback) {
                if (!calibrationCallbacks.contains(callback)) {
                    calibrationCallbacks.add(callback)
                }
            } else if (callback is ImageCallback) {
                if (!imageCallbacks.contains(callback)) {
                    imageCallbacks.add(callback)
                }
            } else if (callback is StatusCallback) {
                if (!statusCallbacks.contains(callback)) {
                    statusCallbacks.add(callback)
                }
            } else if (callback is UserStatusCallback) {
                if (!userStatusCallbacks.contains(callback)) {
                    userStatusCallbacks.add(callback)
                }
            }
        }
    }

    fun removeCallbacks(vararg callbacks: GazeTrackerCallback?) {
        for (callback in callbacks) {
            gazeCallbacks.remove(callback)
            calibrationCallbacks.remove(callback)
            imageCallbacks.remove(callback)
            statusCallbacks.remove(callback)
        }
    }

    fun startGazeTracking(): Boolean {
        if (hasGazeTracker()) {
            gazeTracker!!.startTracking()
            return true
        }
        return false
    }

    fun stopGazeTracking(): Boolean {
        if (isTracking) {
            gazeTracker!!.stopTracking()
            return true
        }
        return false
    }

    fun startCalibration(modeType: CalibrationModeType?, criteria: AccuracyCriteria?): Boolean {
        if (hasGazeTracker()) {
            return gazeTracker!!.startCalibration(modeType, criteria)
        }
        return false
    }

    fun stopCalibration(): Boolean {
        if (isCalibrating) {
            gazeTracker!!.stopCalibration()
            return true
        }
        return false
    }

    fun startCollectingCalibrationSamples(): Boolean {
        if (isCalibrating) {
            return gazeTracker!!.startCollectSamples()
        }
        return false
    }

    val isTracking: Boolean
        get() {
            if (hasGazeTracker()) {
                return gazeTracker!!.isTracking
            }
            return false
        }

    val isCalibrating: Boolean
        get() {
            if (hasGazeTracker()) {
                return gazeTracker!!.isCalibrating
            }
            return false
        }

    enum class LoadCalibrationResult {
        SUCCESS,
        FAIL_DOING_CALIBRATION,
        FAIL_NO_CALIBRATION_DATA,
        FAIL_HAS_NO_TRACKER
    }

    fun loadCalibrationData(): LoadCalibrationResult {
        if (!hasGazeTracker()) {
            return LoadCalibrationResult.FAIL_HAS_NO_TRACKER
        }
        val calibrationData = loadCalibrationData(
            mContext.get()!!
        )
        return if (calibrationData != null) {
            if (!gazeTracker!!.setCalibrationData(calibrationData)) {
                LoadCalibrationResult.FAIL_DOING_CALIBRATION
            } else {
                LoadCalibrationResult.SUCCESS
            }
        } else {
            LoadCalibrationResult.FAIL_NO_CALIBRATION_DATA
        }
    }

    fun setCameraPreview(preview: TextureView) {
        this.cameraPreview = WeakReference(preview)
        if (hasGazeTracker()) {
            gazeTracker!!.setCameraPreview(preview)
        }
    }

    fun removeCameraPreview(preview: TextureView) {
        if (cameraPreview!!.get() === preview) {
            this.cameraPreview = null
            if (hasGazeTracker()) {
                gazeTracker!!.removeCameraPreview()
            }
        }
    }

    // GazeTracker Callbacks
    private val initializationCallback =
        InitializationCallback { gazeTracker, initializationErrorType ->
            updateGazeTracker(gazeTracker)
            for (initializationCallback in initializationCallbacks) {
                initializationCallback.onInitialized(gazeTracker, initializationErrorType)
            }
            initializationCallbacks.clear()
            if (gazeTracker != null) {
                gazeTracker.setCallbacks(
                    gazeCallback,
                    calibrationCallback,
                    imageCallback,
                    statusCallback,
                    userStatusCallback
                )
                if (cameraPreview != null) {
                    gazeTracker.setCameraPreview(cameraPreview!!.get())
                }
            }
        }

    private val gazeCallback =
        GazeCallback { gazeInfo ->
            for (gazeCallback in gazeCallbacks) {
                gazeCallback.onGaze(gazeInfo)
            }
        }

    private val userStatusCallback: UserStatusCallback = object : UserStatusCallback {
        override fun onAttention(timestampBegin: Long, timestampEnd: Long, attentionScore: Float) {
            for (userStatusCallback in userStatusCallbacks) {
                userStatusCallback.onAttention(timestampBegin, timestampEnd, attentionScore)
            }
        }

        override fun onBlink(
            timestamp: Long,
            isBlinkLeft: Boolean, isBlinkRight: Boolean, isBlink: Boolean,
            leftOpenness: Float, rightOpenness: Float
        ) {
            for (userStatusCallback in userStatusCallbacks) {
                userStatusCallback.onBlink(
                    timestamp,
                    isBlinkLeft,
                    isBlinkRight,
                    isBlink,
                    leftOpenness,
                    rightOpenness
                )
            }
        }

        override fun onDrowsiness(timestamp: Long, isDrowsiness: Boolean, intensity: Float) {
            for (userStatusCallback in userStatusCallbacks) {
                userStatusCallback.onDrowsiness(timestamp, isDrowsiness, intensity)
            }
        }
    }

    private val calibrationCallback: CalibrationCallback = object : CalibrationCallback {
        override fun onCalibrationProgress(v: Float) {
            for (calibrationCallback in calibrationCallbacks) {
                calibrationCallback.onCalibrationProgress(v)
            }
        }

        override fun onCalibrationNextPoint(v: Float, v1: Float) {
            for (calibrationCallback in calibrationCallbacks) {
                calibrationCallback.onCalibrationNextPoint(v, v1)
            }
        }

        override fun onCalibrationFinished(doubles: DoubleArray) {
            saveCalibrationData(mContext.get()!!, doubles)
            for (calibrationCallback in calibrationCallbacks) {
                calibrationCallback.onCalibrationFinished(doubles)
            }
        }
    }

    private val imageCallback =
        ImageCallback { l, bytes ->
            for (imageCallback in imageCallbacks) {
                imageCallback.onImage(l, bytes)
            }
        }

    private val statusCallback: StatusCallback = object : StatusCallback {
        override fun onStarted() {
            for (statusCallback in statusCallbacks) {
                statusCallback.onStarted()
            }
        }

        override fun onStopped(statusErrorType: StatusErrorType) {
            for (statusCallback in statusCallbacks) {
                statusCallback.onStopped(statusErrorType)
            }
        }
    }

    private fun updateGazeTracker(gazeTracker: GazeTracker) {
        this.gazeTracker = gazeTracker
    }

    companion object {
        @Volatile
        private var instance: GazeTrackerManager? = null

        fun getInstance(context: Context): GazeTrackerManager {
            return instance ?: synchronized(this) {
                instance ?: GazeTrackerManager(context.applicationContext).also { instance = it }
            }
        }
    }
}
