package com.example.flashlighv1

import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String
    private lateinit var switch: ToggleButton
    private lateinit var swithImage: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        switch = findViewById(R.id.button)

        swithImage = findViewById(R.id.image)
        setListeners()
        initiateFlash()
    }

    private fun setListeners(){
        switch.setOnCheckedChangeListener { _, isChecked -> turnOnOff(isChecked) }
    }

    private fun initiateFlash() {
        val isFlashAvailable = applicationContext.packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        if(!isFlashAvailable){
            showError()
        }



        try {
            cameraId = cameraManager.cameraIdList.first()
        }catch (e: CameraAccessException){
            e.printStackTrace()
        }
    }

    private fun showError(){
        var alert = AlertDialog.Builder(this).create()
        alert.setTitle("Error")
        alert.setMessage("This device does not have flash...")
        alert.setButton(DialogInterface.BUTTON_POSITIVE,"Understood") {_, _-> finish() }
        alert.show()
    }

    private fun turnOnOff(status: Boolean){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cameraManager.setTorchMode(cameraId, status)
        }

        setUi(status)
    }

    private fun setUi(status: Boolean) {
        when(status){
            true -> {
                swithImage.setImageResource(R.drawable.ic_knife_switch_on)

            }
            false -> {
                swithImage.setImageResource(R.drawable.ic_knife_switch_off)

            }
        }
    }


}