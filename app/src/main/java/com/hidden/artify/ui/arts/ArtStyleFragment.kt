package com.hidden.artify.ui.arts

import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hidden.artify.R
import com.hidden.artify.core.fragments.BaseFragment
import com.hidden.artify.data.model.ArtStyleModel
import com.hidden.artify.databinding.FragmentArtStyleBinding
import com.hidden.artify.extensions.popBack
import com.hidden.artify.ui.home.ArtStyleAdapter
import com.hidden.artify.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtStyleFragment : BaseFragment<HomeViewModel, FragmentArtStyleBinding>(), ArtStyleAdapter.OnArtItemSelectedListener {

    private val artStyleAdapter by lazy { ArtStyleAdapter() }

    override fun viewModelClass() = HomeViewModel::class.java

    override fun getResourceLayoutId() = R.layout.fragment_art_style

    override fun onInitDataBinding() {
        viewBinding.ivRight.setOnClickListener {
            popBack()
        }
        setupArtStyles()
    }

    private fun setupArtStyles() {
        artStyleAdapter.submitList(viewModel.artList)
        artStyleAdapter.setOnArtItemSelectedListener(this)
        viewBinding.rvArtStyle.apply {
            adapter = artStyleAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
            setHasFixedSize(true)
        }
    }

    override fun onResume() {
        super.onResume()
//        viewBinding.etSearch.post {
//            val inputManager: InputMethodManager =
//                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            inputManager.showSoftInput(viewBinding.etSearch, InputMethodManager.SHOW_IMPLICIT)
//        }
    }

    override fun onArtItemSelected(item: ArtStyleModel) {
        Log.d("salimmm", "onItemSelected artStyleModel $item")
    }
}