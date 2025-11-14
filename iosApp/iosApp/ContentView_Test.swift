import SwiftUI
import shared

/**
 * Test View - Tests basic Kotlin framework WITHOUT Compose
 *
 * Use this to verify the shared framework is working before
 * attempting Compose integration
 */
struct ContentView_Test: View {
    @State private var message: String = "Loading..."
    @State private var greeting: String = ""

    var body: some View {
        VStack(spacing: 20) {
            Text("Framework Test")
                .font(.largeTitle)
                .fontWeight(.bold)

            // Test basic Kotlin function call
            Text(message)
                .font(.body)
                .foregroundColor(.green)
                .multilineTextAlignment(.center)
                .padding()

            Text(greeting)
                .font(.body)
                .foregroundColor(.blue)

            Button("Call Kotlin Function") {
                // Call Kotlin function from shared framework
                message = TestFunctionKt.getTestMessage()
                greeting = TestFunctionKt.greet(name: "iOS User")
            }
            .buttonStyle(.borderedProminent)

            Divider()
                .padding()

            Text("If you see the green message above, the Kotlin framework is working!")
                .font(.caption)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
        }
        .padding()
        .onAppear {
            // Test on appear
            message = TestFunctionKt.getTestMessage()
            greeting = TestFunctionKt.greet(name: "iPhone")
        }
    }
}

struct ContentView_Test_Previews: PreviewProvider {
    static var previews: some View {
        ContentView_Test()
    }
}
