import SwiftUI
import shared

@main
struct iOSApp: App {

    init() {
        // Initialize PocketBase client
        PocketBaseClient.shared.initialize()

        // Auto-create collections (optional, for development)
        Task {
            await PocketBaseSetup.shared.ensureCollectionsExist()
        }
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
