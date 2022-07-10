package com.dotrothschild.mysamplesensorswithlivedata.ui.main

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dotrothschild.mysamplesensorswithlivedata.R

// viewmodel only serves as a host for livedata
//class MainViewModel(application: Application) : AndroidViewModel(application) {
class MainViewModel(application: Application) : AndroidViewModel(application) {
    val azimuthLiveData = AzimuthLiveData()

    // inner class just to have access to application
    inner class AzimuthLiveData( ) : LiveData<List<Double>>() //: LiveData<String>()
        , SensorEventListener {
        private val VALUE_DRIFT = 0.05f
        //hold copies of the accelerometer and magnetometer data
        private var mAccelerometerData = FloatArray(3)
        private var mMagnetometerData = FloatArray(3)
        val rotationMatrix = FloatArray(9)  // could this be in onSensorChanged() ??
        val orientationValues = FloatArray(3)  // could this be in onSensorChanged() ??
        var outputPitch: Int = 0
        var outputAzimiuth: Int = 0
        private val sensorManager
            get() = getApplication<Application>().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) { }

        override fun onSensorChanged(sensorEvent: SensorEvent) {
            when (sensorEvent.sensor.type) {
                //Cloning those values prevents the data you're currently interested in from being changed by more recent data before you're done with it.
                Sensor.TYPE_ACCELEROMETER -> mAccelerometerData = sensorEvent.values.clone()
                Sensor.TYPE_MAGNETIC_FIELD -> mMagnetometerData = sensorEvent.values.clone()
                else -> return
            }
            val rotationOK = SensorManager.getRotationMatrix(
                rotationMatrix,
                null, mAccelerometerData, mMagnetometerData
            )
            if (rotationOK) {
                /*
                How far the device is oriented or tilted with respect to the Earth's coordinate system. There are three components to orientation:

                Azimuth: The direction (north/south/east/west) the device is pointing. 0 is magnetic north.
                Pitch: The top-to-bottom tilt of the device. 0 is flat.
                Roll: The left-to-right tilt of the device. 0 is flat.
                 */
                // All three angles are measured in radians, and range from -π (-3.141) to π.
                SensorManager.getOrientation(rotationMatrix, orientationValues)
                var azimuth = orientationValues[0] * (180/Math.PI)
                if (azimuth < 0) {azimuth += 360}
                var pitch = orientationValues[1] * (180/Math.PI) // positive points down, higher - is bigger angle
                var roll = orientationValues[2] * (180/Math.PI) // negative is left doww
                if (Math.abs(pitch) < VALUE_DRIFT) {
                    pitch = 0.0
                }
                if (Math.abs(roll) < VALUE_DRIFT) {
                    roll = 0.0
                }
                //string postValue("Azimuth as degrees: $azimuth \n pitch as degrees: $pitch \n roll as degrees: $roll") // sensor name: ${event.sensor.name} Incline array: $inclinationArray[0]") (pitch -45 degrees means tilt up 45 degrees
                postValue(listOf(azimuth, pitch, roll))
            }
        }

        override fun onActive() {
            Log.d("***** Entering onActive", this.toString())
            sensorManager.let { sm ->
                sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).let {
                    sm.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)

                    if (it != null) {
                        Log.d("***** Register sensor: ", it.name)
                    }
                }
            }
            sensorManager.let { sm ->
                sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD).let {
                    sm.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)

                    if (it != null) {
                        Log.d("***** Register sensor: ", it.name)
                    }
                }

            }
        }
        override fun onInactive() {
            sensorManager.unregisterListener(this)
        }

    }


}
/*
public static float[] getOrientation(float[] R, float[] values) {
column 0: vector pointing to phone's right, azimuth
column 1: vector pointing to phone's up, pitch
column 2: vector pointing to phone's front, yaw
     *   /  R[ 0]   R[ 1]   R[ 2]  \
     *   |  R[ 3]   R[ 4]   R[ 5]  |
     *   \  R[ 6]   R[ 7]   R[ 8]  /

    values[0] = (float) Math.atan2(R[1], R[4]);

 */