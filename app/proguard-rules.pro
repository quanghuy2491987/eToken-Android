# Recommended package so to be consistent with Ezio SDK
# !!! it is extremely important to 'flatten' or to 'repackage' into package 'util',
# so to hide important packages that cannot be obfuscated further !!!
-flattenpackagehierarchy util

# Global Rules for all SDKs
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
-renamesourcefileattribute SourceFile
-dontpreverify
-verbose
-dontoptimize

# for maintenance purposes, keep this files confidential
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# EZIO Specific Rules
-optimizations !code/allocation/variable,!code/simplification/arithmetic,!field/*,!class/merging/*

#### ----- EZIO LIB
-keep class util.a.z.** {*;}
-keep class util.d.** {*;}
-keep class com.neurotec.** {*;}
-keep class com.sun.jna.** {*;}

# supress warning
-dontwarn util.a.z.**
-dontwarn com.sun.jna.**
-dontwarn com.neurotec.**
-dontwarn javax.naming.**

# supress notes
-dontnote com.gemalto.idp.mobile.**
-dontnote util.**
-dontnote android.net.**
-dontnote org.apache.**
# Suppress notes about dynamic referenced class used by PRNGFixes (not part of Android API)
-dontnote org.apache.harmony.xnet.provider.jsse.NativeCrypto

# native
-keepclasseswithmembernames class * {
    native <methods>;
}

# Global JNA Rules
-keep,allowobfuscation interface  com.sun.jna.Library

-keep,allowobfuscation interface  * extends com.sun.jna.Library

-keep,allowobfuscation interface  * extends com.sun.jna.Callback

-keepclassmembers interface  * extends com.sun.jna.Library {
    <methods>;
}

-keepclassmembers interface  * extends com.sun.jna.Callback {
    <methods>;
}

-keep class com.sun.jna.CallbackReference {
    void dispose();
    com.sun.jna.Callback getCallback(java.lang.Class,com.sun.jna.Pointer,boolean);
    com.sun.jna.Pointer getFunctionPointer(com.sun.jna.Callback,boolean);
    com.sun.jna.Pointer getNativeString(java.lang.Object,boolean);
    java.lang.ThreadGroup initializeThread(com.sun.jna.Callback,com.sun.jna.CallbackReference$AttachOptions);
}

-keep,includedescriptorclasses class com.sun.jna.Native {
    com.sun.jna.Callback$UncaughtExceptionHandler callbackExceptionHandler;
    void dispose();
    java.lang.Object fromNative(com.sun.jna.FromNativeConverter,java.lang.Object,java.lang.reflect.Method);
    com.sun.jna.NativeMapped fromNative(java.lang.Class,java.lang.Object);
    com.sun.jna.NativeMapped fromNative(java.lang.reflect.Method,java.lang.Object);
    java.lang.Class nativeType(java.lang.Class);
    java.lang.Object toNative(com.sun.jna.ToNativeConverter,java.lang.Object);
}

-keep class com.sun.jna.Callback$UncaughtExceptionHandler {
    void uncaughtException(com.sun.jna.Callback,java.lang.Throwable);
}

-keep class com.sun.jna.Native$ffi_callback {
    void invoke(long,long,long);
}

-keep class com.sun.jna.Structure$FFIType$FFITypes {
    <fields>;
}

-keep class com.sun.jna.ToNativeConverter {
    java.lang.Class nativeType();
}

-keep class com.sun.jna.NativeMapped {
    java.lang.Object toNative();
}

-keep class com.sun.jna.IntegerType {
    long value;
}

-keep class com.sun.jna.PointerType {
    com.sun.jna.Pointer pointer;
}

-keep class com.sun.jna.LastErrorException {
    <init>(int);
    <init>(java.lang.String);
}

-keep class com.sun.jna.Pointer {
    long peer;
    <init>(long);
}

-keep class com.sun.jna.WString {
    <init>(java.lang.String);
}

-keep class com.sun.jna.Structure {
    long typeInfo;
    com.sun.jna.Pointer memory;
    <init>(int);
    void autoRead();
    void autoWrite();
    com.sun.jna.Pointer getTypeInfo();
    com.sun.jna.Structure newInstance(java.lang.Class,long);
}
-dontobfuscate

