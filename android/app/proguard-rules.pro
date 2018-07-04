# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Applications/ADT_Bundle/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

### Will keep line numbers and file name obfuscation
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

### Common
-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient
-keep @interface android.webkit.JavascriptInterface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

### For UI tests
-keepclasseswithmembers public class android.support.v7.widget.RecyclerView { *; }

### Google Play services
-keep class com.google.android.gms.* { *; }
-dontwarn com.google.android.gms.**

### Crashlytics SDK
-keepattributes *Annotation*
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

### Butterknife
-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }
