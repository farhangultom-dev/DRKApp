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

class LampuraFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view : View = inflater!!.inflate(R.layout.fragment_lampura, container, false)
        val btn1: Button = view.findViewById(R.id.button1)
        btn1.setOnClickListener(this)
        return view

    }

    companion object {
        fun newInstance(): LampuraFragment {
            return LampuraFragment()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button1 -> {
                val gmmIntentUri = Uri.parse("google.streetview:cbll=-4.858840,104.933727")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }

            else -> {
            }
        }
    }

}