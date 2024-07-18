package com.openkin.automaticwatering.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.bt.ui.DeviceListFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.openkin.automaticwatering.R
import com.openkin.automaticwatering.databinding.FragmentMainBinding
import com.openkin.automaticwatering.utils.ControllerCommand.WATERING_ON
import com.openkin.automaticwatering.utils.ControllerCommand.WATERING_OFF

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private var wateringInProgress = false
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
        setSettingsOnClickListener()
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

    private fun setSettingsOnClickListener() {
        binding.settingsButton.setOnClickListener { openSettings() }
        binding.wateringButton.setOnClickListener { switchWatering() }
    }

    private fun switchWatering() {
        if (wateringInProgress) {
            viewModel.sendCommand(WATERING_ON)
            binding.wateringButton.setBackgroundColor( //не работает
                ContextCompat.getColor(requireActivity(), R.color.red)
            )
            binding.wateringButton.setText(R.string.stop_watering)
        } else {
            viewModel.sendCommand(WATERING_OFF)
            binding.wateringButton.setBackgroundColor(
                ContextCompat.getColor(requireActivity(), R.color.green)
            )
            binding.wateringButton.setText(R.string.start_watering)
        }
        wateringInProgress = !wateringInProgress
    }

    private fun openSettings() {
        activity
            ?.supportFragmentManager
            ?.beginTransaction()
            ?.addToBackStack(null)
            ?.add(R.id.main_container, DeviceListFragment.createFragment())
            ?.commit()
    }

    companion object {
        @JvmStatic
        fun createFragment(extras: Bundle? = null) = MainFragment().apply {
            arguments = extras ?: Bundle()
        }
    }
}
