package com.example.decideforme

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.decideforme.databinding.ActivityResultBinding
import com.google.android.gms.location.LocationServices

class ResultActivity : AppCompatActivity() {
    var result: String = ""

    private lateinit var activityResultBinding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityResultBinding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(activityResultBinding.root)

        result = intent.getStringExtra("result")!!
        activityResultBinding.txtResult.text = result

        activityResultBinding.txtResult.setOnClickListener {
            if (checkLocationPermission()) {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                val location = fusedLocationClient.lastLocation
                location.addOnSuccessListener {
                    findOnMap(it, result)
                }

            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    0
                )
                if (checkLocationPermission()) {
                    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                    val location = fusedLocationClient.lastLocation
                    location.addOnSuccessListener {
                        findOnMap(it, result)
                    }
                }
            }
        }

        activityResultBinding.btnExit.setOnClickListener {
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("Permission", "${permissions[0]} was granted")
            if (checkLocationPermission()) {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                val location = fusedLocationClient.lastLocation
                location.addOnSuccessListener {
                    findOnMap(it, result)
                }
            }
        }
    }

    private fun checkLocationPermission(): Boolean {
        var resultBoolean = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        Log.d("checkLocationPermission", "Permission for location: $resultBoolean")
        return resultBoolean
    }

    private fun findOnMap(location: Location?, resultString: String?) {
        if (location != null) {
            var lat = location.latitude
            var long = location.longitude
            val uri = Uri.parse("geo:$lat,$long?q=" + Uri.encode(resultString))

            // Create a Uri from an intent string. Use the result to create an Intent.
            //val gmmIntentUri = Uri.parse("google.streetview:cbll=46.414382,10.013988")

            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
            // Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage("com.google.android.apps.maps")

            // Attempt to start an activity that can handle the Intent
            startActivity(mapIntent)
        }
    }
}
