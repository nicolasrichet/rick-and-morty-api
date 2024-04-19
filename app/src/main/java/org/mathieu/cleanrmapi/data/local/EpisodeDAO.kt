package org.mathieu.cleanrmapi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.mathieu.cleanrmapi.data.local.objects.CharacterObject

@Dao
interface EpisodeDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(episodes: List<EpisodeObject>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createLink(link: List<CharacterEpisodeObject>)


    @Query("select * from ${RMDatabase.EPISODE_TABLE} where id in (select episodeId from ${RMDatabase.CHARACTER_EPISODE_TABLE} where characterId = :characterId)")
    suspend fun getEpisodesForCharacter(characterId: Int): List<EpisodeObject>

}