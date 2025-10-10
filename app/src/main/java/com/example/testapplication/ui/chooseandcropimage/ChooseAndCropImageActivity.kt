package com.example.testapplication.ui.chooseandcropimage

import ImageAdapter
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityChooseAndCropImageBinding
import com.yalantis.ucrop.UCrop
import java.io.File

class ChooseAndCropImageActivity : BaseActivity<ActivityChooseAndCropImageBinding>() {

    private var selectionTriggeredPosition: Int? = null

    private val selectedImages = mutableListOf<Uri>()
    private val selectedForAction = mutableSetOf<Uri>()
    private var isSelectionMode = false

    private var croppingImageUri: Uri? = null
    private var capturedImageUri: Uri? = null

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ChooseAndCropImageActivity::class.java)
        }
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityChooseAndCropImageBinding {
        return ActivityChooseAndCropImageBinding.inflate(inflater)
    }

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
            croppingImageUri = null
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
                    if (index != -1) selectedImages[index] = uri
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
        setOnClickListener()
    }

    private fun setOnClickListener() {
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

            btnDelete.setOnClickListener {
                if (selectedForAction.isNotEmpty()) {
                    selectedImages.removeAll(selectedForAction)
                    selectedForAction.clear()
                    isSelectionMode = false
                    refreshGrid()
                }
            }

            btnCancelSelection.setOnClickListener {
                selectedForAction.clear()
                isSelectionMode = false
                refreshGrid()
            }

            btnShare.setOnClickListener {
                if (selectedForAction.isNotEmpty()) {
                    shareSelectedImages()
                }
            }
        }
    }

    private fun refreshGrid() {
        binding.gridView.adapter = ImageAdapter(
            context = this,
            images = selectedImages,
            isSelectionMode = isSelectionMode,
            selectedItems = selectedForAction,
            onItemClick = { position ->
                if (!isSelectionMode) {
                    croppingImageUri = selectedImages[position]
                    cropImage(selectedImages[position])
                }
            },
            onItemLongClick = { position, uri ->
                if (!isSelectionMode) {
                    isSelectionMode = true
                    selectedForAction.add(uri)
                    selectionTriggeredPosition = position
                    refreshGrid()
                    binding.gridView.post {
                        selectionTriggeredPosition?.let { pos ->
                            animateSelectionAtPosition(pos)
                            selectionTriggeredPosition = null
                        }
                    }
                }
            }
        )

        binding.selectionActions.visibility = if (isSelectionMode) View.VISIBLE else View.GONE
    }

    private fun animateSelectionAtPosition(position: Int) {
        val first = binding.gridView.firstVisiblePosition
        val childIndex = position - first
        if (childIndex >= 0 && childIndex < binding.gridView.childCount) {
            val child = binding.gridView.getChildAt(childIndex) ?: return
            val iv = child.findViewWithTag<ImageView>("imageView")
            val overlay = child.findViewWithTag<ImageView>("overlay")

            if (overlay != null) {
                overlay.visibility = View.VISIBLE
                overlay.run { setImageResource(android.R.drawable.checkbox_on_background) }
                overlay.scaleX = 1f
                overlay.scaleY = 1f
                overlay.animate().scaleX(1.3f).scaleY(1.3f).setDuration(160).start()
            }
            if (iv != null) {
                iv.scaleX = 1f
                iv.scaleY = 1f
                iv.animate().scaleX(0.9f).scaleY(0.9f).setDuration(160).start()
            }
        }
    }

    private fun shareSelectedImages() {
        val shareIntent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            type = "image/*"
            putParcelableArrayListExtra(
                Intent.EXTRA_STREAM,
                ArrayList(selectedForAction)
            )
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(shareIntent, "Share images via"))
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


