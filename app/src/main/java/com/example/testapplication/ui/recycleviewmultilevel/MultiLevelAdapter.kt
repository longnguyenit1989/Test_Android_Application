import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.databinding.ItemClassBinding
import com.example.testapplication.databinding.ItemSchoolBinding
import com.example.testapplication.databinding.ItemTeacherBinding
import com.example.testapplication.extension.beGone
import com.example.testapplication.extension.beVisible
import com.example.testapplication.model.ClassRoom
import com.example.testapplication.model.School
import com.example.testapplication.model.Teacher

class MultiLevelAdapter(private val schools: List<School>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_SCHOOL = 0
        private const val TYPE_CLASS = 1
        private const val TYPE_TEACHER = 2
    }

    private val displayList = mutableListOf<Any>().apply {
        addAll(schools)
    }

    override fun getItemViewType(position: Int): Int = when (displayList[position]) {
        is School -> TYPE_SCHOOL
        is ClassRoom -> TYPE_CLASS
        is Teacher -> TYPE_TEACHER
        else -> error("Invalid item type at position $position")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_SCHOOL -> SchoolViewHolder(ItemSchoolBinding.inflate(inflater, parent, false))
            TYPE_CLASS -> ClassViewHolder(ItemClassBinding.inflate(inflater, parent, false))
            TYPE_TEACHER -> TeacherViewHolder(ItemTeacherBinding.inflate(inflater, parent, false))
            else -> error("Invalid viewType $viewType")
        }
    }

    override fun getItemCount(): Int = displayList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = displayList[position]) {
            is School -> (holder as SchoolViewHolder).bind(item)
            is ClassRoom -> (holder as ClassViewHolder).bind(item)
            is Teacher -> (holder as TeacherViewHolder).bind(item)
        }
    }

    inner class SchoolViewHolder(private val binding: ItemSchoolBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(school: School) = with(binding) {
            tvSchool.text = school.name
            ivArrow.rotation = if (school.isExpanded) 180f else 0f

            root.setOnClickListener {
                toggleExpand(school, adapterPosition)
                ivArrow.animate().rotation(if (school.isExpanded) 180f else 0f).setDuration(200).start()
            }
        }
    }

    inner class ClassViewHolder(private val binding: ItemClassBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(classRoom: ClassRoom) = with(binding) {
            tvClass.text = classRoom.name
            ivArrow.apply {
                visibility = if (classRoom.teachers.isNotEmpty()) View.VISIBLE else View.GONE
                rotation = if (classRoom.isExpanded) 180f else 0f
            }

            root.setOnClickListener {
                toggleExpand(classRoom, layoutPosition)
                ivArrow.animate().rotation(if (classRoom.isExpanded) 180f else 0f).setDuration(200).start()
            }
        }
    }

    inner class TeacherViewHolder(private val binding: ItemTeacherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(teacher: Teacher) = with(binding) {
            tvTeacher.text = teacher.name
        }
    }

    private fun toggleExpand(item: Any, position: Int) {
        when (item) {
            is School -> {
                if (item.isExpanded) collapseSchool(item, position)
                else expandSchool(item, position)
            }
            is ClassRoom -> {
                if (item.isExpanded) collapseClass(item, position)
                else expandClass(item, position)
            }
        }
    }

    private fun expandSchool(school: School, pos: Int) {
        school.isExpanded = true
        displayList.addAll(pos + 1, school.classes)
        notifyItemRangeInserted(pos + 1, school.classes.size)
    }

    private fun collapseSchool(school: School, pos: Int) {
        school.isExpanded = false

        val toRemove = buildList {
            for (c in school.classes) {
                add(c)
                if (c.isExpanded) addAll(c.teachers)
                c.isExpanded = false
            }
        }

        displayList.removeAll(toRemove)
        notifyItemRangeRemoved(pos + 1, toRemove.size)
    }

    private fun expandClass(classRoom: ClassRoom, pos: Int) {
        classRoom.isExpanded = true
        displayList.addAll(pos + 1, classRoom.teachers)
        notifyItemRangeInserted(pos + 1, classRoom.teachers.size)
    }

    private fun collapseClass(classRoom: ClassRoom, pos: Int) {
        classRoom.isExpanded = false
        displayList.subList(pos + 1, pos + 1 + classRoom.teachers.size).clear()
        notifyItemRangeRemoved(pos + 1, classRoom.teachers.size)
    }
}


