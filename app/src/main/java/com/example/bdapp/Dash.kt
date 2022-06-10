package com.example.bdapp

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bdapp.databinding.FragmentDashBinding
import androidx.navigation.fragment.findNavController as find


class Dash : Fragment() {
    private var _binding: FragmentDashBinding? = null
    private val binding get() = _binding!!
    protected lateinit var admin: AdminSqliteOpenHelper
    protected lateinit var bd: SQLiteDatabase
    protected var TABLE: String = "'estudiante'"

    private lateinit var nc: String
    private lateinit var nombre: String
    private lateinit var apellido: String
    private lateinit var semestre: String
    private lateinit var grupo: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDashBinding.inflate(inflater, container, false)
        admin = AdminSqliteOpenHelper(binding.root.context, "administracion", null, 1)
        mostrarLista()
        with(binding) {
            obtenerDatos()
            btnRegistrar.setOnClickListener { registrar() }
            btnNumC.setOnClickListener { buscarPorNControl() }
            btnNombre.setOnClickListener { buscarPorNombre() }
            btnDeleteNC.setOnClickListener { eliminar() }
            btnModificar.setOnClickListener { modificar() }
            btnListar.setOnClickListener { mostrarLista() }
        }
        return binding.root
    }

    //Metodo para validar que todos los campos esten llenos
    private fun validarDatos(): Boolean {
        return nc.isEmpty() || nombre.isEmpty() || apellido.isEmpty()
                || semestre.isEmpty() || grupo.isEmpty()
    }

    //Validar que el campo no este vacio
    private fun validarNC() = nc.isEmpty()

    //Validar que el campo no este vacio

    private fun obtenerDatos() {
        nc = binding.edtNC.text.toString()
        nombre = binding.edtNombre.text.toString()
        apellido = binding.edtAPM.text.toString()
        semestre = binding.cbxSemestre.selectedItem.toString()
        grupo = binding.cbxGrupo.selectedItem.toString()
    }
    // Obtener valores de los campos
    private fun registro(): ContentValues {
        return ContentValues().apply {
            put("nombre", nombre)
            put("numc", nc)
            put("apellidos", apellido)
            put("semestre", semestre)
            put("grupo", grupo)
        }
    }
    //Registrar datos
    private fun registrar() {
        obtenerDatos()
        if (!validarDatos()) {
            //bd = admin.writableDatabase
            message("${registro()}")
            //bd.insert("$TABLE", null, registro())
            //bd.close()
            message("Registro exitoso")
            //cleanFields()
        } else {
            message("Ingrese todos los datos")
        }
    }

    private fun modificar() {
        if (validarDatos()) {
            message("Ingrese todos los datos")
        } else {
            bd = admin.writableDatabase
            bd.insert("$TABLE", null, registro())
            val cant = bd.update("$TABLE", registro(), "numc='$nc'", null)
            bd.close()
            message(if (cant == 1) "Modificado" else "No existe")
        }
    }

    private fun eliminar() {
        if (validarNC()) {
            message("Ingrese el numero de control")
        } else {
            bd = admin.writableDatabase
            val cant = bd.delete("$TABLE", "numc=$nc", null)
            bd.close()
            message(if (cant == 1) "Eliminado" else "No existe")
        }
    }

    private fun buscarPorNombre() {
        if (validarNC()) {
            message("Ingrese el nombre")
        } else {
            bd = admin.writableDatabase
            val cursor = bd.rawQuery("select * from $TABLE where numc=$nc", null)
            bd.close()
            if (cursor.moveToFirst()) {
                binding.edtNC.setText(cursor.getString(0))
                binding.edtNombre.setText(cursor.getString(1))
                binding.edtAPM.setText(cursor.getString(2))
                binding.cbxSemestre.setSelection(cursor.getString(3).toString().toInt() - 1)
                binding.cbxGrupo.setSelection(cursor.getString(4).toString().toInt() - 1)
            } else {
                message("No existe")
            }
        }
    }

    private fun buscarPorNControl() {
        if (validarNC()) {
            message("Ingrese el numero de control")
        } else {
            bd = admin.writableDatabase
            val cursor = bd.rawQuery("select * from $TABLE where nombre=$nombre", null)
            bd.close()
            if (cursor.moveToFirst()) {
                binding.edtNC.setText(cursor.getString(0))
                binding.edtNombre.setText(cursor.getString(1))
                binding.edtAPM.setText(cursor.getString(2))
                binding.cbxSemestre.setSelection(cursor.getString(3).toString().toInt() - 1)
                binding.cbxGrupo.setSelection(cursor.getString(4).toString().toInt() - 1)
            } else {
                message("No existe")
            }
        }
    }

    private fun mostrarLista() = binding.btnListar.setOnClickListener {
        find().navigate(R.id.action_dash_to_listStudents)
    }

    //Mensaje de informacion
    private fun message(str: String) = Toast.makeText(context, str, Toast.LENGTH_SHORT).show()

    private fun cleanFields() {
        binding.edtNC.text.clear()
        binding.edtNombre.text.clear()
        binding.edtAPM.text.clear()
        binding.cbxSemestre.setSelection(0)
        binding.cbxGrupo.setSelection(0)
    }
}