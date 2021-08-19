package com.example.openmobiliser.ui.submission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.openmobiliser.databinding.FragmentSubmissionBinding

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
        return root
    }
}