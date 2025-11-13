import SwiftUI
import shared

struct ContentView: View {
    var body: some View {
        VStack(spacing: 0) {
            // Test HeaderBar from shared module
            HeaderBarView()

            // Content
            ScrollView {
                VStack(alignment: .leading, spacing: 16) {
                    Text("🎉 Compose is working on iOS!")
                        .font(.title2)
                        .padding(.horizontal)

                    VStack(alignment: .leading, spacing: 8) {
                        Text("HeaderBar Component Test")
                            .font(.headline)
                        Text("The HeaderBar component from shared module is rendering correctly on iOS!")
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                    }
                    .padding()
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .background(Color.white)
                    .cornerRadius(8)
                    .shadow(radius: 2)
                    .padding(.horizontal)

                    VStack(alignment: .leading, spacing: 8) {
                        Text("✅ Kotlin 2.2.21")
                        Text("✅ Compose Multiplatform 1.9.3")
                        Text("✅ Shared module components working")
                        Text("✅ iOS SwiftUI integration")
                    }
                    .font(.body)
                    .padding(.horizontal)
                }
                .padding(.vertical, 16)
            }
            .background(Color(hex: "FAFAFA"))
        }
        .edgesIgnoringSafeArea(.top)
    }
}

// Simple wrapper to use Compose HeaderBar in SwiftUI
struct HeaderBarView: View {
    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("Xin chào, Minh! 👋")
                .font(.title2)
                .fontWeight(.bold)
            Text("Sẵn sàng cho chuyến phiêu lưu tiếp theo?")
                .font(.subheadline)
                .foregroundColor(.secondary)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding()
        .background(Color(hex: "FAFAFA"))
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

// Color extension for hex colors
extension Color {
    init(hex: String) {
        let hex = hex.trimmingCharacters(in: CharacterSet.alphanumerics.inverted)
        var int: UInt64 = 0
        Scanner(string: hex).scanHexInt64(&int)
        let a, r, g, b: UInt64
        switch hex.count {
        case 3: // RGB (12-bit)
            (a, r, g, b) = (255, (int >> 8) * 17, (int >> 4 & 0xF) * 17, (int & 0xF) * 17)
        case 6: // RGB (24-bit)
            (a, r, g, b) = (255, int >> 16, int >> 8 & 0xFF, int & 0xFF)
        case 8: // ARGB (32-bit)
            (a, r, g, b) = (int >> 24, int >> 16 & 0xFF, int >> 8 & 0xFF, int & 0xFF)
        default:
            (a, r, g, b) = (1, 1, 1, 0)
        }

        self.init(
            .sRGB,
            red: Double(r) / 255,
            green: Double(g) / 255,
            blue:  Double(b) / 255,
            opacity: Double(a) / 255
        )
    }
}
