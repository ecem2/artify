package com.hidden.artify.ui.generate

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hidden.artify.R
import com.hidden.artify.core.common.ArgumentKey
import com.hidden.artify.core.common.ArgumentKey.EDIT_PHOTO_MODEL
import com.hidden.artify.core.common.ArgumentKey.REQUEST_MODEL
import com.hidden.artify.core.fragments.BaseFragment
import com.hidden.artify.data.model.GenerateModel
import com.hidden.artify.data.model.RequestModel
import com.hidden.artify.data.model.SizeModel
import com.hidden.artify.data.remote.ApiService
import com.hidden.artify.databinding.FragmentGenerateBinding
import com.hidden.artify.extensions.makePostRequest
import com.hidden.artify.extensions.popBack
import com.hidden.artify.ui.edit.EditPhotoFragment
import com.hidden.artify.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

@AndroidEntryPoint
class GenerateFragment : BaseFragment<HomeViewModel, FragmentGenerateBinding>() {
    private var generatedImage: GenerateModel? = null
    private val args: GenerateFragmentArgs by navArgs()
    private val client = OkHttpClient()
    private val imageList: ArrayList<GenerateModel> = ArrayList()
    private val generateAdapter by lazy { GenerateAdapter(onItemClicked = ::onArtItemClicked) }
    private val requiredPermissions: Array<String> = arrayOf(
        android.Manifest.permission.READ_MEDIA_IMAGES
    )
    private var isStorageImagePermitted = false

    override fun viewModelClass() = HomeViewModel::class.java

    override fun getResourceLayoutId() = R.layout.fragment_generate

