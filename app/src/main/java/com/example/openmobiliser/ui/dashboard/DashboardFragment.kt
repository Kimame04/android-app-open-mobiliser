package com.example.openmobiliser.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.openmobiliser.databinding.FragmentDashboardBinding
import com.example.openmobiliser.models.Locations
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DashboardFragment : Fragment() {

    private lateinit var homeViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

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
        val text: TextView = binding.displayText

        val list = Locations.get()
        for (item in list){
            val title = item.title
            text.append(title as CharSequence?)
            text.append("\n")
        }
        return root
    }

    override fun onResume() {
        super.onResume()
        Locations.retrieveLocations()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}