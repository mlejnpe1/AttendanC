package cz.uhk.fim.attendancapp.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cz.uhk.fim.attendancapp.model.Trip
import java.util.Calendar

@Composable
fun AddTripDialog(onDismiss: () -> Unit, onSave: (Trip) -> Unit, existingTrips: List<Trip>, existingTrip: Trip? = null) {
    var title by remember { mutableStateOf(existingTrip?.title ?: "") }
    var selectedDate by remember { mutableStateOf(existingTrip?.date ?: "") }
    var location by remember { mutableStateOf(existingTrip?.location ?: "") }

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Přidat novou výpravu") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Název výpravy") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = selectedDate,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Datum") },
                    leadingIcon = {
                        IconButton(onClick = {
                            val calendar = Calendar.getInstance()
                            val year = calendar.get(Calendar.YEAR)
                            val month = calendar.get(Calendar.MONTH)
                            val day = calendar.get(Calendar.DAY_OF_MONTH)

                            DatePickerDialog(
                                context,
                                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                                    selectedDate =
                                        "${selectedDayOfMonth.toString().padStart(2, '0')}.${(selectedMonth + 1).toString().padStart(2, '0')}.$selectedYear"
                                },
                                year, month, day
                            ).show()
                        }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Vybrat datum")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            val calendar = Calendar.getInstance()
                            val year = calendar.get(Calendar.YEAR)
                            val month = calendar.get(Calendar.MONTH)
                            val day = calendar.get(Calendar.DAY_OF_MONTH)

                            DatePickerDialog(
                                context,
                                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                                    selectedDate =
                                        "${selectedDayOfMonth.toString().padStart(2, '0')}.${(selectedMonth + 1).toString().padStart(2, '0')}.$selectedYear"
                                },
                                year, month, day
                            ).show()
                        }
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Místo") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && selectedDate.isNotBlank() && location.isNotBlank()) {
                        val id = existingTrip?.id ?: (existingTrips.maxOfOrNull { it.id } ?: 0) + 1
                        val newTrip = Trip(id = id, title = title, date = selectedDate, location = location)
                        onSave(newTrip)
                    }
                }
            ) {
                Text(if (existingTrip == null) "Uložit" else "Upravit")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Zrušit")
            }
        }
    )
}
