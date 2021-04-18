package com.georgcantor.wallpaperapp.ui.fragment.gallery

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.georgcantor.wallpaperapp.R
import com.georgcantor.wallpaperapp.databinding.FragmentGalleryBinding
import com.georgcantor.wallpaperapp.ui.activity.detail.DetailActivity
import com.georgcantor.wallpaperapp.util.Constants.PIC_EXTRA
import com.georgcantor.wallpaperapp.util.startActivity
import com.georgcantor.wallpaperapp.util.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    private val binding by viewBinding(FragmentGalleryBinding::bind)
    private val viewModel: GalleryViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val request = (findNavController().currentDestination?.label).toString()

        val galleryAdapter = GalleryAdapter { pic ->
            requireActivity().startActivity<DetailActivity> { putExtra(PIC_EXTRA, pic) }
        }

        galleryAdapter.addLoadStateListener {
            binding.progressBar.isVisible = it.source.refresh is LoadState.Loading
        }

        binding.picturesRecycler.apply {
            layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
            adapter = galleryAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPicListStream(request).collectLatest {
                galleryAdapter.submitData(it)
            }
        }
    }
}