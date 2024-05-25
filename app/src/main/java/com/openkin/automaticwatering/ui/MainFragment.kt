package com.openkin.automaticwatering.ui

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bt.bluetooth.BluetoothController
import com.example.bt.ui.DeviceListFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.openkin.automaticwatering.R
import com.openkin.automaticwatering.databinding.FragmentMainBinding
import com.openkin.automaticwatering.utils.Constants
import com.openkin.automaticwatering.utils.Constants.CONNECTED_DEVICE_MAC_ADDRESS

class MainFragment : Fragment(), BluetoothController.Listener {

    private lateinit var binding: FragmentMainBinding
    private lateinit var bluetoothController: BluetoothController
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var sharedPrefs: SharedPreferences? = null

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
        initBluetoothAdapter()
        initBluetoothController()
        initSharedPrefs()
        connectToDevice()
    }

    override fun onConnected() {
        activity?.runOnUiThread {  }
    }

    override fun onDisconnected() {
        activity?.runOnUiThread {  }
    }

    override fun onMessageReceived(message: String) {
        activity?.runOnUiThread {
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onError(error: String) {
        activity?.runOnUiThread {  }
    }

    private fun sendMessage(message: String) {
        bluetoothController.sendMessage(message)
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

    private fun initBluetoothAdapter() {
        val bluetoothService = activity?.getSystemService(Context.BLUETOOTH_SERVICE)
        bluetoothAdapter = (bluetoothService as BluetoothManager).adapter
    }

    private fun initBluetoothController() {
        bluetoothController = BluetoothController(bluetoothAdapter)
    }

    private fun initSharedPrefs() {
        sharedPrefs = activity?.getSharedPreferences(
            Constants.BLUETOOTH_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    private fun connectToDevice() {
        val macAddress = sharedPrefs?.getString(CONNECTED_DEVICE_MAC_ADDRESS, "")
        val isBluetoothOn = bluetoothAdapter.isEnabled
        if(!macAddress.isNullOrEmpty() && isBluetoothOn) {
            bluetoothController.connect(macAddress, this)
        } else {
            //Экран с сообщение о необходимости подключения к устройству
        }
    }

    private fun setSettingsOnClickListener() {
        binding.settingsButton.setOnClickListener {
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.addToBackStack(null)
                ?.add(R.id.main_container, DeviceListFragment.createFragment())
                ?.commit()
        }
    }

    companion object {
        @JvmStatic
        fun createFragment(extras: Bundle? = null) = MainFragment().apply {
            arguments = extras ?: Bundle()
        }
    }
}
