package com.github.keeperus.extendedrecycleradapter.interfaces

/**
 * Interface that notifies adapter about the state of item.
 *
 * adapter.notifyItemChanged is called on method [onSelected(item, isSelected)] use.
 *
 * @author keeperus
 * @param [T] the type of a list item.
 */
interface OnListItemSelectedListener<T : Any> {

    /**
     * @param [item] list item.
     * @param [isSelected] is list item selected or not.
     */
    fun onSelected(item: T, isSelected: Boolean)
}
