package com.example.daerahrawankecelakaanapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.daerahrawankecelakaanapp.fragment.*
import kotlinx.android.synthetic.main.activity_info_daerah.*

class InfoDaerahActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_daerah)

        // kita ganti title dan kita hilangkan shadownya
        supportActionBar?.title = "Informasi Daerah Rawan"
        supportActionBar?.elevation = 0.0f

        val adapter = TabAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        viewPager.adapter = adapter

        tabLayout.setupWithViewPager(viewPager)
    }

    class TabAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior) {
        private val tabName : Array<String> = arrayOf("Bandar Lampung", "Lampung Barat", "Lampung Utara", "Lampung Selatan", "Lampung Tengah","Lampung Timur", "Pringsewu")

        override fun getItem(position: Int): Fragment = when (position) {
            0 -> BalamFragment()
            1 -> LambarFragment()
            2 -> LampuraFragment()
            3 -> LamselFragment()
            4 -> LamtengFragment()
            5 -> LamtimFragment()
            6 -> TanggamusFragment()
            else -> BalamFragment()
        }

        override fun getCount(): Int = 7
        override fun getPageTitle(position: Int): CharSequence? = tabName.get(position)
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