#!/bin/bash
set -e

# ============================
# LazyTravel Android Setup Script (1 VSCode Task)
# ============================

export ANDROID_HOME="$HOME/Library/Android/sdk"
export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator:$ANDROID_HOME/cmdline-tools/latest/bin:$PATH"

EMULATOR_NAME="Pixel_7_API_36"
APK_PATH="androidApp/build/outputs/apk/debug/androidApp-debug.apk"
PACKAGE_NAME="com.lazytravel.android"

echo "🤖 LazyTravel Android Setup"
echo "============================"
echo ""

# 1️⃣ Check if emulator is running
EMULATOR_PID=$(pgrep -f "emulator.*$EMULATOR_NAME" || true)

if [ -z "$EMULATOR_PID" ]; then
    echo "🚀 Emulator not running, launching GUI in this terminal..."
    # Launch emulator in foreground (terminal will stay open)
    "$ANDROID_HOME/emulator/emulator" -avd "$EMULATOR_NAME" -netdelay none -netspeed full &
    EMULATOR_PID=$!
    echo "⏳ Waiting for emulator to boot..."
    adb wait-for-device
else
    echo "ℹ️ Emulator already running"
fi

echo "✅ Emulator ready"
echo ""

# 2️⃣ Build shared + APK
echo "📦 Step 2: Building shared module and APK..."
./gradlew :shared:assembleRelease :androidApp:assembleDebug
echo "✅ Build complete"
echo ""

# 3️⃣ Install APK
echo "📦 Step 3: Installing APK..."
adb install -r "$APK_PATH"
echo "✅ APK installed"
echo ""

# 4️⃣ Launch app
echo "🚀 Step 4: Launching app..."
adb shell monkey -p "$PACKAGE_NAME" -c android.intent.category.LAUNCHER 1
echo ""
echo "🎉 Android app launched successfully!"
