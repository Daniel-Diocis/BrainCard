package com.example.braincard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.size
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
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
        val ContenitoreCercati=binding.ContenitoreCercati
        val scrollOnline=binding.scrollOnline
        val scrollRicercaOnline=binding.scrollRicercaOnline
        val barraRicerca=binding.ricercaOnline
        var currentIndex = 0
        localBtn.setOnClickListener{
            currentIndex = viewSwitcher.displayedChild
            if(currentIndex==0){
                viewSwitcher.showNext()
            }

        }
        onlineBtn.setOnClickListener{
            currentIndex = viewSwitcher.displayedChild
            if(currentIndex==1){
                viewSwitcher.showPrevious()
            }
            scrollOnline.visibility=View.VISIBLE
            scrollRicercaOnline.visibility=View.GONE

        }
        // Imposta un listener per il pulsante di ricerca
        barraRicerca.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // L'utente ha premuto il pulsante di ricerca
                if (!query.isNullOrEmpty()) {
                    viewModel.cercaBar(query)

                    currentIndex=viewSwitcher.displayedChild
                    if (currentIndex==1){
                        viewSwitcher.showNext()
                    }
                    scrollOnline.visibility=View.GONE
                    scrollRicercaOnline.visibility=View.VISIBLE

                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Non fare nulla quando il testo cambia
                return false
            }


        })
        viewModel.GruppiLocale.observe(viewLifecycleOwner, Observer { gruppi ->
            ContenitoreLocale.removeAllViews()
            for(gruppo in gruppi){
                val deckButton = Button(requireContext())
                deckButton.text=gruppo.nome

            ContenitoreLocale.addView(deckButton)
                deckButton.setOnClickListener{
                    val bundle = bundleOf("gruppoId" to gruppo.id)
                    Log.e("AndroidRuntime", gruppo.id)
                    findNavController().navigate(R.id.action_navigation_dashboard_to_gruppoUploadFragment, bundle)
                }

            }

        })
        viewModel.AllGruppi.observe(viewLifecycleOwner, Observer { gruppi ->
            ContenitoreOnline.removeAllViews()
            //if(gruppi.isNullOrEmpty()) viewModel.creaGruppi() al momento sembra non servire
            val bannerAdp=BannerAdapter(gruppi)
            bannerAdp.populate(ContenitoreOnline) { item ->

                val bundle = bundleOf("gruppoId" to item.id)
                findNavController().navigate(
                    R.id.action_navigation_dashboard_to_gruppo_DownloadFragment2,bundle
                )

            }

        })
        viewModel.GruppiCercati.observe(viewLifecycleOwner, Observer { gruppi ->
            ContenitoreCercati.removeAllViews()
            //if(gruppi.isNullOrEmpty()) viewModel.creaGruppi() al momento sembra non servire


            val bannerAdp=BannerAdapter(gruppi)

            bannerAdp.populate(ContenitoreCercati){item ->

                val bundle = bundleOf("gruppoId" to item.id)
                Log.e("importo",bundle.toString())
                findNavController().navigate(
                    R.id.action_navigation_dashboard_to_gruppo_DownloadFragment2,bundle
                )}
            Log.e("carica",ContenitoreCercati.size.toString())
        })




        return binding.root

    }




}