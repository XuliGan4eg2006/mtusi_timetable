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
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mtusi_timetable.screens.CheckNetConn
import com.example.mtusi_timetable.screens.Greeting
import com.example.mtusi_timetable.screens.InfoScreen
import com.example.mtusi_timetable.screens.LoadingScreen
import com.example.mtusi_timetable.screens.SelectGroup
import com.example.mtusi_timetable.screens.TimeTable
import com.example.mtusi_timetable.screens.UpdateMe
import com.example.mtusi_timetable.ui.theme.Mtusi_timetableTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

const val serverUrl = "http://192.168.0.156:8000"


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  //for transparent status bar
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

    var networkAvailable by remember { mutableStateOf(false) }
    var isReady by remember { mutableStateOf(false) }
    val appVersion = context.packageManager.getPackageInfo(context.packageName, 0).versionCode
    var appActualVersion by remember {
        mutableIntStateOf(999)
    }

    if (checkNetwork(context = LocalContext.current)){
        networkAvailable = true
        println("Network available")
    } else {
        println("Network not available")
        startScreen = "CheckNetwork"
    }

    LaunchedEffect(key1 = true) {
        if (networkAvailable) {
            withContext(Dispatchers.IO) {
                val retAppVerJson = makeRequest("$serverUrl/app_version")
                val jsonHandler = Json { this.ignoreUnknownKeys = true }
                val decodedMap = jsonHandler.decodeFromString<Map<String, Int>>(
                    retAppVerJson
                )
                println("Actual version: ${decodedMap["actual_version"]}")

                appActualVersion = decodedMap["actual_version"]!!.toInt()
                isReady = true
            }
        }
    }


    if (startScreen != "CheckNetwork") {

        val sharedPreferences =
            context.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val group = sharedPreferences.getString("group", "none").toString()

        startScreen = if (!isReady) {
            "LoadingScreen"
        } else {
            if (appVersion < appActualVersion) {
                "UpdateMe"
            } else {
                if (group == "none") {
                    "HelloScreen"
                } else {
                    "TimeTable"
                }
            }
        }
    }

    NavHost(navController = navController, startDestination = startScreen) {
        composable("MainScreen") { MainScreen() }
        composable("UpdateMe") { UpdateMe(appVersion) }
        composable("LoadingScreen") { LoadingScreen() }
        composable("HelloScreen") { Greeting(navController = navController) }
        composable("SelectGroup") { SelectGroup(navController = navController) }
        composable("TimeTable") { TimeTable(navController = navController) }
        composable("InfoScreen") { InfoScreen(navController = navController) }
        composable("CheckNetwork"){ CheckNetConn(navController = navController)}
    }
}

