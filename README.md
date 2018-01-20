# ExtendedRecyclerAdapter

Simple adapter written in `kotlin`. You may use it to display data with RecyclerView.
Making easy to create a list with header and/or footer.
You can extend this adapter to add some sticky headers or something.

## Installation (to be published tomorrow, 20.01.2018)
```
    compile "com.github.keeperus:extendedrecyclerview:1.0"
```
## Usage

Create adapter and display your data in just two lines:
```
    val data = ArrayList<SomeData> //next two lines
    val adapter: ExtendedRecyclerAdapter<SomeData, SomeDataView> =
                    ExtendedRecyclerAdapter<SomeData, SomeDataView>(data, R.layout.list_some_data_view)
        adapter.attachToRecyclerView(list_view)
```
Adapter constructor must have next parameters:
 -  data - ArrayList of your data classes which you want to be displayed
 -  layoutId - your xml layout resource containing SomeDataView component
Additional parameter:
 -  viewId - id of a SomeDataView component in layoutId - default value is R.id.list_item

Function attachToRecyclerView must have only one parameter:
 -  recyclerView - your RecyclerView
Additional parameter:
 -  startFromBot - boolean value. *true* if you want a messenger-like displaying of items, otherwise *false*.
This function is setting adapter to RecyclerView and setting a LinearLayoutManager if startFromBot = false or
a StaggeredGridLayoutManager if startFromBot = true.

## SomeDataView example
```
class SomeDataView : LinearLayout, ExtendedRecyclerItemInterface<SomeData> {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) :
                    super(context, attrs, defStyleAttr) {
        view = LayoutInflater.from(context).inflate(R.layout.view_chat_dialog, this, true) as View
    }

    var view: View? = null

    override fun setItem(item: SomeData,
                        isSelected: Boolean,
                        position: Int,
                        list: ArrayList<SomeData>,
                        hasHeader: Boolean,
                        highlight: String?,
                        extra: Any?) {
    }

    override fun onRecycle() { }

    override var innerClickListener: View.OnClickListener? = null
    override var onListItemSelectedListener: OnListItemSelectedListener<SomeData>? = null

}
```
## R.layout.list_some_data_view example

```
<?xml version="1.0" encoding="utf-8"?>
<com.bitbucket.keeperus.extendedrecycleradapter.ui.views.SomeDataView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/list_item"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```

## Adapter functions

### Header and footer

    You can set a header view or/and a footer view to your adapter

```
    val headerView = LayoutInflater.from(context).inflate(R.layout.some_header, null, false)
    adapter.setHeader(headerView) // set a header. adapter.notifyDataSetChanged is called automatically
    adapter.setHeader(null) remove a header. adapter.notifyDataSetChanged is called automatically
```

### Highlight
    You can add some highlights for you TextViews with Spannables.
    Can be useful for filtering your data by some text value.

```
    val query: String?
    adapter.setHighlight(query)
```

### Handling view clicks

```
   adapter.onClickListener = View.OnClickListener { /**do something*/}
```

### Handling clicks and stated on the SomeDataView side

```
    override fun setItem(item: SomeData,
                        isSelected: Boolean,
                        position: Int, list:
                        ArrayList<SomeData>,
                        hasHeader: Boolean,
                        highlight: String?,
                        extra: Any?) {
        setOnClickListener(innerClickListener)
    }
```

or

```
    override fun setItem(item: SomeData,
                        isSelected: Boolean,
                        position: Int,
                        list: ArrayList<SomeData>,
                        hasHeader: Boolean,
                        highlight: String?,
                        extra: Any?) {
        someButton.setTag(R.id.data_extra, item)
        someButton.setOnClickListener {
            innerClickListener?.onClick(it)
            onListItemSelectedListener?.onSelected(item, !isSelected)
        }
    }
```

and later you can use it:

```
   adapter.onClickListener = View.OnClickListener {
        val item = it.getTag(R.id.data_extra) as SomeData
        val viewId = it.id
        when (viewId) {
                R.id.some_id -> someFunction(item)
                else -> anotherFunction(item)
        }
   }
```
