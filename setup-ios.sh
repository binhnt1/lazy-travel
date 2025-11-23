#!/bin/bash
set -e

# Add gem executables to PATH
export PATH="$PATH:/opt/homebrew/lib/ruby/gems/3.4.0/bin"

echo "🚀 LazyTravel iOS Setup"
echo "======================="
echo ""

# Step 1: Build shared framework
echo "📦 Step 1: Building shared XCFramework..."
./gradlew :shared:assembleSharedDebugXCFramework
echo "✅ Framework built"
echo ""

# Step 2: Copy resources from KMP to iOS App
echo "📁 Step 2: Copying resources to iOS app bundle..."

SRC_RESOURCES="shared/src/androidMain/resources"
DST_RESOURCES="iosApp/Resources"

mkdir -p "$DST_RESOURCES"
cp -R "$SRC_RESOURCES/." "$DST_RESOURCES/"

echo "✅ Resources copied to iosApp/Resources"
echo ""

# Step 3: Install pods
echo "📦 Step 3: Installing CocoaPods dependencies..."
cd iosApp
pod install
cd ..
echo "✅ Pods installed"
echo ""

# Step 4: Build & Run iOS app on simulator
echo "📱 Step 4: Building & launching iOS app on simulator..."
SIMULATOR_NAME="iPhone 17 Pro"

# Mở Simulator GUI
open -a Simulator

xcodebuild \
  -workspace iosApp/iosApp.xcworkspace \
  -scheme iosApp \
  -destination "platform=iOS Simulator,name=$SIMULATOR_NAME" \
  clean build | xcpretty

# Boot simulator
xcrun simctl boot "$SIMULATOR_NAME" 2>/dev/null || echo "ℹ️  Simulator already running"

# Bring simulator window lên front
osascript -e 'tell application "Simulator" to activate'

# Install and launch app
APP_PATH="iosApp/build/iosApp/Build/Products/Debug-iphonesimulator/iosApp.app"
xcrun simctl install booted "$APP_PATH"
xcrun simctl launch booted com.lazytravel.iosApp

echo ""
echo "✅ App launched successfully on $SIMULATOR_NAME"
echo "🎉 Setup complete!"