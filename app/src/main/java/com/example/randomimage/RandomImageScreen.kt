package com.example.randomimage

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter

enum class ImageCategory(val displayName: String) {
    ALBUM("Album"),
    ANIMAL("Animal"),
    BAKERY("Bakery"),
    BOOK("Book"),
    COUNTRY("Country"),
    FLOWER("Flower"),
    FOOD("Food"),
    GAME("Game"),
    MOVIE("Movie"),
    MUSIC("Music"),
    SPORT("Sport"),
    CUSTOM("Custom")
}

@Composable
fun RandomImageScreen() {
    val context = LocalContext.current
    val categoryList = ImageCategory.values().toList()
    var selectedCategory by remember { mutableStateOf(ImageCategory.ANIMAL) }
    var customCategory by remember { mutableStateOf("") }
    var width by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var imageUrl by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Random Image App",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Select Category",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 12.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded = true })
                .padding(vertical = 16.dp, horizontal = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = selectedCategory.displayName,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Arrow",
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(),
        ) {
            categoryList.forEach { category ->
                DropdownMenuItem(
                    onClick = {
                        selectedCategory = category
                        expanded = false
                    }
                ) {
                    Text(text = category.displayName)
                }
            }
        }

        if (selectedCategory == ImageCategory.CUSTOM) {
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = customCategory,
                onValueChange = { customCategory = it },
                label = { Text(text = "Custom Category") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = width,
            onValueChange = { width = it },
            label = { Text(text = "Width (8-2000 pixels)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = height,
            onValueChange = { height = it },
            label = { Text(text = "Height (8-2000 pixels)") },
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = errorMessage,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.error,
            modifier = Modifier.padding(top = 8.dp)
        )

        Button(
            onClick = {
                errorMessage = ""
                if (width.isNotEmpty() && height.isNotEmpty()) {
                    val widthInt = width.toIntOrNull()
                    val heightInt = height.toIntOrNull()

                    if (widthInt != null && heightInt != null && widthInt in 8..2000 && heightInt in 8..2000) {
                        val category =
                            if (selectedCategory == ImageCategory.CUSTOM) customCategory else selectedCategory.displayName
                        imageUrl = "https://loremflickr.com/$widthInt/$heightInt/$category"
                    } else {
                        errorMessage =
                            "Please enter a value between 8 and 2000 pixels."
                    }
                } else {
                    errorMessage = "Please enter width and height."
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Show Image")
        }
        if (imageUrl.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = "Random Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(shape = MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}
