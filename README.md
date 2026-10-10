# Kay Joystick Mapper

Android TV app for mapping PS4 controller buttons to Android TV actions through an Accessibility Service.

## What it can do

- Detect common PS4 buttons: Cross, Circle, Square, Triangle, L1, R1, L2, R2, Options, Share, L3, R3, and PS.
- Map those buttons to Android TV actions: Back, Home, Recents, media play/pause, next/previous media, and volume up/down.
- Show a simple controller test screen so you can check which buttons Android TV is actually delivering.

## Device compatibility

- Android 8.0 is supported. The app minimum is Android 6.0, so API 26 devices are inside the supported range.
- ARM 32-bit (`armeabi-v7a`) is supported because the app is Java-only and does not ship native `.so` libraries.
- GPU model and OpenGL ES support do not affect this app because it does not render games or 3D graphics.

## Important limitation

Android does not allow a normal app to globally inject arbitrary controller buttons into other apps or games. That means mapping "Cross to R1" everywhere usually needs system/root/ADB-level privileges. This app uses the public Accessibility API, so it focuses on system and media actions.

Some Android TV builds also reserve the PS button, Share button, or game buttons while a game is focused. Reserved buttons may not reach this app.

## Build

Open the folder in Android Studio, let Gradle sync, then build `app`.

This project is intentionally Java-only and has no third-party dependencies.

Gradle needs a modern Java runtime. If the command-line build says it is using Java 8, install/use JDK 17 or open the project from Android Studio and select its bundled JDK.

## Use

1. Install the APK on Android TV.
2. Open **Kay Joystick Mapper**.
3. Choose **Open Accessibility Settings**.
4. Enable **Kay Joystick Mapper**.
5. Return to the app and choose the action for each PS4 button.

## ملاحظات بالعربي

هذا التطبيق يستخدم Accessibility Service حتى يلتقط أزرار يد PS4 على Android TV ويحولها إلى أوامر مثل رجوع، الرئيسية، التحكم بالوسائط، ورفع/خفض الصوت.

مواصفات جهازك مناسبة: Android 8.0 مدعوم، وARM 32-bit يعمل لأن التطبيق Java فقط ولا يحتاج مكتبات native خاصة بـ `armeabi-v7a`. كرت Mali-T860 وOpenGL ES لا يؤثران على عمل التطبيق.

التبديل الكامل بين أزرار الجويستك داخل كل الألعاب، مثل جعل زر X يعمل كـ R1 عالميًا، غير متاح لتطبيق عادي بدون صلاحيات نظام أو root أو ADB لأن Android يمنع حقن أزرار كنترولر عامة.
