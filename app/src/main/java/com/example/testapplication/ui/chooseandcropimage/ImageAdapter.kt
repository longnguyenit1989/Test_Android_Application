import android.content.Context
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ImageAdapter(
    private val context: Context,
    private val images: List<Uri>,
    private val onItemClick: (position: Int) -> Unit
) : BaseAdapter() {

    override fun getCount() = images.size
    override fun getItem(position: Int) = images[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView = convertView as? ImageView ?: ImageView(context).apply {
            layoutParams = ViewGroup.LayoutParams(300, 300)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }

        Glide.with(context)
            .load(images[position])
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(imageView)

        imageView.setOnClickListener { onItemClick(position) }
        return imageView
    }
}
