package com.adindaef.todolist

data class Model(var id:Long?, var nama:String?){
    companion object{
        val TABLE_NAME:String = "TABLE_NAME"
        val ID:String = "ID"
        val NAMA:String = "NAMA"
    }
}