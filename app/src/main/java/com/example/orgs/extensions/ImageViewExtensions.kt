package com.example.orgs.extensions

import android.widget.ImageView
import coil.ImageLoader
import com.example.orgs.R
import coil.load


fun ImageView.tentaCarregarImagem(url: String? = null, imageLoader: ImageLoader) {

    load(url, imageLoader) {
        fallback(R.drawable.erro)
        error(R.drawable.erro)
        placeholder(R.drawable.placeholder)
    }
}