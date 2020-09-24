package com.decideforme

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
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    var result: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        result = intent.getStringExtra("result")
        txtResult.text = result

        txtResult.setOnClickListener {
            if(checkLocationPermission()) {
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                findOnMap(location, result)
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
                if(checkLocationPermission())
                {
                    val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    findOnMap(location, result)
                }
            }
        }

        btnExit.setOnClickListener {
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 0 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.d("Permission", "${permissions[0]} was granted")
            var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if(checkLocationPermission())
            {
                var location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                findOnMap(location, result)
            }
        }
    }

    private fun checkLocationPermission(): Boolean {
        var resultBoolean = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        Log.d("checkLocationPermission", "Permission for location: $resultBoolean")
        return resultBoolean
    }

    private fun findOnMap(location: Location?, resultString: String?){
        if(location != null){
            var lat = location.latitude
            var long = location.longitude
            val uri = Uri.parse("geo:$lat,$long?q="+Uri.encode(resultString))


            var googleIntent = Intent(Intent.ACTION_VIEW, uri)
                googleIntent.setPackage("com.google.android.apps.maps")
                googleIntent.resolveActivity(packageManager)?.let { _ ->
                    startActivity(googleIntent)
                }
            }
        }
    }
