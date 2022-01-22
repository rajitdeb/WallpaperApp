package com.example.wallpaperapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wallpaperapp.adapter.HomeRecyclerAdapter
import com.example.wallpaperapp.adapter.PhotoLoadStateAdapter
import com.example.wallpaperapp.databinding.FragmentHomeBinding
import com.example.wallpaperapp.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import android.graphics.drawable.BitmapDrawable

import android.graphics.Bitmap

import android.app.WallpaperManager
import android.widget.ImageView


class HomeFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        val homeRecyclerAdapter = HomeRecyclerAdapter {
            findNavController().navigate(HomeFragmentDirections.actionHomeToWallpaperPreview(it))
        }

        binding.homeRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = homeRecyclerAdapter.withLoadStateHeaderAndFooter(
                header = PhotoLoadStateAdapter { homeRecyclerAdapter.retry() },
                footer = PhotoLoadStateAdapter { homeRecyclerAdapter.retry() }
            )
            setHasFixedSize(true)
        }

        lifecycleScope.launchWhenCreated {
            mainViewModel.photos.collectLatest {
                homeRecyclerAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}