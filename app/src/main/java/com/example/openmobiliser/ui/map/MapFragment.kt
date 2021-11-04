package com.example.openmobiliser.ui.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.*
import androidx.lifecycle.ViewModelProvider
import com.example.openmobiliser.InfoActivity
import com.example.openmobiliser.R
import com.example.openmobiliser.SubmitActivity
import com.example.openmobiliser.databinding.FragmentMapBinding
import com.example.openmobiliser.models.Locations
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.CollectionReference
import org.osmdroid.views.MapView
import java.io.Serializable

class MapFragment : Fragment(), OnMapReadyCallback{

    private lateinit var galleryViewModel: MapViewModel
    private lateinit var map: MapView
    private var _binding: FragmentMapBinding? = null
    private lateinit var locations: CollectionReference

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
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val locations = Locations.get()

        val sg = LatLng(1.3521,103.8198)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sg,15.0f))

        locations.forEach {
            googleMap.addMarker(MarkerOptions()
                .position(
                    LatLng(it.lat,it.long),
                )
                .title(it.title)
            ).tag = it
        }

        googleMap.setOnMapLongClickListener { result ->
            val intent = Intent(activity, SubmitActivity::class.java)
                .putExtra("lat",result.latitude)
                .putExtra("long",result.longitude)
            startActivity(intent)
        }

        googleMap.setOnInfoWindowClickListener { marker ->
            val intent = Intent(activity, InfoActivity::class.java)
                .putExtra("marker", marker.tag as Serializable)
            startActivity(intent)
        }

    }

}