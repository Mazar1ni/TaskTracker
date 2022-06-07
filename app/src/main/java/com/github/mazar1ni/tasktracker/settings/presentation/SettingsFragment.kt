package com.github.mazar1ni.tasktracker.settings.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mazar1ni.tasktracker.R
import com.github.mazar1ni.tasktracker.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)
        binding.errorText.setText(R.string.screen_does_not_implement_yet)
        return binding.root
    }
}