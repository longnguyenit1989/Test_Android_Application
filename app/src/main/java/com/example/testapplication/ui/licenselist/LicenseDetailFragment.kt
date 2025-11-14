package com.example.testapplication.ui.licenselist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import com.example.testapplication.base.BaseFragment
import com.example.testapplication.databinding.FragmentLicenseDetailBinding

class LicenseDetailFragment : BaseFragment<FragmentLicenseDetailBinding>() {

    companion object {
        fun newInstance(bundle: Bundle): LicenseDetailFragment {
            val fragment = LicenseDetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLicenseDetailBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private fun initData() {
        val bundle = requireArguments()
        val name = bundle.getString("lib_name") ?: ""
        val license = bundle.getString("lib_license") ?: ""

        binding.tvLibName.text = name
        binding.tvDesc.text = HtmlCompat.fromHtml(
            license,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }
}


