package com.example.testapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val id: Int,
    val userName: String,
    val avatarUrl: String,
    val content: String,
    val images: List<String>,
    var likes: Int,
    var comments: Int,
    var shares: Int,
    val date: String
) : Parcelable

val samplePosts = listOf(
    Post(
        id = 1,
        userName = "Dung Phuong Tran",
        avatarUrl = "https://i.pravatar.cc/150?img=5",
        content = "Lớp mình chính thức phá kỷ lục với việc họp lớp 3 năm liên tiếp 😂 và đã có lịch họp không chỉ cho năm sau mà còn tới 50 năm nữa luôn 🤣",
        images = listOf(
            "https://picsum.photos/id/1011/800/600",
            "https://picsum.photos/id/1012/800/600",
            "https://picsum.photos/id/1013/800/600",
            "https://picsum.photos/id/1014/800/600",
            "https://picsum.photos/id/1015/800/600",
            "https://picsum.photos/id/1016/800/600"
        ),
        likes = 61,
        comments = 3,
        shares = 2,
        date = "2 Th11, 2024 · 🌍"
    ),
    Post(
        id = 2,
        userName = "Nguyen Van A",
        avatarUrl = "https://i.pravatar.cc/150?img=12",
        content = "Hôm nay trời đẹp quá, cùng hội bạn ra phố cà phê chill 🍂☕",
        images = listOf(
            "https://picsum.photos/id/1021/800/600",
            "https://picsum.photos/id/1022/800/600",
            "https://picsum.photos/id/1023/800/600",
            "https://picsum.photos/id/1024/800/600",
            "https://picsum.photos/id/1025/800/600"
        ),
        likes = 145,
        comments = 12,
        shares = 4,
        date = "1 Th11, 2024 · 📱"
    ),
    Post(
        id = 3,
        userName = "Tran Thi B",
        avatarUrl = "https://i.pravatar.cc/150?img=8",
        content = "Chúc mọi người cuối tuần vui vẻ 💐",
        images = listOf(
            "https://picsum.photos/id/1035/800/600",
            "https://picsum.photos/id/1036/800/600",
            "https://picsum.photos/id/1037/800/600",
            "https://picsum.photos/id/1038/800/600",
            "https://picsum.photos/id/1039/800/600"
        ),
        likes = 78,
        comments = 6,
        shares = 1,
        date = "31 Th10, 2024 · 🌏"
    ),
    Post(
        id = 4,
        userName = "Pham Anh Tuan",
        avatarUrl = "https://i.pravatar.cc/150?img=15",
        content = "Một ngày bận rộn nhưng thật ý nghĩa. Team tuyệt vời 💪🔥",
        images = listOf(
            "https://picsum.photos/id/1040/800/600",
            "https://picsum.photos/id/1041/800/600",
            "https://picsum.photos/id/1042/800/600",
            "https://picsum.photos/id/1043/800/600",
            "https://picsum.photos/id/1044/800/600"
        ),
        likes = 230,
        comments = 25,
        shares = 9,
        date = "29 Th10, 2024 · 💻"
    ),
    Post(
        id = 5,
        userName = "Le Thu Ha",
        avatarUrl = "https://i.pravatar.cc/150?img=20",
        content = "Cùng nhau đi biển Nha Trang 🌊☀️ thật tuyệt vời!",
        images = listOf(
            "https://picsum.photos/id/1050/800/600",
            "https://picsum.photos/id/1051/800/600",
            "https://picsum.photos/id/1052/800/600",
            "https://picsum.photos/id/1053/800/600",
            "https://picsum.photos/id/1054/800/600",
            "https://picsum.photos/id/1055/800/600"
        ),
        likes = 312,
        comments = 41,
        shares = 15,
        date = "27 Th10, 2024 · 🏖"
    ),
    Post(
        id = 6,
        userName = "Nguyen Kim Cuong",
        avatarUrl = "https://i.pravatar.cc/150?img=23",
        content = "Một buổi chiều thảnh thơi cùng quyển sách yêu thích 📚☕",
        images = listOf(
            "https://picsum.photos/id/1060/800/600",
            "https://picsum.photos/id/1061/800/600",
            "https://picsum.photos/id/1062/800/600",
            "https://picsum.photos/id/1063/800/600",
            "https://picsum.photos/id/1064/800/600"
        ),
        likes = 89,
        comments = 7,
        shares = 2,
        date = "25 Th10, 2024 · 📖"
    )
)

