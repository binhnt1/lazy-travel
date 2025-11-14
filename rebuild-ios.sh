#!/bin/bash

# Script to rebuild iOS framework quickly during development
# Usage: ./rebuild-ios.sh

set -e

echo "🔨 Rebuilding iOS Framework..."

# Clean previous build (optional, comment out for faster builds)
# ./gradlew :shared:clean

# Build XCFramework
./gradlew :shared:assembleSharedDebugXCFramework

echo "✅ Framework rebuilt successfully!"
echo "📍 Location: shared/build/XCFrameworks/debug/shared.xcframework"
echo ""
echo "Next steps:"
echo "1. Go to Xcode"
echo "2. Press ⌘ + Shift + K (Clean Build Folder)"
echo "3. Press ⌘ + R (Run)"
