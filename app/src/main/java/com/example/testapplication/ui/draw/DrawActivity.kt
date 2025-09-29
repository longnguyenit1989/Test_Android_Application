package com.example.testapplication.ui.draw

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.SeekBar
import android.widget.Toast
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
            radioGroupColors.setOnCheckedChangeListener { _, checkedId ->
                val color = when (checkedId) {
                    R.id.radioBlack -> Color.BLACK
                    R.id.radioRed -> Color.RED
                    R.id.radioBlue -> Color.BLUE
                    else -> Color.BLACK
                }
                drawView.setStrokeColor(color)
                updateSeekBarHeight(seekBarWidth, seekBarWidth.progress, color)
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

                        if (seekBar != null) {
                            val color = drawView.getStrokeColor()
                            updateSeekBarHeight(seekBar, strokeWidth.toInt(), color)
                        }
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

    private fun updateSeekBarHeight(seekBar: SeekBar, strokeWidth: Int, progressColor: Int) {
        val barHeight = (strokeWidth / 2).coerceAtLeast(2)

        val backgroundShape = ShapeDrawable(RectShape()).apply {
            paint.style = Paint.Style.FILL
            paint.color = Color.LTGRAY
            intrinsicHeight = barHeight
        }

        val progressShape = ShapeDrawable(RectShape()).apply {
            paint.style = Paint.Style.FILL
            paint.color = progressColor
            intrinsicHeight = barHeight
        }

        val progressClip = ClipDrawable(progressShape, Gravity.START, ClipDrawable.HORIZONTAL)

        val layers = arrayOf<Drawable>(backgroundShape, progressClip)
        val layerDrawable = LayerDrawable(layers).apply {
            setId(0, android.R.id.background)
            setId(1, android.R.id.progress)
        }

        seekBar.progressDrawable = layerDrawable

        val params = seekBar.layoutParams
        params.height = barHeight * 3
        seekBar.layoutParams = params
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