package com.example.orgs.ui.activity

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.orgs.R
import com.example.orgs.databas.AppDatabase
import com.example.orgs.databinding.ActivityFormularioProdutoBinding
import com.example.orgs.extensions.tentaCarregarImagem
import com.example.orgs.model.Produto
import com.example.orgs.ui.dialog.FormularioImagemDialog
import java.math.BigDecimal

class FormularioProdutoActivity :
    AppCompatActivity(R.layout.activity_formulario_produto) {

    ///private lateinit var binding: ActivityFormularioProdutoBinding
    private val binding by lazy {
        ActivityFormularioProdutoBinding.inflate(layoutInflater)
    }

    private var url: String? = null
    private var idProduto = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ///binding = ActivityFormularioProdutoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Cadastrar produto"

        // Criar o ImageLoader
        val imageLoader = ImageLoader.Builder(this)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()

        configuraBotaoSalvar()

        binding.activityFormularioProdutoImagem.setOnClickListener() {
            FormularioImagemDialog(this)
                .mostra(url) {
                    imagem ->
                    url = imagem
                    binding.activityFormularioProdutoImagem.tentaCarregarImagem(url, imageLoader)
                }
        }
        intent.getParcelableExtra<Produto>(CHAVE_PRODUTO)?.let { produtoCarregado ->
            title = "Alterar produto"
            idProduto = produtoCarregado.id
            url = produtoCarregado.imagem
            binding.activityFormularioProdutoImagem
                .tentaCarregarImagem(url, imageLoader)
            binding.activityFormularioProdutoNome
                .setText(produtoCarregado.nome)
            binding.activityFormularioProdutoDescricao
                .setText(produtoCarregado.descricao)
            binding.activityFormularioProdutoValor
                .setText(produtoCarregado.valor.toPlainString())
        }
    }

    private fun configuraBotaoSalvar() {
        val botaoSalvar = binding.activityFormularioProdutoBotaoSalvar
        val db = AppDatabase.instancia(this)
        val produtoDao = db.produtoDao()
//        val dao = ProdutoDao()
        botaoSalvar.setOnClickListener {
            val produtoNovo = criaProduto()
            if(idProduto > 0){
                produtoDao.atualiza(produtoNovo)
            } else {
                produtoDao.salva(produtoNovo)
            }
            finish()
        }
    }

    private fun criaProduto(): Produto {
        val campoNome = binding.activityFormularioProdutoNome
        val nome = campoNome.text.toString()
        val campoDescricao = binding.activityFormularioProdutoDescricao
        val descricao = campoDescricao.text.toString()
        val campoValor = binding.activityFormularioProdutoValor
        val valorEmTexto = campoValor.text.toString()
        val valor = if (valorEmTexto.isBlank()) {
            BigDecimal.ZERO
        } else {
            BigDecimal(valorEmTexto)
        }

        return Produto(
            id = idProduto,
            nome = nome,
            descricao = descricao,
            valor = valor,
            imagem = url
        )
    }

}