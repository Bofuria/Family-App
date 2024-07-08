# Family app

**Table of Contents**

[TOCM]

Small application (3 screens) that helps you to decide what should you eat for a dinner and keep track of all meals. The app also has feature to edit the menu by deleting and adding new meals. 

*Development stack: MVVM, Hilt Dagger, Room, Jetpack Compose, LiveData, Flow, Coroutines.*

### Structure

#### UI

Consists of 3 Fragments: Main, History, Menu. Menu Fragments is created with Compose M3 materials, while first two Fragments are following traditional apporach. 

- ##### Main Fragment
	Allows user to go through the list of meals and choose what meal he want for a dinner. User can either skip item, add to the history list or shuffle the whole list to strart all over again.
- ##### History Fragment
	 Allows user to see what and when did he add to the list. User can also manually add new meal to the history with separate button, or delete/edit existing one. 
- ##### Menu Fragment
	Here, all possible meals are displayed. User can add and delete items. 

#### Database

Room database is used to store all the data. Consists of three table, each corresponds to its Fragment.

#### Dependency Injection

DI is achieved by using Hilt Dagger. 




