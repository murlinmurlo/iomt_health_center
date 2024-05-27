package com.iomt.android.room.bcrecord

import androidx.room.*
import com.iomt.android.mqtt.MqttRecordMessage
import com.iomt.android.room.basic.BasicEntity
import com.iomt.android.room.devicechar.DeviceCharacteristicLinkEntity
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/**
 * @property timestamp [LocalDateTime] of record
 * @property value received value
 * @property isSynchronized flag that defines if the data was sent to MQTT broker or not
 */
@Entity(
    tableName = "bcrecord",
)
@Serializable
data class BcRecordEntity(
    val timestamp: LocalDateTime,
    val value: String,
    @ColumnInfo(name = "is_sync", defaultValue = "0")
    var isSynchronized: Boolean = false,
) : BasicEntity() {
    /**
     * @return [MqttRecordMessage] from this [BcRecordEntity]
     */
    fun toMqttRecordMessage() = MqttRecordMessage(value, timestamp)
}