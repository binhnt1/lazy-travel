import SwiftUI
import shared

struct ContentView: View {
    @StateObject private var viewModel = DestinationViewModelWrapper()

    var body: some View {
        NavigationView {
            Group {
                switch viewModel.uiState {
                case .loading:
                    ProgressView()
                        .progressViewStyle(CircularProgressViewStyle())

                case .success(let destinations):
                    List(destinations, id: \.id) { destination in
                        DestinationRow(destination: destination)
                    }

                case .error(let message):
                    VStack {
                        Image(systemName: "exclamationmark.triangle")
                            .font(.largeTitle)
                            .foregroundColor(.red)
                        Text("Error: \(message)")
                            .foregroundColor(.red)
                            .padding()
                    }
                }
            }
            .navigationTitle("Lazy Travel")
        }
        .onAppear {
            viewModel.loadDestinations()
        }
    }
}

struct DestinationRow: View {
    let destination: Destination

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text(destination.name)
                .font(.headline)
                .fontWeight(.bold)

            Text(destination.description_)
                .font(.subheadline)
                .foregroundColor(.secondary)

            HStack {
                Text("⭐ \(String(format: "%.1f", destination.rating))")
                    .font(.caption)

                Spacer()

                Text("\(Int(destination.price).formatted()) VNĐ")
                    .font(.caption)
                    .fontWeight(.bold)
            }
        }
        .padding(.vertical, 4)
    }
}

// MARK: - ViewModel Wrapper
class DestinationViewModelWrapper: ObservableObject {
    private let viewModel = AppModule.shared.provideDestinationViewModel()

    @Published var uiState: UiState = .loading

    enum UiState {
        case loading
        case success([Destination])
        case error(String)
    }

    init() {
        observeUiState()
    }

    func loadDestinations() {
        viewModel.loadDestinations()
    }

    private func observeUiState() {
        // Observe Kotlin Flow from Swift
        // Note: You'll need to add a helper in the shared module for better Flow observation
        // For now, we'll simulate with a simple callback
        Task {
            await MainActor.run {
                viewModel.uiState.collect { state in
                    if let loadingState = state as? DestinationViewModel.UiStateLoading {
                        self.uiState = .loading
                    } else if let successState = state as? DestinationViewModel.UiStateSuccess {
                        self.uiState = .success(successState.destinations)
                    } else if let errorState = state as? DestinationViewModel.UiStateError {
                        self.uiState = .error(errorState.message)
                    }
                } onCompletion: { _ in }
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

extension Int {
    func formatted() -> String {
        let formatter = NumberFormatter()
        formatter.numberStyle = .decimal
        formatter.groupingSeparator = "."
        return formatter.string(from: NSNumber(value: self)) ?? "\(self)"
    }
}
