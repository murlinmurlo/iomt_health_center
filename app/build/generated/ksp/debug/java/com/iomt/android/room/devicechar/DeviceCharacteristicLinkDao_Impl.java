package com.iomt.android.room.devicechar;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class DeviceCharacteristicLinkDao_Impl implements DeviceCharacteristicLinkDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DeviceCharacteristicLinkEntity> __insertionAdapterOfDeviceCharacteristicLinkEntity;

  private final EntityDeletionOrUpdateAdapter<DeviceCharacteristicLinkEntity> __deletionAdapterOfDeviceCharacteristicLinkEntity;

  private final EntityDeletionOrUpdateAdapter<DeviceCharacteristicLinkEntity> __updateAdapterOfDeviceCharacteristicLinkEntity;

  public DeviceCharacteristicLinkDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDeviceCharacteristicLinkEntity = new EntityInsertionAdapter<DeviceCharacteristicLinkEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `device_characteristic_link` (`device_id`,`characteristic_id`,`id`) VALUES (?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DeviceCharacteristicLinkEntity value) {
        stmt.bindLong(1, value.getDeviceId());
        stmt.bindLong(2, value.getCharacteristicId());
        if (value.getId() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindLong(3, value.getId());
        }
      }
    };
    this.__deletionAdapterOfDeviceCharacteristicLinkEntity = new EntityDeletionOrUpdateAdapter<DeviceCharacteristicLinkEntity>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `device_characteristic_link` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DeviceCharacteristicLinkEntity value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getId());
        }
      }
    };
    this.__updateAdapterOfDeviceCharacteristicLinkEntity = new EntityDeletionOrUpdateAdapter<DeviceCharacteristicLinkEntity>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `device_characteristic_link` SET `device_id` = ?,`characteristic_id` = ?,`id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DeviceCharacteristicLinkEntity value) {
        stmt.bindLong(1, value.getDeviceId());
        stmt.bindLong(2, value.getCharacteristicId());
        if (value.getId() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindLong(3, value.getId());
        }
        if (value.getId() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, value.getId());
        }
      }
    };
  }

  @Override
  public Object insert(final DeviceCharacteristicLinkEntity entity,
      final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          long _result = __insertionAdapterOfDeviceCharacteristicLinkEntity.insertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object delete(final DeviceCharacteristicLinkEntity entity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfDeviceCharacteristicLinkEntity.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final DeviceCharacteristicLinkEntity entity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfDeviceCharacteristicLinkEntity.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object getByDeviceId(final long deviceId,
      final Continuation<? super List<DeviceCharacteristicLinkEntity>> continuation) {
    final String _sql = "SELECT * FROM device_characteristic_link WHERE device_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, deviceId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<List<DeviceCharacteristicLinkEntity>>() {
      @Override
      public List<DeviceCharacteristicLinkEntity> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
            final int _cursorIndexOfCharacteristicId = CursorUtil.getColumnIndexOrThrow(_cursor, "characteristic_id");
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final List<DeviceCharacteristicLinkEntity> _result = new ArrayList<DeviceCharacteristicLinkEntity>(_cursor.getCount());
            while(_cursor.moveToNext()) {
              final DeviceCharacteristicLinkEntity _item;
              final long _tmpDeviceId;
              _tmpDeviceId = _cursor.getLong(_cursorIndexOfDeviceId);
              final long _tmpCharacteristicId;
              _tmpCharacteristicId = _cursor.getLong(_cursorIndexOfCharacteristicId);
              _item = new DeviceCharacteristicLinkEntity(_tmpDeviceId,_tmpCharacteristicId);
              final Long _tmpId;
              if (_cursor.isNull(_cursorIndexOfId)) {
                _tmpId = null;
              } else {
                _tmpId = _cursor.getLong(_cursorIndexOfId);
              }
              _item.setId(_tmpId);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
            _statement.release();
          }
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object getById(final long id,
      final Continuation<? super DeviceCharacteristicLinkEntity> continuation) {
    final String _sql = "SELECT * FROM device_characteristic_link WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DeviceCharacteristicLinkEntity>() {
      @Override
      public DeviceCharacteristicLinkEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
          final int _cursorIndexOfCharacteristicId = CursorUtil.getColumnIndexOrThrow(_cursor, "characteristic_id");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final DeviceCharacteristicLinkEntity _result;
          if(_cursor.moveToFirst()) {
            final long _tmpDeviceId;
            _tmpDeviceId = _cursor.getLong(_cursorIndexOfDeviceId);
            final long _tmpCharacteristicId;
            _tmpCharacteristicId = _cursor.getLong(_cursorIndexOfCharacteristicId);
            _result = new DeviceCharacteristicLinkEntity(_tmpDeviceId,_tmpCharacteristicId);
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
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public DeviceCharacteristicLinkEntity getByDeviceIdAndCharacteristicId(final long deviceId,
      final long characteristicId) {
    final String _sql = "SELECT * FROM device_characteristic_link WHERE device_id = ? AND characteristic_id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, deviceId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, characteristicId);
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
      try {
        final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "device_id");
        final int _cursorIndexOfCharacteristicId = CursorUtil.getColumnIndexOrThrow(_cursor, "characteristic_id");
        final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
        final DeviceCharacteristicLinkEntity _result;
        if(_cursor.moveToFirst()) {
          final long _tmpDeviceId;
          _tmpDeviceId = _cursor.getLong(_cursorIndexOfDeviceId);
          final long _tmpCharacteristicId;
          _tmpCharacteristicId = _cursor.getLong(_cursorIndexOfCharacteristicId);
          _result = new DeviceCharacteristicLinkEntity(_tmpDeviceId,_tmpCharacteristicId);
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
        __db.setTransactionSuccessful();
        return _result;
      } finally {
        _cursor.close();
        _statement.release();
      }
    } finally {
      __db.endTransaction();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
