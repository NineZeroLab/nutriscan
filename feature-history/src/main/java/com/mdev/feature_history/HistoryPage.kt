package com.mdev.feature_history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mdev.feature_history.databinding.FragmentHistoryPageBinding

class HistoryPage : Fragment() {

    private lateinit var viewBinding: FragmentHistoryPageBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentHistoryPageBinding.inflate(inflater, container, false)
        return viewBinding.root
    }
}