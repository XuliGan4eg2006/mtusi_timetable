package com.example.mtusi_timetable

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mtusi_timetable.screens.CheckNetConn
import com.example.mtusi_timetable.screens.Greeting
import com.example.mtusi_timetable.screens.InfoScreen
import com.example.mtusi_timetable.screens.SelectGroup
import com.example.mtusi_timetable.screens.TimeTable
import com.example.mtusi_timetable.ui.theme.Mtusi_timetableTheme
import com.example.mtusi_timetable.ui.theme.backColor

const val serverUrl = "http://87.251.77.69:8000"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Mtusi_timetableTheme {
                MainScreen()
            }
        }
    }
}

fun checkNotificationPermission(context: Context){
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 12)
        }
    }
    else{
        Log.i("NotificationStatus","Have Permission")
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val navController = rememberNavController()
    var startScreen by remember { mutableStateOf("Greeting") }

    Log.i("checkNetwork", checkNetwork(context = LocalContext.current).toString())


    Box(modifier = Modifier
        .fillMaxSize()
        .background(backColor)) {

        startScreen = if (!checkNetwork(context = context)) {
            "CheckNetwork"
        } else {
            val sharedPreferences =
                LocalContext.current.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
            val group = sharedPreferences.getString("group", "none").toString()

            if (group == "none") {
                "HelloScreen"
            } else {
                "TimeTable"
            }
        }
        NavHost(navController = navController, startDestination = startScreen) {
            composable("HelloScreen") { Greeting(navController = navController) }
            composable("SelectGroup") { SelectGroup(navController = navController) }
            composable("TimeTable") { TimeTable(navController = navController) }
            composable("InfoScreen") { InfoScreen(navController = navController) }
            composable("CheckNetwork"){ CheckNetConn(navController = navController)}
        }
    }
}
