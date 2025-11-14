import SwiftUI
import shared

@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            // IMPORTANT: Using ContentView_Test to test framework WITHOUT Compose
            // This helps isolate the crash issue
            // If this works: framework OK, Compose has issues
            // If this crashes: framework has fundamental issues
            ContentView_Test()
        }
    }
}
