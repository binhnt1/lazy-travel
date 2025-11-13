import SwiftUI

// MARK: - Trip Status
enum TripStatus {
    case ongoing
    case upcoming
    case past

    var color: Color {
        switch self {
        case .ongoing: return Color(hex: "4CAF50")
        case .upcoming: return Color(hex: "2196F3")
        case .past: return Color(hex: "757575")
        }
    }

    var label: String {
        switch self {
        case .ongoing: return "Đang diễn ra"
        case .upcoming: return "Sắp tới"
        case .past: return "Đã kết thúc"
        }
    }

    var icon: String {
        switch self {
        case .ongoing: return "🔥"
        case .upcoming: return "⏰"
        case .past: return "✓"
        }
    }
}

// MARK: - Trip Card Component
struct TripCard: View {
    let destination: String
    let imageUrl: String?
    let dateRange: String
    let participants: [String] // Avatar names or URLs
    let status: TripStatus
    let progress: Double? // 0.0 to 1.0, nil for upcoming/past
    let onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            VStack(spacing: 0) {
                // Image Header
                ZStack(alignment: .topLeading) {
                    // Destination Image
                    Rectangle()
                        .fill(
                            LinearGradient(
                                colors: [Color(hex: "667EEA"), Color(hex: "764BA2")],
                                startPoint: .topLeading,
                                endPoint: .bottomTrailing
                            )
                        )
                        .frame(height: 140)
                        .overlay(
                            Text(destination.prefix(1))
                                .font(.system(size: 48, weight: .bold))
                                .foregroundColor(.white.opacity(0.3))
                        )

                    // Status Badge
                    HStack(spacing: 4) {
                        Text(status.icon)
                            .font(.system(size: 12))
                        Text(status.label)
                            .font(.system(size: 11, weight: .semibold))
                    }
                    .foregroundColor(.white)
                    .padding(.horizontal, 10)
                    .padding(.vertical, 5)
                    .background(status.color)
                    .cornerRadius(12)
                    .padding(12)
                }

                // Content
                VStack(alignment: .leading, spacing: 12) {
                    // Destination Name
                    Text(destination)
                        .font(.system(size: 18, weight: .bold))
                        .foregroundColor(Color(hex: "212121"))
                        .frame(maxWidth: .infinity, alignment: .leading)

                    // Date Range
                    HStack(spacing: 6) {
                        Text("📅")
                            .font(.system(size: 14))
                        Text(dateRange)
                            .font(.system(size: 13))
                            .foregroundColor(Color(hex: "757575"))
                    }

                    // Participants
                    HStack(spacing: 8) {
                        Text("👥")
                            .font(.system(size: 14))

                        // Avatar Stack
                        HStack(spacing: -8) {
                            ForEach(0..<min(participants.count, 3), id: \.self) { index in
                                Circle()
                                    .fill(
                                        LinearGradient(
                                            colors: [Color(hex: "FFD26F"), Color(hex: "FF8C42")],
                                            startPoint: .topLeading,
                                            endPoint: .bottomTrailing
                                        )
                                    )
                                    .frame(width: 24, height: 24)
                                    .overlay(
                                        Text(String(participants[index].prefix(1)))
                                            .font(.system(size: 10, weight: .bold))
                                            .foregroundColor(.white)
                                    )
                                    .overlay(
                                        Circle()
                                            .stroke(Color.white, lineWidth: 2)
                                    )
                            }

                            if participants.count > 3 {
                                Circle()
                                    .fill(Color(hex: "E0E0E0"))
                                    .frame(width: 24, height: 24)
                                    .overlay(
                                        Text("+\(participants.count - 3)")
                                            .font(.system(size: 9, weight: .bold))
                                            .foregroundColor(Color(hex: "757575"))
                                    )
                                    .overlay(
                                        Circle()
                                            .stroke(Color.white, lineWidth: 2)
                                    )
                            }
                        }

                        Text("\(participants.count) người")
                            .font(.system(size: 12))
                            .foregroundColor(Color(hex: "757575"))

                        Spacer()
                    }

                    // Progress Bar (only for ongoing trips)
                    if let progress = progress, status == .ongoing {
                        VStack(alignment: .leading, spacing: 6) {
                            HStack {
                                Text("Tiến độ")
                                    .font(.system(size: 11))
                                    .foregroundColor(Color(hex: "757575"))

                                Spacer()

                                Text("\(Int(progress * 100))%")
                                    .font(.system(size: 11, weight: .bold))
                                    .foregroundColor(Color(hex: "4CAF50"))
                            }

                            GeometryReader { geometry in
                                ZStack(alignment: .leading) {
                                    RoundedRectangle(cornerRadius: 10)
                                        .fill(Color(hex: "E0E0E0"))
                                        .frame(height: 6)

                                    RoundedRectangle(cornerRadius: 10)
                                        .fill(Color(hex: "4CAF50"))
                                        .frame(width: geometry.size.width * progress, height: 6)
                                }
                            }
                            .frame(height: 6)
                        }
                    }
                }
                .padding(16)
            }
            .background(Color.white)
            .cornerRadius(16)
            .shadow(color: Color.black.opacity(0.08), radius: 8, x: 0, y: 2)
        }
        .buttonStyle(PlainButtonStyle())
    }
}

// MARK: - Preview
struct TripCard_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 16) {
            TripCard(
                destination: "Nha Trang",
                imageUrl: nil,
                dateRange: "15-18 Tháng 12",
                participants: ["Minh", "Lan", "Hoa", "Nam", "An"],
                status: .ongoing,
                progress: 0.65,
                onTap: {}
            )

            TripCard(
                destination: "Đà Lạt",
                imageUrl: nil,
                dateRange: "20-22 Tháng 12",
                participants: ["Minh", "Lan"],
                status: .upcoming,
                progress: nil,
                onTap: {}
            )
        }
        .padding()
        .previewLayout(.sizeThatFits)
        .background(Color(hex: "F5F5F5"))
    }
}
