package com.openkin.automaticwatering.ui

sealed interface ControllerState

object ConnectedState: ControllerState

object DisconnectedState: ControllerState

data class ReceivedMessageState(val message: String): ControllerState

data class ErrorState(val error: String): ControllerState
