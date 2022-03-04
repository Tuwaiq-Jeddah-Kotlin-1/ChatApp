package com.myprojects.chatapp.fragments

import android.icu.util.UniversalTimeScale
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.myprojects.chatapp.R
import com.myprojects.chatapp.utils.Utils


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        (activity as AppCompatActivity).supportActionBar?.title =
            getString(R.string.settings_fragment)

        Utils.hideBottomNav(this)
    }

}