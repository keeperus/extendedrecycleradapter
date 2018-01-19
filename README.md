# extendedrecycleradapter

Simple adapter written in `kotlin`. You may use it to display data with RecyclerView.
Making easy to create a list with header and/or footer.
You can extend this adapter to add some sticky headers or something.

## Installation
```
    compile "com.github.keeperus:extendedrecyclerview:1.0"
```
## Usage

Create adapter and display your data in just two lines:
```
    val data = ArrayList<SomeData> //next two lines
    val adapter: ExtendedRecyclerAdapter<SomeData, SomeDataView> =
                    ExtendedRecyclerAdapter<SomeData, SomeDataView>(R.layout.list_some_data_view), data)
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
This function is setting adapter to RecyclerView and ...TBC...
