package com.iomt.android.mqtt

import android.hardware.ConsumerIrManager.CarrierFrequencyRange

/**
 * Data class that represents MQTT topic
 *
 * @property userId id of currently logged-in user
 * @property deviceMac MAC address of Bluetooth LE device
 * @property characteristicName internal representation of characteristic name
 */
data class Topic(
    val userId: Long,
    val deviceMac: String,
    val characteristicName: String,
) {
    /**
     * Topic name format:
     *   `c/{user_id}/{mac_address}/{characteristicName}`
     *
     * @return topic name
     */
    fun toTopicName() = "c/$userId/$deviceMac/$characteristicName"
}

data class BcTopic(
    val userId: Long,
    val deviceMac: String,
    val frequency: Int
) {
    /**
     * Topic name format:
     *   `c/{user_id}/{mac_address}/{characteristicName}`
     *
     * @return topic name
     */
    fun toTopicName() = "ecg/$userId/$deviceMac/$frequency/3"
}