    override fun onInitDataBinding() {
        viewBinding.apply {
            buttonEdit.isEnabled = false
            buttonEdit.isClickable = false
            buttonShare.isEnabled = false
            buttonShare.isClickable = false
        }
        setupLottie()
       // setupArtStyles()
        viewBinding.ivBack.setOnClickListener {
            popBack()
        }

        Log.e("salimmm", "args.requestModel ${args.requestModel}")


        viewBinding.buttonDownload.setOnClickListener {
            if (!isStorageImagePermitted) {
                requiredPermissionStorageImages()
            } else {
                if (imageList.isNotEmpty()) {
                    downloadImage(imageList[0].image)
                }
            }
        }

        viewBinding.buttonEdit.setOnClickListener {
            Log.d("aassaa", "Edit butonuna tıklandı.")
            sendRequest(
                args.requestModel.prompt,
                args.requestModel.count,
                args.requestModel.size
            )
            val action =
                GenerateFragmentDirections.actionGenerateFragmentToEditPhotoFragment()
            try {
                findNavController().navigate(action)
            } catch (e: Exception) {
            }
        }
    }
    private fun navigateToEditGenerateFragment(editModel: SizeModel) {
        val bundle = Bundle()
        bundle.putParcelable(EDIT_PHOTO_MODEL, editModel)
        val editPhotoFragment = EditPhotoFragment()
        editPhotoFragment.arguments = bundle
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.container, editPhotoFragment)
            addToBackStack(ArgumentKey.GENERATE_SCREEN)
            attach(GenerateFragment())
            commit()
        }
    }

    private fun requiredPermissionStorageImages() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                requiredPermissions[0]
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            isStorageImagePermitted = true
            if (imageList.isNotEmpty()) {
                downloadImage(imageList[0].image)
            }
        } else {
            requestPermissionLauncher.launch(requiredPermissions[0])
        }
    }

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                isStorageImagePermitted = true
                if (imageList.isNotEmpty()) {
                    downloadImage(imageList[0].image)
                }
            } else {
                isStorageImagePermitted = false
            }
        }

    private fun writeFileToStorage(body: ResponseBody): Boolean {
        val nameOfFile = ""
        var location: File? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            try {
                location = File(MediaStore.Downloads.EXTERNAL_CONTENT_URI.toString(), nameOfFile)
                if (location.exists()) {
                    location.delete()
                }

                val values: ContentValues = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, nameOfFile)
                values.put(MediaStore.Images.Media.DISPLAY_NAME, nameOfFile)
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                var uri: Uri? = null
                uri = requireContext().contentResolver.insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    values
                )
                val descriptor: ParcelFileDescriptor? =
                    uri?.let { requireContext().contentResolver.openFileDescriptor(it, "w") }
                val fileDescriptor: FileDescriptor? = descriptor?.fileDescriptor
                val inputStream: InputStream = body.byteStream()
                val fileReader: ByteArray = byteArrayOf(4096.toByte())
                val fileSize: Long = body.contentLength()
                var fileSizeDownloaded = 0
                val outputStream: OutputStream = FileOutputStream(fileDescriptor)
                while (true) {
                    val read: Int = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read
                    Log.d("salimmm", "file download size: $fileSizeDownloaded from $fileSize")
                }

                outputStream.flush()
                if (inputStream != null) {
                    inputStream.close()
                }

                if (outputStream != null) {
                    outputStream.close()
                }

                val readLocation: File = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .toString() + "/" + nameOfFile
                )
                Log.d("salimmm", "readLocation $readLocation")
            } catch (e: Exception) {
                Log.e("salimmm", "WRITE EXC ${e.localizedMessage}")
            }
            return true
        } else {
            return false
        }
    }

    private fun setupLottie() {
        viewBinding.apply {
           // buttonDownload.visibility = View.GONE
            ivGeneratedImage.visibility = View.GONE
            animationView.visibility = View.VISIBLE
            animationView.playAnimation()
        }
    }

    private fun downloadImage(fileUrl: String) {
        var newUrl = ""
        newUrl = if (fileUrl.last() != '/') {
            "$fileUrl/"
        } else {
            fileUrl
        }
        Log.d("salimmmm", "fileUrl -> $newUrl")
        val builder: Retrofit.Builder = Retrofit.Builder().baseUrl(newUrl)
        val retrofit: Retrofit = builder.build()
        val downloadService: ApiService = retrofit.create(ApiService::class.java)
        val call: Call<ResponseBody> = downloadService.downloadFile(newUrl)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val writeToDisk: Boolean =
                            response.body()?.let { writeFileToStorage(it) } == true
                        Log.d("salimmmm", "FILE DOWNLOADED OR NOT STATUS -> $writeToDisk")
                    } catch (e: Exception) {
                        Log.d("salimmmm", "onResponse Exception -> ${e.localizedMessage}")
                    }
                } else {

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("salimmmm", "FILE DOWNLOAD onFailure -> $t")
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun sendRequest(text: String, count: Int, size: String) {
        val requestBody = """
           {
           "prompt":"$text",
           "n":$count,
           "size":"$size"
           }
        """.trimIndent()

        if (activity != null && context != null) {
            client.makePostRequest(requestBody.toRequestBody()) { result ->
                requireActivity().runOnUiThread {
                    viewBinding.apply {
                        buttonEdit.isEnabled = true
                        buttonEdit.isClickable = true
                        buttonShare.isEnabled = true
                        buttonShare.isClickable = true
                        animationView.cancelAnimation()
                        animationView.visibility = View.GONE
                        tvGenerating.visibility = View.GONE
                      //  buttonDownload.visibility = View.VISIBLE
                        ivGeneratedImage.visibility = View.VISIBLE
                    }

                    for (i in 0 until count) {
                        imageList.add(
                            GenerateModel(
                                image = result.getJSONObject(i).getString("url").trim(),
                                animation = R.raw.loading
                            )
                        )
                    }
                    Glide.with(requireContext()).load(imageList[0].image)
                        .into(viewBinding.ivGeneratedImage)

                    generateAdapter.submitList(imageList)
                    generateAdapter.imagesLoaded = true
                    generateAdapter.notifyDataSetChanged()
                }
            }
        }
    }

//    private fun setupArtStyles() {
//        generateAdapter.submitList(viewModel.imageList)
//        viewBinding.rvArts.apply {
//            adapter = generateAdapter
//            layoutManager =
//                object : LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false) {
//                    override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
//                        lp.width = width / 3
//                        return true
//                    }
//                }
//            setHasFixedSize(true)
//        }
//    }

    private fun onArtItemClicked(artStyleModel: GenerateModel, i: Int) {
        Log.d("salimmm", "artStyleModel $artStyleModel")
    }
}