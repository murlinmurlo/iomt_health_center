package com.iomt.android.room;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import com.iomt.android.room.bcrecord.BcRecordDao;
import com.iomt.android.room.bcrecord.BcRecordDao_Impl;
import com.iomt.android.room.characteristic.CharacteristicDao;
import com.iomt.android.room.characteristic.CharacteristicDao_Impl;
import com.iomt.android.room.device.DeviceDao;
import com.iomt.android.room.device.DeviceDao_Impl;
import com.iomt.android.room.devicechar.DeviceCharacteristicLinkDao;
import com.iomt.android.room.devicechar.DeviceCharacteristicLinkDao_Impl;
import com.iomt.android.room.record.RecordDao;
import com.iomt.android.room.record.RecordDao_Impl;
import com.iomt.android.room.statistics.StatisticsDao;
import com.iomt.android.room.statistics.StatisticsDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile CharacteristicDao _characteristicDao;

  private volatile DeviceDao _deviceDao;

  private volatile DeviceCharacteristicLinkDao _deviceCharacteristicLinkDao;

  private volatile RecordDao _recordDao;

  private volatile BcRecordDao _bcRecordDao;

  private volatile StatisticsDao _statisticsDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `characteristic` (`name` TEXT NOT NULL, `pretty_name` TEXT NOT NULL, `service_uuid` TEXT NOT NULL, `char_uuid` TEXT NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `device` (`name` TEXT NOT NULL, `mac` TEXT NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `device_characteristic_link` (`device_id` INTEGER NOT NULL, `characteristic_id` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT, FOREIGN KEY(`device_id`) REFERENCES `device`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`characteristic_id`) REFERENCES `characteristic`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        _db.execSQL("CREATE INDEX IF NOT EXISTS `index_device_characteristic_link_device_id` ON `device_characteristic_link` (`device_id`)");
        _db.execSQL("CREATE INDEX IF NOT EXISTS `index_device_characteristic_link_characteristic_id` ON `device_characteristic_link` (`characteristic_id`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `record` (`device_char_link_id` INTEGER NOT NULL, `timestamp` TEXT NOT NULL, `value` TEXT NOT NULL, `is_sync` INTEGER NOT NULL DEFAULT 0, `id` INTEGER PRIMARY KEY AUTOINCREMENT, FOREIGN KEY(`device_char_link_id`) REFERENCES `device_characteristic_link`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        _db.execSQL("CREATE INDEX IF NOT EXISTS `index_record_device_char_link_id` ON `record` (`device_char_link_id`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `bcrecord` (`timestamp` TEXT NOT NULL, `value` TEXT NOT NULL, `is_sync` INTEGER NOT NULL DEFAULT 0, `id` INTEGER PRIMARY KEY AUTOINCREMENT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `statistics` (`timestamp` TEXT NOT NULL, `all_count` INTEGER NOT NULL, `sync_count` INTEGER NOT NULL, `device_char_link_id` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT, FOREIGN KEY(`device_char_link_id`) REFERENCES `device_characteristic_link`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        _db.execSQL("CREATE INDEX IF NOT EXISTS `index_statistics_device_char_link_id` ON `statistics` (`device_char_link_id`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'aaef980fdd5522053574bb0f8be2e9c6')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `characteristic`");
        _db.execSQL("DROP TABLE IF EXISTS `device`");
        _db.execSQL("DROP TABLE IF EXISTS `device_characteristic_link`");
        _db.execSQL("DROP TABLE IF EXISTS `record`");
        _db.execSQL("DROP TABLE IF EXISTS `bcrecord`");
        _db.execSQL("DROP TABLE IF EXISTS `statistics`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      public void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        _db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      public RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsCharacteristic = new HashMap<String, TableInfo.Column>(5);
        _columnsCharacteristic.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacteristic.put("pretty_name", new TableInfo.Column("pretty_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacteristic.put("service_uuid", new TableInfo.Column("service_uuid", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacteristic.put("char_uuid", new TableInfo.Column("char_uuid", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCharacteristic.put("id", new TableInfo.Column("id", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCharacteristic = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCharacteristic = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCharacteristic = new TableInfo("characteristic", _columnsCharacteristic, _foreignKeysCharacteristic, _indicesCharacteristic);
        final TableInfo _existingCharacteristic = TableInfo.read(_db, "characteristic");
        if (! _infoCharacteristic.equals(_existingCharacteristic)) {
          return new RoomOpenHelper.ValidationResult(false, "characteristic(com.iomt.android.room.characteristic.CharacteristicEntity).\n"
                  + " Expected:\n" + _infoCharacteristic + "\n"
                  + " Found:\n" + _existingCharacteristic);
        }
        final HashMap<String, TableInfo.Column> _columnsDevice = new HashMap<String, TableInfo.Column>(3);
        _columnsDevice.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevice.put("mac", new TableInfo.Column("mac", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDevice.put("id", new TableInfo.Column("id", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDevice = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDevice = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDevice = new TableInfo("device", _columnsDevice, _foreignKeysDevice, _indicesDevice);
        final TableInfo _existingDevice = TableInfo.read(_db, "device");
        if (! _infoDevice.equals(_existingDevice)) {
          return new RoomOpenHelper.ValidationResult(false, "device(com.iomt.android.room.device.DeviceEntity).\n"
                  + " Expected:\n" + _infoDevice + "\n"
                  + " Found:\n" + _existingDevice);
        }
        final HashMap<String, TableInfo.Column> _columnsDeviceCharacteristicLink = new HashMap<String, TableInfo.Column>(3);
        _columnsDeviceCharacteristicLink.put("device_id", new TableInfo.Column("device_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceCharacteristicLink.put("characteristic_id", new TableInfo.Column("characteristic_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDeviceCharacteristicLink.put("id", new TableInfo.Column("id", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDeviceCharacteristicLink = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysDeviceCharacteristicLink.add(new TableInfo.ForeignKey("device", "CASCADE", "NO ACTION",Arrays.asList("device_id"), Arrays.asList("id")));
        _foreignKeysDeviceCharacteristicLink.add(new TableInfo.ForeignKey("characteristic", "CASCADE", "NO ACTION",Arrays.asList("characteristic_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesDeviceCharacteristicLink = new HashSet<TableInfo.Index>(2);
        _indicesDeviceCharacteristicLink.add(new TableInfo.Index("index_device_characteristic_link_device_id", false, Arrays.asList("device_id"), Arrays.asList("ASC")));
        _indicesDeviceCharacteristicLink.add(new TableInfo.Index("index_device_characteristic_link_characteristic_id", false, Arrays.asList("characteristic_id"), Arrays.asList("ASC")));
        final TableInfo _infoDeviceCharacteristicLink = new TableInfo("device_characteristic_link", _columnsDeviceCharacteristicLink, _foreignKeysDeviceCharacteristicLink, _indicesDeviceCharacteristicLink);
        final TableInfo _existingDeviceCharacteristicLink = TableInfo.read(_db, "device_characteristic_link");
        if (! _infoDeviceCharacteristicLink.equals(_existingDeviceCharacteristicLink)) {
          return new RoomOpenHelper.ValidationResult(false, "device_characteristic_link(com.iomt.android.room.devicechar.DeviceCharacteristicLinkEntity).\n"
                  + " Expected:\n" + _infoDeviceCharacteristicLink + "\n"
                  + " Found:\n" + _existingDeviceCharacteristicLink);
        }
        final HashMap<String, TableInfo.Column> _columnsRecord = new HashMap<String, TableInfo.Column>(5);
        _columnsRecord.put("device_char_link_id", new TableInfo.Column("device_char_link_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecord.put("timestamp", new TableInfo.Column("timestamp", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecord.put("value", new TableInfo.Column("value", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecord.put("is_sync", new TableInfo.Column("is_sync", "INTEGER", true, 0, "0", TableInfo.CREATED_FROM_ENTITY));
        _columnsRecord.put("id", new TableInfo.Column("id", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRecord = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysRecord.add(new TableInfo.ForeignKey("device_characteristic_link", "CASCADE", "NO ACTION",Arrays.asList("device_char_link_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesRecord = new HashSet<TableInfo.Index>(1);
        _indicesRecord.add(new TableInfo.Index("index_record_device_char_link_id", false, Arrays.asList("device_char_link_id"), Arrays.asList("ASC")));
        final TableInfo _infoRecord = new TableInfo("record", _columnsRecord, _foreignKeysRecord, _indicesRecord);
        final TableInfo _existingRecord = TableInfo.read(_db, "record");
        if (! _infoRecord.equals(_existingRecord)) {
          return new RoomOpenHelper.ValidationResult(false, "record(com.iomt.android.room.record.RecordEntity).\n"
                  + " Expected:\n" + _infoRecord + "\n"
                  + " Found:\n" + _existingRecord);
        }
        final HashMap<String, TableInfo.Column> _columnsBcrecord = new HashMap<String, TableInfo.Column>(4);
        _columnsBcrecord.put("timestamp", new TableInfo.Column("timestamp", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBcrecord.put("value", new TableInfo.Column("value", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBcrecord.put("is_sync", new TableInfo.Column("is_sync", "INTEGER", true, 0, "0", TableInfo.CREATED_FROM_ENTITY));
        _columnsBcrecord.put("id", new TableInfo.Column("id", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBcrecord = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBcrecord = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBcrecord = new TableInfo("bcrecord", _columnsBcrecord, _foreignKeysBcrecord, _indicesBcrecord);
        final TableInfo _existingBcrecord = TableInfo.read(_db, "bcrecord");
        if (! _infoBcrecord.equals(_existingBcrecord)) {
          return new RoomOpenHelper.ValidationResult(false, "bcrecord(com.iomt.android.room.bcrecord.BcRecordEntity).\n"
                  + " Expected:\n" + _infoBcrecord + "\n"
                  + " Found:\n" + _existingBcrecord);
        }
        final HashMap<String, TableInfo.Column> _columnsStatistics = new HashMap<String, TableInfo.Column>(5);
        _columnsStatistics.put("timestamp", new TableInfo.Column("timestamp", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStatistics.put("all_count", new TableInfo.Column("all_count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStatistics.put("sync_count", new TableInfo.Column("sync_count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStatistics.put("device_char_link_id", new TableInfo.Column("device_char_link_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStatistics.put("id", new TableInfo.Column("id", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStatistics = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysStatistics.add(new TableInfo.ForeignKey("device_characteristic_link", "CASCADE", "NO ACTION",Arrays.asList("device_char_link_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesStatistics = new HashSet<TableInfo.Index>(1);
        _indicesStatistics.add(new TableInfo.Index("index_statistics_device_char_link_id", false, Arrays.asList("device_char_link_id"), Arrays.asList("ASC")));
        final TableInfo _infoStatistics = new TableInfo("statistics", _columnsStatistics, _foreignKeysStatistics, _indicesStatistics);
        final TableInfo _existingStatistics = TableInfo.read(_db, "statistics");
        if (! _infoStatistics.equals(_existingStatistics)) {
          return new RoomOpenHelper.ValidationResult(false, "statistics(com.iomt.android.room.statistics.StatisticsEntity).\n"
                  + " Expected:\n" + _infoStatistics + "\n"
                  + " Found:\n" + _existingStatistics);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "aaef980fdd5522053574bb0f8be2e9c6", "341abbbec940c2af08a6b09adccd0573");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "characteristic","device","device_characteristic_link","record","bcrecord","statistics");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `characteristic`");
      _db.execSQL("DELETE FROM `device`");
      _db.execSQL("DELETE FROM `device_characteristic_link`");
      _db.execSQL("DELETE FROM `record`");
      _db.execSQL("DELETE FROM `bcrecord`");
      _db.execSQL("DELETE FROM `statistics`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(CharacteristicDao.class, CharacteristicDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DeviceDao.class, DeviceDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DeviceCharacteristicLinkDao.class, DeviceCharacteristicLinkDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RecordDao.class, RecordDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BcRecordDao.class, BcRecordDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StatisticsDao.class, StatisticsDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public CharacteristicDao characteristicDao() {
    if (_characteristicDao != null) {
      return _characteristicDao;
    } else {
      synchronized(this) {
        if(_characteristicDao == null) {
          _characteristicDao = new CharacteristicDao_Impl(this);
        }
        return _characteristicDao;
      }
    }
  }

  @Override
  public DeviceDao deviceDao() {
    if (_deviceDao != null) {
      return _deviceDao;
    } else {
      synchronized(this) {
        if(_deviceDao == null) {
          _deviceDao = new DeviceDao_Impl(this);
        }
        return _deviceDao;
      }
    }
  }

  @Override
  public DeviceCharacteristicLinkDao deviceCharacteristicLinkDao() {
    if (_deviceCharacteristicLinkDao != null) {
      return _deviceCharacteristicLinkDao;
    } else {
      synchronized(this) {
        if(_deviceCharacteristicLinkDao == null) {
          _deviceCharacteristicLinkDao = new DeviceCharacteristicLinkDao_Impl(this);
        }
        return _deviceCharacteristicLinkDao;
      }
    }
  }

  @Override
  public RecordDao recordDao() {
    if (_recordDao != null) {
      return _recordDao;
    } else {
      synchronized(this) {
        if(_recordDao == null) {
          _recordDao = new RecordDao_Impl(this);
        }
        return _recordDao;
      }
    }
  }

  @Override
  public BcRecordDao bcrecordDao() {
    if (_bcRecordDao != null) {
      return _bcRecordDao;
    } else {
      synchronized(this) {
        if(_bcRecordDao == null) {
          _bcRecordDao = new BcRecordDao_Impl(this);
        }
        return _bcRecordDao;
      }
    }
  }

  @Override
  public StatisticsDao statisticsDao() {
    if (_statisticsDao != null) {
      return _statisticsDao;
    } else {
      synchronized(this) {
        if(_statisticsDao == null) {
          _statisticsDao = new StatisticsDao_Impl(this);
        }
        return _statisticsDao;
      }
    }
  }
}
