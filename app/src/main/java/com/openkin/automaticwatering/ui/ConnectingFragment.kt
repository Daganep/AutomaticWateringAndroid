package com.openkin.automaticwatering.ui

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.example.bt.bluetooth.BluetoothController
import com.google.android.material.snackbar.Snackbar
import com.openkin.automaticwatering.R
import com.openkin.automaticwatering.databinding.FragmentConnectingBinding
import com.openkin.automaticwatering.utils.Constants

class ConnectingFragment : Fragment(), BluetoothController.Listener {

    private val viewModel: MainViewModel by activityViewModels()
    private var sharedPrefs: SharedPreferences? = null
    private lateinit var binding: FragmentConnectingBinding
    private lateinit var bluetoothController: BluetoothController
    private lateinit var bluetoothAdapter: BluetoothAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConnectingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSharedPrefs()
        initUi()
        initBluetoothAdapter()
        initBluetoothController()
        connectToDevice()
    }

    private fun initUi() {
        binding.tryToConnectButton.setOnClickListener {
            tryToConnect()
        }
    }

    private fun initSharedPrefs() {
        sharedPrefs = activity?.getSharedPreferences(
            Constants.BLUETOOTH_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    private fun initBluetoothAdapter() {
        val bluetoothService = activity?.getSystemService(Context.BLUETOOTH_SERVICE)
        bluetoothAdapter = (bluetoothService as BluetoothManager).adapter
    }

    private fun initBluetoothController() {
        bluetoothController = BluetoothController(bluetoothAdapter)
    }

    private fun connectToDevice() {
        val macAddress = sharedPrefs?.getString(Constants.CONNECTED_DEVICE_MAC_ADDRESS, "")
        val isBluetoothOn = bluetoothAdapter.isEnabled
        if(!macAddress.isNullOrEmpty() && isBluetoothOn) {
            bluetoothController.connect(macAddress, this)
        } else {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.addToBackStack(null)
                ?.add(R.id.main_container, SettingsFragment.createFragment())
                ?.commit()
        }
    }

    private fun tryToConnect() {
        val macAddress = sharedPrefs?.getString(Constants.CONNECTED_DEVICE_MAC_ADDRESS, "")
        val isBluetoothOn = bluetoothAdapter.isEnabled
        if(!macAddress.isNullOrEmpty() && isBluetoothOn) {
            bluetoothController.connect(macAddress, this)
            binding.connectingImage.isVisible = true
            binding.connectingLabel.isVisible = true
            binding.notConnectedLabel.isVisible = false
            binding.tryToConnectButton.isVisible = false
        }
    }

    override fun onConnected() {
        activity?.runOnUiThread {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.addToBackStack(null)
                ?.replace(R.id.main_container, MainFragment.createFragment())
                ?.commit()
        }
    }

    override fun onDisconnected() { }

    override fun onError(error: String) {
        activity?.runOnUiThread {
            Snackbar.make(binding.root,
                getString(R.string.connecting_error),
                Snackbar.LENGTH_LONG
            ).show()
            with(binding) {
                connectingImage.isVisible = false
                connectingLabel.isVisible = false
                notConnectedLabel.isVisible = true
                tryToConnectButton.isVisible = true
            }
        }
        Log.e(TAG, error)
    }

    override fun onMessageReceived(message: String) { }

    companion object {
        @JvmStatic
        fun createFragment(extras: Bundle? = null) = ConnectingFragment().apply {
            arguments = extras ?: Bundle()
        }
        private const val TAG = "AutoWatering"
    }
}
