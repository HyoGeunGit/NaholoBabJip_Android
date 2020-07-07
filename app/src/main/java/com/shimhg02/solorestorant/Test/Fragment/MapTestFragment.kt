package com.shimhg02.solorestorant.Test.Fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.shimhg02.solorestorant.R
import com.shimhg02.solorestorant.Test.Activity.TestInfoActivity
import com.shimhg02.solorestorant.utils.GpsUtil.GpsTracker
import com.shimhg02.solorestorant.network.Data.LocationData.LocationRepo
import com.shimhg02.solorestorant.network.Retrofit.Client
import kotlinx.android.synthetic.main.activity_locationtest.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @notice Test 그룹
 * @description 맵 프레그먼트
 */

class MapTestFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    private lateinit var lastLocation: Location
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_locationtest, container, false)
        getPlaceData()
        val mapFragment: SupportMapFragment? = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    lastLocation = p0.lastLocation
                    placeMarkerOnMap(LatLng(lastLocation.latitude, lastLocation.longitude))
                }
            }
            createLocationRequest()

            return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        mMap.setOnMapClickListener(this)
        setUpMap()
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        val str = p0?.snippet
        var resultAdress = str?.split("end")
        if(!p0!!.isInfoWindowShown){
            cardview_googlemap.visibility = View.VISIBLE
            if (resultAdress?.get(0)==null|| resultAdress[0] =="") {
                Glide.with(this.context).load("https://lh3.googleusercontent.com/proxy/-l5EXoipH0EzWLvWzU6FYMmpE-_0YEkrIk1DedV3ArTnKTsdabs8pteGV4QfVaJNhm3ZIrxJuxpKTH47uuZ3YdsoH8E215eb-s60vMxIkD6A8XPEGPceinsqObWCqpvnz6l3Zj1aU5hT7z6Ny0xo").into(image_hello)

            }
            else{
                Glide.with(this.context).load(resultAdress.get(0)).into(image_hello)
            }
            name_tv.text = p0.title
            subtitle_tv.text = resultAdress!!.get(1)
            test_secure.text = resultAdress[2]
            Toast.makeText(context,test_secure.text,Toast.LENGTH_SHORT).show()
            if(name_tv.text == null|| name_tv.text == "" || subtitle_tv.text == null){
                cardview_googlemap.visibility = View.GONE
            }
            cardview_googlemap.setOnClickListener {
                val intent = Intent(context, TestInfoActivity::class.java)
                intent.putExtra("chekOn", test_secure.text.toString())
                startActivity(intent)
            }
        }
        return true
    }
    override fun onMapClick(p0: LatLng?) {
        cardview_googlemap.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }

    }


    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
    }

    private fun setUpMap() {

        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener(activity!!) { location ->
                if (location != null) {
                    lastLocation = location
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    placeMarkerOnMap(currentLatLng)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                }
            }
        }
    }

    private fun placeMarkerOnMap(location: LatLng) {
//        val markerOptions = MarkerOptions().position(location)
//        val titleStr = getAddress(location)
//        markerOptions.title(titleStr)
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
//        mMap.addMarker(markerOptions)
    }



    private fun startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(activity!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(activity!!)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            // 6
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(activity,
                        REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }
    }

//     fun getAddress(latLng: LatLng): String {
//        val geocoder = Geocoder(activity)
//        val addresses: List<Address>?
//        val address: Address?
//        var addressText = ""
//        try {
//            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
//            if (null != addresses && !addresses.isEmpty()) {
//                address = addresses[0]
//                for (i in 0 until address.maxAddressLineIndex) {
//                    addressText += if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(i)
//                }
//            }
//        } catch (e: IOException) {
//            Log.e("MapsActivity", e.localizedMessage)
//        }
//
//        return addressText
//    }

    fun getPlaceData(){

        var gpsTracker =
            GpsTracker(this.context)
        val latitude: Double = gpsTracker.getLatitude()
        val longitude: Double = gpsTracker.getLongitude()
        Client.retrofitService.getPlace(latitude.toString(),longitude.toString(),1000).enqueue(object : Callback<ArrayList<LocationRepo>> {
            override fun onResponse(call: Call<ArrayList<LocationRepo>>?, response: Response<ArrayList<LocationRepo>>?) {
                val repo = response!!.body()
                when (response.code()) {
                    200 -> {
                        repo!!.indices.forEach {
                            val marker = LatLng(repo[it].lat.toDouble(), repo[it].lng.toDouble())
                            mMap.addMarker(MarkerOptions().position(marker).title(repo[it].placeName).snippet(repo[it].placePhoto + "end" + repo[it].vicinity + "end" + repo[it].placeId))
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(marker))
                            setUpMap()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<LocationRepo>>?, t: Throwable?) {
                Log.v("FngTest", "fail!!")
            }
        })

    }


}