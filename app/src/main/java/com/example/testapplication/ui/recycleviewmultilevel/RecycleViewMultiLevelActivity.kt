package com.example.testapplication.ui.recycleviewmultilevel

import MultiLevelAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityReycleViewMultiLevelBinding
import com.example.testapplication.model.ClassRoom
import com.example.testapplication.model.School
import com.example.testapplication.model.Teacher

class RecycleViewMultiLevelActivity : BaseActivity<ActivityReycleViewMultiLevelBinding>() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, RecycleViewMultiLevelActivity::class.java)
        }
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityReycleViewMultiLevelBinding {
        return ActivityReycleViewMultiLevelBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRecycleViewMultiLevel()
    }

    private fun setupRecycleViewMultiLevel() {
        binding.recyclerViewMultiLevel.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewMultiLevel.adapter = MultiLevelAdapter(getDummyData())
    }

    private fun getDummyData(): MutableList<School> {
        return mutableListOf(
            School(
                "Nguyễn Trường Tộ",
                listOf(
                    ClassRoom(
                        "Lớp 1",
                        listOf(
                            Teacher("Nguyễn Văn A"),
                            Teacher("Nguyễn Văn B"),
                            Teacher("Nguyễn Văn C")
                        )
                    ),
                    ClassRoom(
                        "Lớp 2",
                        listOf(
                            Teacher("Nguyễn Văn D"),
                            Teacher("Nguyễn Văn E"),
                            Teacher("Nguyễn Văn F")
                        )
                    ),
                    ClassRoom(
                        "Lớp 3",
                        listOf(
                            Teacher("Nguyễn Văn G"),
                            Teacher("Nguyễn Văn H"),
                            Teacher("Nguyễn Văn I")
                        )
                    ),
                    ClassRoom(
                        "Lớp 4",
                        listOf(
                            Teacher("Nguyễn Văn J"),
                            Teacher("Nguyễn Văn K"),
                            Teacher("Nguyễn Văn L")
                        )
                    ),
                    ClassRoom(
                        "Lớp 5",
                        listOf(
                            Teacher("Nguyễn Văn M"),
                            Teacher("Nguyễn Văn N"),
                            Teacher("Nguyễn Văn O")
                        )
                    )
                )
            ),
            School(
                "Mạc Đĩnh Chi",
                listOf(
                    ClassRoom(
                        "Lớp 6",
                        listOf(
                            Teacher("Nguyễn Văn P"),
                            Teacher("Nguyễn Văn Q"),
                            Teacher("Nguyễn Văn R")
                        )
                    ),
                    ClassRoom(
                        "Lớp 7",
                        listOf(
                            Teacher("Nguyễn Văn S"),
                            Teacher("Nguyễn Văn T"),
                            Teacher("Nguyễn Văn U")
                        )
                    ),
                    ClassRoom(
                        "Lớp 8",
                        listOf(
                            Teacher("Nguyễn Văn V"),
                            Teacher("Nguyễn Văn W"),
                            Teacher("Nguyễn Văn X")
                        )
                    ),
                    ClassRoom(
                        "Lớp 9",
                        listOf(
                            Teacher("Nguyễn Văn Y"),
                            Teacher("Nguyễn Văn Z"),
                            Teacher("Nguyễn Văn AA")
                        )
                    )
                )
            ),
            School(
                "Phan Đình Phùng",
                listOf(
                    ClassRoom(
                        "Lớp 10",
                        listOf(
                            Teacher("Nguyễn Văn AB"),
                            Teacher("Nguyễn Văn AC"),
                            Teacher("Nguyễn Văn AD")
                        )
                    ),
                    ClassRoom(
                        "Lớp 11",
                        listOf(
                            Teacher("Nguyễn Văn AE"),
                            Teacher("Nguyễn Văn AF"),
                            Teacher("Nguyễn Văn AG")
                        )
                    ),
                    ClassRoom(
                        "Lớp 12",
                        listOf(
                            Teacher("Nguyễn Văn AH"),
                            Teacher("Nguyễn Văn AI"),
                            Teacher("Nguyễn Văn AJ")
                        )
                    )
                )
            )
        )
    }

}