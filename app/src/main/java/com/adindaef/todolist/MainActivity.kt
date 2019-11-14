package com.adindaef.todolist

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: RvAdapter
    private var list = ArrayList<Model>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        adapter = RvAdapter(this, list)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)

        getData()

        fab.setOnClickListener { view ->
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Add a task")
            builder.setMessage("What do you want to do?")
            val inputField = EditText(this)
            builder.setView(inputField)
            builder.setPositiveButton("Add"){
                dialog, which ->
                database.use {
                    insert(Model.TABLE_NAME,
                        Model.NAMA to inputField.text.toString())

                    toast("Data successfully Added")
                }
                getData()
            }
            builder.setNegativeButton("Cancel", null)

            builder.create().show()
        }
    }

    private fun getData() {
        database.use {
            list.clear()
            val result = select(Model.TABLE_NAME)
            val data = result.parseList(classParser<Model>())
            list.addAll(data)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
