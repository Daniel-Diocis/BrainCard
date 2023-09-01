package com.example.braincard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class GruppoUploadFragment : Fragment() {

    companion object {
        fun newInstance() = GruppoUploadFragment()
    }

    private lateinit var viewModel: GruppoUploadViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gruppo_upload, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GruppoUploadViewModel::class.java)
        // TODO: Use the ViewModel
    }

}