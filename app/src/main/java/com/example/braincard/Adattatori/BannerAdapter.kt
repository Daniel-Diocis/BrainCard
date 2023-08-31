package com.example.braincard.Adattatori
import android.view.LayoutInflater
import android.widget.LinearLayout

import androidx.databinding.DataBindingUtil
import com.example.braincard.R
import com.example.braincard.data.model.GruppoFire
import com.example.braincard.databinding.FragmentGruppoBannerBinding

class BannerAdapter(private val items: MutableList<GruppoFire>) {

    fun populate(parentLayout: LinearLayout) {
        val inflater = LayoutInflater.from(parentLayout.context)

        for (item in items) {
            val binding: FragmentGruppoBannerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_gruppo_banner, parentLayout, false)
            binding.banner = item

            parentLayout.addView(binding.root)
        }
    }
}
