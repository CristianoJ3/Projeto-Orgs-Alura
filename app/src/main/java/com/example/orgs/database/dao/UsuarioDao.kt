package com.example.orgs.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.orgs.ui.theme.Usuario

@Dao
interface UsuarioDao {

    @Insert
    suspend fun salva(usuario: Usuario)
}