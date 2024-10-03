package com.iomt.android.room.bcrecord

import androidx.room.*
import com.iomt.android.room.record.RecordEntity
import kotlinx.datetime.LocalDateTime

/**
 * Data Access Object of [BcRecordEntity]
 */
@Dao
interface BcRecordDao {
    /**
     * @param bcrecordEntity
     * @return generated id for [bcrecordEntity]
     */
    @Insert
    suspend fun insert(bcrecordEntity: BcRecordEntity): Long

    /**
     * @param bcrecordEntity [BcRecordEntity] to update (should have id not null)
     */
    @Update
    suspend fun update(bcrecordEntity: BcRecordEntity)

    /**
     * @param bcrecordEntity [BcRecordEntity] to delete (should have id not null)
     */
    @Delete
    suspend fun delete(bcrecordEntity: BcRecordEntity)

    /**
     * @return all [BcRecordEntity] where [BcRecordEntity.isSynchronized] is false
     */
    @Query("SELECT * FROM bcrecord WHERE is_sync = 0")
    suspend fun getNotSynchronized(): List<BcRecordEntity>

    /**
     * @param localDateTime
     */
    @Query("DELETE FROM record WHERE is_sync = 1 AND timestamp < :localDateTime")
    suspend fun cleanSynchronizedRecordsOlderThen(localDateTime: LocalDateTime)

    /**
     * @param deviceCharacteristicLinkId [RecordEntity.deviceCharacteristicLinkId]
     * @param localDateTime [LocalDateTime] that should be older than oll the fetched data
     * @param secondsInterval minimal period of data
     * @return [List] of [RecordEntity] of requested DeviceCharacteristicLink that
     *         is older than [localDateTime] and the period is [secondsInterval]
     */
    @Query("""
        SELECT * FROM bcrecord 
        WHERE timestamp >= :localDateTime 
            AND is_sync = 1
            AND id IN (
                SELECT MIN(id)
                FROM bcrecord 
                WHERE timestamp >= :localDateTime 
                    AND is_sync = 1
                GROUP BY CAST(julianday(timestamp) * 60 * 60 * 24 / :secondsInterval AS INTEGER)
            )
    """)
    suspend fun getPeriodicalRecordsByLinkIdNotOlderThen(
        //deviceCharacteristicLinkId: Long,
        localDateTime: LocalDateTime,
        secondsInterval: Long,
    ): List<BcRecordEntity>

    /**
     * @return number of all records present in database
     */
    @Query("SELECT COUNT(*) FROM bcrecord")
    suspend fun countAll(): Long

    /**
     * @return number of synchronized records present in database
     */
    @Query("SELECT COUNT(*) FROM bcrecord WHERE is_sync = 1")
    suspend fun countSynchronized(): Long
}
