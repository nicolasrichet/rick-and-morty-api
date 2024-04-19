// Déclaration du package où se trouve ce fichier
package org.mathieu.cleanrmapi.ui.screens.characters

// Importations nécessaires
import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import org.mathieu.cleanrmapi.domain.models.character.Character
import org.mathieu.cleanrmapi.domain.repositories.CharacterRepository
import org.mathieu.cleanrmapi.ui.core.Destination
import org.mathieu.cleanrmapi.ui.core.ViewModel

// Interface scellée définissant les actions possibles pour les personnages
sealed interface CharactersAction {
    // Action pour sélectionner un personnage
    data class SelectedCharacter(val character: Character): CharactersAction
    object LoadMoreCharacters: CharactersAction
}

// ViewModel pour l'écran des personnages
class CharactersViewModel(application: Application) : ViewModel<CharactersState>(CharactersState(), application) {

    // Injection de dépendance pour le repository des personnages
    private val characterRepository: CharacterRepository by inject()

    // Initialisation
    init {
        // Collecte des données à partir du repository des personnages
        collectData(
            source = { characterRepository.getCharacters() }
        ) {
            // En cas de succès
            onSuccess {
                // Mettre à jour l'état avec la liste de personnages et effacer les erreurs
                updateState { copy(characters = it, error = null) }
            }
            // En cas d'échec
            onFailure {
                // Mettre à jour l'état avec une liste vide de personnages et le message d'erreur
                updateState { copy(characters = emptyList(), error = it.toString()) }
            }
            // Mettre à jour l'état pour indiquer que le chargement est terminé
            updateState { copy(isLoading = false) }
        }
    }

    // Fonction pour gérer les actions des personnages
    fun handleAction(action: CharactersAction) {
        when(action) {
            // Si l'action est de sélectionner un personnage
            is CharactersAction.SelectedCharacter -> selectedCharacter(action.character)
            CharactersAction.LoadMoreCharacters -> loadMoreCharacters()
        }
    }

    private fun loadMoreCharacters() {
        viewModelScope.launch {
            characterRepository.loadMore();
        }
    }

    // Fonction pour gérer la sélection d'un personnage
    private fun selectedCharacter(character: Character) =
        // Envoyer un événement pour naviguer vers les détails du personnage sélectionné
        sendEvent(Destination.CharacterDetails(character.id.toString()))

}

// État de l'écran des personnages
data class CharactersState(
    // Indique si les données sont en cours de chargement
    val isLoading: Boolean = true,
    // Liste des personnages
    val characters: List<Character> = emptyList(),
    // Message d'erreur en cas de problème
    val error: String? = null
)
