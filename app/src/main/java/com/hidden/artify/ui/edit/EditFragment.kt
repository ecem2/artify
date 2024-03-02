package com.hidden.artify.ui.edit

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.hidden.artify.R
import com.hidden.artify.core.fragments.BaseFragment
import com.hidden.artify.data.model.SizeModel
import com.hidden.artify.databinding.FragmentEditBinding
import com.hidden.artify.extensions.popBack
import com.hidden.artify.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditFragment : BaseFragment<HomeViewModel, FragmentEditBinding>() {

    private val args: EditFragmentArgs by navArgs()

    override fun viewModelClass() = HomeViewModel::class.java

    override fun getResourceLayoutId() = R.layout.fragment_edit

    override fun onInitDataBinding() {
        if (args.imageModel == null) {
            viewBinding.llNoPhoto.visibility = View.VISIBLE
        } else {
            viewBinding.llNoPhoto.visibility = View.GONE
            val image = args.imageModel!!.icon
            Glide.with(requireContext()).load(image).into(viewBinding.ivEditedPhoto)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                val permissionList = PERMISSIONS_REQUIRED.toMutableList()
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                PERMISSIONS_REQUIRED = permissionList.toTypedArray()
            }
        }

        viewBinding.buttonCamera.setOnClickListener {
            if (!hasPermissions(requireContext())) {
                activityResultLauncher.launch(PERMISSIONS_REQUIRED)
            } else {
                navigateToCamera()
            }
        }

        viewBinding.buttonGallery.setOnClickListener {
            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            changeImage.launch(pickImg)
        }

        viewBinding.ivBack.setOnClickListener { popBack(R.id.homeFragment) }
    }

    private val changeImage =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val imgUri = data?.data
                findNavController().navigate(EditFragmentDirections.actionEditFragmentToEditPhotoFragment(
                    SizeModel(
                        icon = imgUri.toString(),
                        size = "256x256",
                        isSelected = false
                    )
                ))
            }
        }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in PERMISSIONS_REQUIRED && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(context, "Permission request denied", Toast.LENGTH_LONG).show()
            } else {
                navigateToCamera()
            }
        }

    private fun navigateToCamera() {
        lifecycleScope.launch {
            findNavController().navigate(EditFragmentDirections.actionEditFragmentToCameraFragment())
        }
    }

    companion object {
        private var PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)
        /** Convenience method used to check if all permissions required by this app are granted */
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}