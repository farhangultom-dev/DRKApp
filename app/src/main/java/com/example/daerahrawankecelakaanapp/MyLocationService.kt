package com.example.daerahrawankecelakaanapp

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.google.android.gms.location.LocationResult
import java.lang.StringBuilder

class MyLocationService : BroadcastReceiver() {
    companion object {
        val ACTION_PROCESS_UPDATE="com.example.daerahrawankecelakaanapp.UPDATE_LOCATION";
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null)
        {
            val action = intent!!.action
            if (action.equals(ACTION_PROCESS_UPDATE))
            {
                val result = LocationResult.extractResult(intent!!)
                if (result != null)
                {
                    val location = result.lastLocation
                    val location_string = StringBuilder(location.latitude.toString())
                        .append("/").append(location.longitude).toString()
                    try{
                        MapsActivity.getMainInstance().updateTextView(location_string)
                    }catch (e:Exception)
                    {
                        //if add in kill mode
                        Toast.makeText(context,location_string,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


}
