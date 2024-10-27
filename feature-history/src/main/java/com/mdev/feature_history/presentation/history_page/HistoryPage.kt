package com.mdev.feature_history.presentation.history_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdev.feature_history.databinding.FragmentHistoryPageBinding
import com.mdev.feature_history.navigation.HistoryNavigator
import kotlinx.coroutines.launch
import javax.inject.Inject

class HistoryPage : Fragment() {

    private lateinit var viewModel: HistoryPageViewModel
    private lateinit var viewBinding: FragmentHistoryPageBinding
    @Inject
    lateinit var navigator: HistoryNavigator
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[HistoryPageViewModel::class.java]
        viewBinding = FragmentHistoryPageBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = SearchHistoryAdapter(searchHistoryItems = mutableListOf()){ productId ->
            navigator.navigateToProductDetailsPage(this@HistoryPage, productId)
        }
        viewBinding.rvHistoryList.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.rvHistoryList.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect{ state ->
                    adapter.updateData(state.searchHistory)
                }
            }
        }

        viewBinding.svHistoryList.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    adapter.filter(newText)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })
    }
}