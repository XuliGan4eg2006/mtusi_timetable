package com.example.mtusi_timetable.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mtusi_timetable.makeRequest
import com.example.mtusi_timetable.serverUrl
import com.example.mtusi_timetable.ui.theme.backColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectGroup(navController: NavController) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .background(backColor)
            .fillMaxSize()
    ) {

        val groupList = remember { mutableListOf<String>() }
        var isReady by remember { mutableStateOf(false) }
        //getting data via makeRequest
        LaunchedEffect(true) {
            val jsonHandler = Json { this.ignoreUnknownKeys = true }

            val result =
                jsonHandler.decodeFromString<Map<String, List<String>>>(withContext(Dispatchers.IO) {
                    makeRequest("$serverUrl/groups")
                })

            for (i in 0 until result["groups"]!!.size) {
                groupList.add(result["groups"]!![i])
            }
            isReady = true
        }

        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            var expanded by remember { mutableStateOf(false) }
            var selectedIndex by remember { mutableIntStateOf(0) }

            Text(text = "Выберите группу", color = Color.White)

            AnimatedVisibility(visible = isReady) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    modifier = Modifier.padding(top = 10.dp),
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    TextField(
                        value = groupList[selectedIndex],
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }) {
                        groupList.forEachIndexed { index, s ->
                            DropdownMenuItem(
                                onClick = { selectedIndex = index; expanded = false },
                                text = { Text(text = s) })
                        }
                    }
                }

                Button(modifier = Modifier.padding(top = 70.dp), onClick = {
                    val sharedPreferences =
                        context.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("group", groupList[selectedIndex])
                    editor.apply()
                    Log.i("groupUpdated", groupList[selectedIndex])
                    Toast.makeText(context, "Group Updated", Toast.LENGTH_SHORT).show()
                    navController.navigate("TimeTable")
                }) {
                    Text(text = "Продолжить")
                }
            }
        }
    }
}