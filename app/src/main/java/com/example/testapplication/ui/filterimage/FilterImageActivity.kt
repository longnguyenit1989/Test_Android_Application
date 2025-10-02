package com.example.testapplication.ui.filterimage

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.common.FilterItem
import com.example.testapplication.databinding.ActivityFilterImageBinding
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorInvertFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGaussianBlurFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSketchFilter
import androidx.core.graphics.createBitmap

class FilterImageActivity : BaseActivity<ActivityFilterImageBinding>() {
    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, FilterImageActivity::class.java)
        }
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityFilterImageBinding {
        return ActivityFilterImageBinding.inflate(inflater)
    }

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            Glide.with(this)
                .asBitmap()
                .load(selectedUri)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        binding.apply {
                            gpuImageView.gpuImage.deleteImage()
                            gpuImageView.requestRender()
                            gpuImageView.setImage(resource)
                        }
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            btnChoose.setOnClickListener {
                imagePickerLauncher.launch("image/*")
            }
        }

        setRvFilter()
    }

    private fun setRvFilter() {
        val filters = listOf(
            FilterItem("Original", GPUImageFilter(), true),
            FilterItem("Sepia", GPUImageSepiaToneFilter()),
            FilterItem("Gray", GPUImageGrayscaleFilter()),
            FilterItem("Blur", GPUImageGaussianBlurFilter(5f)),
            FilterItem("Contrast", GPUImageContrastFilter(2.0f)),
            FilterItem("Sketch", GPUImageSketchFilter()),
            FilterItem("Invert", GPUImageColorInvertFilter())
        )

        val adapter = FilterAdapter(filters) { filter ->
            binding.gpuImageView.setFilter(filter)
        }

        binding.rvFilters.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvFilters.adapter = adapter
    }
}