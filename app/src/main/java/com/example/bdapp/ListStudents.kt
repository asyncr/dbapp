package com.example.bdapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.bdapp.databinding.FragmentListStudentsBinding

class ListStudents : Fragment() {
    private var _binding: FragmentListStudentsBinding? = null
    private val binding get() = _binding!!
    protected var TABLE: String = "estudiante"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListStudentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initList() {
        val list: ListView = binding.list
        //Creamos un arreglo vacío
        var estudiantes = ArrayList<String>()
        val adapter = ArrayAdapter<String>(
            binding.root.context,
            android.R.layout.simple_list_item_1,
            estudiantes
        )
        list.setAdapter(adapter)
        val admin = AdminSqliteOpenHelper(binding.root.context, "estudiante", null, 1)
        val bd = admin.readableDatabase
        val fila = bd.rawQuery("select * from ${this.TABLE}", null)

        while (fila.moveToNext()) {
            estudiantes.add("${fila.getString(0)}, ${fila.getString(1)},${fila.getString(2)}, ${fila.getString(3)}, ${fila.getString(4)}")
            adapter.notifyDataSetChanged()
        }

        bd.close()
    }

}