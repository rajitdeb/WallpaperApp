package com.example.wallpaperapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wallpaperapp.R
import com.example.wallpaperapp.adapter.HomeRecyclerAdapter
import com.example.wallpaperapp.adapter.PhotoLoadStateAdapter
import com.example.wallpaperapp.databinding.FragmentDiscoverBinding
import com.example.wallpaperapp.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest

class DiscoverFragment : Fragment() {

    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()
    lateinit var homeRecyclerAdapter: HomeRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDiscoverBinding.inflate(layoutInflater, container, false)

        setHasOptionsMenu(true)

        homeRecyclerAdapter = HomeRecyclerAdapter {
            Toast.makeText(context, "Wallpaper: $it", Toast.LENGTH_LONG).show()
            findNavController().navigate(DiscoverFragmentDirections.actionDiscoverToWallpaperPreview(it))
        }

        binding.discoverRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = homeRecyclerAdapter.withLoadStateHeaderAndFooter(
                header = PhotoLoadStateAdapter { homeRecyclerAdapter.retry() },
                footer = PhotoLoadStateAdapter { homeRecyclerAdapter.retry() }
            )
            setHasFixedSize(true)
        }

        binding.btnRetry.setOnClickListener {
            homeRecyclerAdapter.retry()
        }

        homeRecyclerAdapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                discoverRv.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                resultsErrorTv.isVisible = loadState.source.refresh is LoadState.Error

                // empty view
                if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        homeRecyclerAdapter.itemCount < 1) {
                    discoverRv.isVisible = false
                    noResultFoundErrorTv.isVisible = true
                } else {
                    noResultFoundErrorTv.isVisible = false
                }
            }
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_discover, menu)

        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null){
                    binding.discoverRv.scrollToPosition(0)
                    lifecycleScope.launchWhenCreated {
                        mainViewModel.searchPhotos(query).collectLatest {
                            homeRecyclerAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                        }
                    }
                    // this will automatically hide keyboard on submit pressed
                    searchView.clearFocus()
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }
}