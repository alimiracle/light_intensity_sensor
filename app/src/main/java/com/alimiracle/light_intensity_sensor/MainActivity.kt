package com.alimiracle.light_intensity_sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener, OnSeekBarChangeListener {
    private var status = 1
    private var lit = 0.0
    private var a = 0.0
    private var b = 1.0

    private lateinit var mSensorManager: SensorManager
    private var mSensors: Sensor? = null

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(p0: SensorEvent?) {
        //Sensor change value
        val millibarsOfPressure = p0!!.values[0]
        if (status == 0) {
            Toast.makeText(this, "There's light.", Toast.LENGTH_SHORT).show()
            status = 1
        }

        if (p0.sensor.type == Sensor.TYPE_LIGHT) {

            lit = lit * a + millibarsOfPressure * b
            if (lit == 0.0) {
                Toast.makeText(this, "There's no light, its so dark.", Toast.LENGTH_SHORT).show()
                status = 0
            }


            result_text.text = "%.2f".format(lit) + " lx"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //Define the sensor
        mSensors = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        seekbar.max = 10
//        seekbar.min = 0


// = (MAX - MIN) / STEP
        seekbar.setOnSeekBarChangeListener(this)


        // set on-click listener
        btn_click_me.setOnClickListener {
            hide()
            // your code to perform when the user clicks on the button
            if (btn_click_me.text.toString() == "Start") {

                mSensorManager.registerListener(this, mSensors, SensorManager.SENSOR_DELAY_NORMAL)
                btn_click_me.text = "Stop"

            } else {
                mSensorManager.unregisterListener(this)
                btn_click_me.text = "Start"


            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
// called when progress is changed
        val progress_custom = 1.0 - progress.toDouble() * 0.1


        progress_text.text = "%.1f".format(progress_custom)

    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
// called when tracking the seekbar is started
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
// called when tracking the seekbar is stoped
        val progress_custom = 1.0 - seekbar.progress.toDouble() * 0.1
        b = progress_custom
        a = 1.0 - progress_custom

    }


    override fun onPause() {
        super.onPause()
        //unregister the sensor onPause else it will be active even if the activity is closed
        mSensorManager.unregisterListener(this)

    }


    private fun hide() {
        //Toggle
        if (result_text.visibility == View.VISIBLE) {
            result_text.visibility = View.INVISIBLE
            seekbar.visibility = View.INVISIBLE
            progress_text.visibility = View.INVISIBLE
            result_holder.visibility = View.INVISIBLE
            filter_holder.visibility = View.INVISIBLE
        } else {
            result_text.visibility = View.VISIBLE
            seekbar.visibility = View.VISIBLE
            progress_text.visibility = View.VISIBLE
            result_holder.visibility = View.VISIBLE
            filter_holder.visibility = View.VISIBLE

        }
    }

}
