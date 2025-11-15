#!/bin/bash

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
# Lấy path build products
APP_PATH=$(xcodebuild \
  -workspace iosApp/iosApp.xcworkspace \
  -scheme iosApp \
  -configuration Debug \
  -sdk iphonesimulator \
  -showBuildSettings | grep -i "BUILT_PRODUCTS_DIR" | awk '{print $3}')/iosApp.app

# Kiểm tra tồn tại
if [ ! -d "$APP_PATH" ]; then
  echo "❌ App not found at $APP_PATH"
  exit 1
fi

echo "✅ Found app at $APP_PATH"

# Boot simulator (nếu chưa boot)
xcrun simctl boot "iPhone 17 Pro" || true

# Install app
xcrun simctl install booted "$APP_PATH"

# Launch app
xcrun simctl launch booted com.lazytravel.ios

