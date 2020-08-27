# SmartSpinner
A TextView that have a spinner background that can be customizing .Like line color of bottom line or dropdown arrow and so on.</br>
An important feature is RTl support .</br>
# Importing :</br>
add this to dependencies block in build.gradle file :</br>
<pre>implementation 'ir.smartdevelopers:smart-spinner:1.0'</pre></br>
![Image](https://github.com/smartdevelopers-ir/SmartSpinner/blob/master/images/spinner.png)
# Usage :
```xml
<ir.smartdevelopers.test.customView.SmartSpinner
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="LTR Text"
        android:textColor="#151515"
        android:textSize="14sp"
        android:layoutDirection="rtl"
        app:arrowSize="10dp"
        app:tint="@color/colorAccent"
        app:lineColor="@color/colorPrimaryDark"
        app:arrowColor="#304FFE"
        app:roundArrow="true"
        app:spStyle="underlined"
        />
```
If you set `tint` , `lineColor` and `arrowColor` will be overriden with `tint`.
</br>
To change ripple color set `colorControlHighlight` and for bottom line pressed color set `colorControlActive` in theme. 
