# Lab 03 - Exercise 12: Poker Hands

## Yêu cầu
Mở rộng lớp `Hand` để hỗ trợ trò chơi poker: xác định loại bài poker của một tay bài và so sánh hai tay bài theo độ mạnh.

## Các file chính (package `e3.chapter3`)
- `Hand.java`: thêm `getPokerHandType()`, `createByPokerHandTypeComparator()` (so theo loại bài) và `createByStrengthComparator()` (so đầy đủ: loại bài, rồi giá trị lá bài).
- `PokerHandType.java`: enum 9 loại bài, từ HIGH_CARD đến STRAIGHT_FLUSH.
- `PokerHandDemo.java`: chương trình kiểm thử.
- `Card.java`, `Rank.java`, `Suit.java`: các lớp có sẵn của sách.

## Quyết định thiết kế
- **Tay bài không đủ 5 lá:** chỉ phân loại khi đúng 5 lá (`@pre size() == 5`, kiểm tra bằng `assert`), vì sảnh, thùng, full house được định nghĩa trên đúng 5 lá. Một `Hand` vẫn được phép chưa đủ lá, chỉ phép phân loại mới yêu cầu đủ 5 lá. Lưu ý: `assert` chỉ có hiệu lực khi chạy với tùy chọn `-ea`.
- **Phân loại:** đếm số lá theo từng hạng, kết hợp kiểm tra cùng chất (flush) và liền nhau (straight).
- **Sảnh:** nhận cả sảnh Ace thấp (A-2-3-4-5, tính là sảnh 5 cao) và sảnh Ace cao (10-J-Q-K-A). Dãy vòng như Q-K-A-2-3 không phải sảnh.
- **So sánh đầy đủ (`createByStrengthComparator`):** so loại bài trước. Nếu cùng loại, so các giá trị theo độ quan trọng giảm dần: các hạng được xếp theo số lần xuất hiện, rồi theo giá trị (Ace là cao nhất). Ví dụ full house K-K-K-5-5 thắng 2-2-2-A-A, hai đôi thì so đôi lớn, đôi nhỏ rồi lá lẻ. Với sảnh chỉ so lá cao nhất. Hai tay chỉ khác chất thì bằng nhau.
- **So sánh chỉ theo loại bài (`createByPokerHandTypeComparator`):** giữ lại phiên bản đơn giản, hai tay cùng loại được coi là bằng nhau.

## Cách chạy
Chạy `PokerHandDemo` (Java 21). Kết quả mong đợi: 25 dòng `OK` và không có dòng `FAIL`, chia thành ba phần: các loại bài, so sánh theo loại, so sánh đầy đủ.