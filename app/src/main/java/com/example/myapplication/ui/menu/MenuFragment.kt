package com.example.myapplication.ui.menu

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMenuBinding
import com.example.myapplication.db.entity.MealState
import com.example.myapplication.db.util.HistoryListAdapter
import com.example.myapplication.db.util.MealAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MenuFragment : Fragment() {

    private val viewModel: MenuViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MenuScreen(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(viewModel: MenuViewModel) {
    val context = LocalContext.current
    val meals by viewModel.meals.observeAsState(emptyList())
    val error by viewModel.error.observeAsState("")
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menu") },
                actions = {
                    IconButton(onClick = { showDialog = true }) { // TODO: Must be in navbar (MenuHost)
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            if (error.isNotEmpty()) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            }

            MealList(meals, viewModel)

            if (showDialog) {
                AddAlertDialog(onDismiss = { showDialog = false }, viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MealList(meals: List<MealState>, viewModel: MenuViewModel) {
    LazyColumn(Modifier.fillMaxSize()) {
        items(meals) { meal ->
            MealItem(
                meal = meal,
                onEdit = { updatedMeal ->
                    viewModel.updateMeal(updatedMeal.id, updatedMeal.name)
                },
                onDelete = { mealToDelete ->
                    viewModel.deleteMeal(mealToDelete)
                }
            )
        }
    }
}

@Composable
fun MealItem(meal: MealState, onEdit: (MealState) -> Unit, onDelete: (MealState) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxWidth().padding(8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = meal.name, modifier = Modifier.weight(1f))

            Box {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Options")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        onClick = { showEditDialog = true },
                        text = { Text("Edit") }
                    )
                    DropdownMenuItem(
                        onClick = { showDeleteDialog = true },
                        text = { Text("Delete") }
                    )

                    if (showEditDialog) {
                        EditMealDialog(
                            meal = meal,
                            onDismiss = { showEditDialog = false },
                            onConfirm = { newName ->
                                onEdit(meal.copy(name = newName))
                            }
                        )
                    }

                    if (showDeleteDialog) {
                        DeleteMealDialog(
                            meal = meal,
                            onDismiss = { showDeleteDialog = false },
                            onConfirm = {
                                onDelete(meal)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EditMealDialog(
    meal: MealState,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var newName by remember { mutableStateOf(meal.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Rename ${meal.name}") },
        text = {
            Column {
                Text("Enter new name for the meal:")
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("New Name") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(newName)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun DeleteMealDialog(
    meal: MealState,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete ${meal.name}") },
        text = { Text("Are you sure you want to delete '${meal.name}'?") },
        confirmButton = {
            Button(onClick = {
                onConfirm()
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AddAlertDialog(onDismiss: () -> Unit, viewModel: MenuViewModel) {
    val context = LocalContext.current
    var textState by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add new meal") },
        text = {
            Column {
                OutlinedTextField(
                    value = textState,
                    onValueChange = { textState = it },
                    label = { Text("Meal Name") }
                )
                if (textState.isEmpty()) {
                    Text("Name cannot be empty", color = Color.Red)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (textState.isNotEmpty()) {
                        viewModel.addNewMeal(textState)
                        onDismiss()
                    } else {
                        Toast.makeText(context, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
