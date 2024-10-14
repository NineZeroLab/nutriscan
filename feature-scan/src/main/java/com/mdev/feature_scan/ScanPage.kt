package com.mdev.feature_scan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mdev.feature_scan.databinding.FragmentScanPageBinding

class ScanPage : Fragment() {
    private lateinit var viewBinding: FragmentScanPageBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentScanPageBinding.inflate(inflater,container,false)
        return viewBinding.root
    }

}