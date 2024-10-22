package com.example.orgs.extensions

import android.widget.ImageView
import coil.ImageLoader
import com.example.orgs.R
import coil.load


fun ImageView.tentaCarregarImagem(
    url: String? = null, imageLoader: ImageLoader,
    // transformação de parâmetro com valor padrão para possibilitar a alteração do fallback
    // essa mesma técnica pode ser utilizada para o error e placeholder também
    fallback: Int = R.drawable.imagem_padrao
) {

    load(url, imageLoader) {
        fallback(fallback)
        error(R.drawable.erro)
        placeholder(R.drawable.placeholder)
    }
}