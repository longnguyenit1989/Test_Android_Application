package com.example.testapplication.ui.licenselist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityListLibraryBinding
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.util.withContext

class LicenseListActivity : BaseActivity<ActivityListLibraryBinding>() {

    private lateinit var adapter: LicenseListAdapter

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, LicenseListActivity::class.java)
        }
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityListLibraryBinding {
        return ActivityListLibraryBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLibs()
    }

    private fun initLibs() {
        val libs = Libs.Builder().withContext(this@LicenseListActivity).build()
        val uniqueLibs = libs.libraries.distinctBy { it.uniqueId }

        adapter = LicenseListAdapter { bundle ->
            supportFragmentManager
                .beginTransaction()
                .add(
                    android.R.id.content,
                    LicenseDetailFragment.newInstance(bundle),
                    LicenseDetailFragment::class.java.simpleName
                )
                .addToBackStack(LicenseDetailFragment::class.java.simpleName)
                .commitAllowingStateLoss()
        }

        binding.rvLicenseList.adapter = adapter
        adapter.submitList(uniqueLibs)
    }

}
