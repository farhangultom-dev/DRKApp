package com.example.daerahrawankecelakaanapp.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.daerahrawankecelakaanapp.R

class LamselFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater!!.inflate(R.layout.fragment_lamsel, container, false)
        val btn1: Button = view.findViewById(R.id.button1)
        val btn2: Button = view.findViewById(R.id.button2)
        val btn3: Button = view.findViewById(R.id.button3)
        val btn4: Button = view.findViewById(R.id.button4)
        val btn5: Button = view.findViewById(R.id.button5)
        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
        btn3.setOnClickListener(this)
        btn4.setOnClickListener(this)
        btn5.setOnClickListener(this)
        return view
    }

    companion object {
        fun newInstance(): LamselFragment {
            return LamselFragment()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button1 -> {
                val gmmIntentUri = Uri.parse("google.streetview:cbll=-5.459735,105.314745")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
            R.id.button2 -> {
                val gmmIntentUri = Uri.parse("google.streetview:cbll= -5.561501,105.377603")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
            R.id.button3 -> {
                val gmmIntentUri = Uri.parse("google.streetview:cbll= -5.799987,105.718910")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
            R.id.button4 -> {
                val gmmIntentUri = Uri.parse("google.streetview:cbll=-5.680991,105.580196")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
            R.id.button5 -> {
                val gmmIntentUri = Uri.parse("google.streetview:cbll=-5.633601,105.530494")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }

            else -> {
            }
        }
    }
}