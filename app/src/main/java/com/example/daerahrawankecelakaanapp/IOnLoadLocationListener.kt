package com.example.daerahrawankecelakaanapp

interface IOnLoadLocationListener {
    fun onLocationLoadSuccess(LatLngs:List<MyLatLng>)
    fun onLoadLocationLoadFailed(message:String)
}