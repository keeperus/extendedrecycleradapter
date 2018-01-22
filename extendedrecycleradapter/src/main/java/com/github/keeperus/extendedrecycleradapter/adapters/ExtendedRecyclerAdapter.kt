package com.github.keeperus.extendedrecycleradapter.adapters
/**
 *    Copyright 2018 Alex Shapkin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TableRow
import com.github.keeperus.extendedrecycleradapter.R
import com.github.keeperus.extendedrecycleradapter.interfaces.ExtendedRecyclerItemInterface
import com.github.keeperus.extendedrecycleradapter.interfaces.OnListItemSelectedListener
import java.util.ArrayList

/**
 * Simple adapter you can use to display data with RecyclerView.
 *
 * You can extend this adapter to add some sticky headers or something.
 *
 *
 * @author keeperus
 * @param [T] the type of the list items.
 * @param [V] the type of a view responsible for displaying list item. Must extend a [ViewGroup] and implement an [ExtendedRecyclerItemInterface];
 * @param [data] ArrayList<[T]> of data items.
 * @param [layoutId] layout resource Id that contains a [V] view.
 * @param [viewId] resource Id of a view, default value is [R.id.list_item].
 */
open class ExtendedRecyclerAdapter<T : Any, V>(//
        val data: ArrayList<T>,
        private val layoutId: Int,
        private val viewId: Int = R.id.list_item) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>()
        where V : ViewGroup, V : ExtendedRecyclerItemInterface<T> {

    companion object {
        private val TYPE_DATA   = 0
        private val TYPE_HEADER = 100
        private val TYPE_FOOTER = 200
    }

    val selected = ArrayList<T>()

    private var header: View? = null
    private var footer: View? = null
    private var highlight: String? = null
    var extra: Any? = null

    /**
     * Attaches adapter to the [RecyclerView] and sets a [LinearLayoutManager] if [startFromBot] = true or a [StaggeredGridLayoutManager] if [startFromBot] = false
     *
     * @param [recyclerView] view you want to set adapter to.
     * @param [startFromBot] true if you want a messenger-like displaying of items.
     */
    fun attachToRecyclerView(recyclerView: RecyclerView, startFromBot: Boolean = false) {
        recyclerView.adapter = this
        if (startFromBot) {
            val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            layoutManager.reverseLayout = !startFromBot
            recyclerView.layoutManager = layoutManager
            if (itemCount > 0)
                recyclerView.scrollToPosition(itemCount - 1)
        } else {
            val layoutManager = LinearLayoutManager(recyclerView.context)
//            layoutManager.stackFromEnd = startFromBot
//            layoutManager.reverseLayout = !startFromBot
            recyclerView.layoutManager = layoutManager
        }
    }


    class CustomViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    inner class ItemViewHolder<V : View?>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var view: V = itemView.findViewById<V>(viewId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when {
            viewType == ExtendedRecyclerAdapter.Companion.TYPE_DATA -> {
                val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
                ItemViewHolder<V>(view)
            }
            viewType >= ExtendedRecyclerAdapter.Companion.TYPE_FOOTER -> ExtendedRecyclerAdapter.CustomViewHolder(footer)
            else -> ExtendedRecyclerAdapter.CustomViewHolder(header)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(position)
        if (type == ExtendedRecyclerAdapter.Companion.TYPE_DATA) {
            val viewHolder = holder as ItemViewHolder<*>
            val itemView = viewHolder.view as V
            val item = getItem(position - if (header == null) 0 else 1)
            itemView.innerClickListener = onClickListener
            itemView.onListItemSelectedListener = onListItemSelectedListener
            itemView.setItem(item, selected.contains(item), position, data,header != null, highlight, extra)
        } else {
            val viewHolder = holder as ExtendedRecyclerAdapter.CustomViewHolder
            val view = viewHolder.itemView
            val params = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
            view.layoutParams = params
        }
    }

    private fun getItem(position: Int): T {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    operator fun contains(`object`: T): Boolean {
        return data.contains(`object`)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder?) {
        if (holder!!.itemView is ExtendedRecyclerItemInterface<*>) {
            val viewHolder = holder as ItemViewHolder<*>
            val itemView = viewHolder.view as V
            itemView.onRecycle()
        }
        super.onViewRecycled(holder)
    }

    /**
     * @return item count increased by 1 if there is a header and increased by 1 if there is a footer
     */
    override fun getItemCount(): Int {
        return data.size + (if (footer == null) 0 else 1) + if (header == null) 0 else 1
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position < (if (header == null) 0 else 1) -> ExtendedRecyclerAdapter.TYPE_HEADER + position
            position >= (if (header == null) 0 else 1) + data.size -> ExtendedRecyclerAdapter.TYPE_FOOTER + position - (if (header == null) 0 else 1) - data.size
            else -> ExtendedRecyclerAdapter.TYPE_DATA
        }
    }

    /**
     * Assign a View.OnClickListener to each [View] in [RecyclerView].
     */
    var onClickListener: OnClickListener? = null

    private val onListItemSelectedListener = object : OnListItemSelectedListener<T> {
        override fun onSelected(item: T, isSelected: Boolean) {
            if (isSelected) {
                selected.add(item)
            } else {
                selected.remove(item)
            }
            notifyItemChanged(data.indexOf(item) + if (header == null) 0 else 1)
        }
    }

    /**
     * @param [highlight] highlighting the text in the view while filtering data by text.
     */
    fun setHighlight(highlight: String) {
        this.highlight = highlight
    }

    /**
     * @param [view] add or remove(assign null) header to the [RecyclerView].
     */
    fun setHeaderView(view: View?) {
        header = view
        notifyDataSetChanged()
    }

    /**
     * @param [view] add or remove(assign null) footer to the [RecyclerView].
     */
    fun setFooterView(view: View?) {
        footer = view
        notifyDataSetChanged()
    }

}
