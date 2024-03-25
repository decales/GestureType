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
    var mode: StateMachineMode by mutableStateOf(StateMachineMode.INSERT)
    private var action: StateMachineAction by mutableStateOf(StateMachineAction.DEFAULT)
    private var modifier: Int by mutableIntStateOf(1);
    var command: String by mutableStateOf("")

    fun execute(modifier: Int, keys: List<Int>, description: String) {
        val modifiedKeys = mutableListOf<Int>()
        repeat(modifier) {modifiedKeys.addAll(keys)}
        bluetoothClient.send(modifiedKeys)
        command = description
    }

    fun input(input: String) {
        when (mode) {
            StateMachineMode.INSERT -> {
                when(input) {
                    "DoubleTap" -> mode = StateMachineMode.COMMAND
                    "TwoSwipeLeft" -> execute(modifier, listOf(), "Delete character")
                    else -> if (input.length == 1) execute(1, listOf(), "Insert '$input'")
                }
            }
            StateMachineMode.COMMAND -> {
                if ((input.isDigitsOnly())) modifier = input.toInt()
                else {
                    if (action == StateMachineAction.SELECT) {
                        when(input) {
                            "DoubleTap" -> mode = StateMachineMode.INSERT
                            "OneSwipeLeft" -> execute(modifier, listOf(), "Select $modifier character(s) to left")
                            "OneSwipeRight" -> execute(modifier, listOf(), "Select $modifier character(s) to right")
                            "TwoSwipeLeft" -> execute(modifier, listOf(), "Select $modifier word(s) to left")
                            "TwoSwipeRight" -> execute(modifier, listOf(), "Select $modifier word(s) to right")
                        }
                        action = StateMachineAction.DEFAULT
                    }
                    else {
                        when(input) {
                            "DoubleTap" -> mode = StateMachineMode.INSERT
                            "S" -> action = StateMachineAction.SELECT
                            "C" -> execute(1, listOf(), "Copy selection")
                            "X" -> execute(1, listOf(), "Cut/delete selection")
                            "V" -> execute(1, listOf(), "Paste selection")
                            "OneSwipeLeft" -> execute(modifier, listOf(), "Move cursor $modifier character(s) to left")
                            "OneSwipeRight" -> execute(modifier, listOf(), "Move cursor $modifier character(s) to right")
                            "TwoSwipeLeft" -> execute(modifier, listOf(), "Move cursor $modifier word(s) to left")
                            "TwoSwipeRight" -> execute(modifier, listOf(), "Move cursor $modifier word(s) to right")
                        }
                    }
                    if (action != StateMachineAction.SELECT) modifier = 1
                }
            }
        }
    }
}


