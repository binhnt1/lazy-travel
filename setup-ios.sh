#!/bin/bash

# Script to setup iOS project automatically - ONE TIME ONLY
# Usage: ./setup-ios.sh

set -e

echo "🚀 LazyTravel iOS Setup"
echo "======================="
echo ""

# Check if CocoaPods is installed
if ! command -v pod &> /dev/null; then
    echo "❌ CocoaPods not found!"
    echo ""
    echo "Installing CocoaPods..."
    sudo gem install cocoapods
    echo "✅ CocoaPods installed"
    echo ""
fi

# Step 1: Build shared framework
echo "📦 Step 1: Building shared framework..."
./gradlew :shared:assembleSharedDebugXCFramework
echo "✅ Framework built"
echo ""

# Step 2: Install pods
echo "📦 Step 2: Installing CocoaPods dependencies..."
cd iosApp
pod install
echo "✅ Pods installed"
echo ""

# Step 3: Done
echo "✅ Setup Complete!"
echo ""
echo "Next steps:"
echo "1. Open Xcode: open iosApp.xcworkspace"
echo "2. Select your Team in Signing & Capabilities"
echo "3. Select iPhone simulator"
echo "4. Press ⌘ + R to run"
echo ""
echo "Note: Always open .xcworkspace (not .xcodeproj) from now on!"
