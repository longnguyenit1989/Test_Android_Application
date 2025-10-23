package com.example.testapplication.model

import java.io.Serializable

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
) : Serializable


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
            "https://picsum.photos/id/1022/800/600"
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
            "https://picsum.photos/id/1035/800/600"
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
            "https://picsum.photos/id/1042/800/600"
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
            "https://picsum.photos/id/1054/800/600"
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
            "https://picsum.photos/id/1061/800/600"
        ),
        likes = 89,
        comments = 7,
        shares = 2,
        date = "25 Th10, 2024 · 📖"
    )
)

