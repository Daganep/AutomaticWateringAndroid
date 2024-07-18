package com.openkin.automaticwatering.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bt.bluetooth.BluetoothController
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel(), BluetoothController.Listener {

    private lateinit var bluetoothController: BluetoothController
    private val subscriptions = CompositeDisposable()
    private val _controllerState: MutableLiveData<ControllerState> = MutableLiveData()
    val controllerState: LiveData<ControllerState> get() = _controllerState

    fun connectToDevice(bluetoothController: BluetoothController, macAddress: String) {
        this.bluetoothController = bluetoothController
        bluetoothController.connect(macAddress, this)
    }

    fun parseReceivedMessage(message: String) {
        _controllerState.value = ReceivedMessageState(message)
    }

    override fun onConnected() {
        subscriptions.add(
            Single.just("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ _controllerState.value = ConnectedState }, { })
        )
    }

    override fun onDisconnected() {
        _controllerState.value = DisconnectedState
    }

    override fun onError(error: String) {
        subscriptions.add(
            Single.just(error)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ _controllerState.value = ErrorState(error) }, { })
        )
    }

    override fun onMessageReceived(message: String) {
        parseReceivedMessage(message)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}
