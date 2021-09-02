package com.example.openmobiliser.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.openmobiliser.R
import com.example.openmobiliser.databinding.FragmentMapBinding
import com.example.openmobiliser.models.Location
import com.example.openmobiliser.ui.submission.SubmissionFragment
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapFragment : Fragment() {

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

        val fab: View = binding.fab
        fab.setOnClickListener { view ->

            /*childFragmentManager.beginTransaction().apply {

                replace(R.id.map_fragment_container,SubmissionFragment())
                commit()
            }*/
        }

        map = binding.map
        val mapController = map.controller
        mapController.setZoom(15.0)
        val startPoint = GeoPoint(1.3521, 103.8198)
        mapController.setCenter(startPoint)

        val locations = Firebase.firestore.collection("locations")

        val text = binding.displayText

        val title = binding.testTitle
        val description = binding.testDesc
        val lat = binding.testLat
        val long = binding.testLong

        val btn = binding.testBtn
        btn.setOnClickListener {
            val loc = Location(title.text.toString(), description.text.toString(),lat.text.toString().toDouble(),long.text.toString().toDouble())
            locations.document().set(loc)
            Toast.makeText(this.context,"note saved",Toast.LENGTH_SHORT).show()
        }

        locations.get().addOnSuccessListener { result ->
            for (document in result){
                val title = document.get("title")
                text.append(title as CharSequence?)
                text.append("\n")
            }
            Toast.makeText(this.context, "data retrieved successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{ exception ->
            Toast.makeText(this.context, "failed to retrieve data", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}