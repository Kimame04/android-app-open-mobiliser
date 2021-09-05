package com.example.openmobiliser.ui.submission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.openmobiliser.R
import com.example.openmobiliser.databinding.FragmentSubmissionBinding
import com.example.openmobiliser.models.Location
import com.example.openmobiliser.ui.map.MapFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SubmissionFragment: Fragment() {

    private lateinit var submissionViewModel: SubmissionViewModel
    private var _binding: FragmentSubmissionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        submissionViewModel = SubmissionViewModel()
        _binding = FragmentSubmissionBinding.inflate(inflater,container,false)
        val root: View = binding.root

        val locations = Firebase.firestore.collection("locations")

        val title = binding.testTitle
        val description = binding.testDesc
        val lat = binding.testLat
        val long = binding.testLong

        val btn = binding.confirmButton
        btn.setOnClickListener { view ->
            val loc = Location(title.text.toString(), description.text.toString(),lat.text.toString().toDouble(),long.text.toString().toDouble())
            locations.document().set(loc)
            Toast.makeText(this.context,"note saved", Toast.LENGTH_SHORT).show()
            view.isVisible = false
            childFragmentManager.beginTransaction().apply {
                replace(R.id.submission_fragment,MapFragment())
                commit()
            }
            btn.isVisible = false
        }

        return root
    }
}