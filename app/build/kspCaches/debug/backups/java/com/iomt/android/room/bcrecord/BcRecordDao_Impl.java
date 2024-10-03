package com.iomt.android.room.bcrecord;

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
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.datetime.LocalDateTime;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BcRecordDao_Impl implements BcRecordDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BcRecordEntity> __insertionAdapterOfBcRecordEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<BcRecordEntity> __deletionAdapterOfBcRecordEntity;

  private final EntityDeletionOrUpdateAdapter<BcRecordEntity> __updateAdapterOfBcRecordEntity;

  private final SharedSQLiteStatement __preparedStmtOfCleanSynchronizedRecordsOlderThen;

  public BcRecordDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBcRecordEntity = new EntityInsertionAdapter<BcRecordEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `bcrecord` (`timestamp`,`value`,`is_sync`,`id`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, BcRecordEntity value) {
        final String _tmp = __converters.fromLocalDateTime(value.getTimestamp());
        if (_tmp == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, _tmp);
        }
        if (value.getValue() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getValue());
        }
        final int _tmp_1 = value.isSynchronized() ? 1 : 0;
        stmt.bindLong(3, _tmp_1);
        if (value.getId() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, value.getId());
        }
      }
    };
    this.__deletionAdapterOfBcRecordEntity = new EntityDeletionOrUpdateAdapter<BcRecordEntity>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `bcrecord` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, BcRecordEntity value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getId());
        }
      }
    };
    this.__updateAdapterOfBcRecordEntity = new EntityDeletionOrUpdateAdapter<BcRecordEntity>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `bcrecord` SET `timestamp` = ?,`value` = ?,`is_sync` = ?,`id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, BcRecordEntity value) {
        final String _tmp = __converters.fromLocalDateTime(value.getTimestamp());
        if (_tmp == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, _tmp);
        }
        if (value.getValue() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getValue());
        }
        final int _tmp_1 = value.isSynchronized() ? 1 : 0;
        stmt.bindLong(3, _tmp_1);
        if (value.getId() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, value.getId());
        }
        if (value.getId() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, value.getId());
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
  public Object insert(final BcRecordEntity bcrecordEntity,
      final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          long _result = __insertionAdapterOfBcRecordEntity.insertAndReturnId(bcrecordEntity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object delete(final BcRecordEntity bcrecordEntity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBcRecordEntity.handle(bcrecordEntity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final BcRecordEntity bcrecordEntity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBcRecordEntity.handle(bcrecordEntity);
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
  public Object getNotSynchronized(final Continuation<? super List<BcRecordEntity>> continuation) {
    final String _sql = "SELECT * FROM bcrecord WHERE is_sync = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<BcRecordEntity>>() {
      @Override
      public List<BcRecordEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfIsSynchronized = CursorUtil.getColumnIndexOrThrow(_cursor, "is_sync");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<BcRecordEntity> _result = new ArrayList<BcRecordEntity>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final BcRecordEntity _item;
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
            _item = new BcRecordEntity(_tmpTimestamp,_tmpValue,_tmpIsSynchronized);
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
  public Object getPeriodicalRecordsByLinkIdNotOlderThen(final LocalDateTime localDateTime,
      final long secondsInterval, final Continuation<? super List<BcRecordEntity>> continuation) {
    final String _sql = "\n"
            + "        SELECT * FROM bcrecord \n"
            + "        WHERE timestamp >= ? \n"
            + "            AND is_sync = 1\n"
            + "            AND id IN (\n"
            + "                SELECT MIN(id)\n"
            + "                FROM bcrecord \n"
            + "                WHERE timestamp >= ? \n"
            + "                    AND is_sync = 1\n"
            + "                GROUP BY CAST(julianday(timestamp) * 60 * 60 * 24 / ? AS INTEGER)\n"
            + "            )\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    final String _tmp = __converters.fromLocalDateTime(localDateTime);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    _argIndex = 2;
    final String _tmp_1 = __converters.fromLocalDateTime(localDateTime);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp_1);
    }
    _argIndex = 3;
    _statement.bindLong(_argIndex, secondsInterval);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<BcRecordEntity>>() {
      @Override
      public List<BcRecordEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfValue = CursorUtil.getColumnIndexOrThrow(_cursor, "value");
          final int _cursorIndexOfIsSynchronized = CursorUtil.getColumnIndexOrThrow(_cursor, "is_sync");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<BcRecordEntity> _result = new ArrayList<BcRecordEntity>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final BcRecordEntity _item;
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
            _item = new BcRecordEntity(_tmpTimestamp,_tmpValue,_tmpIsSynchronized);
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
    final String _sql = "SELECT COUNT(*) FROM bcrecord";
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
    final String _sql = "SELECT COUNT(*) FROM bcrecord WHERE is_sync = 1";
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

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
