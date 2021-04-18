package com.georgcantor.wallpaperapp.ui.fragment.favorites

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.georgcantor.wallpaperapp.R
import com.georgcantor.wallpaperapp.databinding.FragmentFavoritesBinding
import com.georgcantor.wallpaperapp.util.gone
import com.georgcantor.wallpaperapp.util.viewBinding
import com.georgcantor.wallpaperapp.util.visible
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private val binding by viewBinding(FragmentFavoritesBinding::bind)
    private val viewModel: FavoritesViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)?.gone()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.favorites.observe(viewLifecycleOwner) {
            binding.emptyTv.isVisible = it.isNullOrEmpty()
            binding.picturesRecycler.apply {
                setHasFixedSize(true)
                layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
                adapter = FavoritesAdapter(it) {
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavorites()
    }

    override fun onDetach() {
        activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)?.visible()
        super.onDetach()
    }
}