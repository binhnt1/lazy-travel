import SwiftUI
import shared

@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            // Using ContentView with Compose Multiplatform
            // Info.plist has been configured for ProMotion displays
            ContentView()
        }
    }
}
