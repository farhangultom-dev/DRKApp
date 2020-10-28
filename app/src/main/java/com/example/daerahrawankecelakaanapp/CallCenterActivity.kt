package com.example.daerahrawankecelakaanapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class CallCenterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_center)

        supportActionBar?.title = "Pusat Panggilan"
        supportActionBar?.elevation = 0.0f
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.optionmenu, menu)
        return super.onCreateOptionsMenu(menu)
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

    fun btn_dakar(view: View) {
        val nomor = "113"
        val panggil = Intent(Intent.ACTION_DIAL)
        panggil.data = Uri.fromParts("tel", nomor, null)
        startActivity(panggil)
    }
    fun btn_ambulance(view: View) {
        val nomor = "118"
        val panggil = Intent(Intent.ACTION_DIAL)
        panggil.data = Uri.fromParts("tel", nomor, null)
        startActivity(panggil)
    }
    fun btn_polisi(view: View) {
        val nomor = "110"
        val panggil = Intent(Intent.ACTION_DIAL)
        panggil.data = Uri.fromParts("tel", nomor, null)
        startActivity(panggil)
    }
}