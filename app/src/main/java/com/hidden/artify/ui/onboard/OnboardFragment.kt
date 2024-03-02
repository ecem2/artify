package com.hidden.artify.ui.onboard

import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hidden.artify.R
import com.hidden.artify.core.fragments.BaseFragment
import com.hidden.artify.databinding.FragmentOnboardBinding
import com.hidden.artify.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OnboardFragment : BaseFragment<OnboardViewModel, FragmentOnboardBinding>() {

    override fun viewModelClass() = OnboardViewModel::class.java

    override fun getResourceLayoutId() = R.layout.fragment_onboard

    override fun onInitDataBinding() {
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNav)
        navBar.visibility = View.GONE
        viewBinding.buttonStart.setOnClickListener {
            viewModel.saveOnBoardingState()
            launchHomeScreen()
        }
    }

    private fun launchHomeScreen() {
        startActivity(MainActivity.newIntent(requireContext())).also {
            requireActivity().finish()
        }
    }
}