package com.example.cognaitive

data class MemoryCard(
    val id: Int,
    var isFlipped: Boolean = false,
    val imageResId: Int
)

object data {
    val MemoryCardList = mutableListOf(
        MemoryCard(1, imageResId = R.drawable.card_image1),
        MemoryCard(2, imageResId = R.drawable.card_image2),
        MemoryCard(3, imageResId = R.drawable.card_image3),
        MemoryCard(4, imageResId = R.drawable.card_image4),
        MemoryCard(5, imageResId = R.drawable.card_image5),
        MemoryCard(6, imageResId = R.drawable.card_image6),
        MemoryCard(7, imageResId = R.drawable.card_image7),
        MemoryCard(8, imageResId = R.drawable.card_image8),
        MemoryCard(9, imageResId = R.drawable.card_image9),
        MemoryCard(10, imageResId = R.drawable.card_image10),
        MemoryCard(11, imageResId = R.drawable.card_image11),
        MemoryCard(12, imageResId = R.drawable.card_image12),
        MemoryCard(13, imageResId = R.drawable.card_image13),
        MemoryCard(14, imageResId = R.drawable.card_image14),
        MemoryCard(15, imageResId = R.drawable.card_image15),
        MemoryCard(16, imageResId = R.drawable.card_image16),
        MemoryCard(17, imageResId = R.drawable.card_image17),
        MemoryCard(18, imageResId = R.drawable.card_image18),
    )
}