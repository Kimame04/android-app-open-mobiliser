package com.example.openmobiliser.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.openmobiliser.R
import com.example.openmobiliser.databinding.FragmentMapBinding
import com.example.openmobiliser.ui.submission.SubmissionFragment
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapFragment : Fragment() {

    private lateinit var galleryViewModel: MapViewModel
    private lateinit var map: MapView
    private var _binding: FragmentMapBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProvider(this).get(MapViewModel::class.java)

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val fab: View = binding.fab
        fab.setOnClickListener { view ->
            childFragmentManager.beginTransaction().apply {
                replace(R.id.map_fragment_container,SubmissionFragment())
                commit()
            }
        }

        map = binding.map
        val mapController = map.controller
        mapController.setZoom(15.0)
        val startPoint = GeoPoint(1.3521, 103.8198)
        mapController.setCenter(startPoint)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}