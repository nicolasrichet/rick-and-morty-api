package org.mathieu.cleanrmapi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import org.mathieu.cleanrmapi.data.local.objects.CharacterObject

@Database(
    entities = [
        CharacterObject::class,
        EpisodeObject::class,
        CharacterObject::class
    ],
    version = 2,
    exportSchema = false
)
abstract class RMDatabase: RoomDatabase() {

    abstract fun characterDAO(): CharacterDAO

    companion object {
        const val CHARACTER_TABLE = "character_table"
        const val EPISODE_TABLE = "episode_table"
        const val CHARACTER_EPISODE_TABLE = "character_episode_table"
    }

}