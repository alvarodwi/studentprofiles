# Get rid of package names, makes file smaller
-repackageclasses

# --> ViewBinding
# ViewBindingDelegate uses Reflection.
-keepclassmembers class ** implements androidx.viewbinding.ViewBinding {
    public static ** bind(android.view.View);
}
