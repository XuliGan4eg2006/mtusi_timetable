@file:OptIn(ExperimentalFoundationApi::class)

package com.example.mtusi_timetable

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mtusi_timetable.ui.theme.Mtusi_timetableTheme
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
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
        println("Have Permission")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Greeting(context: Context) {
    //        checkNotificationPermission(context)
    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.w(TAG, "Fetching FCM registration token failed", task.exception)
            return@OnCompleteListener
        }

        // Get new FCM registration token
        val token = task.result
        Log.w(TAG, token.toString())
    })

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Gray)){

    val pagerState = rememberPagerState(pageCount = {3})

    HorizontalPager(state = pagerState, modifier = Modifier.align(Alignment.Center)) { page ->
            when (page) {
                0 -> Column(modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        val pageOffset = (
                                (pagerState.currentPage - page) + pagerState
                                    .currentPageOffsetFraction
                                ).absoluteValue

                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }) {
                    Text(text = "Page 1", modifier = Modifier.align(Alignment.CenterHorizontally), textAlign = TextAlign.Center)
                    Image(painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = "", modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                1 -> Column(modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        val pageOffset = (
                                (pagerState.currentPage - page) + pagerState
                                    .currentPageOffsetFraction
                                ).absoluteValue

                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }) {
                    Text(text = "Page 2", modifier = Modifier.align(Alignment.CenterHorizontally), textAlign = TextAlign.Center)
                    Image(painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = "", modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                2 -> Column(modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        val pageOffset = (
                                (pagerState.currentPage - page) + pagerState
                                    .currentPageOffsetFraction
                                ).absoluteValue

                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }) {
                    Text(text = "Page 3", modifier = Modifier.align(Alignment.CenterHorizontally), textAlign = TextAlign.Center)
                    Image(painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = "", modifier = Modifier.align(Alignment.CenterHorizontally))
                }

            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectGroup(context: Context, navController: NavController) {
    Box(modifier = Modifier
        .background(Color.Gray)
        .fillMaxSize()) {

        val groupList = remember { mutableListOf<String>()}
        var isReady by remember { mutableStateOf(false)}
        //getting data via makeRequest
        LaunchedEffect(true) {
            val result = JSONObject(withContext(Dispatchers.IO){makeRequest(context, "")})
            val groups = result.getJSONArray("groups")

            Log.i("groups", groups.toString())

            for (i in 0 until groups.length()) {
                groupList.add(groups.getString(i))
            }
            isReady = true
        }

        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            var expanded by remember { mutableStateOf(false) }
            var selectedIndex by remember { mutableIntStateOf(0) }

            Text(text = "Select Group")

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

                Button(modifier = Modifier.padding(top = 70.dp),onClick = {
                    val sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("group", groupList[selectedIndex])
                    editor.apply()
                    Log.i("groupUpdated", groupList[selectedIndex])
                    Toast.makeText(context, "Group Updated", Toast.LENGTH_SHORT).show()
                    navController.navigate("TimeTable")
                }) {
                    Text(text = "Submit")
                }
            }
        }
    }
}

@Composable
fun TimeTable(context: Context, navController: NavController) {
    val testData = Thread{ makeRequest(context, "")}.start()

    val classesList by remember { mutableStateOf(listOf<String>("test", "test1", "test2")) }

//        if (classesList.isEmpty()) {
//            Text(text = "Loading...")
//        }
    Column(modifier = Modifier.fillMaxSize()) {
        Card(colors = CardDefaults.cardColors(containerColor = Color.DarkGray), modifier = Modifier.padding(top = 5.dp, start = 5.dp)) {
            Text(text = "TimeTable")
        }
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(classesList) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                    modifier = Modifier.size(width = 400.dp, height = 500.dp),
                    border = BorderStroke(1.dp, Color.Transparent)
                ) {
                    Text(text = it)
                }
            }
        }
    }
}


@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "TimeTable") {
        composable("HelloScreen") { Greeting(context = LocalContext.current) }
        composable("SelectGroup") { SelectGroup(context = LocalContext.current, navController = navController) }
        composable("TimeTable") { TimeTable(context = LocalContext.current, navController = navController) }
    }
}



//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun GreetingPreview() {
//    Greeting(LocalContext.current)
//}