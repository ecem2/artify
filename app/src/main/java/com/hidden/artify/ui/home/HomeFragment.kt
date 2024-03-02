package com.hidden.artify.ui.home

import android.content.Context
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hidden.artify.R
import com.hidden.artify.core.common.Util.EMPTY_STRING
import com.hidden.artify.core.fragments.BaseFragment
import com.hidden.artify.data.model.ArtStyleModel
import com.hidden.artify.data.model.RequestModel
import com.hidden.artify.data.model.SizeModel
import com.hidden.artify.databinding.FragmentHomeBinding
import com.hidden.artify.extensions.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(),
    SizeAdapter.OnItemSelectedListener, ArtStyleAdapter.OnArtItemSelectedListener {

    private val sizeAdapter by lazy { SizeAdapter() }
    private val artStyleAdapter by lazy { ArtStyleAdapter() }
    private lateinit var promptMessage: String
    private var selectedCount: Int = 0
    private lateinit var selectedSize: String
    private lateinit var selectedArtStyle: String

    override fun viewModelClass() = HomeViewModel::class.java

    override fun getResourceLayoutId() = R.layout.fragment_home

    override fun onInitDataBinding() {
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )
        selectedArtStyle = viewModel.artList[0].name
        promptMessage = EMPTY_STRING
        selectedSize = getString(R.string.size_one)
        selectedCount = 4

        setupArtStyles()
        viewBinding.buttonGenerate.setOnClickListener {
            promptMessage = viewBinding.etPrompt.text.toString()
            if (promptMessage.isNotBlank() && promptMessage.isNotEmpty()) {
                hideKeyboard()
                val requestModel = RequestModel(
                    prompt = promptMessage,
                    count = selectedCount,
                    size = selectedSize
                )
                viewBinding.etPrompt.text?.clear()
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToGenerateFragment(
                        requestModel
                    )
                )
            } else {
                Toast.makeText(requireContext(), "Please enter a prompt", Toast.LENGTH_SHORT).show()
            }
        }

        viewBinding.tvSeeAll.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToArtStyleFragment())
        }
    }

    private fun setupArtStyles() {
        artStyleAdapter.setOnArtItemSelectedListener(this)
        viewBinding.rvArtStyle.apply {
            adapter = artStyleAdapter
            layoutManager =
                object : LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false) {
                    override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                        lp.width = width / 2
                        return true
                    }
                }
            setHasFixedSize(true)
        }
        artStyleAdapter.submitList(viewModel.artList)
    }

    override fun onResume() {
        super.onResume()
        viewBinding.etPrompt.post {
            val inputManager: InputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(viewBinding.etPrompt, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun onItemSelected(item: SizeModel) {
        selectedSize = item.size.toString()
    }

    override fun onArtItemSelected(item: ArtStyleModel) {
        selectedArtStyle = item.name
    }
}