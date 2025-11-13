import SwiftUI

// MARK: - Passport Card Component
struct PassportCard: View {
    let userName: String
    let level: Int
    let xp: Int
    let maxXP: Int
    let tripsCount: Int
    let countriesCount: Int
    let photosCount: Int
    let badges: [String]

    var body: some View {
        VStack(spacing: 0) {
            // Header with gradient
            ZStack(alignment: .topTrailing) {
                LinearGradient(
                    colors: [Color(hex: "667EEA"), Color(hex: "764BA2")],
                    startPoint: .topLeading,
                    endPoint: .bottomTrailing
                )
                .frame(height: 120)

                // Premium Badge
                Text("⭐ Premium")
                    .font(.system(size: 11, weight: .bold))
                    .foregroundColor(.white)
                    .padding(.horizontal, 8)
                    .padding(.vertical, 4)
                    .background(Color.white.opacity(0.2))
                    .cornerRadius(12)
                    .padding(12)
            }

            // Content
            VStack(spacing: 16) {
                // Avatar & Level
                HStack(spacing: 12) {
                    // Avatar
                    Circle()
                        .fill(
                            LinearGradient(
                                colors: [Color(hex: "FFD26F"), Color(hex: "FF8C42")],
                                startPoint: .topLeading,
                                endPoint: .bottomTrailing
                            )
                        )
                        .frame(width: 60, height: 60)
                        .overlay(
                            Text(String(userName.prefix(1)))
                                .font(.system(size: 24, weight: .bold))
                                .foregroundColor(.white)
                        )

                    // User Info
                    VStack(alignment: .leading, spacing: 4) {
                        Text(userName)
                            .font(.system(size: 18, weight: .bold))
                            .foregroundColor(Color(hex: "212121"))

                        HStack(spacing: 4) {
                            Text("Level \(level)")
                                .font(.system(size: 13, weight: .semibold))
                                .foregroundColor(Color(hex: "667EEA"))

                            Text("•")
                                .foregroundColor(Color(hex: "BDBDBD"))

                            Text("Explorer")
                                .font(.system(size: 13))
                                .foregroundColor(Color(hex: "757575"))
                        }
                    }

                    Spacer()
                }

                // XP Progress
                VStack(alignment: .leading, spacing: 6) {
                    HStack {
                        Text("XP: \(xp)/\(maxXP)")
                            .font(.system(size: 12, weight: .medium))
                            .foregroundColor(Color(hex: "757575"))

                        Spacer()

                        Text("\(Int((Double(xp) / Double(maxXP)) * 100))%")
                            .font(.system(size: 12, weight: .bold))
                            .foregroundColor(Color(hex: "667EEA"))
                    }

                    // Progress Bar
                    GeometryReader { geometry in
                        ZStack(alignment: .leading) {
                            RoundedRectangle(cornerRadius: 10)
                                .fill(Color(hex: "E0E0E0"))
                                .frame(height: 8)

                            RoundedRectangle(cornerRadius: 10)
                                .fill(
                                    LinearGradient(
                                        colors: [Color(hex: "667EEA"), Color(hex: "764BA2")],
                                        startPoint: .leading,
                                        endPoint: .trailing
                                    )
                                )
                                .frame(width: geometry.size.width * (CGFloat(xp) / CGFloat(maxXP)), height: 8)
                        }
                    }
                    .frame(height: 8)
                }

                // Stats Grid
                HStack(spacing: 12) {
                    StatItem(icon: "🗺️", value: "\(tripsCount)", label: "Chuyến đi")
                    Divider().frame(height: 30)
                    StatItem(icon: "🌍", value: "\(countriesCount)", label: "Quốc gia")
                    Divider().frame(height: 30)
                    StatItem(icon: "📸", value: "\(photosCount)", label: "Ảnh")
                }

                // Badges
                VStack(alignment: .leading, spacing: 8) {
                    Text("Huy hiệu")
                        .font(.system(size: 13, weight: .semibold))
                        .foregroundColor(Color(hex: "757575"))

                    ScrollView(.horizontal, showsIndicators: false) {
                        HStack(spacing: 8) {
                            ForEach(badges, id: \.self) { badge in
                                Text(badge)
                                    .font(.system(size: 24))
                                    .frame(width: 50, height: 50)
                                    .background(Color(hex: "F5F5F5"))
                                    .cornerRadius(12)
                                    .shadow(color: Color.black.opacity(0.05), radius: 2, x: 0, y: 1)
                            }
                        }
                    }
                }
            }
            .padding(16)
        }
        .background(Color.white)
        .cornerRadius(16)
        .shadow(color: Color.black.opacity(0.1), radius: 8, x: 0, y: 2)
    }
}

// MARK: - Stat Item Component
private struct StatItem: View {
    let icon: String
    let value: String
    let label: String

    var body: some View {
        VStack(spacing: 4) {
            Text(icon)
                .font(.system(size: 20))

            Text(value)
                .font(.system(size: 16, weight: .bold))
                .foregroundColor(Color(hex: "212121"))

            Text(label)
                .font(.system(size: 11))
                .foregroundColor(Color(hex: "757575"))
        }
        .frame(maxWidth: .infinity)
    }
}

// MARK: - Preview
struct PassportCard_Previews: PreviewProvider {
    static var previews: some View {
        PassportCard(
            userName: "Minh Nguyen",
            level: 12,
            xp: 2850,
            maxXP: 5000,
            tripsCount: 23,
            countriesCount: 8,
            photosCount: 342,
            badges: ["🏆", "🎒", "🗺️", "📸", "⛰️"]
        )
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
