package com.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ui.theme.Dimens
import com.ui.theme.MiRegistroTheme

@Composable
fun AvatarCircular(
    @DrawableRes imagen: Int,
    descripcion: String,
    modifier: Modifier = Modifier
){
    Image(
        painter = painterResource(imagen),
        contentDescription = descripcion,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(Dimens.tamanoAvatar)
            .shadow(elevation = 6.dp, shape = CircleShape)
            .clip(CircleShape)
            .border(
                Dimens.bordeAvatar, MaterialTheme.colorScheme.surface, CircleShape
            )
    )
}

@Preview(showBackground = true)
@Composable
fun AvatarCircularPreview(){
    MiRegistroTheme {
        AvatarCircular(
            android.R.drawable.sym_def_app_icon,
            "Foto de perfil",
            modifier = Modifier.padding(16.dp)
        )
    }
}