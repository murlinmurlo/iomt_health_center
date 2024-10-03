package com.iomt.android.room.record;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.datetime.LocalDateTime;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RecordDao_Impl implements RecordDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RecordEntity> __insertionAdapterOfRecordEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<RecordEntity> __deletionAdapterOfRecordEntity;

  private final EntityDeletionOrUpdateAdapter<RecordEntity> __updateAdapterOfRecordEntity;

  private final SharedSQLiteStatement __preparedStmtOfCleanSynchronizedRecordsOlderThen;

  public RecordDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRecordEntity = new EntityInsertionAdapter<RecordEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `record` (`device_char_link_id`,`timestamp`,`value`,`is_sync`,`id`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, RecordEntity value) {
        stmt.bindLong(1, value.getDeviceCharacteristicLinkId());
        final String _tmp = __converters.fromLocalDateTime(value.getTimestamp());
        if (_tmp == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, _tmp);
        }
        if (value.getValue() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getValue());
        }
        final int _tmp_1 = value.isSynchronized() ? 1 : 0;
        stmt.bindLong(4, _tmp_1);
        if (value.getId() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, value.getId());
        }
      }
    };
    this.__deletionAdapterOfRecordEntity = new EntityDeletionOrUpdateAdapter<RecordEntity>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `record` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, RecordEntity value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getId());
        }
      }
    };
    this.__updateAdapterOfRecordEntity = new EntityDeletionOrUpdateAdapter<RecordEntity>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `record` SET `device_char_link_id` = ?,`timestamp` = ?,`value` = ?,`is_sync` = ?,`id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, RecordEntity value) {
        stmt.bindLong(1, value.getDeviceCharacteristicLinkId());
        final String _tmp = __converters.fromLocalDateTime(value.getTimestamp());
        if (_tmp == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, _tmp);
        }
        if (value.getValue() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getValue());
        }
        final int _tmp_1 = value.isSynchronized() ? 1 : 0;
        stmt.bindLong(4, _tmp_1);
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
    this.__preparedStmtOfCleanSynchronizedRecordsOlderThen = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM record WHERE is_sync = 1 AND timestamp < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final RecordEntity recordEntity,
      final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          long _result = __insertionAdapterOfRecordEntity.insertAndReturnId(recordEntity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object delete(final RecordEntity recordEntity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRecordEntity.handle(recordEntity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final RecordEntity recordEntity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRecordEntity.handle(recordEntity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object cleanSynchronizedRecordsOlderThen(final LocalDateTime localDateTime,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfCleanSynchronizedRecordsOlderThen.acquire();
        int _argIndex = 1;
        final String _tmp = __converters.fromLocalDateTime(localDateTime);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, _tmp);
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfCleanSynchronizedRecordsOlderThen.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object getNotSynchronized(final Continuation<? super List<RecordEntity>> continuation) {
    final String _sql = "SELECT * FROM record WHERE is_sync = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<RecordEntity>>() {
      @Override
      public List<RecordEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDeviceCharacteristicLinkId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_char_link_id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfIsSynchronized = CursorUtil.getColumnIndexOrThrow(_cursor, "is_sync");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<RecordEntity> _result = new ArrayList<RecordEntity>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final RecordEntity _item;
            final long _tmpDeviceCharacteristicLinkId;
            _tmpDeviceCharacteristicLinkId = _cursor.getLong(_cursorIndexOfDeviceCharacteristicLinkId);
            final LocalDateTime _tmpTimestamp;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfTimestamp)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfTimestamp);
            }
            _tmpTimestamp = __converters.toLocalDateTime(_tmp);
            final String _tmpValue;
            if (_cursor.isNull(_cursorIndexOfValue)) {
              _tmpValue = null;
            } else {
              _tmpValue = _cursor.getString(_cursorIndexOfValue);
            }
            final boolean _tmpIsSynchronized;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsSynchronized);
            _tmpIsSynchronized = _tmp_1 != 0;
            _item = new RecordEntity(_tmpDeviceCharacteristicLinkId,_tmpTimestamp,_tmpValue,_tmpIsSynchronized);
            final Long _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getLong(_cursorIndexOfId);
            }
            _item.setId(_tmpId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object getPeriodicalRecordsByLinkIdNotOlderThen(final long deviceCharacteristicLinkId,
      final LocalDateTime localDateTime, final long secondsInterval,
      final Continuation<? super List<RecordEntity>> continuation) {
    final String _sql = "\n"
            + "        SELECT * FROM record \n"
            + "        WHERE device_char_link_id = ? \n"
            + "            AND timestamp >= ? \n"
            + "            AND is_sync = 1\n"
            + "            AND id IN (\n"
            + "                SELECT MIN(id)\n"
            + "                FROM record \n"
            + "                WHERE device_char_link_id = ? \n"
            + "                    AND timestamp >= ? \n"
            + "                    AND is_sync = 1\n"
            + "                GROUP BY CAST(julianday(timestamp) * 60 * 60 * 24 / ? AS INTEGER)\n"
            + "            )\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 5);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, deviceCharacteristicLinkId);
    _argIndex = 2;
    final String _tmp = __converters.fromLocalDateTime(localDateTime);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    _argIndex = 3;
    _statement.bindLong(_argIndex, deviceCharacteristicLinkId);
    _argIndex = 4;
    final String _tmp_1 = __converters.fromLocalDateTime(localDateTime);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp_1);
    }
    _argIndex = 5;
    _statement.bindLong(_argIndex, secondsInterval);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<RecordEntity>>() {
      @Override
      public List<RecordEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDeviceCharacteristicLinkId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_char_link_id");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfIsSynchronized = CursorUtil.getColumnIndexOrThrow(_cursor, "is_sync");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<RecordEntity> _result = new ArrayList<RecordEntity>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final RecordEntity _item;
            final long _tmpDeviceCharacteristicLinkId;
            _tmpDeviceCharacteristicLinkId = _cursor.getLong(_cursorIndexOfDeviceCharacteristicLinkId);
            final LocalDateTime _tmpTimestamp;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfTimestamp)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfTimestamp);
            }
            _tmpTimestamp = __converters.toLocalDateTime(_tmp_2);
            final String _tmpValue;
            if (_cursor.isNull(_cursorIndexOfValue)) {
              _tmpValue = null;
            } else {
              _tmpValue = _cursor.getString(_cursorIndexOfValue);
            }
            final boolean _tmpIsSynchronized;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfIsSynchronized);
            _tmpIsSynchronized = _tmp_3 != 0;
            _item = new RecordEntity(_tmpDeviceCharacteristicLinkId,_tmpTimestamp,_tmpValue,_tmpIsSynchronized);
            final Long _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getLong(_cursorIndexOfId);
            }
            _item.setId(_tmpId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object countAll(final Continuation<? super Long> continuation) {
    final String _sql = "SELECT COUNT(*) FROM record";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if(_cursor.moveToFirst()) {
            final long _tmp;
            _tmp = _cursor.getLong(0);
            _result = _tmp;
          } else {
            _result = 0L;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object countSynchronized(final Continuation<? super Long> continuation) {
    final String _sql = "SELECT COUNT(*) FROM record WHERE is_sync = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if(_cursor.moveToFirst()) {
            final long _tmp;
            _tmp = _cursor.getLong(0);
            _result = _tmp;
          } else {
            _result = 0L;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object countAllGroupedByLinkId(final Continuation<? super Map<Long, Long>> continuation) {
    final String _sql = "SELECT device_char_link_id, COUNT(*) FROM record GROUP BY device_char_link_id";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Map<Long, Long>>() {
      @Override
      public Map<Long, Long> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _columnIndexOfDeviceCharLinkId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_char_link_id");
          final int _columnIndexOfCOUNT = CursorUtil.getColumnIndexOrThrow(_cursor, "COUNT(*)");
          final Map<Long, Long> _result = new LinkedHashMap<Long, Long>();
          while (_cursor.moveToNext()) {
            final Long _key;
            _key = _cursor.getLong(_columnIndexOfDeviceCharLinkId);
            if (_cursor.isNull(_columnIndexOfCOUNT)) {
              _result.put(_key, null);
              continue;
            }
            final Long _value;
            final long _tmp;
            _tmp = _cursor.getLong(_columnIndexOfCOUNT);
            _value = _tmp;
            if (!_result.containsKey(_key)) {
              _result.put(_key, _value);
            }
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object countSynchronizedGroupedByLinkId(
      final Continuation<? super Map<Long, Long>> continuation) {
    final String _sql = "SELECT device_char_link_id, COUNT(*) FROM record WHERE is_sync = 1 GROUP BY device_char_link_id";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Map<Long, Long>>() {
      @Override
      public Map<Long, Long> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _columnIndexOfDeviceCharLinkId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_char_link_id");
          final int _columnIndexOfCOUNT = CursorUtil.getColumnIndexOrThrow(_cursor, "COUNT(*)");
          final Map<Long, Long> _result = new LinkedHashMap<Long, Long>();
          while (_cursor.moveToNext()) {
            final Long _key;
            _key = _cursor.getLong(_columnIndexOfDeviceCharLinkId);
            if (_cursor.isNull(_columnIndexOfCOUNT)) {
              _result.put(_key, null);
              continue;
            }
            final Long _value;
            final long _tmp;
            _tmp = _cursor.getLong(_columnIndexOfCOUNT);
            _value = _tmp;
            if (!_result.containsKey(_key)) {
              _result.put(_key, _value);
            }
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
