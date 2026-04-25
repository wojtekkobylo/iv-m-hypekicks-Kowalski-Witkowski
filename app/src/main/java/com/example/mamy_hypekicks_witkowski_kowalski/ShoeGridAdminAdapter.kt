package com.example.mamy_hypekicks_witkowski_kowalski

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.mamy_hypekicks_witkowski_kowalski.Models.Shoe

class ShoeGridAdminAdapter(
    private val context: Context,
    private val list: MutableList<Shoe>,
    private val onDelete: (Shoe) -> Unit
) : BaseAdapter() {

    override fun getCount() = list.size
    override fun getItem(position: Int) = list[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_shoe, parent, false)

        val image = view.findViewById<ImageView>(R.id.imageShoe)
        val name = view.findViewById<TextView>(R.id.textName)
        val price = view.findViewById<TextView>(R.id.textPrice)

        val shoe = list[position]

        name.text = "${shoe.brand} ${shoe.modelName}"
        price.text = "${shoe.resellPrice} zł"

        Glide.with(context).load(shoe.imageUrl).into(image)

        view.setOnLongClickListener {
            onDelete(shoe)
            true
        }

        return view
    }
}