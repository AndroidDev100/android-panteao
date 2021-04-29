package panteao.make.ready.utils.helpers.downloads.room

import android.content.Context
import androidx.room.*
import javax.inject.Singleton
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration


@Database(entities = [DownloadedVideo::class, DownloadedEpisodes::class], version = 7, exportSchema = false)
@Singleton
abstract class DownloadDatabase : RoomDatabase() {
    abstract fun downloadVideoDao(): DownloadVideoDao
    abstract fun downloadEpisodeDao(): DownloadEpisodeDao

    companion object {
        @Volatile
        private var instance: DownloadDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
                ?: synchronized(LOCK) {
            instance
                    ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
                DownloadDatabase::class.java, "enveu.db")
                .build()
        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE downloadedVideos " + " ADD COLUMN download_status INTEGER")
            }
        }
    }

}