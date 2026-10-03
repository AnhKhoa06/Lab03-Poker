# Lab 03 - Exercise 12: Poker Hands

## Yêu cầu
Mở rộng lớp `Hand` để hỗ trợ trò chơi poker: xác định loại bài poker của một tay bài và so sánh hai tay bài theo độ mạnh.

## Các file chính (package `e3.chapter3`)
- `Hand.java`: thêm `getPokerHandType()` và `createByPokerHandTypeComparator()`.
- `PokerHandType.java`: enum 9 loại bài, từ HIGH_CARD đến STRAIGHT_FLUSH.
- `PokerHandDemo.java`: chương trình kiểm thử.
- `Card.java`, `Rank.java`, `Suit.java`: các lớp có sẵn của sách.

## Quyết định thiết kế
- **Tay bài không đủ 5 lá:** chỉ phân loại khi đúng 5 lá (`assert size() == 5`), vì sảnh, thùng, full house được định nghĩa trên đúng 5 lá.
- **Phân loại:** đếm số lá theo từng hạng, kết hợp kiểm tra cùng chất (flush) và liền nhau (straight).
- **Đơn giản hóa:** sảnh chỉ tính Ace thấp (A-2-3-4-5), và so sánh chỉ theo loại bài, không dùng giá trị lá bài để phá hòa.

## Cách chạy
Chạy `PokerHandDemo` (Java 21). Kết quả mong đợi: 11 dòng `OK` và 3 dòng so sánh (`-4`, `4`, `0`).
