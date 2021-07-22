package panteao.make.ready.utils.helpers.downloads.db;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DownloadItemEntity.class}, version = 1, exportSchema = false)
public abstract class DownloadDataBase extends RoomDatabase {
    private static final String LOG_TAG = DownloadDataBase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "penteaodownloaddb";
    private static DownloadDataBase sInstance;

    public static DownloadDataBase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        DownloadDataBase.class, DownloadDataBase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract DownloadDao downloadDao();
}