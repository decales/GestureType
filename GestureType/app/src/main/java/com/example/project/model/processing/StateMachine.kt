package com.example.project.model.processing

import android.os.Build
import android.view.KeyEvent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly


@RequiresApi(Build.VERSION_CODES.S)
class StateMachine(
    private val transmissionClient: TransmissionClient
) {
    enum class StateMachineMode { COMMAND, INSERT }
    enum class StateMachineAction { DEFAULT, SELECT }

    var mode: StateMachineMode by mutableStateOf(StateMachineMode.INSERT)
    private var action: StateMachineAction by mutableStateOf(StateMachineAction.DEFAULT)
    private var modifier: Int by mutableIntStateOf(1);
    var command: String by mutableStateOf("")

    fun exec(key: Int, controlDown: Boolean, shiftDown: Boolean, modifier: Int, description: String) {
        transmissionClient.sendKeys(key, controlDown, shiftDown, modifier)
        command = description
    }

    fun input(input: String) {
        when (mode) {
            StateMachineMode.INSERT -> {
                when(input) {
                    "DoubleTap" -> mode = StateMachineMode.COMMAND
                    "OneSwipeLeft" -> exec(KeyEvent.KEYCODE_DEL, false, false, 1, "Delete character")
                    "OneSwipeRight" -> exec(KeyEvent.KEYCODE_SPACE, false, false, 1, "Insert space")
                    "TwoSwipeLeft" -> exec(KeyEvent.KEYCODE_COMMA, false, false, 1, "Insert comma")
                    "TwoSwipeRight" -> exec(KeyEvent.KEYCODE_PERIOD, false, false, 1, "Insert period")
                    else -> {
                        val mapping: Pair<Int, Boolean>? = charKeycodeMap[input]
                        if (mapping != null)  {
                            exec(mapping.first, false, mapping.second, 1, "Insert '$input'")
                        }
                    }
                }
            }
            StateMachineMode.COMMAND -> {
                if ((input.isDigitsOnly())) modifier = input.toInt()
                else {
                    if (action == StateMachineAction.SELECT) {
                        when(input) {
                            "DoubleTap" -> mode = StateMachineMode.INSERT
                            "OneSwipeLeft" -> exec(KeyEvent.KEYCODE_DPAD_LEFT, false, true, modifier, "Select $modifier character(s) to left")
                            "OneSwipeRight" -> exec(KeyEvent.KEYCODE_DPAD_RIGHT, false, true, modifier, "Select $modifier character(s) to right")
                            "TwoSwipeLeft" -> exec(KeyEvent.KEYCODE_DPAD_LEFT, true, true, modifier, "Select $modifier word(s) to left")
                            "TwoSwipeRight" -> exec(KeyEvent.KEYCODE_DPAD_RIGHT, true, true, modifier, "Select $modifier word(s) to right")
                        }
                        action = StateMachineAction.DEFAULT
                    }
                    else {
                        when(input) {
                            "DoubleTap" -> mode = StateMachineMode.INSERT
                            "S" -> action = StateMachineAction.SELECT
                            "C" -> exec(KeyEvent.KEYCODE_C, true, false, 1, "Copy selection")
                            "X" -> exec(KeyEvent.KEYCODE_X, true, false, 1, "Cut/delete selection")
                            "V" -> exec(KeyEvent.KEYCODE_V, true, false, 1, "Paste selection")
                            "OneSwipeLeft" -> exec(KeyEvent.KEYCODE_DPAD_LEFT, false, false, modifier, "Move cursor $modifier character(s) to left")
                            "OneSwipeRight" -> exec(KeyEvent.KEYCODE_DPAD_RIGHT, false, false, modifier, "Move cursor $modifier character(s) to right")
                            "TwoSwipeLeft" -> exec(KeyEvent.KEYCODE_DPAD_LEFT, true, false, modifier, "Move cursor $modifier word(s) to left")
                            "TwoSwipeRight" -> exec(KeyEvent.KEYCODE_DPAD_RIGHT, true, false, modifier, "Move cursor $modifier word(s) to right")
                        }
                        if (action != StateMachineAction.SELECT) modifier = 1
                    }
                }
            }
        }
    }

    private val charKeycodeMap: Map<String, Pair<Int, Boolean>> = mapOf(
        "a" to (KeyEvent.KEYCODE_A to false),
        "b" to (KeyEvent.KEYCODE_B to false),
        "c" to (KeyEvent.KEYCODE_C to false),
        "d" to (KeyEvent.KEYCODE_D to false),
        "e" to (KeyEvent.KEYCODE_E to false),
        "f" to (KeyEvent.KEYCODE_F to false),
        "g" to (KeyEvent.KEYCODE_G to false),
        "h" to (KeyEvent.KEYCODE_H to false),
        "i" to (KeyEvent.KEYCODE_I to false),
        "j" to (KeyEvent.KEYCODE_J to false),
        "k" to (KeyEvent.KEYCODE_K to false),
        "l" to (KeyEvent.KEYCODE_L to false),
        "m" to (KeyEvent.KEYCODE_M to false),
        "n" to (KeyEvent.KEYCODE_N to false),
        "o" to (KeyEvent.KEYCODE_O to false),
        "p" to (KeyEvent.KEYCODE_P to false),
        "q" to (KeyEvent.KEYCODE_Q to false),
        "r" to (KeyEvent.KEYCODE_R to false),
        "s" to (KeyEvent.KEYCODE_S to false),
        "t" to (KeyEvent.KEYCODE_T to false),
        "u" to (KeyEvent.KEYCODE_U to false),
        "v" to (KeyEvent.KEYCODE_V to false),
        "w" to (KeyEvent.KEYCODE_W to false),
        "x" to (KeyEvent.KEYCODE_X to false),
        "y" to (KeyEvent.KEYCODE_Y to false),
        "z" to (KeyEvent.KEYCODE_Z to false),

        "A" to (KeyEvent.KEYCODE_A to true),
        "B" to (KeyEvent.KEYCODE_B to true),
        "C" to (KeyEvent.KEYCODE_C to true),
        "D" to (KeyEvent.KEYCODE_D to true),
        "E" to (KeyEvent.KEYCODE_E to true),
        "F" to (KeyEvent.KEYCODE_F to true),
        "G" to (KeyEvent.KEYCODE_G to true),
        "H" to (KeyEvent.KEYCODE_H to true),
        "I" to (KeyEvent.KEYCODE_I to true),
        "J" to (KeyEvent.KEYCODE_J to true),
        "K" to (KeyEvent.KEYCODE_K to true),
        "L" to (KeyEvent.KEYCODE_L to true),
        "M" to (KeyEvent.KEYCODE_M to true),
        "N" to (KeyEvent.KEYCODE_N to true),
        "O" to (KeyEvent.KEYCODE_O to true),
        "P" to (KeyEvent.KEYCODE_P to true),
        "Q" to (KeyEvent.KEYCODE_Q to true),
        "R" to (KeyEvent.KEYCODE_R to true),
        "S" to (KeyEvent.KEYCODE_S to true),
        "T" to (KeyEvent.KEYCODE_T to true),
        "U" to (KeyEvent.KEYCODE_U to true),
        "V" to (KeyEvent.KEYCODE_V to true),
        "W" to (KeyEvent.KEYCODE_W to true),
        "X" to (KeyEvent.KEYCODE_X to true),
        "Y" to (KeyEvent.KEYCODE_Y to true),
        "Z" to (KeyEvent.KEYCODE_Z to true),

        "0" to (KeyEvent.KEYCODE_0 to false),
        "1" to (KeyEvent.KEYCODE_1 to false),
        "2" to (KeyEvent.KEYCODE_2 to false),
        "3" to (KeyEvent.KEYCODE_3 to false),
        "4" to (KeyEvent.KEYCODE_4 to false),
        "5" to (KeyEvent.KEYCODE_5 to false),
        "6" to (KeyEvent.KEYCODE_6 to false),
        "7" to (KeyEvent.KEYCODE_7 to false),
        "8" to (KeyEvent.KEYCODE_8 to false),
        "9" to (KeyEvent.KEYCODE_9 to false),

        "`" to (KeyEvent.KEYCODE_GRAVE to false),
        "-" to (KeyEvent.KEYCODE_MINUS to false),
        "=" to (KeyEvent.KEYCODE_EQUALS to false),
        "[" to (KeyEvent.KEYCODE_LEFT_BRACKET to false),
        "]" to (KeyEvent.KEYCODE_RIGHT_BRACKET to false),
        "\\" to (KeyEvent.KEYCODE_BACKSLASH to false),
        ";" to (KeyEvent.KEYCODE_SEMICOLON to false),
        "'" to (KeyEvent.KEYCODE_APOSTROPHE to false),
        "," to (KeyEvent.KEYCODE_COMMA to false),
        "." to (KeyEvent.KEYCODE_PERIOD to false),
        "/" to (KeyEvent.KEYCODE_SLASH to false),

        "!" to (KeyEvent.KEYCODE_1 to true),
        "@" to (KeyEvent.KEYCODE_2 to true),
        "#" to (KeyEvent.KEYCODE_3 to true),
        "$" to (KeyEvent.KEYCODE_4 to true),
        "%" to (KeyEvent.KEYCODE_5 to true),
        "^" to (KeyEvent.KEYCODE_6 to true),
        "&" to (KeyEvent.KEYCODE_7 to true),
        "*" to (KeyEvent.KEYCODE_8 to true),
        "(" to (KeyEvent.KEYCODE_9 to true),
        ")" to (KeyEvent.KEYCODE_0 to true),
        "_" to (KeyEvent.KEYCODE_MINUS to true),
        "+" to (KeyEvent.KEYCODE_EQUALS to true),
        "{" to (KeyEvent.KEYCODE_LEFT_BRACKET to true),
        "}" to (KeyEvent.KEYCODE_RIGHT_BRACKET to true),
        "|" to (KeyEvent.KEYCODE_BACKSLASH to true),
        ":" to (KeyEvent.KEYCODE_SEMICOLON to true),
        "\"" to (KeyEvent.KEYCODE_APOSTROPHE to true),
        "<" to (KeyEvent.KEYCODE_COMMA to true),
        ">" to (KeyEvent.KEYCODE_PERIOD to true),
        "?" to (KeyEvent.KEYCODE_SLASH to true),
        "~" to (KeyEvent.KEYCODE_GRAVE to true)
    )
}


