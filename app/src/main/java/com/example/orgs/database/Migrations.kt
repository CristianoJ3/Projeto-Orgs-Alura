package com.example.orgs.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""CREATE TABLE IF NOT EXISTS 'Usuario' (
            |'id' TEXTE NOT NULL, 
            |'nome' TEXT NOT NULL, 
            |'senha' TEXT NOT NULL, 
            |PRIMARY KEY('id'))""".trimMargin())
    }

}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Produto ADD COLUMN 'usuarioId' TEXT")
    }

}