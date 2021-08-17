package com.example.openmobiliser.ui.map

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.openmobiliser.R
import com.example.openmobiliser.databinding.FragmentMapBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.config.Configuration.*
import org.osmdroid.util.GeoPoint
import java.util.prefs.Preferences

class MapFragment : Fragment() {

    private lateinit var galleryViewModel: MapViewModel
    private lateinit var map: MapView
    private var _binding: FragmentMapBinding? = null
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1;

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

        val textView: TextView = binding.textGallery
        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val fab: View = binding.fab
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
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