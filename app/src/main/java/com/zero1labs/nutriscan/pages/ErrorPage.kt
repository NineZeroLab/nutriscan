package com.zero1labs.nutriscan.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.zero1labs.nutriscan.R
import com.zero1labs.nutriscan.viewModels.AppViewModel

class ErrorPage : Fragment(R.layout.fragment_error_page) {
    private lateinit var tvErrorText: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]


        tvErrorText = view.findViewById(R.id.tv_error_text)
        tvErrorText.text = viewModel.uiState.value.error.toString()
    }

}