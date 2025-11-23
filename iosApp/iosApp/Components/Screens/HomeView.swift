import SwiftUI

// MARK: - Home View (Authenticated)
struct HomeView: View {
    @State private var selectedTab = 0

    var body: some View {
        NavigationView {
            ZStack(alignment: .bottom) {
                // Main Content
                ScrollView {
                    VStack(spacing: 20) {
                        // Header
                        HeaderBar(type: .greeting(
                            userName: "Minh",
                            subtitle: "Sẵn sàng cho chuyến phiêu lưu tiếp theo?"
                        ))

                        VStack(spacing: 20) {
                            // Passport Card
                            PassportCard(
                                userName: "Minh Nguyen",
                                level: 12,
                                xp: 2850,
                                maxXP: 5000,
                                tripsCount: 23,
                                countriesCount: 8,
                                photosCount: 342,
                                badges: ["🏆", "🎒", "🗺️", "📸", "⛰️", "🏝️"]
                            )
                            .padding(.horizontal, 16)

                            // Section: Current Trips
                            VStack(alignment: .leading, spacing: 12) {
                                HStack {
                                    VStack(alignment: .leading, spacing: 4) {
                                        Text("Chuyến Đi Của Bạn")
                                            .font(.system(size: 20, weight: .bold))
                                            .foregroundColor(Color(hex: "212121"))

                                        Text("Đang diễn ra và sắp tới")
                                            .font(.system(size: 13))
                                            .foregroundColor(Color(hex: "757575"))
                                    }

                                    Spacer()

                                    Button(action: {}) {
                                        Text("Xem tất cả →")
                                            .font(.system(size: 13, weight: .semibold))
                                            .foregroundColor(Color(hex: "FF6B35"))
                                    }
                                }
                                .padding(.horizontal, 16)

                                ScrollView(.horizontal, showsIndicators: false) {
                                    HStack(spacing: 12) {
                                        TripCard(
                                            destination: "Nha Trang",
                                            imageUrl: nil,
                                            dateRange: "15-18 Tháng 12",
                                            participants: ["Minh", "Lan", "Hoa", "Nam", "An"],
                                            status: .ongoing,
                                            progress: 0.65,
                                            onTap: {}
                                        )
                                        .frame(width: 300)

                                        TripCard(
                                            destination: "Đà Lạt",
                                            imageUrl: nil,
                                            dateRange: "20-22 Tháng 12",
                                            participants: ["Minh", "Lan"],
                                            status: .upcoming,
                                            progress: nil,
                                            onTap: {}
                                        )
                                        .frame(width: 300)
                                    }
                                    .padding(.horizontal, 16)
                                }
                            }

                            // Destination Carousel
                            DestinationCarousel(destinations: sampleDestinations)

                            // Quick Actions
                            VStack(alignment: .leading, spacing: 12) {
                                Text("Hành Động Nhanh")
                                    .font(.system(size: 20, weight: .bold))
                                    .foregroundColor(Color(hex: "212121"))
                                    .padding(.horizontal, 16)

                                ScrollView(.horizontal, showsIndicators: false) {
                                    HStack(spacing: 12) {
                                        QuickActionCard(icon: "➕", title: "Tạo chuyến đi", color: "FF6B35")
                                        QuickActionCard(icon: "🗳️", title: "Vote điểm đến", color: "2196F3")
                                        QuickActionCard(icon: "👥", title: "Tìm bạn đồng hành", color: "4CAF50")
                                        QuickActionCard(icon: "📝", title: "Lịch trình gợi ý", color: "9C27B0")
                                    }
                                    .padding(.horizontal, 16)
                                }
                            }

                            // Bottom Spacing for Tab Bar
                            Color.clear.frame(height: 80)
                        }
                    }
                }
                .background(Color(hex: "FAFAFA"))

                // Bottom Navigation
                BottomNavigationBar(selectedTab: $selectedTab)
            }
            .navigationBarHidden(true)
            .edgesIgnoringSafeArea(.bottom)
        }
    }

    // Sample Data
    private var sampleDestinations: [DestinationData] {
        [
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
            ),
            DestinationData(
                name: "Phú Quốc",
                location: "Kiên Giang",
                rating: 4.8,
                reviewCount: 1543,
                price: "5.000.000đ",
                imageGradient: ["FA709A", "FEE140"]
            )
        ]
    }
}

// MARK: - Quick Action Card
struct QuickActionCard: View {
    let icon: String
    let title: String
    let color: String

    var body: some View {
        VStack(spacing: 12) {
            Circle()
                .fill(Color(hex: color).opacity(0.15))
                .frame(width: 60, height: 60)
                .overlay(
                    Text(icon)
                        .font(.system(size: 28))
                )

            Text(title)
                .font(.system(size: 12, weight: .medium))
                .foregroundColor(Color(hex: "212121"))
                .multilineTextAlignment(.center)
                .frame(width: 100)
        }
        .padding(.vertical, 12)
        .frame(width: 120)
        .background(Color.white)
        .cornerRadius(16)
        .shadow(color: Color.black.opacity(0.05), radius: 8, x: 0, y: 2)
    }
}

// MARK: - Bottom Navigation Bar
struct BottomNavigationBar: View {
    @Binding var selectedTab: Int

    var body: some View {
        HStack {
            TabBarItem(icon: "🏠", label: "Trang chủ", isSelected: selectedTab == 0) {
                selectedTab = 0
            }
            TabBarItem(icon: "🗺️", label: "Khám phá", isSelected: selectedTab == 1) {
                selectedTab = 1
            }
            TabBarItem(icon: "➕", label: "Tạo mới", isSelected: selectedTab == 2) {
                selectedTab = 2
            }
            TabBarItem(icon: "👥", label: "Cộng đồng", isSelected: selectedTab == 3) {
                selectedTab = 3
            }
            TabBarItem(icon: "👤", label: "Cá nhân", isSelected: selectedTab == 4) {
                selectedTab = 4
            }
        }
        .padding(.vertical, 8)
        .background(Color.white)
        .shadow(color: Color.black.opacity(0.1), radius: 10, x: 0, y: -2)
    }
}

// MARK: - Tab Bar Item
struct TabBarItem: View {
    let icon: String
    let label: String
    let isSelected: Bool
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            VStack(spacing: 4) {
                Text(icon)
                    .font(.system(size: 24))

                Text(label)
                    .font(.system(size: 10, weight: isSelected ? .semibold : .regular))
                    .foregroundColor(isSelected ? Color(hex: "FF6B35") : Color(hex: "757575"))
            }
            .frame(maxWidth: .infinity)
        }
    }
}

// MARK: - Preview
struct HomeView_Previews: PreviewProvider {
    static var previews: some View {
        HomeView()
    }
}
