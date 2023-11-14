# Rapport Labo 03 DAA
- Flavio Sovilla
- Kevin Ferati
- Malo Romano

## 4. Questions complémentaires  
  
### 4.1 Pour le champ remark, destiné à accueillir un texte pouvant être plus long qu’une seule ligne, quelle configuration particulière faut-il faire dans le fichier XML pour que son comportement soit correct ? Nous pensons notamment à la possibilité de faire des retours à la ligne, d’activer le correcteur orthographique et de permettre au champ de prendre la taille nécessaire.

Nous avons ajouté android:inputType="textMultiLine". Ceci va permettre à notre texte d'avoir des lignes multiples et typqieuement ajouter des retours à la ligne. Le correcteur orthographique est normalement acttivé par défaut dans le widget. Nous n'avons donc pas à nous soucier de cela. Quant à la taille nécessaire, nous "matchons" le parent pour la longueur et faisons un "wrap_content" pour la hauteur. Ce qui fait que le texte va s'aggrandir en fonction de ce que l'on rentre."

### 4.2 Pour afficher la date sélectionnée via le DatePicker nous pouvons utiliser un DateFormat permettant par exemple d’afficher 12 juin 1996 à partir d’une instance de Date. Le formatage des dates peut être relativement différent en fonction des langues, la traduction des mois par exemple, mais également des habitudes régionales différentes : la même date en anglais britannique serait 12th June 1996 et en anglais américain June 12, 1996. Comment peut-on gérer cela au mieux ?



### 4.3.a. Si vous avez utilisé le DatePickerDialog1 du SDK. En cas de rotation de l’écran du smartphone lorsque le dialogue est ouvert, une exception android.view.WindowLeaked sera présente dans les logs, à quoi est-elle due ?
Cela provient du fait que l'on essaie de montrer une boîte de dialogue après que l'activité l'ait détruite et qu'une nouvelle ait été crée. Tout cela pendant que le dialogue "DatePickerDialog" reste ouvert, va créer des opérations asynchrones. 




### 4.4 Lors du remplissage des champs textuels, vous pouvez constater que le bouton « suivant » présent sur le clavier virtuel permet de sauter automatiquement au prochain champ à saisir, cf. Fig. 2. Est-ce possible de spécifier son propre ordre de remplissage du questionnaire ? Arrivé sur le dernier champ, est-il possible de faire en sorte que ce bouton soit lié au bouton de validation du questionnaire ?



### 4.5 Pour les deux Spinners (nationalité et secteur d’activité), comment peut-on faire en sorte que le premier choix corresponde au choix null, affichant par exemple « Sélectionner » ? Comment peut-on gérer cette valeur pour ne pas qu’elle soit confondue avec une réponse ?

Nous avons créé un Adapter custom pour la liste d'options du Spinner. Cet adapter intègre un élément à l'index 0. L'Adapter custom indique toujours que l'option est désactivé, et il ne l'affiche dans la vue "dropdown" des items. Il n'est donc pas possible de la séléctionner.

