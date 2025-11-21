import SwiftUI

// MARK: - Header Types
enum HeaderType {
    case hero(logoText: String, onLoginTap: () -> Void)
    case greeting(userName: String, subtitle: String)
    case navigation(title: String, subtitle: String?, onBackTap: () -> Void, showMenu: Bool, onMenuTap: () -> Void)
    case tripNavigation(tripTitle: String, dateRange: String, participantCount: Int, onBackTap: () -> Void)
}

// MARK: - HeaderBar Component
struct HeaderBar: View {
    let type: HeaderType

    var body: some View {
        switch type {
        case .hero(let logoText, let onLoginTap):
            HeroHeader(logoText: logoText, onLoginTap: onLoginTap)
        case .greeting(let userName, let subtitle):
            GreetingHeader(userName: userName, subtitle: subtitle)
        case .navigation(let title, let subtitle, let onBackTap, let showMenu, let onMenuTap):
            NavigationHeader(title: title, subtitle: subtitle, onBackTap: onBackTap, showMenu: showMenu, onMenuTap: onMenuTap)
        case .tripNavigation(let tripTitle, let dateRange, let participantCount, let onBackTap):
            TripNavigationHeader(tripTitle: tripTitle, dateRange: dateRange, participantCount: participantCount, onBackTap: onBackTap)
        }
    }
}

// MARK: - Hero Header (Landing Page)
private struct HeroHeader: View {
    let logoText: String
    let onLoginTap: () -> Void

    var body: some View {
        HStack {
            // Logo
            HStack(spacing: 8) {
                Text("✈️")
                    .font(.system(size: 24))
                Text(logoText)
                    .font(.system(size: 18, weight: .bold))
                    .foregroundColor(.white)
            }

            Spacer()

            // Login Button
            Button(action: onLoginTap) {
                Text("Đăng nhập")
                    .font(.system(size: 13, weight: .semibold))
                    .foregroundColor(.white)
                    .padding(.horizontal, 16)
                    .padding(.vertical, 8)
                    .background(Color.white.opacity(0.2))
                    .cornerRadius(8)
                    .overlay(
                        RoundedRectangle(cornerRadius: 8)
                            .stroke(Color.white, lineWidth: 1)
                    )
            }
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 20)
        .background(
            LinearGradient(
                colors: [Color(hex: "FF6B35"), Color(hex: "F7931E")],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
        )
    }
}

// MARK: - Greeting Header (Authenticated Home)
private struct GreetingHeader: View {
    let userName: String
    let subtitle: String

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text("Xin chào, \(userName)! 👋")
                .font(.system(size: 24, weight: .bold))
                .foregroundColor(Color(hex: "212121"))

            Text(subtitle)
                .font(.system(size: 14))
                .foregroundColor(Color(hex: "757575"))
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.horizontal, 16)
        .padding(.vertical, 16)
        .background(Color(hex: "FAFAFA"))
    }
}

// MARK: - Navigation Header (Standard Pages)
private struct NavigationHeader: View {
    let title: String
    let subtitle: String?
    let onBackTap: () -> Void
    let showMenu: Bool
    let onMenuTap: () -> Void

    var body: some View {
        HStack(spacing: 8) {
            // Back Button
            Button(action: onBackTap) {
                Text("←")
                    .font(.system(size: 24))
                    .foregroundColor(Color(hex: "212121"))
                    .frame(width: 40, height: 40)
            }

            // Title & Subtitle
            VStack(alignment: .leading, spacing: 2) {
                Text(title)
                    .font(.system(size: 18, weight: .bold))
                    .foregroundColor(Color(hex: "212121"))

                if let subtitle = subtitle {
                    Text(subtitle)
                        .font(.system(size: 12))
                        .foregroundColor(Color(hex: "757575"))
                }
            }

            Spacer()

            // Optional Menu Button
            if showMenu {
                Button(action: onMenuTap) {
                    Text("⋮")
                        .font(.system(size: 24))
                        .foregroundColor(Color(hex: "212121"))
                        .frame(width: 40, height: 40)
                }
            }
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
        .background(Color(hex: "FAFAFA"))
    }
}

// MARK: - Trip Navigation Header
private struct TripNavigationHeader: View {
    let tripTitle: String
    let dateRange: String
    let participantCount: Int
    let onBackTap: () -> Void

    var body: some View {
        HStack(spacing: 8) {
            // Back Button
            Button(action: onBackTap) {
                Text("←")
                    .font(.system(size: 24))
                    .foregroundColor(Color(hex: "212121"))
                    .frame(width: 40, height: 40)
            }

            // Trip Info
            VStack(alignment: .leading, spacing: 4) {
                Text(tripTitle)
                    .font(.system(size: 16, weight: .bold))
                    .foregroundColor(Color(hex: "212121"))

                HStack(spacing: 12) {
                    Text("📅 \(dateRange)")
                        .font(.system(size: 12))
                        .foregroundColor(Color(hex: "757575"))

                    Text("👥 \(participantCount) người")
                        .font(.system(size: 12))
                        .foregroundColor(Color(hex: "757575"))
                }
            }

            Spacer()
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
        .background(Color(hex: "FAFAFA"))
    }
}

// MARK: - Preview
struct HeaderBar_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 20) {
            HeaderBar(type: .greeting(userName: "Minh", subtitle: "Sẵn sàng cho chuyến phiêu lưu tiếp theo?"))

            HeaderBar(type: .navigation(title: "Tạo Chuyến Đi Mới", subtitle: "Lên kế hoạch chi tiết", onBackTap: {}, showMenu: true, onMenuTap: {}))

            HeaderBar(type: .tripNavigation(tripTitle: "Chuyến đi Nha Trang", dateRange: "15-18/12/2024", participantCount: 5, onBackTap: {}))

            Spacer()
        }
    }
}
