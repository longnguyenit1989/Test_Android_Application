package com.example.testapplication.ui.licenselist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.databinding.ItemLibLicenseBinding
import com.example.testapplication.utils.htmlReadyLicenseContent
import com.mikepenz.aboutlibraries.entity.Library

class LicenseListAdapter(
    private val listener: (Bundle) -> Unit
) : ListAdapter<Library, LicenseListAdapter.ViewHolder>(DIFF_CALLBACK) {

    private companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Library>() {

            override fun areItemsTheSame(
                oldItem: Library,
                newItem: Library
            ): Boolean {
                return oldItem.uniqueId == newItem.uniqueId
            }

            override fun areContentsTheSame(
                oldItem: Library,
                newItem: Library
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLibLicenseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val libraryItem = getItem(position)
        libraryItem?.let {
            holder.bind(it) { bundle ->
                this@LicenseListAdapter.listener.invoke(bundle)
            }
        }
    }

    class ViewHolder(private val binding: ItemLibLicenseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lib: Library, listener: (Bundle) -> Unit) {
            binding.tvLibName.text = lib.name
            binding.tvLibName.setOnClickListener {
                val licenseContent = lib.licenses.firstOrNull()?.htmlReadyLicenseContent ?: ""
                val bundle = Bundle().apply {
                    putString("lib_name", lib.name)
                    putString("lib_license", licenseContent)
                }
                listener.invoke(bundle)
            }
        }
    }
}
