package com.iomt.android.room.characteristic;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.iomt.android.room.Converters;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CharacteristicDao_Impl implements CharacteristicDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CharacteristicEntity> __insertionAdapterOfCharacteristicEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<CharacteristicEntity> __deletionAdapterOfCharacteristicEntity;

  private final EntityDeletionOrUpdateAdapter<CharacteristicEntity> __updateAdapterOfCharacteristicEntity;

  public CharacteristicDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCharacteristicEntity = new EntityInsertionAdapter<CharacteristicEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `characteristic` (`name`,`pretty_name`,`service_uuid`,`char_uuid`,`id`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, CharacteristicEntity value) {
        if (value.getName() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getName());
        }
        if (value.getPrettyName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getPrettyName());
        }
        final String _tmp = __converters.fromUuid(value.getServiceUuid());
        if (_tmp == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, _tmp);
        }
        final String _tmp_1 = __converters.fromUuid(value.getCharacteristicUuid());
        if (_tmp_1 == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, _tmp_1);
        }
        if (value.getId() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, value.getId());
        }
      }
    };
    this.__deletionAdapterOfCharacteristicEntity = new EntityDeletionOrUpdateAdapter<CharacteristicEntity>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `characteristic` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, CharacteristicEntity value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.getId());
        }
      }
    };
    this.__updateAdapterOfCharacteristicEntity = new EntityDeletionOrUpdateAdapter<CharacteristicEntity>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `characteristic` SET `name` = ?,`pretty_name` = ?,`service_uuid` = ?,`char_uuid` = ?,`id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, CharacteristicEntity value) {
        if (value.getName() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getName());
        }
        if (value.getPrettyName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getPrettyName());
        }
        final String _tmp = __converters.fromUuid(value.getServiceUuid());
        if (_tmp == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, _tmp);
        }
        final String _tmp_1 = __converters.fromUuid(value.getCharacteristicUuid());
        if (_tmp_1 == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, _tmp_1);
        }
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
  public Object insert(final CharacteristicEntity entity,
      final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          long _result = __insertionAdapterOfCharacteristicEntity.insertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object delete(final CharacteristicEntity entity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfCharacteristicEntity.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final CharacteristicEntity entity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCharacteristicEntity.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<CharacteristicEntity> getAll() {
    final String _sql = "SELECT * FROM characteristic";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"characteristic"}, new Callable<CharacteristicEntity>() {
      @Override
      public CharacteristicEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfPrettyName = CursorUtil.getColumnIndexOrThrow(_cursor, "pretty_name");
          final int _cursorIndexOfServiceUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "service_uuid");
          final int _cursorIndexOfCharacteristicUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "char_uuid");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final CharacteristicEntity _result;
          if(_cursor.moveToFirst()) {
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpPrettyName;
            if (_cursor.isNull(_cursorIndexOfPrettyName)) {
              _tmpPrettyName = null;
            } else {
              _tmpPrettyName = _cursor.getString(_cursorIndexOfPrettyName);
            }
            final UUID _tmpServiceUuid;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfServiceUuid)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfServiceUuid);
            }
            _tmpServiceUuid = __converters.toUuid(_tmp);
            final UUID _tmpCharacteristicUuid;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfCharacteristicUuid)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfCharacteristicUuid);
            }
            _tmpCharacteristicUuid = __converters.toUuid(_tmp_1);
            _result = new CharacteristicEntity(_tmpName,_tmpPrettyName,_tmpServiceUuid,_tmpCharacteristicUuid);
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

  @Override
  public Object getByNameAndCharacteristicUuid(final String name, final UUID characteristicUuid,
      final Continuation<? super CharacteristicEntity> continuation) {
    final String _sql = "SELECT * FROM characteristic WHERE name = ? AND char_uuid = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
    }
    _argIndex = 2;
    final String _tmp = __converters.fromUuid(characteristicUuid);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CharacteristicEntity>() {
      @Override
      public CharacteristicEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfPrettyName = CursorUtil.getColumnIndexOrThrow(_cursor, "pretty_name");
          final int _cursorIndexOfServiceUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "service_uuid");
          final int _cursorIndexOfCharacteristicUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "char_uuid");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final CharacteristicEntity _result;
          if(_cursor.moveToFirst()) {
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpPrettyName;
            if (_cursor.isNull(_cursorIndexOfPrettyName)) {
              _tmpPrettyName = null;
            } else {
              _tmpPrettyName = _cursor.getString(_cursorIndexOfPrettyName);
            }
            final UUID _tmpServiceUuid;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfServiceUuid)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfServiceUuid);
            }
            _tmpServiceUuid = __converters.toUuid(_tmp_1);
            final UUID _tmpCharacteristicUuid;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfCharacteristicUuid)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfCharacteristicUuid);
            }
            _tmpCharacteristicUuid = __converters.toUuid(_tmp_2);
            _result = new CharacteristicEntity(_tmpName,_tmpPrettyName,_tmpServiceUuid,_tmpCharacteristicUuid);
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
  public Object getByIdsIn(final List<Long> ids,
      final Continuation<? super List<CharacteristicEntity>> continuation) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM characteristic WHERE id IN (");
    final int _inputSize = ids.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (long _item : ids) {
      _statement.bindLong(_argIndex, _item);
      _argIndex ++;
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CharacteristicEntity>>() {
      @Override
      public List<CharacteristicEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfPrettyName = CursorUtil.getColumnIndexOrThrow(_cursor, "pretty_name");
          final int _cursorIndexOfServiceUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "service_uuid");
          final int _cursorIndexOfCharacteristicUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "char_uuid");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<CharacteristicEntity> _result = new ArrayList<CharacteristicEntity>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final CharacteristicEntity _item_1;
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpPrettyName;
            if (_cursor.isNull(_cursorIndexOfPrettyName)) {
              _tmpPrettyName = null;
            } else {
              _tmpPrettyName = _cursor.getString(_cursorIndexOfPrettyName);
            }
            final UUID _tmpServiceUuid;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfServiceUuid)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfServiceUuid);
            }
            _tmpServiceUuid = __converters.toUuid(_tmp);
            final UUID _tmpCharacteristicUuid;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfCharacteristicUuid)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfCharacteristicUuid);
            }
            _tmpCharacteristicUuid = __converters.toUuid(_tmp_1);
            _item_1 = new CharacteristicEntity(_tmpName,_tmpPrettyName,_tmpServiceUuid,_tmpCharacteristicUuid);
            final Long _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getLong(_cursorIndexOfId);
            }
            _item_1.setId(_tmpId);
            _result.add(_item_1);
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
  public Object getById(final long id,
      final Continuation<? super CharacteristicEntity> continuation) {
    final String _sql = "SELECT * FROM characteristic WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CharacteristicEntity>() {
      @Override
      public CharacteristicEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfPrettyName = CursorUtil.getColumnIndexOrThrow(_cursor, "pretty_name");
          final int _cursorIndexOfServiceUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "service_uuid");
          final int _cursorIndexOfCharacteristicUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "char_uuid");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final CharacteristicEntity _result;
          if(_cursor.moveToFirst()) {
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpPrettyName;
            if (_cursor.isNull(_cursorIndexOfPrettyName)) {
              _tmpPrettyName = null;
            } else {
              _tmpPrettyName = _cursor.getString(_cursorIndexOfPrettyName);
            }
            final UUID _tmpServiceUuid;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfServiceUuid)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfServiceUuid);
            }
            _tmpServiceUuid = __converters.toUuid(_tmp);
            final UUID _tmpCharacteristicUuid;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfCharacteristicUuid)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfCharacteristicUuid);
            }
            _tmpCharacteristicUuid = __converters.toUuid(_tmp_1);
            _result = new CharacteristicEntity(_tmpName,_tmpPrettyName,_tmpServiceUuid,_tmpCharacteristicUuid);
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

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
