package com.example.braincard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.braincard.Adattatori.BannerAdapter
import com.example.braincard.databinding.FragmentShopBinding


class ShopFragment : Fragment() {
    private var _binding: FragmentShopBinding?=null



    private lateinit var viewModel: ShopViewModel
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShopBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        val onlineBtn=binding.onlineBtn
        val localBtn=binding.localBtn
        val viewSwitcher=binding.viewSwitcher
        val ContenitoreOnline=binding.listaOnline
        val ContenitoreLocale=binding.listaLocale
        localBtn.setOnClickListener{
            viewSwitcher.showNext()
        }
        onlineBtn.setOnClickListener{
            viewSwitcher.showPrevious()
        }
        viewModel.GruppiLocale.observe(viewLifecycleOwner, Observer { gruppi ->
            ContenitoreLocale.removeAllViews()
            for(gruppo in gruppi){
                val deckButton = Button(requireContext())
                deckButton.text=gruppo.nome

            ContenitoreLocale.addView(deckButton)

            }

        })
        viewModel.AllGruppi.observe(viewLifecycleOwner, Observer { gruppi ->
            ContenitoreOnline.removeAllViews()
            //if(gruppi.isNullOrEmpty()) viewModel.creaGruppi() al momento sembra non servire
            val bannerAdp=BannerAdapter(gruppi)
            bannerAdp.populate(ContenitoreOnline)
        })



        return binding.root

    }
    fun generaBanner(): LinearLayout {
        val banner = LinearLayout(requireContext())
        banner.orientation = LinearLayout.HORIZONTAL

        val verticalLayout = LinearLayout(requireContext())
        verticalLayout.orientation = LinearLayout.VERTICAL
        verticalLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        val text1 = TextView(requireContext())
        text1.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        text1.textSize = 20f // 20sp
        text1.gravity = Gravity.CENTER
        text1.text = "Text 1"


        val text2 = TextView(requireContext())
        text2.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        text2.textSize = 16f // 16sp
        text2.gravity = Gravity.CENTER
        text2.text = "Text 2"

        val text3 = TextView(requireContext())
        text3.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        text3.textSize = 16f // 16sp
        text3.gravity = Gravity.CENTER
        text3.text = "Text 3"

        verticalLayout.addView(text1)
        verticalLayout.addView(text2)
        verticalLayout.addView(text3)

        //banner.addView(image)
        banner.addView(verticalLayout)
        return banner

    }




}