package com.example.daerahrawankecelakaanapp.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.daerahrawankecelakaanapp.MapsActivity
import com.example.daerahrawankecelakaanapp.R


class BalamFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view : View = inflater!!.inflate(R.layout.fragment_balam, container, false)
        val btn: Button = view.findViewById(R.id.button2)
        val btn1: Button = view.findViewById(R.id.button1)
        btn.setOnClickListener(this)
        btn1.setOnClickListener(this)
        return view

    }

    companion object {
        fun newInstance(): BalamFragment {
            return BalamFragment()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button2 -> {
                val gmmIntentUri = Uri.parse("google.streetview:cbll=-5.459735,105.314745")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
            R.id.button1 -> {
                val gmmIntentUri = Uri.parse("google.streetview:cbll=-5.458517,105.314041")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }

            else -> {
            }
        }
    }

}