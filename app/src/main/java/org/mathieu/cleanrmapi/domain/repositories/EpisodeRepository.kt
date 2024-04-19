package org.mathieu.cleanrmapi.domain.repositories


interface EpisodeRepository {

    suspend fun getEpisodes(characterId : Int): List<Episode>
}