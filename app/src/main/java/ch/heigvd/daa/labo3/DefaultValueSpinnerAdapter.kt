/**
 * Kevin Ferati, Malo Romano & Flavio Sovilla
 * This Kotlin class defines a custom ArrayAdapter for use with a Spinner widget.
 * It allows the specification of a default value, ensuring it is not selectable or visible in the dropdown list.
 */

package ch.heigvd.daa.labo3

import android.widget.ArrayAdapter
import android.content.Context
import android.view.View
import android.view.ViewGroup

class DefaultValueSpinnerAdapter<T>(
    context: Context,
    resource: Int,
    defaultValue: T,
    objects: Array<T>
) : ArrayAdapter<T>(context, resource) {
    /**
     * Init the custom adapter with a default value
     */
    init {
        add(defaultValue)
        addAll(*objects)
    }

    /**
     * Return if an option is enabled
     * Overload the standard ArrayAdapter isEnabled function, to return false is it's the first default option
     */
    override fun isEnabled(index: Int): Boolean {
        return (index != 0) && super.isEnabled(index)
    }

    /**
     * Display an item, except if it is the first default option
     */
    override fun getDropDownView(index: Int, convertView: View?, parent: ViewGroup): View {
        return if (index == 0)
            View(context).apply { visibility = View.GONE }
        else
            super.getDropDownView(index, null, parent)
    }
}