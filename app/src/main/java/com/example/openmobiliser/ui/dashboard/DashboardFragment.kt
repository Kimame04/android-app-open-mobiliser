package com.example.openmobiliser.ui.dashboard

import android.content.Context.LOCATION_SERVICE
import android.content.Context.NETWORK_STATS_SERVICE
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getMainExecutor
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.openmobiliser.databinding.FragmentDashboardBinding
import com.example.openmobiliser.models.Location
import com.example.openmobiliser.models.Locations
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList
import java.util.jar.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.transition.Visibility
import com.example.openmobiliser.InfoActivity
import com.example.openmobiliser.SubmitActivity


class DashboardFragment : Fragment() {

    private lateinit var homeViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null
    private var count = 0
    private var currLat = 0.0
    private var currLon = 0.0
    private val CODE = 101

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val chipGroup = binding.dashChipgrp
        val num = binding.dashNum
        val progressBar = binding.dashBar
        val nearest = binding.dashNearest
        val btn = binding.dashBtn

        progressBar.setProgress(0,true)
        num.setText("0")


        var loc = android.location.Location("")

        if (ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            val locationManager = context?.getSystemService(LOCATION_SERVICE) as LocationManager
            loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)!!
        } else{
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), CODE)
            loc.longitude = 0.0
            loc.latitude = 0.0
        }

        Locations.getCategories().forEach {
            val chip = Chip(context)
            chip.setText(it)
            chipGroup.addView(chip)
            chip.isCheckable = true

            chip.setOnClickListener {
                val list: ArrayList<String> = ArrayList()
                chipGroup.children.toList().filter{ (it as Chip).isChecked}.forEach {
                    list.add((it as Chip).text as String)
                }
                val locs = Locations.get().filter{ it.tags.intersect(list).isNotEmpty() }
                if (locs.size > 0) {
                    var min = locs.get(0)
                    locs.forEach {
                        if (calcDistance(it, loc!!) < calcDistance(min, loc)) min = it
                    }
                    nearest.setText(min.title + "\n" + min.description)
                    btn.visibility = View.VISIBLE
                    btn.setText("Visit")
                    btn.setOnClickListener{
                        val intent = Intent(activity, InfoActivity::class.java)
                            .putExtra("marker",min)
                        startActivity(intent)
                    }
                } else{
                    nearest.setText("")
                    btn.visibility = View.GONE
                }
                num.setText(locs.size.toString())
                progressBar.setProgress(locs.size * 10, true)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun calcDistance(loc1: Location, loc2: android.location.Location): Double{
        val lat1 = loc1.lat
        val lon1 = loc1.long
        val lat2 = loc2.latitude
        val lon2 = loc2.longitude
        val R = 6371 // km
        val x: Double = (lon2 - lon1) * Math.cos((lat1 + lat2) / 2)
        val y: Double = lat2 - lat1
        return Math.sqrt(x * x + y * y) * R
    }

    private val locationListener: LocationListener = object : LocationListener {

        override fun onLocationChanged(p0: android.location.Location) {
            currLat = p0.latitude
            currLon = p0.longitude
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

}