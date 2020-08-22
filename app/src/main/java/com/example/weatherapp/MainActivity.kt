@file:Suppress("DEPRECATION")

package com.example.weatherapp

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weatherapp.Common.Common
import com.example.weatherapp.Common.Helper
import com.example.weatherapp.model.OpenWeatherMap
import com.example.weatherapp.model.Sys
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() ,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {
    var PERMISSOION_REQUIRED_CODE=1001
    var PLAY_SERVICE_RESOLUTION_REQUEST=1000
    var mGoogleApiClint:GoogleApiClient ?= null
    var mLocationRequest:LocationRequest?=null
    internal var openWeatherMap =OpenWeatherMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestpermission()
        if (checkPlayService())
            buildGoogleApiClient()
    }
    private fun  requestpermission() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),PERMISSOION_REQUIRED_CODE)

    }
    private fun checkPlayService(): Boolean {
        val resultCode=GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)
        if (resultCode!=ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {

                GooglePlayServicesUtil.getErrorDialog(resultCode,this,PLAY_SERVICE_RESOLUTION_REQUEST).show()
            }
            else {
                Toast.makeText(
                    applicationContext,
                    "This Device doesn't supported",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
            return false
        }
        return true
    }
    private fun buildGoogleApiClient() {

        mGoogleApiClint=GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener (this)
            .addApi(LocationServices.API).build()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            PERMISSOION_REQUIRED_CODE -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    if (checkPlayService()) {
                        buildGoogleApiClient()
                    }
                }
            }
        }






    }
    override fun onConnected(p0: Bundle?) {
        creatLocationRequest()
    }
    fun creatLocationRequest() {
        mLocationRequest= LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest!!.interval=10000
        mLocationRequest!!.fastestInterval=5000
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
        {
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClint,mLocationRequest,this)
    }
    override fun onConnectionSuspended(p0: Int) {
        mGoogleApiClint!!.connect()

    }
    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.i("Error","Connection Failed"+p0.errorCode)

    }
    override fun onLocationChanged(p0: Location) {
        GetWeather().execute(Common.apiRequest(p0.latitude.toString(),p0.longitude.toString()))
    }
    override fun onStart() {
        super.onStart()
        if (mGoogleApiClint != null)
            mGoogleApiClint!!.connect()
    }
    override fun onDestroy() {
        super.onDestroy()
            mGoogleApiClint!!.disconnect()
    }
    override fun onResume() {
        super.onResume()
        checkPlayService()
    }
    private inner class GetWeather : AsyncTask<String, Void, String>()
    {
        internal val pd =ProgressDialog(this@MainActivity)
        override fun onPreExecute() {
            super.onPreExecute()
            pd.setTitle("please wait ... ")
            pd.show()

        }
        override fun doInBackground(vararg params: String?): String {
var stream :String?= null
        var urlString = params[0]
        val http= Helper()
        stream=http.getHTTPData(urlString)
        return stream
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            if (result.contains("Error : Not found city"))
            {
                pd.dismiss()
                return
            }
            val gson=Gson()
            val mType = object :TypeToken<OpenWeatherMap>(){}.type
            openWeatherMap=gson.fromJson<OpenWeatherMap>(result,mType)
            pd.dismiss()
            txtCity.text="${openWeatherMap.name},${openWeatherMap.sys!!.country}"
            txtLastUpdate.text ="Last Update : ${Common.dateNow}"
            txtdiscription.text="${openWeatherMap.weather!![0].discription}"
            txtTime.text="${Common.unixTimeStampToDateTime(openWeatherMap.sys!!.sunrise)}/${Common.unixTimeStampToDateTime(openWeatherMap.sys!!.sunset)}"
            txtHumedity.text="${openWeatherMap.main!!.humidity}"
            txtCelesuis.text="${openWeatherMap.main!!.temp}"
            Picasso.get()
                .load(Common.getImage(openWeatherMap.weather!![0].icon!!))
                .into(imgviewId)




        }


    }
}