package com.openkin.automaticwatering.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.openkin.automaticwatering.R
import com.openkin.automaticwatering.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        initViewPager()
    }

    private fun initViewPager() {
        activity?.let {
            val tabs = listOf(
                StatusFragment.createFragment(),
                SetupFragment.createFragment()
            )
            val tabTitles = listOf(
                getString(R.string.tab_status_title),
                getString(R.string.tab_mode_title)
            )

            binding.viewPager.adapter = ViewPagerAdapter(it, tabs)
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = tabTitles[position]
            }.attach()
        }
    }

    companion object {
        @JvmStatic
        fun createFragment(extras: Bundle? = null) = MainFragment().apply {
            arguments = extras ?: Bundle()
        }
    }
}
