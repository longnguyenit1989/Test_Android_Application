package com.example.testapplication.ui.draw

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.testapplication.R
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityDrawBinding

class DrawActivity : BaseActivity<ActivityDrawBinding>() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, DrawActivity::class.java)
        }
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityDrawBinding {
        return ActivityDrawBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUI()
    }

    private fun setUI() {
        binding.apply {
            val black = ContextCompat.getColor(this@DrawActivity,R.color.black)
            val red = ContextCompat.getColor(this@DrawActivity,R.color.red)
            val blue = ContextCompat.getColor(this@DrawActivity,R.color.blue)

            radioBlack.buttonTintList = ColorStateList.valueOf(black)
            radioRed.buttonTintList = ColorStateList.valueOf(red)
            radioBlue.buttonTintList = ColorStateList.valueOf(blue)

            radioGroupColors.setOnCheckedChangeListener { _, checkedId ->
                val color = when (checkedId) {
                    R.id.radioBlack -> black
                    R.id.radioRed -> red
                    R.id.radioBlue -> blue
                    else -> black
                }
                drawView.setStrokeColor(color)
                updateColorSeekBar(color)
            }

            seekBarWidth.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        progress: Int,
                        fromUser: Boolean
                    ) {
                        val strokeWidth = progress.coerceAtLeast(2).toFloat()
                        drawView.setStrokeWidth(strokeWidth)

                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                }
            )

            btnUndo.setOnClickListener {
                drawView.undo()
            }

            btnRedo.setOnClickListener {
                drawView.redo()
            }

            btnClear.setOnClickListener {
                drawView.clearAll()
            }

            btnSave.setOnClickListener {
                saveDrawingToGallery()
            }
        }
    }

    private fun updateColorSeekBar(color: Int) {
        val colorFilter = BlendModeColorFilter(color, BlendMode.SRC_IN)
        binding.apply {
            seekBarWidth.progressDrawable.colorFilter = colorFilter
            seekBarWidth.thumb.colorFilter = colorFilter
        }
    }

    private fun saveDrawingToGallery() {
        val bitmap = binding.drawView.exportToBitmap()

        val filename = "drawing_${System.currentTimeMillis()}.png"
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyDrawings")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val resolver = contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            resolver.openOutputStream(it)?.use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(it, contentValues, null, null)
            Toast.makeText(this, "Saved to Gallery!", Toast.LENGTH_SHORT).show()
        }
    }

}