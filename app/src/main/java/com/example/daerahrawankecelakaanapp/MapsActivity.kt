package com.example.daerahrawankecelakaanapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQuery
import com.firebase.geofire.GeoQueryEventListener
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.util.jar.Manifest
import kotlin.random.Random

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, IOnLoadLocationListener,
    GeoQueryEventListener {

    override fun onStop() {
        fusedLocationProviderClient!!.removeLocationUpdates(locationCallback!!)
        super.onStop()
    }

    override fun onGeoQueryReady() {

    }

    override fun onKeyEntered(key: String?, location: GeoLocation?) {
        sendNotification("DRApp", String.format("%s Memasuki Daerah Rawan Kecelakaan",key))
    }

    private fun sendNotification(title: String, content: String) {
        Toast.makeText(this,""+content,Toast.LENGTH_SHORT).show()

        val NOTIFICATION_CHANNEL_ID = "drapp_mutiple_location"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID,
                "MyNotification",NotificationManager.IMPORTANCE_DEFAULT)
            //config
            notificationChannel.description = "Channel Description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.vibrationPattern = longArrayOf(0,1000,500,1000)
            notificationChannel.enableVibration(true)

            notificationManager.createNotificationChannel(notificationChannel)
        }

            val builder = NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
            builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher))
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)

            val notification = builder.build()
            notificationManager.notify(java.util.Random().nextInt(),notification)


    }

    override fun onKeyMoved(key: String?, location: GeoLocation?) {
        sendNotification("DRApp", String.format("%s Sedang Bergerak Di Daerah Rawan Kecelekaan",key))
    }

    override fun onKeyExited(key: String?) {
        sendNotification("DRApp", String.format("%s Meninggalkan Daerah Rawan Kecelekaan",key))
    }

    override fun onGeoQueryError(error: DatabaseError?) {
        Toast.makeText(this,""+error!!.message,Toast.LENGTH_SHORT).show()
    }
    override fun onLocationLoadSuccess(LatLngs: List<MyLatLng>) {

        dangerousArea = ArrayList()
        for (myLatLng in LatLngs)
        {
            val convert = LatLng(myLatLng.latitude,myLatLng.longitude)
            dangerousArea!!.add(convert)
        }
        //now, after dangerous area is have data, we will call map display
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //clear map and data add data again
        if (mMap != null)
        {
            mMap!!.clear()
            //add again user marker
            addUserMarker()
            //add circle dangerous area
            addCircleArea()
        }

    }

    private fun addCircleArea() {
        if (geoQuery != null)
        {
            //remove old listener, image if you remove an location in firebase
            //it must be remove listener in geofire too
            geoQuery!!.removeGeoQueryEventListener(this@MapsActivity)
            geoQuery!!.removeAllListeners()
        }
        //add again
        for (latLng in dangerousArea!!)
        {
            mMap!!.addCircle(CircleOptions().center(latLng)
                .radius(500.0) //500 m
                .strokeColor(Color.BLUE)
                .fillColor(0x220000FF)
                .strokeWidth(5.0f)
            )
            //Create GeoQuery when user in dangerous location
            geoQuery = geoFire!!.queryAtLocation(GeoLocation(latLng.latitude,latLng.longitude),0.5) //0.5 = 500 m
            geoQuery!!.addGeoQueryEventListener(this@MapsActivity)
        }
    }

    override fun onLoadLocationLoadFailed(message: String) {
        Toast.makeText(this,""+message,Toast.LENGTH_SHORT).show()
    }

    private var mMap: GoogleMap?= null
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentMarker: Marker? = null
    private lateinit var myLocationRef: DatabaseReference
    private lateinit var dangerousArea:MutableList<LatLng>
    private lateinit var listener:IOnLoadLocationListener

    private lateinit var myCity : DatabaseReference
    private lateinit var lastLocation:Location
    private var geoQuery: GeoQuery?=null
    private lateinit var geoFire: GeoFire

    companion object{
        var instance:MapsActivity?=null


        fun getMainInstance():MapsActivity{
            return instance!!
        }
    }

    fun updateTextView(value:String)
    {
        this@MapsActivity.runOnUiThread(){
            //txt_location.text = value
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        instance = this

        //Request Runtime
        Dexter.withActivity(this)
            .withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    buildLocationRequest()
                    buildLocationCallback()
                    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@MapsActivity)
                    initArea()
                    settingGeoFire()
                    updateLocation()

                    //add dangerous to firebase
                    //addDangerousToFirebase()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {

                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(this@MapsActivity,"Kamu Harus Menyetujui Izin Tersebut",Toast.LENGTH_SHORT).show()
                }

            }).check()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        val inflater = menuInflater
        inflater.inflate(R.menu.optionmenu, menu)
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.infodaerah) {
            startActivity(Intent(this, InfoDaerahActivity::class.java))

        }
        if (item.itemId == R.id.callcenter){
            startActivity(Intent(this, CallCenterActivity::class.java))
        }
        if (item.itemId == R.id.About){
            startActivity(Intent(this, AboutActivity::class.java))
        }
        if (item.itemId == R.id.Maps){
            startActivity(Intent(this, MapsActivity::class.java))
        }
        return true
    }

    private fun updateLocation() {
        buildLocationRequest()
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED )
            return
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,getPendingIntent())
    }

    private fun getPendingIntent(): PendingIntent? {
        val intent = Intent(this@MapsActivity,MyLocationService::class.java)
        intent.setAction(MyLocationService.ACTION_PROCESS_UPDATE)
        return PendingIntent.getBroadcast(this@MapsActivity,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun addDangerousToFirebase() {
        dangerousArea = ArrayList()
        dangerousArea.add(LatLng(5.459735,105.314745))
        dangerousArea.add(LatLng(5.458517,105.314041))
        dangerousArea.add(LatLng(-5.320774,105.199874))
        dangerousArea.add(LatLng(-5.561501,105.377603))
        dangerousArea.add(LatLng(-5.799987,105.718910))
        dangerousArea.add(LatLng(-5.680991,105.580196))
        dangerousArea.add(LatLng(-5.633601,105.530494))
        dangerousArea.add(LatLng(-4.659043,105.191332))
        dangerousArea.add(LatLng(-4.757929,105.195452))
        dangerousArea.add(LatLng(-4.858840,104.933727))
        dangerousArea.add(LatLng(-5.104837,105.655076))
        dangerousArea.add(LatLng(-5.099784,105.523249))
        dangerousArea.add(LatLng(-5.054836,105.406924))
        dangerousArea.add(LatLng(-5.027799,104.446607))
        dangerousArea.add(LatLng(-5.030248,104.442015))
        dangerousArea.add(LatLng(-5.374460,105.041515))

        //submit this list to firebase
        FirebaseDatabase.getInstance()
            .getReference("DangerousArea")
            .child("MyCity")
            .setValue(dangerousArea)
            .addOnCompleteListener { Toast.makeText(this@MapsActivity,"Update",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{ ex -> Toast.makeText(this@MapsActivity,""+ex.message,Toast.LENGTH_SHORT).show()}
    }

    private fun settingGeoFire() {
        myLocationRef = FirebaseDatabase.getInstance().getReference("MyLocation")
        geoFire = GeoFire(myLocationRef)
    }

    private fun initArea() {
        myCity = FirebaseDatabase.getInstance()
            .getReference("DangerousArea")
            .child("MyCity")

        listener = this

        //Add Realtime Change Update
        myCity!!.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapShot: DataSnapshot) {
                //update dangerous area
                val latLngList = ArrayList<MyLatLng>()
                for (locationSnapShot in dataSnapShot.children)
                {
                    val latLng = locationSnapShot.getValue(MyLatLng::class.java)
                    latLngList.add(latLng!!)
                }
                listener!!.onLocationLoadSuccess(latLngList)
            }

        })
    }

    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                if (mMap != null)
                {
                    lastLocation = locationResult!!.lastLocation
                    addUserMarker()
                }
            }
        }
    }

    private fun addUserMarker() {
        geoFire!!.setLocation("Kamu", GeoLocation(lastLocation!!.latitude,
            lastLocation!!.longitude)) {_,_ ->
            if (currentMarker != null ) currentMarker!!.remove()
            currentMarker = mMap!!.addMarker(MarkerOptions().position(LatLng(lastLocation!!.latitude,
                lastLocation!!.longitude))
                .title("Kamu"))
            //After add marker , move camera
            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(currentMarker!!.position,12.0f))
        }
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest!!.interval = 5000
        locationRequest!!.fastestInterval = 3000
        locationRequest!!.smallestDisplacement = 10f
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap!!.uiSettings.isZoomControlsEnabled = true

        if (fusedLocationProviderClient != null)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    return
            }
            fusedLocationProviderClient!!.requestLocationUpdates(locationRequest,locationCallback!!, Looper.myLooper())

            addCircleArea()
        }
    }
}