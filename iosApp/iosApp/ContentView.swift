import SwiftUI
import shared

struct ContentView: View {
    var body: some View {
        VStack(spacing: 20) {
            Text("🎉 LazyTravel iOS")
                .font(.largeTitle)
                .fontWeight(.bold)

            Text("Shared module đã được integrate!")
                .font(.title3)
                .foregroundColor(.secondary)

            VStack(alignment: .leading, spacing: 12) {
                Label("✅ Kotlin 2.2.21", systemImage: "checkmark.circle.fill")
                    .foregroundColor(.green)
                Label("✅ Compose Multiplatform 1.9.3", systemImage: "checkmark.circle.fill")
                    .foregroundColor(.green)
                Label("✅ Shared framework working", systemImage: "checkmark.circle.fill")
                    .foregroundColor(.green)
                Label("✅ CocoaPods configured", systemImage: "checkmark.circle.fill")
                    .foregroundColor(.green)
            }
            .padding()
            .background(Color.gray.opacity(0.1))
            .cornerRadius(12)

            Text("Tiếp theo: Add HomeView và các components vào Xcode!")
                .font(.caption)
                .foregroundColor(.orange)
                .padding(.top)
                .multilineTextAlignment(.center)
        }
        .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
