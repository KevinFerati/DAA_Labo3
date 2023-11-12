package ch.heigvd.daa.labo3

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


// https://stackoverflow.com/a/41637506
class HintAdapter(context: Context, private val hint: String, private val elements: Array<String>) :
    ArrayAdapter<String>(context, R.layout.simple_list_item_1, elements) {
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return if (position == 0) {
            initialSelection(true)
        } else getCustomView(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return if (position == 0) {
            initialSelection(false)
        } else getCustomView(position, convertView, parent)
    }

    override fun getCount(): Int {
        return super.getCount() + 1 // Adjust for initial selection item
    }

    private fun initialSelection(dropdown: Boolean): View {
        // Just an example using a simple TextView. Create whatever default view
        // to suit your needs, inflating a separate layout if it's cleaner.
        val view = TextView(context)
        view.text = hint
        if (dropdown) { // Hidden when the dropdown is opened
            view.height = 0
        }
        return view
    }

    override fun getItem(position: Int): String? {
        return if (position == 0) null else elements[position - 1]
    }

    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Distinguish "real" spinner items (that can be reused) from initial selection item
        val view =
            if (convertView != null && convertView !is TextView) convertView else LayoutInflater.from(
                context
            ).inflate(R.layout.simple_list_item_1, parent, false)

        (view as TextView).text = elements[position - 1]

        return view
    }
}