# Rapport Labo 03 DAA
### 14.11.2023
- Flavio Sovilla
- Kevin Ferati
- Malo Romano

## Questions complémentaires  
  
### 1. 
>_Pour le champ remark, destiné à accueillir un texte pouvant être plus long qu’une seule ligne, quelle configuration particulière faut-il faire dans le fichier XML pour que son comportement soit correct ? Nous pensons notamment à la possibilité de faire des retours à la ligne, d’activer le correcteur orthographique et de permettre au champ de prendre la taille nécessaire._

Pour le champ 'remark', qui doit contenir un texte devant être plus long qu’une seule ligne, nous devons ajouter l'attribut android:inputType="textMultiLine" dans le fichier XML. Cela permet au texte d'avoir des lignes multiples et d'inclure des retours à la ligne. Le correcteur orthographique est normalement activé par défaut dans ce widget, nous n'avons donc pas besoin de nous en soucier.  
Quant à la taille nécessaire, nous configurons android:layout_width="match_parent" pour la longueur et android:layout_height="wrap_content" pour la hauteur. Ainsi, le champ de texte s'adaptera en fonction du contenu que nous inséré. Ceci va assurer une adaptation dynamique à la quantité de texte entrée par l'utilisateur.


### 2. 
>_Pour afficher la date sélectionnée via le DatePicker nous pouvons utiliser un DateFormat permettant par exemple d’afficher 12 juin 1996 à partir d’une instance de Date. Le formatage des dates peut être relativement différent en fonction des langues, la traduction des mois par exemple, mais également des habitudes régionales différentes : la même date en anglais britannique serait 12th June 1996 et en anglais américain June 12, 1996. Comment peut-on gérer cela au mieux ?_

Pour gérer les différences de formatage des dates en fonction de la langue et des régions, nous utilisons Locale.getDefault() dans l'instantiation de l'objet SimpleDateFormat. Cette approche assure que le formatage des dates suivra les paramètres régionaux du dispositif sur lequel l'application est exécutée. En optant pour Locale.getDefault(), nous reprenons ainsi le formatage régional du téléphone de l'utilisateur. Ceci permettra d'afficher la date de l'utilisateur selon son choix de région. Il est notamment possible d'ajouter un format spécifique en indiquant la région souhaitée (exemple: SimpleDateFormat(DATE_FORMAT, Locale.US) pour afficher une date sous forme américaine)


### 3.
>_Si vous avez utilisé le DatePickerDialog1 du SDK. En cas de rotation de l’écran du smartphone lorsque le dialogue est ouvert, une exception android.view.WindowLeaked sera présente dans les logs, à quoi est-elle due ?_

La présence de l'exception android.view.WindowLeaked lors de l'utilisation du DatePickerDialog du SDK est généralement due au fait qu'on tente d'afficher une boîte de dialogue après la destruction de l'activité à laquelle elle est rattachée. Lorsqu'une activité est détruite (exemple: lors d'une rotation d'écran entraînant sa recréation) les anciennes instances de vues ou de dialogues peuvent causer des fuites de fenêtres si elles ne sont pas correctement gérées (état asynchrone entre la fenêtre et l'activité).  
Dans le cas spécifique du DatePickerDialog, il est crucial de s'assurer qu'il est détaché ou fermé avant que l'activité ne soit détruite. Cela devrait être effectué dans les méthodes du cycle de vie de l'activité, telles que onPause() ou onDestroy(). Lorsque nous négligeons cette gestion, des opérations asynchrones sont déclenchées pendant la création de la nouvelle activité, ce qui conduit à l'apparition de l'exception android.view.WindowLeaked dans les logs.


### 4. 
>_Lors du remplissage des champs textuels, vous pouvez constater que le bouton « suivant » présent sur le clavier virtuel permet de sauter automatiquement au prochain champ à saisir, cf. Fig. 2. Est-ce possible de spécifier son propre ordre de remplissage du questionnaire ? Arrivé sur le dernier champ, est-il possible de faire en sorte que ce bouton soit lié au bouton de validation du questionnaire ?_

Oui, nous pouvons spécifier notre propre ordre de remplissage du questionnaire grâce aux attributs 'android:nextFocusDown="@+id/monProchainRemplissage"'. Lorsqu'on atteint le dernier champ, il est possible d'ajouter un gestionnaire d'événements 'setOnEditorActionListener'. Cela va nous permettre de lier le bouton «OK» à l'action 'suivant' afin de déclencher la validation du questionnaire.


### 5. 
>_Pour les deux Spinners (nationalité et secteur d’activité), comment peut-on faire en sorte que le premier choix corresponde au choix null, affichant par exemple « Sélectionner » ? Comment peut-on gérer cette valeur pour ne pas qu’elle soit confondue avec une réponse ?_

Pour les deux Spinners (nationalité et secteur d’activité), nous avons mis en place une sélection nulle, affichant un message qui n'est pas séléctionnable pour l'utilisateur. Cela a été réalisé en créant un adaptateur personnalisé pour la liste d'options des Spinner, intégrant un élément à l'index 0. Et il est configuré comme désactivé, n'apparaissant ainsi pas dans la vue déroulante des options sélectionnables. Ceci nous garantit qu'il ne peut pas être choisi par l'utilisateur.

## Détails d'implémentation

### Utilisation du pattern MVC et explications des composants

Le pattern a été utilisé de la sorte :
- Le contrôleur a été représenté avec la classe `MainActivity`
- Le modèle est la classe fournie `Person` ainsi que ses enfants
- La vue correspondante est le fichier `activity_main.xml`

Nous avons activé la "View Binding" pour faciliter les communications entre la vue et le contrôleur.

La vue a été implémentée à l'aide d'un seul `ConstraintLayout` à l'intérieur d'un `ScrollView` pour permettre de scroller, notamment en orientation paysage ou lorsque le champ commentaire est grand. Pour gérer l'affichage par "colonne", nous utilisons un widget `Barrier`, dont un autre est également utilisé pour adapter l'espace verticale automatiquement. Nous gérons la visibilité des groupes spécifiques grâce au widget `Group`. Nous faisons également usage des styles pour factoriser le code.

L'activité est moins intéressante à analyser. Nous avons simplement l'initialisation des events : clique sur les boutons *save*, *cancel* et d'ouverture du dialogue.
### Externalisation des chaînes de caractères

Afin de séparer les chaînes de caractères affichées et l'implémentation, nous avons agrémenté le fichier strings.xml fourni pour ce laboratoire. Exemple d'une chaîne de caractère :

```xml
<string name="error_empty_fields">Veuillez remplir tous les champs obligatoires.</string>
```

Lorsqu'une chaîne devait contenir une ou plusieurs variables, nous avons utilisé des strings formatées ([String resources](https://developer.android.com/guide/topics/resources/string-resource)). Par exemple :

```xml
<string name="error_invalid_experience">Le nombre d\'années d\'expérience doit être entre %1$d et %2$d.</string>
```

Les valeurs peuvent ensuite être remplacées comme ceci :

```kotlin
resources.getString(R.string.error_invalid_experience, MIN_EXPERIENCE, MAX_EXPERIENCE)
```


