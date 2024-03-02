package com.hidden.artify.ui.edit

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.SurfaceView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.hidden.artify.R
import com.hidden.artify.core.common.Status
import com.hidden.artify.core.fragments.BaseFragment
import com.hidden.artify.databinding.FragmentEditPhotoBinding
import com.hidden.artify.view.WScratchView.OnScratchCallback
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executors


@AndroidEntryPoint
class EditPhotoFragment : BaseFragment<EditPhotoViewModel, FragmentEditPhotoBinding>() {

    private lateinit var generatedImage: String
    private lateinit var scratchedImage: File
    private var mPercentage = 0f
    private var newImage: Bitmap? = null
    private lateinit var imageSize: String
    private val urlList: ArrayList<String> = ArrayList()

    override fun viewModelClass() = EditPhotoViewModel::class.java

    override fun getResourceLayoutId() = R.layout.fragment_edit_photo

    override fun onInitDataBinding() {
        val args = arguments?.let {
            EditPhotoFragmentArgs.fromBundle(
                it
            )
        }
        if (args?.imageModel != null) {
            generatedImage = args.imageModel?.icon.toString()
            imageSize = args.imageModel?.size.toString()

            Log.d("salimmm", "generatedImage $generatedImage")

            val executor = Executors.newSingleThreadExecutor()
            executor.execute {
                newImage = createDrawableFromUrl(generatedImage)
                val d: Drawable = BitmapDrawable(resources, newImage)
                viewBinding.ivEditPhoto.setScratchDrawable(d)
            }

            val imageToErase = viewBinding.ivEditPhoto
            val imgFile = File(args.imageModel?.icon.toString())
            if (imgFile.exists()) {
                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                imageToErase.setScratchBitmap(myBitmap)
                imageToErase.setOnScratchCallback(object : OnScratchCallback() {
                    override fun onScratch(percentage: Float) {
                        updatePercentage(percentage)
                    }

                    override fun onDetach(fingerDetach: Boolean) {
                        if (fingerDetach) {
                            updatePercentage(100F)
                        }
                    }
                })
            }
        } else {
            Toast.makeText(requireContext(), "args?.imageModel == null", Toast.LENGTH_SHORT).show()
        }

        viewBinding.buttonGenerate.setOnClickListener {
            val bitmap = getImageFromView(viewBinding.ivEditPhoto)
            if (bitmap != null) {
                saveToStorage(bitmap)
            }
        }

        viewBinding.ivBack.setOnClickListener {
            findNavController().navigate(EditPhotoFragmentDirections.actionEditPhotoFragmentToEditFragment())
        }

        viewBinding.tvEditReset.setOnClickListener {
            findNavController().navigate(EditPhotoFragmentDirections.actionEditPhotoFragmentToEditFragment())
        }
    }

    private fun createDrawableFromUrl(url: String?): Bitmap? {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.setRequestProperty("User-agent", "Mozilla/4.0")
        connection.connect()
        val input = connection.inputStream
        return BitmapFactory.decodeStream(input)
    }

    private fun saveToStorage(bitmap: Bitmap) {
        val simpleDateFormat = SimpleDateFormat("yyyymmsshhmmss", Locale.getDefault())
        val date = simpleDateFormat.format(Date())
        val imageName = "IMG$date.png"
        var fos: OutputStream?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requireActivity().contentResolver.also { resolver->
                val contentValues: ContentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, imageName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
                scratchedImage = File(imageUri?.path)
            }
        } else {
            val imagesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDirectory, imageName)
            scratchedImage = image
            fos = FileOutputStream(image)
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            Log.d("salimmm", "SUCCESSFULLY SAVED imagesDirectory $imageName")

            viewModel.uploadImage(
                File(generatedImage),
                scratchedImage,
                viewBinding.tvEditDescription.text.toString(),
                4,
                "1024x1024"
            )
            getEditedUrlList()
        }
    }

    private fun updatePercentage(percentage: Float) {
        mPercentage = percentage
    }

    private fun getImageFromView(view: SurfaceView): Bitmap? {
        var image: Bitmap? = null
        try {
            image = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(image)
            view.draw(canvas)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
        }

        val size = imageSize
        var imgWidth = 0
        var imgHeight = 0
        val parts = size.split("x", limit = 2)
        if (parts.size == 2) {
            imgWidth = parts[0].toInt()
            imgHeight = parts[1].toInt()
        }

        return image?.let { resizeImage(it, imgWidth, imgHeight) }
    }

    private fun resizeImage(yourBitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(yourBitmap, newWidth, newHeight, true)
    }

    private fun getEditedUrlList() {
        viewModel.editedImageResponse.observe(viewLifecycleOwner) { editedImages ->
            when (editedImages.status) {
                Status.SUCCESS -> {
                    editedImages.data?.data?.forEach { dataModel ->
                        urlList.add(dataModel.url.toString())
                    }

                }
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        editedImages.message ?: "Error",
                        Toast.LENGTH_LONG
                    ).show()
                }
                Status.LOADING -> {
                    Log.d("salimmm", "QQQQQ")
                }
            }
        }
    }

    private fun saveGeneratedFile(imageUrl: String) {
        val url = URL(imageUrl)
        val input = url.openStream()
        input.use { input ->
            //The sdcard directory e.g. '/sdcard' can be used directly, or
            //more safely abstracted with getExternalStorageDirectory()
            val storagePath = Environment.getExternalStorageDirectory()
            val output: OutputStream = FileOutputStream("$storagePath/myImage.png")
            output.use { output ->
                val buffer = ByteArray(4096.toByte().toInt())
                var bytesRead = 0
                while (input.read(buffer, 0, buffer.size).also { bytesRead = it } >= 0) {
                    output.write(buffer, 0, bytesRead)
                }
            }
        }
    }
}