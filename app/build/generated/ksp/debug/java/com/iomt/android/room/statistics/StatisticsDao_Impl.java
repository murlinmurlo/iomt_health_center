package com.iomt.android.room.statistics;

import android.database.Cursor;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.iomt.android.room.Converters;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;
import kotlinx.datetime.LocalDateTime;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class StatisticsDao_Impl implements StatisticsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<StatisticsEntity> __insertionAdapterOfStatisticsEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<StatisticsEntity> __deletionAdapterOfStatisticsEntity;

  private final EntityDeletionOrUpdateAdapter<StatisticsEntity> __updateAdapterOfStatisticsEntity;

  public StatisticsDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStatisticsEntity = new EntityInsertionAdapter<StatisticsEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `statistics` (`timestamp`,`all_count`,`sync_count`,`device_char_link_id`,`id`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, StatisticsEntity value) {
        final String _tmp = __converters.fromLocalDateTime(value.getTimestamp());
        if (_tmp == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, _tmp);
        }
        stmt.bindLong(2, value.getAllNumber());
        stmt.bindLong(3, value.getSynchronizedNumber());
        stmt.bindLong(4, value.getDeviceCharacteristicLinkId());
        if (value.getId() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, value.getId());
        }
      }
    };
    this.__deletionAdapterOfStatisticsEntity = new EntityDeletionOrUpdateAdapter<StatisticsEntity>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `statistics` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, StatisticsEntity value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getId());
        }
      }
    };
    this.__updateAdapterOfStatisticsEntity = new EntityDeletionOrUpdateAdapter<StatisticsEntity>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `statistics` SET `timestamp` = ?,`all_count` = ?,`sync_count` = ?,`device_char_link_id` = ?,`id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, StatisticsEntity value) {
        final String _tmp = __converters.fromLocalDateTime(value.getTimestamp());
        if (_tmp == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, _tmp);
        }
        stmt.bindLong(2, value.getAllNumber());
        stmt.bindLong(3, value.getSynchronizedNumber());
        stmt.bindLong(4, value.getDeviceCharacteristicLinkId());
        if (value.getId() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, value.getId());
        }
        if (value.getId() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindLong(6, value.getId());
        }
      }
    };
  }

  @Override
  public Object insert(final StatisticsEntity entity,
      final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          long _result = __insertionAdapterOfStatisticsEntity.insertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object delete(final StatisticsEntity entity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfStatisticsEntity.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final StatisticsEntity entity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfStatisticsEntity.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<StatisticsEntity> getAll() {
    final String _sql = "SELECT * FROM statistics";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"statistics"}, new Callable<StatisticsEntity>() {
      @Override
      public StatisticsEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfAllNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "all_count");
          final int _cursorIndexOfSynchronizedNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "sync_count");
          final int _cursorIndexOfDeviceCharacteristicLinkId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_char_link_id");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final StatisticsEntity _result;
          if(_cursor.moveToFirst()) {
            final LocalDateTime _tmpTimestamp;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfTimestamp)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfTimestamp);
            }
            _tmpTimestamp = __converters.toLocalDateTime(_tmp);
            final long _tmpAllNumber;
            _tmpAllNumber = _cursor.getLong(_cursorIndexOfAllNumber);
            final long _tmpSynchronizedNumber;
            _tmpSynchronizedNumber = _cursor.getLong(_cursorIndexOfSynchronizedNumber);
            final long _tmpDeviceCharacteristicLinkId;
            _tmpDeviceCharacteristicLinkId = _cursor.getLong(_cursorIndexOfDeviceCharacteristicLinkId);
            _result = new StatisticsEntity(_tmpTimestamp,_tmpAllNumber,_tmpSynchronizedNumber,_tmpDeviceCharacteristicLinkId);
            final Long _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getLong(_cursorIndexOfId);
            }
            _result.setId(_tmpId);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
