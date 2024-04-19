package org.mathieu.cleanrmapi.ui.screens.characters

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.mathieu.cleanrmapi.domain.models.character.Character
import org.mathieu.cleanrmapi.ui.core.Destination
import org.mathieu.cleanrmapi.ui.core.composables.PreviewContent
import org.mathieu.cleanrmapi.ui.core.navigate
import org.mathieu.cleanrmapi.ui.core.theme.Purple40

private typealias UIState = CharactersState
private typealias UIAction = CharactersAction

@Composable //Décrit la structure de l'interface utilisateur.
fun CharactersScreen(navController: NavController) {
    // Création d'une instance du ViewModel associé à cet écran
    val viewModel: CharactersViewModel = viewModel()

    // Récupération de l'état actuel du ViewModel en tant que State
    val state by viewModel.state.collectAsState()

    // Effet lancé au démarrage de cette composable
    LaunchedEffect(viewModel) {
        // Observation des événements émis par le ViewModel
        viewModel.events
            .onEach { event ->
                // Si un événement de navigation vers les détails d'un personnage est émis,
                // naviguer vers l'écran des détails correspondant
                if (event is Destination.CharacterDetails)
                    navController.navigate(destination = event)
            }
            // Collecte des événements
            .collect()
    }

    // Affichage du contenu de l'écran en passant l'état actuel et la fonction de gestion des actions
    CharactersContent(
        state = state,
        onAction = viewModel::handleAction
    )
}

// Cette annotation supprime les avertissements de l'IDE concernant les paramètres de lambda non utilisés
@SuppressLint("UnusedContentLambdaTargetStateParameter")
// Cette annotation indique l'utilisation de fonctionnalités expérimentales de Jetpack Compose
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
// Définition de la fonction composable CharactersContent
@Composable
private fun CharactersContent(
    // État de l'interface utilisateur par défaut
    state: UIState = UIState(),
    // Fonction pour gérer les actions de l'interface utilisateur
    onAction: (UIAction) -> Unit = { }
) = Scaffold(topBar = {
    // Barre supérieure affichant le titre "Characters"
    Text(
        modifier = Modifier
            .background(Purple40) // Couleur d'arrière-plan
            .padding(16.dp) // Marge intérieure
            .fillMaxWidth(), // Remplissage de la largeur maximale
        text = "Characters", // Texte affiché
        textAlign = TextAlign.Center, // Alignement du texte au centre
        color = Color.White, // Couleur du texte
        fontSize = 16.sp, // Taille de la police
        fontWeight = FontWeight.Medium // Poids de la police
    )
}) { paddingValues ->
    // Contenu principal de l'écran

    // Boîte occupant tout l'espace disponible et centrée
    Box(
        modifier = Modifier
            .fillMaxSize() // Remplissage de la taille maximale
            .padding(paddingValues), // Marge intérieure
        contentAlignment = Alignment.Center // Alignement du contenu au centre
    ) {
        // Contenu animé basé sur l'état du message d'erreur
        AnimatedContent(targetState = state.error != null, label = "") {
            // Si un message d'erreur est présent, affiche-le
            state.error?.let { error ->
                Text(
                    modifier = Modifier.padding(16.dp), // Marge intérieure
                    text = error, // Texte d'erreur
                    textAlign = TextAlign.Center, // Alignement du texte au centre
                    color = Purple40, // Couleur du texte
                    fontSize = 32.sp, // Taille de la police
                    fontWeight = FontWeight.Medium, // Poids de la police
                    lineHeight = 36.sp // Hauteur de ligne
                )
            } ?: LazyColumn {
                // Si aucun message d'erreur, affiche la liste des personnages

                // Liste paresseuse affichant les éléments de l'état des personnages
                itemsIndexed(state.characters) { index, character ->
                    // Carte représentant chaque personnage
                    CharacterCard(
                        modifier = Modifier
                            .padding(8.dp) // Marge extérieure
                            .clickable { // Rendre la carte cliquable
                                // Lorsque la carte est cliquée, effectuer une action sur le personnage sélectionné
                                onAction(CharactersAction.SelectedCharacter(character))
                            },
                        character = character // Personnage à afficher dans la carte
                    )
                    // Detect when scrolled to the bottom
                    if (index == state.characters.size - 1) {
                        onAction(CharactersAction.LoadMoreCharacters)
                    }
                }
            }
        }
    }
}

// Définition de la fonction composable CharacterCard
@Composable
private fun CharacterCard(
    // Modifier permettant de définir les propriétés de la carte
    modifier: Modifier,
    // Caractère représentant les données à afficher dans la carte
    character: Character
) =
    // Création d'une rangée pour afficher les informations du personnage
    Row(
        // Modifier permettant de définir les propriétés de la rangée
        modifier = modifier
            // Ombre autour de la carte
            .shadow(5.dp)
            // Fond blanc de la carte
            .background(Color.White)
            // Remplissage maximal de la largeur
            .fillMaxWidth()
            // Marge intérieure horizontale de 16dp et verticale de 12dp
            .padding(horizontal = 16.dp, vertical = 12.dp),
        // Alignement vertical du contenu au centre
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Affichage asynchrone de l'image du personnage
        SubcomposeAsyncImage(
            // Modifier définissant la taille de l'image et son contour circulaire
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape),
            // URL de l'image du personnage
            model = character.avatarUrl,
            // Description de contenu nulle car l'image est décorative
            contentDescription = null
        )

        // Espacement horizontal de 12dp
        Spacer(modifier = Modifier.width(12.dp))

        // Affichage du nom du personnage
        Text(text = character.name)
    }



// Fonction composable utilisée pour afficher un aperçu de l'écran des personnages
@Preview
@Composable
private fun CharactersPreview() = PreviewContent {
    // Affichage du contenu de l'écran des personnages dans l'aperçu
    CharactersContent()
}


