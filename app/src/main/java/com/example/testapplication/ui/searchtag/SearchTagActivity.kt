package com.example.testapplication.ui.searchtag

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.SearchView
import android.widget.Toast
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivitySearchTagBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class SearchTagActivity : BaseActivity<ActivitySearchTagBinding>() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SearchTagActivity::class.java)
        }
    }

    private val tagList = mutableListOf(
        "Kotlin", "Android", "Java", "Flutter", "Compose", "Jetpack", "RecyclerView", "Search"
    )
    private val selectedTags = mutableSetOf<String>()

    override fun inflateBinding(inflater: LayoutInflater): ActivitySearchTagBinding {
        return ActivitySearchTagBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        binding.apply {
            displayTags(tagList, chipGroupTags)

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    val text = query?.trim()
                    if (!text.isNullOrEmpty()) {
                        if (!tagList.contains(text)) {
                            tagList.add(text)
                            Toast.makeText(this@SearchTagActivity, "Added tag: $text", Toast.LENGTH_SHORT).show()
                        }
                        displayTags(tagList, chipGroupTags)
                        binding.searchView.setQuery("", false)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val filtered = tagList.filter { it.contains(newText ?: "", ignoreCase = true) }
                    displayTags(filtered, chipGroupTags)
                    return true
                }
            })

            searchView.apply {
                isIconified = false
                requestFocus()
            }
        }
    }

    private fun displayTags(tags: List<String>, chipGroup: ChipGroup) {
        chipGroup.removeAllViews()
        for (tag in tags) {
            val chip = Chip(this)
            chip.text = tag
            chip.isCheckable = true
            chip.isChecked = selectedTags.contains(tag)
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedTags.add(tag)
                } else {
                    selectedTags.remove(tag)
                }
            }
            chipGroup.addView(chip)
        }
    }
}
