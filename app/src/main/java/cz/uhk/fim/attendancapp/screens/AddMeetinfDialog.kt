package cz.uhk.fim.attendancapp.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import cz.uhk.fim.attendancapp.model.Meeting
import java.util.Calendar

@Composable
fun AddMeetingDialog(
    onDismiss: () -> Unit,
    onSave: (Meeting) -> Unit,
    existingMeetings: List<Meeting>,
    existingMeeting: Meeting? = null
) {
    var title by remember { mutableStateOf(existingMeeting?.title ?: "") }
    var selectedDate by remember { mutableStateOf(existingMeeting?.date ?: "") }
    var description by remember { mutableStateOf(existingMeeting?.description ?: "") }

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(if (existingMeeting == null) "Přidat novou schůzku" else "Upravit schůzku") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Název schůzky") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = selectedDate,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Datum") },
                    leadingIcon = {
                        IconButton(onClick = {
                            showDatePicker(context) { date -> selectedDate = date }
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
                            showDatePicker(context) { date -> selectedDate = date }
                        }
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Popis") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && selectedDate.isNotBlank() && description.isNotBlank()) {
                        val id = existingMeeting?.id ?: (existingMeetings.maxOfOrNull { it.id } ?: 0) + 1
                        val newMeeting = Meeting(id = id, title = title, date = selectedDate, description = description)
                        onSave(newMeeting)
                    }
                }
            ) {
                Text(if (existingMeeting == null) "Uložit" else "Upravit")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Zrušit")
            }
        }
    )
}

private fun showDatePicker(context: android.content.Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDayOfMonth ->
            val formattedDate = "${selectedDayOfMonth.toString().padStart(2, '0')}." +
                    "${(selectedMonth + 1).toString().padStart(2, '0')}.$selectedYear"
            onDateSelected(formattedDate)
        },
        year, month, day
    ).show()
}
