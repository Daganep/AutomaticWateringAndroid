package com.openkin.automaticwatering.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.openkin.automaticwatering.databinding.FragmentSetupBinding

class SetupFragment : Fragment() {

    private lateinit var binding: FragmentSetupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetupBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun createFragment(extras: Bundle? = null) = SetupFragment().apply {
            arguments = extras ?: Bundle()
        }
    }
}