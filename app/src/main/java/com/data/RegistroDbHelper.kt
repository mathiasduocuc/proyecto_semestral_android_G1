
package com.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Registro(
    val id: Long = 0,
    val acceso: String,
    val clasificacion: String,
    val rangoEtario: String,
    val tipoCompania: String,
    val comprasOtraTienda: Boolean,
    val fecha: String,
    val hora: String,
    val observador: String
)

class RegistroDbHelper(context: Context) :
    SQLiteOpenHelper(context, NOMBRE_BD, null, VERSION_BD) {

    companion object {
        private const val NOMBRE_BD = "accesosur.db"
        private const val VERSION_BD = 1

        private const val TABLA_REGISTROS = "registros"

        private const val ID = "id"
        private const val ACCESO = "acceso"
        private const val CLASIFICACION = "clasificacion"
        private const val RANGO_ETARIO = "rango_etario"
        private const val TIPO_COMPANIA = "tipo_compania"
        private const val COMPRAS_OTRA_TIENDA = "compras_otra_tienda"
        private const val FECHA = "fecha"
        private const val HORA = "hora"
        private const val OBSERVADOR = "observador"

        private const val SQL_CREAR_REGISTROS =
            "CREATE TABLE $TABLA_REGISTROS (" +
                    "$ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$ACCESO TEXT NOT NULL, " +
                    "$CLASIFICACION TEXT NOT NULL, " +
                    "$RANGO_ETARIO TEXT NOT NULL, " +
                    "$TIPO_COMPANIA TEXT NOT NULL, " +
                    "$COMPRAS_OTRA_TIENDA INTEGER NOT NULL, " +
                    "$FECHA TEXT NOT NULL, " +
                    "$HORA TEXT NOT NULL, " +
                    "$OBSERVADOR TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREAR_REGISTROS)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
    }

    fun insertarRegistro(registro: Registro): Long {
        return writableDatabase.insert(
            TABLA_REGISTROS,
            null,
            valoresDe(registro)
        )
    }

    fun listarRegistros(): List<Registro> {
        return consultarRegistros(
            "SELECT * FROM $TABLA_REGISTROS ORDER BY $ID DESC",
            null
        )
    }

    fun buscarPorFecha(fecha: String): List<Registro> {
        return consultarRegistros(
            "SELECT * FROM $TABLA_REGISTROS WHERE $FECHA = ? ORDER BY $ID DESC",
            arrayOf(fecha)
        )
    }

    fun buscarPorObservador(correo: String): List<Registro> {
        return consultarRegistros(
            "SELECT * FROM $TABLA_REGISTROS WHERE $OBSERVADOR = ? ORDER BY $ID DESC",
            arrayOf(correo)
        )
    }

    private fun valoresDe(registro: Registro) = ContentValues().apply {
        put(ACCESO, registro.acceso)
        put(CLASIFICACION, registro.clasificacion)
        put(RANGO_ETARIO, registro.rangoEtario)
        put(TIPO_COMPANIA, registro.tipoCompania)
        put(
            COMPRAS_OTRA_TIENDA,
            if (registro.comprasOtraTienda) 1 else 0
        )
        put(FECHA, registro.fecha)
        put(HORA, registro.hora)
        put(OBSERVADOR, registro.observador)
    }

    private fun consultarRegistros(
        sql: String,
        args: Array<String>?
    ): List<Registro> {
        val lista = mutableListOf<Registro>()

        readableDatabase.rawQuery(sql, args).use { cursor ->
            while (cursor.moveToNext()) {
                lista.add(leerRegistro(cursor))
            }
        }

        return lista
    }

    private fun leerRegistro(cursor: Cursor): Registro {
        return Registro(
            id = cursor.getLong(cursor.getColumnIndexOrThrow(ID)),
            acceso = cursor.getString(cursor.getColumnIndexOrThrow(ACCESO)),
            clasificacion = cursor.getString(
                cursor.getColumnIndexOrThrow(CLASIFICACION)
            ),
            rangoEtario = cursor.getString(
                cursor.getColumnIndexOrThrow(RANGO_ETARIO)
            ),
            tipoCompania = cursor.getString(
                cursor.getColumnIndexOrThrow(TIPO_COMPANIA)
            ),
            comprasOtraTienda = cursor.getInt(
                cursor.getColumnIndexOrThrow(COMPRAS_OTRA_TIENDA)
            ) == 1,
            fecha = cursor.getString(cursor.getColumnIndexOrThrow(FECHA)),
            hora = cursor.getString(cursor.getColumnIndexOrThrow(HORA)),
            observador = cursor.getString(
                cursor.getColumnIndexOrThrow(OBSERVADOR)
            )
        )
    }
}


