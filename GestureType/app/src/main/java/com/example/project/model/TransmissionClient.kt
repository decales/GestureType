package com.example.project.model

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import android.view.KeyEvent
import androidx.annotation.RequiresApi
import com.example.project.model.bluetooth.BluetoothClient
import com.example.project.model.bluetooth.KeyboardReport

@RequiresApi(Build.VERSION_CODES.Q)
@SuppressLint("MissingPermission")
class TransmissionClient(
    val bluetoothClient: BluetoothClient
) {

    //val keyPosition : IntArray = IntArray(6){0}


    fun sendKeys() {
        if (bluetoothClient.isConnected) {
            if (!bluetoothClient.hidDevice?.sendReport(bluetoothClient.hostDevice, KeyboardReport.id, KeyboardReport.bytes)!!) {
                Log.e(TAG, "Report wasn't sent")
            }
        }
    }


    fun customSender(modifier_checked_state: Int) {
        if (bluetoothClient.isConnected) {
            sendKeys()
            if(modifier_checked_state==0) sendNullKeys()
            else {
                KeyboardReport.key1=0.toByte()
                sendKeys()
            }
        }

    }


    fun sendNullKeys() {
        KeyboardReport.bytes.fill(0)
        if (!bluetoothClient.hidDevice?.sendReport(bluetoothClient.hostDevice, KeyboardReport.id, KeyboardReport.bytes)!!) {
            Log.e(TAG, "Report wasn't sent")
        }
    }

    fun keyEventHandler(keyEventCode: Int, event : KeyEvent, modifier_checked_state: Int, keyCode:Int): Boolean{


        val byteKey = KeyboardReport.KeyEventMap[keyEventCode]

        if(byteKey!=null)
        {
            //setModifiers(event)
            if(event.keyCode== KeyEvent.KEYCODE_AT || event.keyCode== KeyEvent.KEYCODE_POUND || event.keyCode== KeyEvent.KEYCODE_STAR)
            {
                KeyboardReport.leftShift=true
            }
            KeyboardReport.key1=byteKey.toByte()
            customSender(modifier_checked_state)

            return true
        }
        else
        {
            return false
        }



    }


    fun sendKeyboard(keyCode : Int, event : KeyEvent, modifier_checked_state :Int): Boolean {


        return keyEventHandler(event.keyCode, event, modifier_checked_state, keyCode)
    }



}