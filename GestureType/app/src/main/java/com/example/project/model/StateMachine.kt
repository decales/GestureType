package com.example.project.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly

enum class StateMachineMode {
    COMMAND, INSERT
}

enum class StateMachineAction {
    DEFAULT, SELECT
}

class StateMachine(
    private val bluetoothClient: BluetoothClient,
) {
    private var mode: StateMachineMode by mutableStateOf(StateMachineMode.INSERT)
    private var action: StateMachineAction by mutableStateOf(StateMachineAction.DEFAULT)
    private var modifier: Int by mutableIntStateOf(1);

    fun getCommand(input: String) {
        when (mode) {
            StateMachineMode.INSERT -> {
                when(input) {
                    "DoubleTap" -> mode = StateMachineMode.COMMAND
                    "TwoSwipeLeft" -> bluetoothClient.send(modifier, "")
                    else -> bluetoothClient.send(1, "")
                }
            }
            StateMachineMode.COMMAND -> {
                if ((input.isDigitsOnly())) modifier = input.codePointAt(0)
                else {
                    if (action == StateMachineAction.SELECT) {
                        when(input) {
                            "DoubleTap" -> mode = StateMachineMode.INSERT
                            "OneSwipeLeft" -> bluetoothClient.send(modifier, "")
                            "OneSwipeRight" -> bluetoothClient.send(modifier, "")
                            "TwoSwipeLeft" -> bluetoothClient.send(modifier, "")
                            "TwoSwipeRight" -> bluetoothClient.send(modifier, "")
                        }
                        action = StateMachineAction.DEFAULT
                    }
                    else {
                        when(input) {
                            "DoubleTap" -> mode = StateMachineMode.INSERT
                            "S" -> action = StateMachineAction.SELECT
                            "C" -> bluetoothClient.send(1, "")
                            "X" -> bluetoothClient.send(1, "")
                            "V" -> bluetoothClient.send(modifier, "")
                            "OneSwipeLeft" -> bluetoothClient.send(modifier, "")
                            "OneSwipeRight" -> bluetoothClient.send(modifier, "")
                            "TwoSwipeLeft" -> bluetoothClient.send(modifier, "")
                            "TwoSwipeRight" -> bluetoothClient.send(modifier, "")
                        }
                    }
                }
            }
        }
    }
}


