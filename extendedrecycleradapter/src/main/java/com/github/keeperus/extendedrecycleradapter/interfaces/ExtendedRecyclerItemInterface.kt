package com.github.keeperus.extendedrecycleradapter.interfaces

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

import android.view.View
/***
 * An interface which a view must implement to be displayed in an ExtendedRecyclerAdapter.
 *
 * @author keeperus
 * @param [T] the type of a list data.
 ***/
interface ExtendedRecyclerItemInterface<T : Any> {

    /**
     * fun setItem is called when you list item is getting displayed.
     *
     * @param [item] list item;
     * @param [isSelected] is list item selected or not;
     * @param [position] position of item in data list;
     * @param [list] data list;
     * @param [hasHeader] is there a header in the recycler view. if true, than actual position in list is increased by 1;
     * @param [highlight] used to highlight the text when you filter items by some text value;
     * @param [extra] any parameter you wish to add to your view.
     */
    fun setItem(item: T, isSelected: Boolean, position: Int, list: ArrayList<T>, hasHeader: Boolean, highlight: String?, extra: Any?)

    /**
     * clean up your view in here
     */
    fun onRecycle()

    /**
     * Should be called by a view on user interaction.
     *
     * You can assign it your whole view or any element or views interface.
     *
     * View Ids can help to manage which view interface has been used.
     *
     * You can pass the data through the view tags.
     */
    var innerClickListener: View.OnClickListener?

    /**
     * Should be called by a view on user interaction
     */
    var onListItemSelectedListener: OnListItemSelectedListener<T>?
}