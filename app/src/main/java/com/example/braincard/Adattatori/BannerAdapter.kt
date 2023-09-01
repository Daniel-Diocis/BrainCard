package com.example.braincard.Adattatori
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.os.bundleOf

import androidx.databinding.DataBindingUtil


import androidx.navigation.fragment.findNavController
import com.example.braincard.R
import com.example.braincard.data.model.GruppoFire
import com.example.braincard.databinding.FragmentGruppoBannerBinding

class BannerAdapter(private val items: MutableList<GruppoFire>) {

    fun populate(parentLayout: LinearLayout,onItemClick: (GruppoFire) -> Unit) {
        val inflater = LayoutInflater.from(parentLayout.context)

        for (item in items) {
            val binding: FragmentGruppoBannerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_gruppo_banner, parentLayout, false)
            binding.banner = item
            // Associa un evento all'elemento
            binding.Layoutbanner.setOnClickListener {
                onItemClick(item)
            }
            

            parentLayout.addView(binding.root)
        }

    }




}
