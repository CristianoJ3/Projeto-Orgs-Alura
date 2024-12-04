package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RestrictTo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.R
import com.example.orgs.databas.AppDatabase
import com.example.orgs.database.dao.ProdutoDao
import com.example.orgs.databinding.ActivityListaProdutosBinding
import com.example.orgs.model.Produto
import com.example.orgs.ui.recyclerview.adapter.ListaProdutosAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "ListaProdutosActivity"

class ListaProdutosActivity : AppCompatActivity(R.layout.activity_lista_produtos) {

    private val adapter = ListaProdutosAdapter(context = this)

    private val binding by lazy {
        ActivityListaProdutosBinding.inflate(layoutInflater)
    }

    private val produtoDao by lazy {
        val db = AppDatabase.instancia(this)
        db.produtoDao()
    }

    private val usuarioDao by lazy{
        AppDatabase.instancia(this).usuarioDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configuraRecyclerView()
        configuraFab()
        setContentView(binding.root)

        lifecycleScope.launch {
            launch {
                produtoDao.buscaTodos().collect { produtos ->
                    adapter.atualiza(produtos)
                }
            }
            intent.getStringExtra("CHAVE_USUARIO_ID")?.let { usuarioId ->
                usuarioDao.buscaPorId(usuarioId).collect {
                    Log.i("ListaProdutos", "onCreate: $it")
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_lista_ordenacao, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        lifecycleScope.launch {
            val produtosOrdenado: Flow<List<Produto>>? = when (item.itemId) {
                R.id.menu_lista_produtos_ordenar_nome_asc ->
                    produtoDao.buscaTodosOrdenadorPorNomeAsc()
                R.id.menu_lista_produtos_ordenar_nome_desc ->
                    produtoDao.buscaTodosOrdenadorPorNomeDesc()
                R.id.menu_lista_produtos_ordenar_descricao_asc ->
                    produtoDao.buscaTodosOrdenadorPorDescricaoAsc()
                R.id.menu_lista_produtos_ordenar_descricao_desc ->
                    produtoDao.buscaTodosOrdenadorPorDescricaoDesc()
                R.id.menu_lista_produtos_ordenar_valor_asc ->
                    produtoDao.buscaTodosOrdenadorPorValorAsc()
                R.id.menu_lista_produtos_ordenar_valor_desc ->
                    produtoDao.buscaTodosOrdenadorPorValorDesc()
                R.id.menu_lista_produtos_ordenar_sem_ordem ->
                    produtoDao.buscaTodos()
                else -> null
            }

            produtosOrdenado?.collect { it ->
                adapter.atualiza(it)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configuraFab() {
        val fab = binding.activityListaProdutosFab
        fab.setOnClickListener {
            vaiParaFormularioProduto()
        }
    }

    private fun vaiParaFormularioProduto() {
        val intent = Intent(this, FormularioProdutoActivity::class.java)
        startActivity(intent)
    }

    private fun configuraRecyclerView() {
        val recyclerView = binding.activityListaProdutosRecyclerView
        recyclerView.adapter = adapter

        // implementação do listener para abrir a Activity de detalhes do produto
        // com o produto clicado
        adapter.quandoClicaNoItem =
            {
                Log.i("Formulario", "Produto clicado: $it")

                val intent = Intent(this, DetalhesProdutoActivity::class.java).apply {
                    putExtra(CHAVE_PRODUTO_ID, it.id)
                }
                startActivity(intent)
            }
        adapter.quandoClicaEmEditar = {
            Log.i(TAG, "configuraRecyclerView: Editar $it")
        }
        adapter.quandoClicaEmRemover = {
            Log.i(TAG, "configuraRecyclerView: Remover $it")
        }
    }
}