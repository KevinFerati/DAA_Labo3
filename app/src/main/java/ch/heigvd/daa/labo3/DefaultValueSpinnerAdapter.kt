package ch.heigvd.daa.labo3

import android.R.attr.resource
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class DefaultValueArrayAdapter(ctx: Context,
                                  private val layout: Int,
                                  private val defaultValue: String,
                                  values: Array<String>): ArrayAdapter<String>(ctx, layout, values) {

    private var hintShown: Boolean = false;

    override fun getItem(position: Int): String? {
        return super.getItem(position)
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (hintShown) {
            return super.getView(position, convertView, parent);
        }

        hintShown = true;
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(layout, parent, false)
        (view as TextView).text = defaultValue
        return view
    }
}