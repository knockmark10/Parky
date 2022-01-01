package com.markoid.parky.core.presentation.extensions

import android.bluetooth.BluetoothAdapter

object BluetoothExtensions {

    fun getBondedDevicesWithAny(valueForAny: String): Array<String> {
        // Check the adapter. If its null, return empty list
        val bluetoothAdapter: BluetoothAdapter =
            BluetoothAdapter.getDefaultAdapter() ?: return arrayOf()
        // Create a list of devices name with default value 'any'
        val devices = mutableListOf<String>()
        // Add 'any' option to the list
        devices.add(valueForAny)
        // Get bonded devices list. Filter our the ones without a name
        val bondedDevices: List<String> = bluetoothAdapter.bondedDevices
            ?.filter { it.name != null }
            ?.map { it.name }
            ?: emptyList()
        // Add the devices to the returning list
        devices.addAll(bondedDevices)
        // Finally return the list
        return devices.toTypedArray()
    }
}
