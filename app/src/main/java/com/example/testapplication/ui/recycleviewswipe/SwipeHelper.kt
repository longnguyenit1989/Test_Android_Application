import android.animation.ObjectAnimator
import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.ui.recycleviewswipe.RecycleViewSwipeAdapter

class SwipeHelper : ItemTouchHelper.Callback() {

    private val swipeStates = mutableMapOf<Int, Float>()

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(0, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val holder = viewHolder as RecycleViewSwipeAdapter.SwipeViewHolder
        val foregroundView = holder.binding.viewForeground
        val deleteButton = holder.binding.btnDelete
        val density = foregroundView.context.resources.displayMetrics.density
        val marginPx = 32 * density
        val halfButtonWidth = deleteButton.width / 2f

        val previousTranslation = swipeStates[holder.layoutPosition] ?: 0f

        if (isCurrentlyActive) {
            val newTranslation = (previousTranslation + dX).coerceAtMost(0f)
            foregroundView.translationX = newTranslation
        } else {
            val currentTranslation = foregroundView.translationX
            val targetTranslation = if (currentTranslation < -halfButtonWidth) {
                -deleteButton.width - marginPx
            } else {
                0f
            }

            ObjectAnimator.ofFloat(foregroundView, "translationX", currentTranslation, targetTranslation)
                .setDuration(150)
                .start()

            swipeStates[holder.layoutPosition] = targetTranslation
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        val holder = viewHolder as RecycleViewSwipeAdapter.SwipeViewHolder
        val foregroundView = holder.binding.viewForeground
        val deleteButton = holder.binding.btnDelete
        val density = foregroundView.context.resources.displayMetrics.density
        val marginPx = 32 * density
        val halfButtonWidth = deleteButton.width / 2f

        val targetTranslation = if (foregroundView.translationX < -halfButtonWidth) {
            -deleteButton.width - marginPx
        } else {
            0f
        }

        ObjectAnimator.ofFloat(foregroundView, "translationX", foregroundView.translationX, targetTranslation)
            .setDuration(150)
            .start()

        swipeStates[holder.layoutPosition] = targetTranslation
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder) = Float.MAX_VALUE
    override fun getSwipeEscapeVelocity(defaultValue: Float) = Float.MAX_VALUE
    override fun getSwipeVelocityThreshold(defaultValue: Float) = Float.MAX_VALUE
}

