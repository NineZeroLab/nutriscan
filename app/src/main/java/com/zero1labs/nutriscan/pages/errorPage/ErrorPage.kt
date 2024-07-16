package com.zero1labs.nutriscan.pages.errorPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.zero1labs.nutriscan.R
import com.zero1labs.nutriscan.viewModels.AppViewModel

class ErrorPage : Fragment(R.layout.fragment_error_page) {
    private lateinit var tvErrorText: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]
        val appCompatActivity = activity as AppCompatActivity
        val materialToolbar: MaterialToolbar= appCompatActivity.findViewById(R.id.mt_app_toolbar)
        appCompatActivity.setSupportActionBar(materialToolbar)


        tvErrorText = view.findViewById(R.id.tv_error_text)
        tvErrorText.text = viewModel.uiState.value.error.toString()
    }

}