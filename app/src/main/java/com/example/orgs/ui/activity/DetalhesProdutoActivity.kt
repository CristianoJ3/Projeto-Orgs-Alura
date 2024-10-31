package com.example.orgs.ui.activity

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.orgs.R
import com.example.orgs.databinding.ActivityDetalhesProdutoBinding
import com.example.orgs.extensions.formataParaMoedaBrasileira
import com.example.orgs.extensions.tentaCarregarImagem
import com.example.orgs.model.Produto
import java.math.BigDecimal

class DetalhesProdutoActivity : AppCompatActivity() {
    private lateinit var imageLoader: ImageLoader

    private val binding by lazy {
        ActivityDetalhesProdutoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.i("Formulario", "DetalhesProdutoActivity criada")

        imageLoader = ImageLoader.Builder(this)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
        tentaCarregarProduto()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalhes_produto, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_detalhes_produto_editar -> {

            }

            R.id.menu_detalhes_produto_remover -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun tentaCarregarProduto() {
        // tentativa de buscar o produto se ele existir,
        // caso contrário, finalizar a Activity
        if (SDK_INT >= 33) {
            val produtoCarregado = intent.getParcelableExtra(CHAVE_PRODUTO, Produto::class.java)
            produtoCarregado?.let {
                Log.i("Formulario", "Produto Carregado: $produtoCarregado")

                preencheCampos(it)
            } ?: finish()
            Log.i("Formulario", "tentaCarregarProduto: $produtoCarregado")

        } else {
            @Suppress("DEPRECATION")
            val produtoCarregado = intent.getParcelableExtra<Produto>(CHAVE_PRODUTO)
            produtoCarregado?.let {
                Log.i("Formulario", "Produto Carregado: $produtoCarregado")

                preencheCampos(it)
            } ?: finish()
            Log.i("Formulario", "tentaCarregarProduto: $produtoCarregado")

        }
    }
    private fun preencheCampos(produtoCarregado: Produto) {
        with(binding) {
            detalhesProdutoImagem.tentaCarregarImagem(produtoCarregado.imagem, imageLoader)
            detalhesProdutoNome.text = produtoCarregado.nome
            detalhesProdutoDescricao.text = produtoCarregado.descricao
            detalhesProdutoValor.text = produtoCarregado.valor.formataParaMoedaBrasileira()
        }
    }
}