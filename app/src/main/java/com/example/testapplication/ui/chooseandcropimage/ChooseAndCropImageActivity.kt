package com.example.testapplication.ui.chooseandcropimage

import ImageAdapter
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityChooseAndCropImageBinding
import com.yalantis.ucrop.UCrop
import java.io.File

class ChooseAndCropImageActivity : BaseActivity<ActivityChooseAndCropImageBinding>() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ChooseAndCropImageActivity::class.java)
        }
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityChooseAndCropImageBinding {
        return ActivityChooseAndCropImageBinding.inflate(inflater)
    }

    private val selectedImages = mutableListOf<Uri>()
    private var croppingImageUri: Uri? = null
    private var capturedImageUri: Uri? = null

    private val pickMultipleMedia = registerForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia(6)
    ) { uris ->
        if (uris.isNotEmpty()) {
            selectedImages.clear()
            selectedImages.addAll(uris)
            refreshGrid()
        }
    }

    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && capturedImageUri != null) {
            cropImage(capturedImageUri!!)
        }
    }

    private val cropImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val resultUri = UCrop.getOutput(result.data!!)
            resultUri?.let { uri ->
                if (croppingImageUri != null) {
                    val index = selectedImages.indexOf(croppingImageUri)
                    if (index != -1) {
                        selectedImages[index] = uri
                    }
                    croppingImageUri = null
                } else {
                    selectedImages.add(uri)
                }
                refreshGrid()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            btnSelectImages.setOnClickListener {
                pickMultipleMedia.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }

            btnCaptureImage.setOnClickListener {
                val photoFile = File(cacheDir, "photo_${System.currentTimeMillis()}.jpg")
                capturedImageUri = FileProvider.getUriForFile(
                    this@ChooseAndCropImageActivity,
                    "${applicationContext.packageName}.provider",
                    photoFile
                )
                takePictureLauncher.launch(capturedImageUri)
            }
        }
    }

    private fun refreshGrid() {
        binding.gridView.adapter =
            ImageAdapter(this, selectedImages) { position ->
                croppingImageUri = selectedImages[position]
                cropImage(selectedImages[position])
            }
    }

    private fun cropImage(uri: Uri) {
        val destinationUri =
            Uri.fromFile(File(cacheDir, "cropped_${System.currentTimeMillis()}.jpg"))
        val options = UCrop.Options().apply {
            setCompressionQuality(90)
            setFreeStyleCropEnabled(true)
        }
        val uCrop = UCrop.of(uri, destinationUri).withOptions(options)
        cropImageLauncher.launch(uCrop.getIntent(this))
    }
}

