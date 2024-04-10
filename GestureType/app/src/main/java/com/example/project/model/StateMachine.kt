package com.example.project.model

import android.view.KeyEvent
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
    private val transmissionClient: TransmissionClient
) {
    var mode: StateMachineMode by mutableStateOf(StateMachineMode.INSERT)
    private var action: StateMachineAction by mutableStateOf(StateMachineAction.DEFAULT)
    private var modifier: Int by mutableIntStateOf(1);
    var command: String by mutableStateOf("")

    fun execute(modifier: Int, keys: List<Int>, description: String) {
        val modifiedKeys = mutableListOf<Int>()
        repeat(modifier) { modifiedKeys.addAll(keys) }
        //bluetoothClient.send(modifiedKeys)
        command = description
    }

    fun input(input: String) {
        when (mode) {
            StateMachineMode.INSERT -> {
                when(input) {
                    "DoubleTap" -> mode = StateMachineMode.COMMAND
                    "TwoSwipeLeft" -> execute(modifier, listOf(KeyEvent.KEYCODE_DEL), "Delete character")
                    else -> execute(1, listOf(KeyEvent.KEYCODE_A), "Insert '$input'")
                }
            }
            StateMachineMode.COMMAND -> {
                if ((input.isDigitsOnly())) modifier = input.toInt()
                else {
                    if (action == StateMachineAction.SELECT) {
                        when(input) {
                            "DoubleTap" -> mode = StateMachineMode.INSERT
                            "OneSwipeLeft" -> execute(modifier, listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_DPAD_LEFT), "Select $modifier character(s) to left")
                            "OneSwipeRight" -> execute(modifier, listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT), "Select $modifier character(s) to right")
                            "TwoSwipeLeft" -> execute(modifier, listOf(KeyEvent.KEYCODE_CTRL_LEFT, KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_DPAD_LEFT), "Select $modifier word(s) to left")
                            "TwoSwipeRight" -> execute(modifier, listOf(KeyEvent.KEYCODE_CTRL_LEFT, KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_DPAD_LEFT), "Select $modifier word(s) to right")
                        }
                        action = StateMachineAction.DEFAULT
                    }
                    else {
                        when(input) {
                            "DoubleTap" -> mode = StateMachineMode.INSERT
                            "S" -> action = StateMachineAction.SELECT
                            "C" -> execute(1, listOf(KeyEvent.KEYCODE_CTRL_LEFT, KeyEvent.KEYCODE_C), "Copy selection")
                            "X" -> execute(1, listOf(KeyEvent.KEYCODE_CTRL_LEFT, KeyEvent.KEYCODE_X), "Cut/delete selection")
                            "V" -> execute(1, listOf(KeyEvent.KEYCODE_CTRL_LEFT, KeyEvent.KEYCODE_V), "Paste selection")
                            "OneSwipeLeft" -> execute(modifier, listOf(KeyEvent.KEYCODE_DPAD_LEFT), "Move cursor $modifier character(s) to left")
                            "OneSwipeRight" -> execute(modifier, listOf(KeyEvent.KEYCODE_DPAD_RIGHT), "Move cursor $modifier character(s) to right")
                            "TwoSwipeLeft" -> execute(modifier, listOf(KeyEvent.KEYCODE_CTRL_LEFT, KeyEvent.KEYCODE_DPAD_LEFT), "Move cursor $modifier word(s) to left")
                            "TwoSwipeRight" -> execute(modifier, listOf(KeyEvent.KEYCODE_CTRL_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT), "Move cursor $modifier word(s) to right")
                        }
                    }
                    if (action != StateMachineAction.SELECT) modifier = 1
                }
            }
        }
    }

    object KeyMapper {
        private val charToKeyCodeMap: Map<Char, List<Int>> = mapOf(

            'a' to listOf(KeyEvent.KEYCODE_A),
            'b' to listOf(KeyEvent.KEYCODE_B),
            'c' to listOf(KeyEvent.KEYCODE_C),
            'd' to listOf(KeyEvent.KEYCODE_D),
            'e' to listOf(KeyEvent.KEYCODE_E),
            'f' to listOf(KeyEvent.KEYCODE_F),
            'g' to listOf(KeyEvent.KEYCODE_G),
            'h' to listOf(KeyEvent.KEYCODE_H),
            'i' to listOf(KeyEvent.KEYCODE_I),
            'j' to listOf(KeyEvent.KEYCODE_J),
            'k' to listOf(KeyEvent.KEYCODE_K),
            'l' to listOf(KeyEvent.KEYCODE_L),
            'm' to listOf(KeyEvent.KEYCODE_M),
            'n' to listOf(KeyEvent.KEYCODE_N),
            'o' to listOf(KeyEvent.KEYCODE_O),
            'p' to listOf(KeyEvent.KEYCODE_P),
            'q' to listOf(KeyEvent.KEYCODE_Q),
            'r' to listOf(KeyEvent.KEYCODE_R),
            's' to listOf(KeyEvent.KEYCODE_S),
            't' to listOf(KeyEvent.KEYCODE_T),
            'u' to listOf(KeyEvent.KEYCODE_U),
            'v' to listOf(KeyEvent.KEYCODE_V),
            'w' to listOf(KeyEvent.KEYCODE_W),
            'x' to listOf(KeyEvent.KEYCODE_X),
            'y' to listOf(KeyEvent.KEYCODE_Y),
            'z' to listOf(KeyEvent.KEYCODE_Z),

            'A' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_A),
            'B' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_B),
            'C' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_C),
            'D' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_D),
            'E' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_E),
            'F' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_F),
            'G' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_G),
            'H' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_H),
            'I' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_I),
            'J' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_J),
            'K' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_K),
            'L' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_L),
            'M' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_M),
            'N' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_N),
            'O' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_O),
            'P' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_P),
            'Q' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_Q),
            'R' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_R),
            'S' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_S),
            'T' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_T),
            'U' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_U),
            'V' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_V),
            'W' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_W),
            'X' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_X),
            'Y' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_Y),
            'Z' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_Z),

            '0' to listOf(KeyEvent.KEYCODE_0),
            '1' to listOf(KeyEvent.KEYCODE_1),
            '2' to listOf(KeyEvent.KEYCODE_2),
            '3' to listOf(KeyEvent.KEYCODE_3),
            '4' to listOf(KeyEvent.KEYCODE_4),
            '5' to listOf(KeyEvent.KEYCODE_5),
            '6' to listOf(KeyEvent.KEYCODE_6),
            '7' to listOf(KeyEvent.KEYCODE_7),
            '8' to listOf(KeyEvent.KEYCODE_8),
            '9' to listOf(KeyEvent.KEYCODE_9),

            '`' to listOf(KeyEvent.KEYCODE_GRAVE),
            '-' to listOf(KeyEvent.KEYCODE_MINUS),
            '=' to listOf(KeyEvent.KEYCODE_EQUALS),
            '[' to listOf(KeyEvent.KEYCODE_LEFT_BRACKET),
            ']' to listOf(KeyEvent.KEYCODE_RIGHT_BRACKET),
            '\\' to listOf(KeyEvent.KEYCODE_BACKSLASH),
            ';' to listOf(KeyEvent.KEYCODE_SEMICOLON),
            '\'' to listOf(KeyEvent.KEYCODE_APOSTROPHE),
            ',' to listOf(KeyEvent.KEYCODE_COMMA),
            '.' to listOf(KeyEvent.KEYCODE_PERIOD),
            '/' to listOf(KeyEvent.KEYCODE_SLASH),

            '!' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_1),
            '@' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_2),
            '#' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_3),
            '$' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_4),
            '%' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_5),
            '^' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_6),
            '&' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_7),
            '*' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_8),
            '(' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_9),
            ')' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_0),
            '_' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_MINUS),
            '+' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_EQUALS),
            '{' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_LEFT_BRACKET),
            '}' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_RIGHT_BRACKET),
            '|' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_BACKSLASH),
            ':' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_SEMICOLON),
            '"' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_APOSTROPHE),
            '<' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_COMMA),
            '>' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_PERIOD),
            '?' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_SLASH),
            '~' to listOf(KeyEvent.KEYCODE_SHIFT_LEFT, KeyEvent.KEYCODE_GRAVE)
        )
    }
}


