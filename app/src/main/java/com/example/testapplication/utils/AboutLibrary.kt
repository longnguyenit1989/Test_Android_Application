package com.example.testapplication.utils

import com.mikepenz.aboutlibraries.entity.Library
import com.mikepenz.aboutlibraries.entity.License

internal val Library.license: License?
  get() = licenses.firstOrNull()

internal val License.htmlReadyLicenseContent: String?
  get() = licenseContent?.replace("\n", "<br />")
