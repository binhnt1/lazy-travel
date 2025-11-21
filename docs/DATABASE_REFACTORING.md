# Database Refactoring - Buddy Request System

## Vấn đề cũ:
- **BuddyRequest** chứa quá nhiều thông tin không liên quan:
  - Host information (rating, reviewsCount, verified, tripsCount)
  - Participant management (currentParticipants, availableSlots, matchedCount)
  - Country data (không chuẩn hóa)

## Giải pháp mới:

### 1. **UserReview** (Mới)
Quản lý đánh giá cho user (host)
- `userId`: User được đánh giá (host)
- `reviewerId`: Người đánh giá
- `tripId`: BuddyRequest ID
- `rating`: 1.0 - 5.0
- `comment`: Nội dung đánh giá
- Các tiêu chí chi tiết: communication, organization, friendliness, reliability
- Metadata: helpful count, reported, verified

**Lợi ích:**
- Tách biệt logic đánh giá ra khỏi User
- Có thể đánh giá chi tiết theo nhiều tiêu chí
- Track được verified reviews (người thực sự đi cùng)

### 2. **TripParticipant** (Mới)
Quản lý người tham gia hành trình
- `tripId`: BuddyRequest ID
- `userId`: Người tham gia
- `status`: PENDING, APPROVED, REJECTED, CANCELLED, COMPLETED
- Request info: message, timestamp
- Approval info: approvedAt, approvedBy, rejectionReason
- Payment info: paidAmount, paymentStatus
- Cached user info: name, avatar, age, gender
- Trip completion: attended, completedAt

**Lợi ích:**
- Quản lý flow: Request → Approval → Payment → Completion
- Host có thể approve/reject requests
- Track được payment status
- Denormalize user info để performance tốt hơn

### 3. **Country** (Mới)
Danh sách quốc gia chuẩn hóa
- `code`: VN, TH, US...
- `name`: Vietnam, Thailand...
- `nameVi`: Việt Nam, Thái Lan...
- `emoji`: 🇻🇳, 🇹🇭...
- Thông tin bổ sung: region, currency, phoneCode, timezone...

**Lợi ích:**
- Data chuẩn hóa, không bị duplicate
- Dễ mở rộng cho multi-country
- Support i18n tốt hơn

### 4. **BuddyRequest** (Đã refactor)
Chỉ chứa thông tin về chuyến đi:
- Core trip info: destination, dates, duration
- Budget & pricing
- Max participants (không lưu current count nữa)
- Description & metadata
- Country code (relation to Country)

**Loại bỏ các trường:**
- ❌ `totalCapacity`, `availableSlots`, `currentParticipants` → Tính từ TripParticipant
- ❌ `hostName`, `hostAvatar`, `rating`, `reviewsCount`, `verified`, `tripsCount` → Query từ User + UserReview
- ❌ `matchedCount` → Không cần thiết
- ❌ `country` (string) → Dùng `countryCode` relation

### 5. **BuddyRequestExtensions** (Mới)
Repository extensions để query enriched data:

```kotlin
data class EnrichedTripInfo(
    val trip: BuddyRequest,
    val host: User?,
    val hostRating: Double,
    val hostReviewsCount: Int,
    val currentParticipants: Int,
    val availableSlots: Int,
    val pendingRequests: Int
)

// Helper functions:
- getHostInfo(userId): Get host information
- getHostRating(userId): Calculate rating from reviews
- getTripParticipantCount(tripId): Get participant statistics
- getEnrichedTripInfo(...): Get all info in one call
```

## Cách sử dụng:

### Trước (Cũ):
```kotlin
val trips = buddyRequestRepo.getRecords<BuddyRequest>()
// Host info, rating, participants đã có sẵn trong BuddyRequest
```

### Sau (Mới):
```kotlin
val userRepo = BaseRepository<User>()
val reviewRepo = BaseRepository<UserReview>()
val participantRepo = BaseRepository<TripParticipant>()
val buddyRequestRepo = BaseRepository<BuddyRequest>()

val trips = buddyRequestRepo.getRecords<BuddyRequest>()
val enrichedTrips = getEnrichedTripsList(
    trips, userRepo, reviewRepo, participantRepo
)

// Hoặc từng trip:
val enrichedTrip = getEnrichedTripInfo(
    trip, userRepo, reviewRepo, participantRepo
)
```

## Lợi ích tổng thể:

### 1. **Single Responsibility Principle**
- Mỗi model có 1 trách nhiệm rõ ràng
- Dễ maintain và test

### 2. **Database Normalization**
- Không duplicate data
- Data consistency tốt hơn
- Dễ update (e.g., user rating update không cần update BuddyRequest)

### 3. **Scalability**
- Dễ thêm features mới (e.g., review system, payment tracking)
- Query performance tốt hơn với proper indexing
- Có thể cache enriched data khi cần

### 4. **Flexibility**
- Có thể query participants riêng (e.g., danh sách người đã tham gia)
- Có thể query reviews riêng (e.g., all reviews của 1 user)
- Có thể filter/sort theo nhiều tiêu chí

## Migration cần làm:

1. ✅ Tạo models mới: UserReview, TripParticipant, Country
2. ✅ Update BuddyRequest: remove deprecated fields
3. ✅ Tạo BuddyRequestExtensions với helper functions
4. ⏳ Update UI components để sử dụng EnrichedTripInfo
5. ⏳ Update BuddyScreen để query enriched data
6. ⏳ Seed data cho các model mới

## Notes:

- Denormalization vẫn được dùng cho performance (e.g., cached user info trong TripParticipant)
- Trade-off: More queries nhưng data cleaner và flexible hơn
- Có thể implement caching layer sau nếu cần optimize performance

