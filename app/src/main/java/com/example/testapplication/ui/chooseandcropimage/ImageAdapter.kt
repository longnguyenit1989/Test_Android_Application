import android.content.Context
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.testapplication.extension.beGone
import com.example.testapplication.extension.beVisible

class ImageAdapter(
    private val context: Context,
    private val images: List<Uri>,
    private val isSelectionMode: Boolean,
    private val selectedItems: MutableSet<Uri>,
    private val onItemClick: (position: Int) -> Unit,
    private val onItemLongClick: (position: Int, uri: Uri) -> Unit
) : BaseAdapter() {

    override fun getCount() = images.size
    override fun getItem(position: Int) = images[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageUri = images[position]

        val container = (convertView as? FrameLayout) ?: FrameLayout(context).apply {
            layoutParams = AbsListView.LayoutParams(300, 300)
        }

        val imageView = (container.findViewWithTag<ImageView>("imageView")
            ?: ImageView(context).apply {
                tag = "imageView"
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                scaleType = ImageView.ScaleType.CENTER_CROP
                container.addView(this)
            })

        val overlay = (container.findViewWithTag("overlay")
            ?: ImageView(context).apply {
                tag = "overlay"
                val sizePx = (25 * context.resources.displayMetrics.density).toInt()
                val margin = 12
                val params = FrameLayout.LayoutParams(sizePx, sizePx, Gravity.TOP or Gravity.END)
                params.setMargins(margin, margin, margin, margin)
                layoutParams = params
                visibility = View.GONE
                container.addView(this)
            })

        Glide.with(context)
            .load(imageUri)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(imageView)

        if (isSelectionMode) {
            overlay.beVisible()
            val isSelected = selectedItems.contains(imageUri)
            overlay.setImageResource(
                if (isSelected) android.R.drawable.checkbox_on_background
                else android.R.drawable.checkbox_off_background
            )

            if (isSelected) {
                imageView.scaleX = 0.9f
                imageView.scaleY = 0.9f
                overlay.scaleX = 1.3f
                overlay.scaleY = 1.3f
            } else {
                imageView.scaleX = 1f
                imageView.scaleY = 1f
                overlay.scaleX = 1f
                overlay.scaleY = 1f
            }
        } else {
            overlay.beGone()
            imageView.scaleX = 1f
            imageView.scaleY = 1f
            overlay.scaleX = 1f
            overlay.scaleY = 1f
        }

        container.setOnClickListener {
            if (isSelectionMode) {
                toggleSelection(imageUri, overlay, imageView)
            } else {
                onItemClick(position)
            }
        }

        container.setOnLongClickListener {
            onItemLongClick(position, imageUri)
            true
        }

        return container
    }

    private fun toggleSelection(uri: Uri, overlay: ImageView, imageView: ImageView) {
        val isSelected = selectedItems.contains(uri)

        if (isSelected) {
            selectedItems.remove(uri)
            overlay.setImageResource(android.R.drawable.checkbox_off_background)

            overlay.animate().scaleX(1f).scaleY(1f).setDuration(120).start()
            imageView.animate().scaleX(1f).scaleY(1f).setDuration(150).start()
        } else {
            selectedItems.add(uri)
            overlay.setImageResource(android.R.drawable.checkbox_on_background)

            overlay.scaleX = 1f
            overlay.scaleY = 1f
            overlay.animate().scaleX(1.3f).scaleY(1.3f).setDuration(150).start()
            imageView.animate().scaleX(0.9f).scaleY(0.9f).setDuration(150).start()
        }
    }
}
