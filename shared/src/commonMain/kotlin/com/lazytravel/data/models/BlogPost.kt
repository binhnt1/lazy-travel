package com.lazytravel.data.models

import com.lazytravel.data.base.BaseModel
import com.lazytravel.data.base.BaseRepository
import com.lazytravel.data.base.baseCollection
import com.lazytravel.data.base.collectionName
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class BlogPost(
    @EncodeDefault val userId: String = "",
    @EncodeDefault val authorName: String = "",
    @EncodeDefault val authorAvatar: String = "",
    @EncodeDefault val title: String = "",
    @EncodeDefault val excerpt: String = "",
    @EncodeDefault val content: String = "",
    @EncodeDefault val category: String = "",
    @EncodeDefault val categoryColor: String = "",
    @EncodeDefault val thumbnail: String = "",
    @EncodeDefault val thumbnailEmoji: String = "",
    @EncodeDefault val readTime: Int = 0,
    @EncodeDefault val views: Int = 0,
    @EncodeDefault val likes: Int = 0,
    @EncodeDefault val published: Boolean = true,
    @EncodeDefault val publishedAt: String = ""
) : BaseModel() {

    override fun serializeToJson(item: BaseModel): String {
        return json.encodeToString(serializer(), item as BlogPost)
    }

    override fun getSchema() = baseCollection(collectionName()) {
        relation("userId") {
            required = true
            collectionId = "users"
            cascadeDelete = false
        }
        text("authorName") { required = true; max = 100 }
        text("authorAvatar") { required = false; max = 500 }
        text("title") { required = true; max = 200 }
        text("excerpt") { required = false; max = 500 }
        text("content") { required = true; max = 50000 }
        text("category") { required = true; max = 50 }
        text("categoryColor") { required = false; max = 20 }
        text("thumbnail") { required = false; max = 500 }
        text("thumbnailEmoji") { required = false; max = 10 }
        number("readTime") { required = false; min = 1.0; max = 300.0; onlyInt = true }
        number("views") { required = false; min = 0.0; onlyInt = true }
        number("likes") { required = false; min = 0.0; onlyInt = true }
        bool("published") { required = false }
        text("publishedAt") { required = false; max = 30 }
    }

    override suspend fun getSeedData(): List<BlogPost> {
        val usersRepo = BaseRepository<User>()
        val users = usersRepo.getRecords<User>().getOrNull() ?: emptyList()

        if (users.isEmpty()) {
            println("⚠️ No users found for seeding blog posts")
            return emptyList()
        }

        val blogPosts = listOf(
            BlogPost(
                userId = users[0].id,
                authorName = "Thanh Hà",
                authorAvatar = "https://i.pravatar.cc/150?img=45",
                title = "10 bãi biển đẹp nhất Việt Nam năm 2024",
                excerpt = "Khám phá những bãi biển hoang sơ, nước trong xanh và cát trắng mịn màng. Từ Phú Quốc đến Quy Nhơn...",
                content = """
                    <h1>10 Bãi Biển Đẹp Nhất Việt Nam Năm 2024</h1>

                    <p>Việt Nam sở hữu hơn 3000km bờ biển với vô số bãi biển tuyệt đẹp, mỗi nơi đều có những nét đặc trưng riêng.
                    Dưới đây là 10 bãi biển phải đến ít nhất một lần trong đời bạn.</p>

                    <h2>1. Phú Quốc - Đảo Ngọc Của Việt Nam</h2>
                    <p>Phú Quốc là <strong>đảo lớn nhất Việt Nam</strong> nằm ở tây nam vịnh Thái Lan. Bãi biển tại đây sạch sẽ,
                    nước trong xanh và cát trắng mịn màng. Đây là địa điểm lý tưởng cho những ai yêu thích <em>lặn biển</em>
                    và tham quan những vùng san hô đa dạng.</p>

                    <h3>Điều không thể bỏ lỡ:</h3>
                    <ul>
                    <li>Lặn biển khám phá san hô Diễm Hương</li>
                    <li>Tham quan vườn tiêu Phú Quốc</li>
                    <li>Thư giãn tại Sunset Sanato</li>
                    <li>Ăn hải sản tươi sống tại chợ đêm</li>
                    </ul>

                    <h2>2. Nha Trang - Thành Phố Biển Năng Động</h2>
                    <p>Nha Trang nổi tiếng với <strong>bãi biển dài 6km</strong> uốn lượn theo hình cánh cung.
                    Nước biển ở đây an toàn, thích hợp cho bơi lội và các hoạt động <em>thể thao nước</em>.</p>

                    <h3>Điều không thể bỏ lỡ:</h3>
                    <ul>
                    <li>Tham quan Tháp Nha Trang</li>
                    <li>Đi tour đảo Hòn Mun</li>
                    <li>Lắng nghe nhạc nước tại bãi biển</li>
                    <li>Thưởng thức cá nướng muối trên bãi biển</li>
                    </ul>

                    <h2>3. Quy Nhơn - Bãi Biển Hoang Sơ</h2>
                    <p>Quy Nhơn là một <strong>bãi biển đẹp nhưng ít người biết tới</strong>. Nơi đây còn rất hoang sơ,
                    nước trong xanh và những hàng dừa dại mọc dọc bờ biển tạo nên một cảnh tượng thơ mộng.</p>

                    <p>Hãy ghé thăm <strong>Quy Nhơn</strong> để trải nghiệm sự yên tĩnh và thanh bình mà những bãi biển khác không có.</p>

                    <h2>Kết Luận</h2>
                    <p>Mỗi bãi biển Việt Nam đều có <em>vẻ đẹp riêng</em> và những điều đặc sắc riêng.
                    Hãy sắp xếp thời gian để khám phá tất cả chúng và tạo ra những kỷ niệm đẹp với gia đình và bạn bè.</p>
                """.trimIndent(),
                category = "HƯỚNG DẪN",
                categoryColor = "#4ECDC4",
                thumbnail = "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=800&q=80",
                thumbnailEmoji = "🏖️",
                readTime = 5,
                views = 1250,
                likes = 89,
                published = true,
                publishedAt = "2024-12-15"
            ),
            BlogPost(
                userId = users.getOrNull(1)?.id ?: users[0].id,
                authorName = "Minh Ngọc",
                authorAvatar = "https://i.pravatar.cc/150?img=32",
                title = "Du lịch Đà Lạt chỉ với 2 triệu/người",
                excerpt = "Bí quyết đi Đà Lạt tiết kiệm mà vẫn trọn vẹn. Ăn uống, nghỉ ngơi và tham quan với ngân sách hợp lý...",
                content = """
                    <h1>Du Lịch Đà Lạt Tiết Kiệm Chỉ 2 Triệu Đồng/Người</h1>

                    <p>Đà Lạt không chỉ dành cho những ai có túi tiền dày. Với <strong>kế hoạch hợp lý</strong>,
                    bạn hoàn toàn có thể tận hưởng vẻ đẹp của thành phố sương mù này chỉ với 2 triệu đồng.</p>

                    <h2>1. Chỗ Ở - 400,000đ/Đêm</h2>
                    <p>Thay vì thuê khách sạn 3-5 sao, hãy tìm kiếm <strong>homestay hoặc phòng trọ</strong>
                    ở những khu vực ngoài trung tâm. Bạn sẽ có chỗ sạch sẽ, an toàn với giá chỉ 400,000-500,000đ/đêm.</p>

                    <h3>Gợi ý:</h3>
                    <ul>
                    <li>Khu vực Đồi 1 (Xã Tà Nung)</li>
                    <li>Homestay gần hồ Tuyền Lâm</li>
                    <li>Phòng trọ gần chợ Dalat</li>
                    </ul>

                    <h2>2. Ăn Uống - 600,000đ</h2>
                    <p>Đà Lạt có rất nhiều <em>quán ăn địa phương</em> với giá rẻ:</p>

                    <ul>
                    <li><strong>Cơm tấm:</strong> 30,000-40,000đ</li>
                    <li><strong>Mì Quảng:</strong> 35,000đ</li>
                    <li><strong>Cà chua nướng, khoai lang nướng:</strong> 20,000đ</li>
                    <li><strong>Trà sữa:</strong> 20,000-25,000đ</li>
                    </ul>

                    <h2>3. Vé Vào Cửa - 400,000đ</h2>
                    <p>Các điểm tham quan chính:</p>

                    <ul>
                    <li>Thác Prenn: 100,000đ</li>
                    <li>Hồ Tuyền Lâm: 50,000đ</li>
                    <li>Làng Cù Lao: 50,000đ</li>
                    <li>Chợ Hoa Đà Lạt: Miễn phí</li>
                    </ul>

                    <h2>4. Khác - 600,000đ</h2>
                    <p>Gồm <em>vé máy bay, xe bus, giải khát, suvenir...</em></p>

                    <h2>Mẹo Du Lịch Tiết Kiệm</h2>
                    <ol>
                    <li>Di chuyển bằng <strong>xe buýt công cộng</strong> thay vì xe tắc xi</li>
                    <li>Ăn tại các <strong>quán cơm hộp địa phương</strong> thay vì nhà hàng du lịch</li>
                    <li>Tham quan <strong>miễn phí hoặc rẻ</strong> như chợ Hoa, Hồ Xuân Hương</li>
                    <li>Mua suvenir tại <strong>chợ địa phương</strong> rẻ hơn cửa hàng du lịch</li>
                    </ol>

                    <h2>Kết Luận</h2>
                    <p>Du lịch <strong>không phải lúc nào cũng đắt</strong>. Với sự chuẩn bị kỹ lưỡng và linh hoạt,
                    bạn hoàn toàn có thể có một chuyến du lịch Đà Lạt tuyệt vời với chi phí ít.</p>
                """.trimIndent(),
                category = "TIẾT KIỆM",
                categoryColor = "#FA709A",
                thumbnail = "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&q=80",
                thumbnailEmoji = "💰",
                readTime = 7,
                views = 2340,
                likes = 156,
                published = true,
                publishedAt = "2024-12-10"
            ),
            BlogPost(
                userId = users.getOrNull(2)?.id ?: users[0].id,
                authorName = "Duy Khánh",
                authorAvatar = "https://i.pravatar.cc/150?img=15",
                title = "Góc chụp ảnh đẹp ở Hội An mà ít ai biết",
                excerpt = "Tránh đám đông, tìm những góc chụp độc đáo tại phố cổ Hội An. Check-in sống ảo cùng ánh đèn lồng...",
                content = """
                    <h1>Góc Chụp Ảnh Đẹp Ở Hội An Mà Ít Ai Biết</h1>

                    <p>Hội An về đêm là <strong>một bức tranh tuyệt đẹp</strong> với ánh đèn lồng rực rỡ.
                    Tuy nhiên, hầu hết du khách đều tập trung ở những địa điểm du lịch nổi tiếng.
                    Dưới đây là những <em>góc chụp ảnh lý tưởng</em> mà ít ai biết đến.</p>

                    <h2>1. Cầu Gỗ - Ban Sáng Sớm</h2>
                    <p>Thay vì đi vào giờ cao điểm (18h-20h) khi đông nghẹt, hãy đi vào <strong>sáng sớm lúc 5-6h</strong>.
                    Nước sông yên tĩnh, ánh nắng vàng ấm tạo nên những bức ảnh siêu đẹp.</p>

                    <h2>2. Phố Tây - Lúc Mặt Trời Lặn</h2>
                    <p>Phố Tây không chỉ nổi tiếng về quán cà phê và quán ăn. <em>Lúc mặt trời lặn</em> (17h30-18h30),
                    ánh sáng ở đây <strong>vàng ươm, mềm mại</strong> là thời điểm vàng để chụp ảnh.</p>

                    <h2>3. Hẻm Phía Sau Chợ Hội An</h2>
                    <p>Phía sau chợ Hội An có <strong>những hẻm nhỏ xinh xắn</strong> với những nhà cổ kính.
                    Nơi đây ít khách du lịch, yên tĩnh và rất thích hợp để chụp ảnh chi tiết.</p>

                    <h2>4. Cầu Cổ Về Đêm Muộn</h2>
                    <p>Nếu bạn không sợ mệt, hãy quay lại Cầu Cổ vào <strong>lúc 22h-23h</strong>.
                    Lúc này <em>đám đông đã tan hết</em>, những chiếc đèn lồng vẫn còn sáng,
                    và bạn có thể chụp ảnh thoải mái.</p>

                    <h2>5. Con Đường Ngô Gia Tự</h2>
                    <p>Đây là <strong>con đường cổ dài nhất</strong> ở Hội An. Vào ban sáng,
                    <em>ánh nắng lọc qua những ngôi nhà cổ</em> tạo nên những bóng đổ độc đáo.</p>

                    <h2>Lời Khuyên Chuyên Nghiệp</h2>
                    <ul>
                    <li>Mang theo <strong>máy ảnh hoặc điện thoại có chế độ night mode</strong></li>
                    <li>Chọn thời gian <strong>sáng sớm hoặc tối muộn</strong> để tránh đám đông</li>
                    <li>Chụp <strong>chi tiết</strong> như cửa sổ, chân dung người dân, không chỉ toàn cảnh</li>
                    <li>Tôn trọng <strong>cuộc sống của cư dân địa phương</strong> khi chụp ảnh</li>
                    </ul>

                    <h2>Kết Luận</h2>
                    <p>Hội An rất đẹp, nhưng để có những bức ảnh <strong>thực sự độc đáo</strong>,
                    bạn cần biết <em>đi lúc nào</em> và <em>đi đâu</em>. Hãy khám phá những góc lạ lẫm của Hội An!</p>
                """.trimIndent(),
                category = "NHIẾP ẢNH",
                categoryColor = "#667EEA",
                thumbnail = "https://images.unsplash.com/photo-1555400038-63f5ba517a47?w=800&q=80",
                thumbnailEmoji = "📸",
                readTime = 6,
                views = 1890,
                likes = 203,
                published = true,
                publishedAt = "2024-12-08"
            ),
            BlogPost(
                userId = users.getOrNull(3)?.id ?: users[0].id,
                authorName = "Hương Ly",
                authorAvatar = "https://i.pravatar.cc/150?img=28",
                title = "15 món ăn vặt Sài Gòn bạn phải thử",
                excerpt = "Food tour khắp Sài Gòn với những món ăn đường phố hấp dẫn. Từ bánh tráng trộn đến chè khúc bạch...",
                content = """
                    <h1>15 Món Ăn Vặt Sài Gòn Bạn Phải Thử Trong Đời</h1>

                    <p>Sài Gòn không chỉ nổi tiếng với những tòa nhà cao tầng mà còn là <strong>thiên đường ẩm thực</strong>
                    với những món ăn vặt độc đáo. Dưới đây là 15 món ăn vặt <em>phải thử ít nhất một lần</em>
                    khi bạn đến Sài Gòn.</p>

                    <h2>1. Bánh Tráng Trộn</h2>
                    <p><strong>Bánh tráng trộn</strong> là một trong những <em>đặc sản nổi tiếng</em> của Sài Gòn.
                    Được trộn với tương cà, giấm, tỏi, ớt tạo nên hương vị chua, cay, mặn rất hấp dẫn.</p>

                    <h3>Địa chỉ:</h3>
                    <ul>
                    <li>Quán Bánh Tráng Trộn trên đường Nguyễn Hữu Cảnh</li>
                    <li>Chợ Bến Thành - Khu vực đồ ăn vặt</li>
                    </ul>

                    <h2>2. Chè Khúc Bạch</h2>
                    <p>Chè <strong>khúc bạch truyền thống</strong> với những khúc bạch mềm, nước chè ngọt thanh.
                    Uống vào <em>chiều tà</em> thật là thoải mái.</p>

                    <h2>3. Bánh Mì Thịt Nướng</h2>
                    <p><strong>Bánh mì Sài Gòn</strong> nổi tiếng với bánh giòn rụm, nhân thịt nướng thơm ngon,
                    rau tươi và <em>pâté béo ngậy</em>.</p>

                    <h2>4. Bánh Cuốn Nóng</h2>
                    <p>Bánh cuốn nhân thịt <strong>nóng hổi vừa làm</strong>, nhúng nước mắm chua ngọt,
                    ăn kèm rau sống rất ngon miệng.</p>

                    <h2>5. Nước Chanh Đào</h2>
                    <p>Chanh đào <strong>tươi mát</strong> uống vào hè Sài Gòn nóng bức là cực kỳ sảng khoái.</p>

                    <h2>6. Bánh Góp Sài Gòn</h2>
                    <p>Bánh góp với <strong>nhân tôm sốt mayonnaise</strong>, rau ngò, ớt tương tác hòa quyện
                    tạo nên hương vị độc đáo.</p>

                    <h2>7. Bánh Cam</h2>
                    <p>Bánh cam <strong>giòn rụm bên ngoài, mềm ngon bên trong</strong>, ăn nóng với sữa
                    hoặc sữa đặc thì siêu ngon.</p>

                    <h2>8. Cơm Cháy</h2>
                    <p><strong>Cơm cháy vàng ươm</strong> nhúng vào nước dùng hoặc tương, cơm giòn tan,
                    rất thích hợp để ăn vặt.</p>

                    <h2>9. Kem Ốc Que</h2>
                    <p>Kem <strong>lạ kỳ với hương vị ốc que</strong> (một loại tôm nước ngọt).
                    Một trải nghiệm <em>độc đáo cho những ai dám thử</em>.</p>

                    <h2>10. Bánh Rán</h2>
                    <p>Bánh rán <strong>giòn rụm, nóng hổi</strong> với nhân đậu xanh hoặc mặn,
                    ăn vừa giòn vừa dẻo thật tuyệt vời.</p>

                    <h2>Những Quán Ăn Nổi Tiếng</h2>
                    <p>Hãy ghé thăm các quán ăn vặt nổi tiếng ở:</p>
                    <ul>
                    <li><strong>Chợ Bến Thành</strong> - Hội tụ của hàng trăm mon ăn vặt</li>
                    <li><strong>Phố Nguyễn Huệ</strong> - Khu phố ẩm thực nổi tiếng</li>
                    <li><strong>Hẻm Chợ Lớn</strong> - Nơi những người bản địa ăn ăn</li>
                    </ul>

                    <h2>Kết Luận</h2>
                    <p>Sài Gòn với <strong>nền ẩm thực đa dạng</strong>, từ đồ ăn vặt rẻ tiền đến những nhà hàng sang trọng.
                    Hãy thử hết tất cả để hiểu rõ hơn về <em>tâm hồn ẩm thực</em> của thành phố này.</p>
                """.trimIndent(),
                category = "ẨM THỰC",
                categoryColor = "#FF9800",
                thumbnail = "https://images.unsplash.com/photo-1559339352-11d035aa65de?w=800&q=80",
                thumbnailEmoji = "🍜",
                readTime = 8,
                views = 3120,
                likes = 278,
                published = true,
                publishedAt = "2024-12-05"
            ),
            BlogPost(
                userId = users.getOrNull(4)?.id ?: users[0].id,
                authorName = "Quang Trung",
                authorAvatar = "https://i.pravatar.cc/150?img=67",
                title = "Chinh phục Fansipan - Nóc nhà Đông Dương",
                excerpt = "Hành trình 2 ngày 1 đêm trekking lên đỉnh cao nhất Việt Nam. Chuẩn bị gì và lưu ý những điều quan trọng...",
                content = """
                    <h1>Chinh Phục Fansipan - Nóc Nhà Đông Dương 3143m</h1>

                    <p>Fansipan cao <strong>3143m so với mặt nước biển</strong>, là đỉnh núi cao nhất
                    <em>Việt Nam và Đông Dương</em>. Chinh phục Fansipan là <strong>mơ ước của nhiều nhà leo núi</strong>.
                    Dưới đây là hướng dẫn chi tiết để bạn có một chuyến trekking an toàn và thành công.</p>

                    <h2>1. Chuẩn Bị Trước Khi Đi</h2>

                    <h3>Sức Khỏe & Thể Lực</h3>
                    <p>Fansipan là một <strong>cuộc leo núi khó khăn</strong>. Bạn cần:</p>
                    <ul>
                    <li>Có <strong>thể lực tốt</strong> - nên tập thể dục 2-3 tháng trước</li>
                    <li>Không bị <strong>sợ độ cao</strong></li>
                    <li>Kiểm tra sức khỏe trước khi đi</li>
                    <li>Chuẩn bị tinh thần sẵn sàng chịu thử thách</li>
                    </ul>

                    <h3>Trang Thiết Bị Cần Thiết</h3>
                    <ul>
                    <li><strong>Ba lô 40-50 lít</strong> để đựng đồ đạc</li>
                    <li><strong>Giày leo núi cao cổ</strong> - rất quan trọng</li>
                    <li><strong>Quần áo thể thao</strong> thấm hút tốt</li>
                    <li><strong>Nón rộng vành</strong> và kính mắt</li>
                    <li><strong>Bình nước 2-3 lít</strong></li>
                    <li><strong>Đèn pin hoặc headlamp</strong></li>
                    <li><strong>Áo mưa</strong> - thời tiết trên núi thay đổi nhanh</li>
                    <li><strong>Dây thừa an toàn</strong> và móc</li>
                    </ul>

                    <h2>2. Lộ Trình Trekking</h2>

                    <h3>Ngày 1: Sapa - Hàng Chuối - Thạch Phòng</h3>
                    <p><strong>Khoảng cách:</strong> ~16km<br>
                    <strong>Thời gian:</strong> 6-7 tiếng<br>
                    <strong>Độ cao:</strong> Từ 1600m lên 2000m</p>

                    <p>Ngày đầu tiên là <em>ngày quen với độ cao và thích ứng</em>.
                    Đừng vội vàng, cần giữ sức cho những ngày tiếp theo.</p>

                    <h3>Ngày 2: Thạch Phòng - Nóc Fansipan - Sapa</h3>
                    <p><strong>Khoảng cách:</strong> ~10km<br>
                    <strong>Thời gian:</strong> 5-6 tiếng<br>
                    <strong>Độ cao:</strong> Từ 2000m lên 3143m</p>

                    <p>Đây là <strong>ngày khó nhất</strong>. Cần khởi hành rất sáng sớm (4h-5h)
                    để đến đỉnh vào giữa trưa trước khi thời tiết xấu.</p>

                    <h2>3. Những Điều Cần Lưu Ý</h2>

                    <h3>Sức Khỏe Trên Đường</h3>
                    <ul>
                    <li>Uống <strong>nước thường xuyên</strong> để tránh mất nước</li>
                    <li>Ăn <strong>nhẹ nhàng</strong> - bánh, hạt khô, khoai</li>
                    <li>Nếu bị <strong>chóng mặt hoặc mệt lử</strong>, hãy dừng lại và từ từ thở sâu</li>
                    <li>Nếu bị <strong>sốc lạnh hoặc sốc nhiệt</strong>, cần sơ cứu ngay</li>
                    </ul>

                    <h3>Thời Tiết & An Toàn</h3>
                    <ul>
                    <li>Fansipan thường <strong>mưa vào chiều tối</strong>, cần cẩn thận</li>
                    <li>Không nên leo vào <strong>mùa mưa lớn</strong> (Tháng 5-9)</li>
                    <li>Luôn đi <strong>cùng với hướng dẫn viên chuyên nghiệp</strong></li>
                    <li>Đội mũ bảo hiểm nếu đi qua những đoạn đá dốc</li>
                    </ul>

                    <h2>4. Những Thứ Sẽ Thấy Ở Đỉnh Fansipan</h2>
                    <p>Khi đạt đến <strong>đỉnh cao nhất Việt Nam</strong>, bạn sẽ:</p>
                    <ul>
                    <li>Nhìn thấy <strong>Sapa bé tí phía dưới</strong></li>
                    <li>Cảm nhận <strong>gió lạnh từ mây mù</strong></li>
                    <li>Có cơ hội chứng kiến <strong>mặt trời mọc từ trên mây</strong></li>
                    <li>Ghi lại <strong>kỷ niệm chinh phục đỉnh cao</strong> đáng nhớ</li>
                    </ul>

                    <h2>Kết Luận</h2>
                    <p>Chinh phục Fansipan không phải <strong>chỉ là một cuộc leo núi</strong>,
                    mà là <em>một hành trình khám phá bản thân</em>. Với sự chuẩn bị kỹ lưỡng
                    và tinh thần kiên cường, bạn chắc chắn sẽ thành công!</p>
                """.trimIndent(),
                category = "PHIÊU LƯU",
                categoryColor = "#11998E",
                thumbnail = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&q=80",
                thumbnailEmoji = "⛰️",
                readTime = 10,
                views = 1560,
                likes = 145,
                published = true,
                publishedAt = "2024-12-01"
            )
        )

        return blogPosts
    }

    companion object {
        fun getSeedData(): List<BlogPost> {
            return listOf(
                BlogPost(
                    userId = "",
                    authorName = "Thanh Hà",
                    authorAvatar = "https://i.pravatar.cc/150?img=45",
                    title = "10 bãi biển đẹp nhất Việt Nam năm 2024",
                    excerpt = "Khám phá những bãi biển hoang sơ, nước trong xanh và cát trắng mịn màng. Từ Phú Quốc đến Quy Nhơn...",
                    content = "Việt Nam sở hữu hơn 3000km bờ biển với vô số bãi biển tuyệt đẹp...",
                    category = "HƯỚNG DẪN",
                    categoryColor = "#4ECDC4",
                    thumbnail = "https://images.unsplash.com/photo-1559827260-dc66d52bef19?w=800&q=80",
                    thumbnailEmoji = "🏖️",
                    readTime = 5,
                    views = 1250,
                    likes = 89,
                    published = true,
                    publishedAt = "2024-12-15"
                ),
                BlogPost(
                    userId = "",
                    authorName = "Minh Ngọc",
                    authorAvatar = "https://i.pravatar.cc/150?img=32",
                    title = "Du lịch Đà Lạt chỉ với 2 triệu/người",
                    excerpt = "Bí quyết đi Đà Lạt tiết kiệm mà vẫn trọn vẹn. Ăn uống, nghỉ ngơi và tham quan với ngân sách hợp lý...",
                    content = "Đà Lạt không chỉ dành cho những ai có túi tiền dày...",
                    category = "TIẾT KIỆM",
                    categoryColor = "#FA709A",
                    thumbnail = "https://images.unsplash.com/photo-1583417319070-4a69db38a482?w=800&q=80",
                    thumbnailEmoji = "💰",
                    readTime = 7,
                    views = 2340,
                    likes = 156,
                    published = true,
                    publishedAt = "2024-12-10"
                ),
                BlogPost(
                    userId = "",
                    authorName = "Duy Khánh",
                    authorAvatar = "https://i.pravatar.cc/150?img=15",
                    title = "Góc chụp ảnh đẹp ở Hội An mà ít ai biết",
                    excerpt = "Tránh đám đông, tìm những góc chụp độc đáo tại phố cổ Hội An. Check-in sống ảo cùng ánh đèn lồng...",
                    content = "Hội An về đêm là một bức tranh tuyệt đẹp với ánh đèn lồng rực rỡ...",
                    category = "NHIẾP ẢNH",
                    categoryColor = "#667EEA",
                    thumbnail = "https://images.unsplash.com/photo-1555400038-63f5ba517a47?w=800&q=80",
                    thumbnailEmoji = "📸",
                    readTime = 6,
                    views = 1890,
                    likes = 203,
                    published = true,
                    publishedAt = "2024-12-08"
                ),
                BlogPost(
                    userId = "",
                    authorName = "Hương Ly",
                    authorAvatar = "https://i.pravatar.cc/150?img=28",
                    title = "15 món ăn vặt Sài Gòn bạn phải thử",
                    excerpt = "Food tour khắp Sài Gòn với những món ăn đường phố hấp dẫn. Từ bánh tráng trộn đến chè khúc bạch...",
                    content = "Sài Gòn không chỉ nổi tiếng với những tòa nhà cao tầng mà còn là thiên đường ẩm thực...",
                    category = "ẨM THỰC",
                    categoryColor = "#FF9800",
                    thumbnail = "https://images.unsplash.com/photo-1559339352-11d035aa65de?w=800&q=80",
                    thumbnailEmoji = "🍜",
                    readTime = 8,
                    views = 3120,
                    likes = 278,
                    published = true,
                    publishedAt = "2024-12-05"
                ),
                BlogPost(
                    userId = "",
                    authorName = "Quang Trung",
                    authorAvatar = "https://i.pravatar.cc/150?img=67",
                    title = "Chinh phục Fansipan - Nóc nhà Đông Dương",
                    excerpt = "Hành trình 2 ngày 1 đêm trekking lên đỉnh cao nhất Việt Nam. Chuẩn bị gì và lưu ý những điều quan trọng...",
                    content = "Fansipan cao 3143m so với mặt nước biển, là đỉnh núi cao nhất Việt Nam và Đông Dương...",
                    category = "PHIÊU LƯU",
                    categoryColor = "#11998E",
                    thumbnail = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&q=80",
                    thumbnailEmoji = "⛰️",
                    readTime = 10,
                    views = 1560,
                    likes = 145,
                    published = true,
                    publishedAt = "2024-12-01"
                )
            )
        }
    }
}
