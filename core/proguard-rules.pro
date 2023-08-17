# Get rid of package names, makes file smaller
-repackageclasses

# --> Data classes
-keep class me.varoa.studentprofiles.core.domain.model.** { *; }
-keep class me.varoa.studentprofiles.core.data.remote.json.** { *; }
-keep class me.varoa.studentprofiles.local.entity.** { *; }
