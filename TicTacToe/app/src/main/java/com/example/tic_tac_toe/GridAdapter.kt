package com.example.tic_tac_toe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton


class GridAdapter(var context: Context, val listener: GridListener, var gridArr: Array<IntArray>) : BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var box: ImageButton
    override fun getCount(): Int {
        return 9
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val row = position/3
        val col = position%3
        val current = gridArr[row][col]
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.tic_box, null)
        }
        box = convertView!!.findViewById(R.id.box_00)
        if (current == 1){
            box.setImageResource(R.drawable.o)
        }else if (current == 2){
            box.setImageResource(R.drawable.x)
        }
        else  box.setImageResource(R.color.white)
        box.setOnClickListener{
            listener.onClick(row,col)
        }
        return convertView
    }
}

interface GridListener {
    fun onClick(row: Int, col:Int)
}