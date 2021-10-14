package com.example.openmobiliser.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.openmobiliser.databinding.FragmentDashboardBinding
import com.example.openmobiliser.models.Location
import com.example.openmobiliser.models.Locations
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class DashboardFragment : Fragment() {

    private lateinit var homeViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null
    private var count = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val chipGroup = binding.dashChipgrp
        val num = binding.dashNum
        val progressBar = binding.dashBar

        /*
        val text: TextView = binding.displayText

        val list = Locations.get()
        for (item in list){
            val title = item.title
            text.append(title as CharSequence?)
            text.append("\n")
        }
         */

        progressBar.setProgress(0,true)

        num.setText("0")

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
                /*
                locs.minOfOrNull {
                    calcDistance(it, )
                }

                 */
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

    fun calcDistance(loc1: Location, loc2: Location): Double{
        val lat1 = loc1.lat
        val lon1 = loc1.long
        val lat2 = loc2.lat
        val lon2 = loc2.long
        val R = 6371 // km
        val x: Double = (lon2 - lon1) * Math.cos((lat1 + lat2) / 2)
        val y: Double = lat2 - lat1
        return Math.sqrt(x * x + y * y) * R
    }

}