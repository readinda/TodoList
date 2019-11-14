package com.adindaef.todolist

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.update
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class RvAdapter(val context: Context, val items:ArrayList<Model>):RecyclerView.Adapter<RvAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position])
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        fun bindItem(model: Model) {
            itemView.itemNama.text = model.nama

            itemView.setOnClickListener {
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle("Edit a task")
                builder.setMessage("What do you want to do?")
                val inputEdit = EditText(itemView.context)
                builder.setView(inputEdit)
                inputEdit.setText(model.nama)
                builder.setPositiveButton("Update") {
                    dialog, which ->
                    itemView.context.database.use {
                        update(Model.TABLE_NAME,
                            Model.NAMA to inputEdit.text.toString())
                            .whereArgs("${Model.ID} = {id}", "id" to model.id!!.toLong())
                            .exec()

                        itemView.context.toast("Data Berhasil Diupdate")
                        itemView.context.startActivity<MainActivity>()
                        (itemView.context as Activity).finish()

                    }
                }

                builder.setNegativeButton("Cancel", null)
                builder.create().show()
            }

            itemView.btnHapus.setOnClickListener {
                val dialog = AlertDialog.Builder(itemView.context)
                dialog.setTitle("Warning!")
                dialog.setMessage("Are you sure to delete this task?")
                dialog.setPositiveButton("Yes"){
                    dialog, which -> itemView.context.database.use {
                    delete(Model.TABLE_NAME,"${Model.ID} = {id}"
                        ,"id" to model.id!!.toLong())
                    itemView.context.startActivity<MainActivity>()
                    (itemView.context as Activity).finish()
                }
                }
                dialog.setNegativeButton("No"){
                    dialog, which ->
                }

                dialog.create().show()

            }
        }

    }
}