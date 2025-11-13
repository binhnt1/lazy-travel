import SwiftUI

// MARK: - Destination Card Component
struct DestinationCard: View {
    let name: String
    let location: String
    let rating: Double
    let reviewCount: Int
    let price: String
    let imageGradient: [String] // Hex colors for gradient
    let onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            ZStack(alignment: .bottomLeading) {
                // Background Gradient (replacing image)
                LinearGradient(
                    colors: imageGradient.map { Color(hex: $0) },
                    startPoint: .topLeading,
                    endPoint: .bottomTrailing
                )
                .frame(height: 200)

                // Gradient Overlay
                LinearGradient(
                    colors: [Color.clear, Color.black.opacity(0.7)],
                    startPoint: .top,
                    endPoint: .bottom
                )

                // Content
                VStack(alignment: .leading, spacing: 8) {
                    // Location
                    HStack(spacing: 4) {
                        Text("📍")
                            .font(.system(size: 12))
                        Text(location)
                            .font(.system(size: 12, weight: .medium))
                            .foregroundColor(.white.opacity(0.9))
                    }

                    // Destination Name
                    Text(name)
                        .font(.system(size: 20, weight: .bold))
                        .foregroundColor(.white)
                        .lineLimit(2)

                    HStack {
                        // Rating
                        HStack(spacing: 4) {
                            Text("⭐")
                                .font(.system(size: 12))
                            Text(String(format: "%.1f", rating))
                                .font(.system(size: 13, weight: .semibold))
                                .foregroundColor(.white)
                            Text("(\(reviewCount))")
                                .font(.system(size: 11))
                                .foregroundColor(.white.opacity(0.8))
                        }

                        Spacer()

                        // Price
                        Text(price)
                            .font(.system(size: 16, weight: .bold))
                            .foregroundColor(.white)
                    }
                }
                .padding(16)
            }
            .frame(width: 280, height: 200)
            .cornerRadius(16)
            .shadow(color: Color.black.opacity(0.15), radius: 10, x: 0, y: 4)
        }
        .buttonStyle(PlainButtonStyle())
    }
}

// MARK: - Destination Carousel
struct DestinationCarousel: View {
    let destinations: [DestinationData]

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            // Section Header
            HStack {
                VStack(alignment: .leading, spacing: 4) {
                    Text("Điểm Đến Phổ Biến")
                        .font(.system(size: 20, weight: .bold))
                        .foregroundColor(Color(hex: "212121"))

                    Text("Khám phá những nơi tuyệt vời")
                        .font(.system(size: 13))
                        .foregroundColor(Color(hex: "757575"))
                }

                Spacer()

                Button(action: {}) {
                    HStack(spacing: 4) {
                        Text("Xem tất cả")
                            .font(.system(size: 13, weight: .semibold))
                            .foregroundColor(Color(hex: "FF6B35"))
                        Text("→")
                            .foregroundColor(Color(hex: "FF6B35"))
                    }
                }
            }
            .padding(.horizontal, 16)

            // Carousel
            ScrollView(.horizontal, showsIndicators: false) {
                HStack(spacing: 16) {
                    ForEach(destinations) { destination in
                        DestinationCard(
                            name: destination.name,
                            location: destination.location,
                            rating: destination.rating,
                            reviewCount: destination.reviewCount,
                            price: destination.price,
                            imageGradient: destination.imageGradient,
                            onTap: {}
                        )
                    }
                }
                .padding(.horizontal, 16)
            }
        }
    }
}

// MARK: - Destination Data Model
struct DestinationData: Identifiable {
    let id = UUID()
    let name: String
    let location: String
    let rating: Double
    let reviewCount: Int
    let price: String
    let imageGradient: [String]
}

// MARK: - Preview
struct DestinationCard_Previews: PreviewProvider {
    static var previews: some View {
        let destinations = [
            DestinationData(
                name: "Vịnh Hạ Long",
                location: "Quảng Ninh",
                rating: 4.8,
                reviewCount: 1234,
                price: "3.500.000đ",
                imageGradient: ["667EEA", "764BA2"]
            ),
            DestinationData(
                name: "Phố Cổ Hội An",
                location: "Quảng Nam",
                rating: 4.9,
                reviewCount: 2156,
                price: "2.800.000đ",
                imageGradient: ["FF6B35", "F7931E"]
            ),
            DestinationData(
                name: "Sapa",
                location: "Lào Cai",
                rating: 4.7,
                reviewCount: 987,
                price: "4.200.000đ",
                imageGradient: ["43E97B", "38F9D7"]
            )
        ]

        return DestinationCarousel(destinations: destinations)
            .previewLayout(.sizeThatFits)
            .background(Color(hex: "F5F5F5"))
    }
}
