package com.example.tapbot.domain.activityresult

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract

class OverlayPermissionContract(private val context: Context) : ActivityResultContract<Unit, Boolean>() {

    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${context.packageName}"))
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return resultCode == Activity.RESULT_OK || Settings.canDrawOverlays(context)
    }
}