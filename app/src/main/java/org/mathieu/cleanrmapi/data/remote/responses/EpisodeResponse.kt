package org.mathieu.cleanrmapi.data.remote.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Modèle représentant un épisode de la série.
 *
 * @property id Identifiant unique de l'épisode.
 * @property name Nom de l'épisode.
 * @property airDate Date de diffusion de l'épisode.
 * @property episode Numéro de l'épisode.
 * @property characters Liste des URL des personnages apparaissant dans cet épisode.
 * @property url URL de l'épisode.
 * @property created Date de création de l'épisode.
 */
@Serializable
data class EpisodeResponse(
    val id: Int,
    val name: String,
    @SerialName("air_date")
    val airDate: String,
    val episode: String,
    val characters: List<String>,
    val url: String,
    val created: String
)