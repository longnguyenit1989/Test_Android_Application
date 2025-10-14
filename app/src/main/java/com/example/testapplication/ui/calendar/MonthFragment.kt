package com.example.testapplication.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testapplication.R
import com.example.testapplication.base.BaseFragment
import com.example.testapplication.databinding.FragmentMonthBinding

class MonthFragment : BaseFragment<FragmentMonthBinding>() {

    companion object {
        private const val KEY_YEAR = "year"
        private const val KEY_MONTH = "month"

        fun newInstance(year: Int, month: Int) = MonthFragment().apply {
            arguments = bundleOf(KEY_YEAR to year, KEY_MONTH to month)
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMonthBinding {
        return FragmentMonthBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val year = requireArguments().getInt(KEY_YEAR)
        val month = requireArguments().getInt(KEY_MONTH)

        val recycler = binding.recyclerMonth
        recycler.layoutManager = GridLayoutManager(requireContext(), 7)
        recycler.adapter = MonthAdapter(year, month)

        recycler.addItemDecoration(GridDividerDecoration(R.color.grey, 1))
    }

}



