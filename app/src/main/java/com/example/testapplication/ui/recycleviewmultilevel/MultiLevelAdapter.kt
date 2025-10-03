import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.databinding.ItemClassBinding
import com.example.testapplication.databinding.ItemSchoolBinding
import com.example.testapplication.databinding.ItemTeacherBinding
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

    private val displayList = mutableListOf<Any>()

    init {
        displayList.addAll(schools)
    }

    override fun getItemViewType(position: Int) = when (displayList[position]) {
        is School -> TYPE_SCHOOL
        is ClassRoom -> TYPE_CLASS
        is Teacher -> TYPE_TEACHER
        else -> -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_SCHOOL -> {
                val binding =
                    ItemSchoolBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SchoolViewHolder(binding)
            }

            TYPE_CLASS -> {
                val binding =
                    ItemClassBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ClassViewHolder(binding)
            }

            TYPE_TEACHER -> {
                val binding =
                    ItemTeacherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TeacherViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount() = displayList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = displayList[position]) {
            is School -> (holder as SchoolViewHolder).bind(item)
            is ClassRoom -> (holder as ClassViewHolder).bind(item)
            is Teacher -> (holder as TeacherViewHolder).bind(item)
        }
    }

    inner class SchoolViewHolder(private val binding: ItemSchoolBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(school: School) {
            binding.tvSchool.text = school.name
            binding.ivArrow.rotation = if (school.isExpanded) 180f else 0f

            binding.root.setOnClickListener {
                val pos = displayList.indexOf(school)
                if (school.isExpanded) collapseSchool(school, pos)
                else expandSchool(school, pos)

                val to = if (school.isExpanded) 180f else 0f
                binding.ivArrow.animate().rotation(to).setDuration(300).start()
            }
        }
    }


    inner class ClassViewHolder(private val binding: ItemClassBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(classRoom: ClassRoom) {
            binding.tvClass.text = classRoom.name
            binding.ivArrow.visibility = if (classRoom.teachers.isNotEmpty()) View.VISIBLE else View.GONE
            binding.ivArrow.rotation = if (classRoom.isExpanded) 180f else 0f

            binding.root.setOnClickListener {
                val pos = displayList.indexOf(classRoom)
                if (classRoom.isExpanded) collapseClass(classRoom, pos)
                else expandClass(classRoom, pos)

                val to = if (classRoom.isExpanded) 180f else 0f
                binding.ivArrow.animate().rotation(to).setDuration(300).start()
            }
        }
    }

    inner class TeacherViewHolder(private val binding: ItemTeacherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(teacher: Teacher) {
            binding.tvTeacher.text = teacher.name
        }
    }

    private fun expandSchool(school: School, position: Int) {
        school.isExpanded = true
        displayList.addAll(position + 1, school.classes)
        notifyItemRangeInserted(position + 1, school.classes.size)
    }

    private fun collapseSchool(school: School, position: Int) {
        school.isExpanded = false
        var removeCount = 0
        for (c in school.classes) {
            if (c.isExpanded) {
                removeCount += c.teachers.size
                c.isExpanded = false
            }
            removeCount++
        }
        displayList.subList(position + 1, position + 1 + removeCount).clear()
        notifyItemRangeRemoved(position + 1, removeCount)
    }

    private fun expandClass(classRoom: ClassRoom, position: Int) {
        classRoom.isExpanded = true
        displayList.addAll(position + 1, classRoom.teachers)
        notifyItemRangeInserted(position + 1, classRoom.teachers.size)
    }

    private fun collapseClass(classRoom: ClassRoom, position: Int) {
        classRoom.isExpanded = false
        displayList.subList(position + 1, position + 1 + classRoom.teachers.size).clear()
        notifyItemRangeRemoved(position + 1, classRoom.teachers.size)
    }

}

