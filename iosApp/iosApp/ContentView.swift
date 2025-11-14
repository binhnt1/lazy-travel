import SwiftUI
import shared

/**
 * Main Content View - Wraps Compose Multiplatform UI
 *
 * Uses UIViewControllerRepresentable to embed the Compose UI
 * (HeroSection and other screens) into SwiftUI
 */
struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea(.all) // Allow full screen Compose UI
    }
}

/**
 * Bridge between SwiftUI and Compose Multiplatform
 *
 * This wrapper allows us to use Compose UI components
 * directly in the iOS app
 */
struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        // Call the MainViewController function from shared module
        // This returns a UIViewController containing our Compose UI
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        // No updates needed - Compose handles its own state
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
