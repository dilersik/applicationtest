package com.example.myapplication.views.ui

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.myapplication.R
import com.example.myapplication.model.Post
import com.example.myapplication.views.viewModel.MainViewModel

@Composable
fun DetailsScreen(
    post: Post,
    viewModel: MainViewModel,
    paddingValues: PaddingValues
) {
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = post.name,
            style = MaterialTheme.typography.headlineSmall
        )
        post.picture?.let {
            AsyncImage(
                model = it,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(top = 8.dp)
            )
        }
        Text(
            text = post.description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFFFC107)
            )
            Text(
                text = "${post.rating}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = post.address,
            modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_VIEW, post.googleMapsLink.toUri())
                ContextCompat.startActivity(context, intent, null)
            },
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            val intent = Intent(Intent.ACTION_VIEW, post.website.toUri())
            ContextCompat.startActivity(context, intent, null)
        }) {
            Text(stringResource(R.string.visit_website))
        }
    }

}

