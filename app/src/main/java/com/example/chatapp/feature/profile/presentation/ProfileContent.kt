package com.example.chatapp.feature.profile.presentation

import android.media.Image
import android.widget.ImageView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ProfileContent(state: ProfileState) {
    Column (modifier = Modifier.fillMaxSize()){
        Text(text = state.name)
        Button(onClick = {}) { }
    }





}


@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfileContent(ProfileState(name = "Кукарцева Ольга", imageUrl = ""))

}