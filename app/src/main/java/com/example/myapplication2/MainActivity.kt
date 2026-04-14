package com.example.myapplication2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserApp() {
    val context = LocalContext.current

    var userList by remember { mutableStateOf(listOf<User>()) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val url = "https://jsonplaceholder.typicode.com/users"
        val queue = Volley.newRequestQueue(context)

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val tempList = mutableListOf<User>()
                for (i in 0 until response.length()) {
                    val user: JSONObject = response.getJSONObject(i)
                    tempList.add(
                        User(
                            name = user.getString("name"),
                            email = user.getString("email")
                        )
                    )
                }
                userList = tempList
            },
            {
                errorMessage = "Failed to load data"
            }
        )

        queue.add(request)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("User List API") })
        }
    ) { padding ->

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(userList) { user ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = "Name: ${user.name}")
                            Text(text = "Email: ${user.email}")
                        }
                    }
                }
            }
        }
    }
}

data class User(
    val name: String,
    val email: String
)