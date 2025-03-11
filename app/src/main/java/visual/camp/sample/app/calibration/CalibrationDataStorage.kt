package visual.camp.sample.app.calibration

import android.content.Context
import android.util.Log

object CalibrationDataStorage {
    private val TAG: String = CalibrationDataStorage::class.java.simpleName
    private const val CALIBRATION_DATA = "calibrationData"

    // Store calibration data to SharedPreference
    @JvmStatic
    fun saveCalibrationData(context: Context, calibrationData: DoubleArray?) {
        if (calibrationData != null && calibrationData.size > 0) {
            val editor = context.getSharedPreferences(TAG, Context.MODE_PRIVATE).edit()
            editor.putString(CALIBRATION_DATA, calibrationData.contentToString())
            editor.apply()
        } else {
            Log.e(TAG, "Abnormal calibration Data")
        }
    }

    // Get calibration data from SharedPreference
    @JvmStatic
    fun loadCalibrationData(context: Context): DoubleArray? {
        val prefs = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
        val saveData = prefs.getString(CALIBRATION_DATA, null)

        if (saveData != null) {
            try {
                val split = saveData.substring(1, saveData.length - 1).split(", ".toRegex())
                    .dropLastWhile { it.isEmpty() }.toTypedArray()
                val array = DoubleArray(split.size)
                for (i in split.indices) {
                    array[i] = split[i].toDouble()
                }
                return array
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "Maybe unmatched type of calibration data")
            }
        }
        return null
    }
}
