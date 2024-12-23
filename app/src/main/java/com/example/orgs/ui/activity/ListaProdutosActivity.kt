package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.example.orgs.R
import com.example.orgs.databas.AppDatabase
import com.example.orgs.databinding.ActivityListaProdutosBinding
import com.example.orgs.extensions.vaiPara
import com.example.orgs.model.Produto
import com.example.orgs.ui.recyclerview.adapter.ListaProdutosAdapter
import dataStore
import kotlinx.coroutines.CoroutineScope

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import usuarioLogadoPreferences

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

    private val usuarioDao by lazy {
        AppDatabase.instancia(this).usuarioDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraRecyclerView()
        configuraFab()

        lifecycleScope.launch {
            launch {
                verificaUsuarioLogado()
            }
        }
    }

    private suspend fun verificaUsuarioLogado() {
        dataStore.data.collect { preferences ->
            preferences[usuarioLogadoPreferences]?.let { usuarioId ->
                buscaUsuario(usuarioId)
            } ?: vaiParaLogin()
        }
    }

    private fun buscaUsuario(usuarioId: String) {
        lifecycleScope.launch {
            usuarioDao.buscaPorId(usuarioId).firstOrNull()?.let {
                launch {
                    buscaProdutosUsuario()
                }
            }
        }
    }

    private fun CoroutineScope.buscaProdutosUsuario() {
        launch {
            produtoDao.buscaTodos().collect { produtos ->
                adapter.atualiza(produtos)
            }
        }
    }

    private fun vaiParaLogin() {
        vaiPara(LoginActivity::class.java)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_lista_produto, menu)
        menuInflater.inflate(R.menu.menu_lista_ordenacao, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_lista_produtos_sair_do_app -> {
                lifecycleScope.launch {
                    deslogaUsuario()
                }
            }
        }

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

    private suspend fun deslogaUsuario() {
        dataStore.edit { preferences ->
            preferences.remove(usuarioLogadoPreferences)
        }
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