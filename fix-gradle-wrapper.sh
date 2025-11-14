#!/bin/bash
# Fix Gradle Wrapper for LazyTravel iOS Development

echo "🔧 Fixing Gradle Wrapper..."

# Create wrapper directory if not exists
mkdir -p gradle/wrapper

# Download gradle-wrapper.jar for Gradle 8.13
echo "📥 Downloading gradle-wrapper.jar..."
curl -L -o gradle/wrapper/gradle-wrapper.jar \
  https://raw.githubusercontent.com/gradle/gradle/v8.13.0/gradle/wrapper/gradle-wrapper.jar

# Make gradlew executable
chmod +x gradlew

echo "✅ Gradle wrapper fixed!"
echo ""
echo "Now you can run:"
echo "  ./gradlew :shared:assembleSharedDebugXCFramework"