val samplePosts1 = listOf(
    Post(
        id = 7,
        userName = "Hoang Minh Khoa",
        avatarUrl = "https://i.pravatar.cc/150?img=30",
        content = "Cuối tuần cùng nhóm bạn leo núi Dinh, mệt nhưng vui hết sảy 🏞️💪",
        images = listOf(
            "https://images.unsplash.com/photo-1501785888041-af3ef285b470?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1500534314209-a25ddb2bd429?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1469474968028-56623f02e42e?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1470770903676-69b98201ea1c?auto=format&fit=crop&w=800&h=600&q=80"
        ),
        likes = 198,
        comments = 21,
        shares = 4,
        date = "23 Th10, 2024 · ⛰️"
    ),
    Post(
        id = 8,
        userName = "Pham Hong Nhung",
        avatarUrl = "https://i.pravatar.cc/150?img=31",
        content = "Ngày nắng đẹp 🌸 Cùng mẹ đi chợ hoa, mua đầy một giỏ toàn hương sắc 💐",
        images = listOf(
            "https://images.unsplash.com/photo-1492684223066-81342ee5ff30?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1501004318641-b39e6451bec6?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1524594227084-dfba8b7e8e2b?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1509042239860-f550ce710b93?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1497032205916-ac775f0649ae?auto=format&fit=crop&w=800&h=600&q=80"
        ),
        likes = 256,
        comments = 18,
        shares = 6,
        date = "21 Th10, 2024 · 🌺"
    ),
    Post(
        id = 9,
        userName = "Nguyen Duc Thang",
        avatarUrl = "https://i.pravatar.cc/150?img=32",
        content = "Một buổi cà phê chill ở quán mới mở trong hẻm, không gian cực nghệ 🎨☕",
        images = listOf(
            "https://images.unsplash.com/photo-1511920170033-f8396924c348?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1525610553991-2bede1a236e2?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1521305916504-4a1121188589?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1515377905703-c4788e51af15?auto=format&fit=crop&w=800&h=600&q=80"
        ),
        likes = 132,
        comments = 10,
        shares = 1,
        date = "19 Th10, 2024 · ☕"
    ),
    Post(
        id = 10,
        userName = "Tran Quynh Anh",
        avatarUrl = "https://i.pravatar.cc/150?img=33",
        content = "Đêm nhạc acoustic thật tuyệt, ai cũng hát hết mình 🎤💫",
        images = listOf(
            "https://images.unsplash.com/photo-1507874457470-272b3c8d8ee2?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1506157786151-b8491531f063?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1511376777868-611b54f68947?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1508214751196-bcfd4ca60f91?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1497032205916-ac775f0649ae?auto=format&fit=crop&w=800&h=600&q=80"
        ),
        likes = 401,
        comments = 39,
        shares = 9,
        date = "17 Th10, 2024 · 🎶"
    ),
    Post(
        id = 11,
        userName = "Le Tuan Kiet",
        avatarUrl = "https://i.pravatar.cc/150?img=34",
        content = "Vừa hoàn thành chặng đua 10km đầu tiên! Cảm giác thật đã 🏃‍♂️🔥",
        images = listOf(
            "https://images.unsplash.com/photo-1521412644187-c49fa049e84d?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1508609349937-5ec4ae374ebf?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1551817958-20204e8a3c54?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1520975698519-59c38f37e1b7?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1546484959-fb9876bfae8f?auto=format&fit=crop&w=800&h=600&q=80"
        ),
        likes = 512,
        comments = 47,
        shares = 12,
        date = "15 Th10, 2024 · 🏅"
    )
)

val samplePosts2 = listOf(
    Post(
        id = 12,
        userName = "Vo Thi Lan",
        avatarUrl = "https://i.pravatar.cc/150?img=40",
        content = "Check-in cùng team tại Hội An cổ kính 🏮🌸",
        images = listOf(
            "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1526483360412-f4dbaf036963?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1505761671935-60b3a7427bad?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1549887534-5fdae89e2b37?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1526481280691-8f5e2dd53c91?auto=format&fit=crop&w=800&h=600&q=80"
        ),
        likes = 378,
        comments = 25,
        shares = 7,
        date = "12 Th10, 2024 · 🏮"
    ),
    Post(
        id = 13,
        userName = "Do Tien Dung",
        avatarUrl = "https://i.pravatar.cc/150?img=41",
        content = "Trải nghiệm làm bánh handmade lần đầu tiên 🍰",
        images = listOf(
            "https://images.unsplash.com/photo-1499636136210-6f4ee915583e?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1499028344343-cd173ffc68a9?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1513104890138-7c749659a591?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1605478508293-6e5f9b9a7c5b?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1556910103-1c02745aae4d?auto=format&fit=crop&w=800&h=600&q=80"
        ),
        likes = 256,
        comments = 19,
        shares = 5,
        date = "9 Th10, 2024 · 🍰"
    ),
    Post(
        id = 14,
        userName = "Bui Bao Ngoc",
        avatarUrl = "https://i.pravatar.cc/150?img=42",
        content = "Lần đầu thử chụp ảnh bằng film camera 📸 cảm giác rất khác!",
        images = listOf(
            "https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1519183071298-a2962eadcdb2?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1495555687392-3f50d6e721cb?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1519681393784-d120267933ba?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?auto=format&fit=crop&w=800&h=600&q=80"
        ),
        likes = 194,
        comments = 13,
        shares = 3,
        date = "7 Th10, 2024 · 📷"
    ),
    Post(
        id = 15,
        userName = "Nguyen Thanh Nam",
        avatarUrl = "https://i.pravatar.cc/150?img=43",
        content = "Một ngày ở Đà Lạt cùng mây và nắng ☁️🌿",
        images = listOf(
            "https://images.unsplash.com/photo-1519681393784-d120267933ba?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1516637090014-cb1ab0d08fc7?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1549887534-5fdae89e2b37?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1500534314209-a25ddb2bd429?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1501785888041-af3ef285b470?auto=format&fit=crop&w=800&h=600&q=80"
        ),
        likes = 412,
        comments = 38,
        shares = 14,
        date = "5 Th10, 2024 · 🌄"
    ),
    Post(
        id = 16,
        userName = "Tran Gia Han",
        avatarUrl = "https://i.pravatar.cc/150?img=44",
        content = "Góc làm việc mới, tối giản và đầy cảm hứng ✨💻",
        images = listOf(
            "https://images.unsplash.com/photo-1505691938895-1758d7feb511?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1505691723518-36a5ac3be353?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1505692794403-38c06ebf5d0d?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1557683316-973673baf926?auto=format&fit=crop&w=800&h=600&q=80",
            "https://images.unsplash.com/photo-1542744095-fcf48d80b0fd?auto=format&fit=crop&w=800&h=600&q=80"
        ),
        likes = 289,
        comments = 20,
        shares = 4,
        date = "2 Th10, 2024 · 🏠"
    )
)






