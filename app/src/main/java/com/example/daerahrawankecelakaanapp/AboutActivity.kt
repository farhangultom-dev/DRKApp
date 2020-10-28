package com.example.daerahrawankecelakaanapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        supportActionBar?.title = "Tentang Aplikasi"
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
}