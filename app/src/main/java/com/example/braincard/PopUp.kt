package com.example.braincard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController


class PopUp : DialogFragment() {
    var vista=""



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment


        val root=inflater.inflate(R.layout.fragment_pop_up, container, false)
        val btn=root.findViewById<Button>(R.id.btnPopUp)
        val text=root.findViewById<TextView>(R.id.editTextPopUp)

        btn.setOnClickListener(){

            val message= PopUpMessage.getInstance()
            message.invia=true
            if (vista=="gruppo"){
                message.messageDeckLiveData.value=text.text.toString()
            }
            else message.messageLiveData.value=text.text.toString()


            dismiss()
        }


        return root

    }



}