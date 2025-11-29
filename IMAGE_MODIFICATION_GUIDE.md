# Hướng Dẫn Thay Đổi Hình Ảnh Trong Project

## Tổng Quan

Project này sử dụng **ImageLoader** (`nhom16oop.utils.ImageLoader`) để quản lý tất cả hình ảnh. Class này có khả năng **tự động scale (resize)** hình ảnh về đúng kích thước cần thiết, do đó:

**✅ BẠN KHÔNG CẦN PHẢI LO LẮNG VỀ KÍCH THƯỚC HÌNH ẢNH GỐC**

Hệ thống sẽ tự động căn chỉnh về đúng kích cỡ bằng phương thức `Image.SCALE_SMOOTH` để đảm bảo chất lượng tốt nhất.

---

## Cơ Chế Hoạt Động

### 1. ImageLoader - Class Quản Lý Hình Ảnh

**File:** `src/main/java/nhom16oop/utils/ImageLoader.java`

```java
public static Image getImage(String path, int width, int height) {
    return imageCache.computeIfAbsent(path, k -> {
        Image img = ImageIO.read(imageUrl);
        return img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    });
}
```

**Đặc điểm:**
- Tự động scale hình ảnh về kích thước `width x height` được chỉ định
- Sử dụng cache để tối ưu hiệu suất
- Hỗ trợ cả PNG và SVG

### 2. Tự Động Chia Tile Cho Bàn Cờ

**File:** `src/main/java/nhom16oop/game/ChessBoard.java`

Hình nền bàn cờ được load như sau:
```java
this.boardImage = ImageLoader.getImage("images/chessboard.png", 
    GameConstants.Board.BOARD_WIDTH,    // 800 pixels
    GameConstants.Board.BOARD_HEIGHT);  // 800 pixels
```

**Kích thước bàn cờ:** 800x800 pixels (8 tiles x 100 pixels mỗi tile)

Khi vẽ lên màn hình:
```java
g.drawImage(boardImage, 0, 0, getWidth(), getHeight(), null);
```

➡️ **Kết luận:** Dù hình ảnh gốc của bạn to hay nhỏ, hệ thống sẽ tự động scale về 800x800 pixels và chia đều cho 64 ô (8x8).

---

## Các Loại Hình Ảnh Có Thể Thay Đổi

### 1. Nền Bàn Cờ (Chessboard Background)

**File cần thay thế:** `src/main/resources/images/chessboard.png`

**Kích thước mục tiêu:** 800x800 pixels (tự động scale)

**Được sử dụng trong:**
- `ChessBoard.java` - Vẽ nền cho toàn bộ bàn cờ

**Lưu ý:**
- Có thể dùng hình bất kỳ kích thước nào, hệ thống sẽ tự scale
- Nên dùng hình vuông để tránh méo
- Hỗ trợ PNG với alpha channel (trong suốt)

---

### 2. Texture Gỗ (Wood Texture)

**File cần thay thế:** `src/main/resources/images/wood_texture.png`

**Kích thước mục tiêu:** Tự động scale theo kích thước dialog

**Được sử dụng trong:**
- `GameModeSelectionDialog.java` - Nền cho dialog chọn chế độ chơi

**Code:**
```java
Image woodTexture = ImageLoader.getImage("images/wood_texture.png", FRAME_WIDTH, FRAME_HEIGHT);
g2d.drawImage(woodTexture, 0, 0, getWidth(), getHeight(), this);
```

---

### 3. Hình Quân Cờ (Chess Pieces)

**Thư mục:** `src/main/resources/images/pieces/`

**Các file:**
- `white_pawn.png`, `black_pawn.png`
- `white_rook.png`, `black_rook.png`
- `white_knight.png`, `black_knight.png`
- `white_bishop.png`, `black_bishop.png`
- `white_queen.png`, `black_queen.png`
- `white_king.png`, `black_king.png`

**Kích thước mục tiêu:** 95x95 pixels (tự động scale)

**Được sử dụng trong:**
- `ChessPiece.java` - Load hình cho từng quân cờ
- `ChessTile.java` - Vẽ quân cờ lên ô (constant `PIECE_SIZE = 95`)
- `PromotionDialog.java` - Hiển thị quân khi phong cấp
- `PlayerPanel.java` - Hiển thị quân bị bắt (scale về 20x20 pixels)

**Code xử lý scale:**
```java
// Trong ChessTile.java - vẽ quân cờ trên bàn
g.drawImage(image, offset, offset, PIECE_SIZE, PIECE_SIZE, null); // 95x95

// Trong PlayerPanel.java - hiển thị quân bị bắt
Image scaledImage = pieceImage.getScaledInstance(CAPTURED_PIECE_SIZE, CAPTURED_PIECE_SIZE, Image.SCALE_SMOOTH); // 20x20
```

---

### 4. Icon Toolbar & UI (Các nút chức năng)

**Thư mục:** `src/main/resources/images/`

**Các file:**
- `hint.png` - Nút gợi ý nước đi
- `resign.png` - Nút đầu hàng
- `flip-board.png` - Nút lật bàn cờ
- `back.png` - Nút undo (quay lại)
- `forward.png` - Nút redo (tiến tới)
- `left-arrow.png` - Mũi tên trái (history)
- `right-arrow.png` - Mũi tên phải (history)
- `handshake.png` - Nút hoà
- `player.png` - Icon người chơi
- `bot.png` - Icon bot/AI
- `stockfish.png` - Icon Stockfish AI

**Kích thước:** Thường là nhỏ (khoảng 24x24 hoặc 32x32 pixels)

**Được sử dụng trong:**
- `ChessToolbar.java` - Các nút điều khiển
- `MoveHistoryPanel.java` - Nút điều hướng lịch sử
- `PlayerPanel.java` - Icon người chơi/AI

**Lưu ý:**
- Các icon này được scale tự động theo kích thước nút
- Nên dùng PNG với nền trong suốt để đẹp hơn

---

## Cách Thay Đổi Hình Ảnh

### Bước 1: Chuẩn Bị Hình Ảnh Mới

- Có thể dùng **BẤT KỲ KÍCH THƯỚC NÀO** (hệ thống sẽ tự scale)
- Khuyến nghị: Dùng kích thước gần với kích thước mục tiêu để đảm bảo chất lượng
- Format khuyến nghị: PNG (hỗ trợ trong suốt)

### Bước 2: Thay Thế File

1. Mở thư mục `src/main/resources/images/` (hoặc `src/main/resources/images/pieces/`)
2. Xóa file cũ hoặc backup nó
3. Paste file mới vào với **ĐÚNG TÊN FILE** như file cũ

**VÍ DỤ:**
```
Thay đổi nền bàn cờ:
src/main/resources/images/chessboard.png  (thay file này)

Thay đổi quân vua trắng:
src/main/resources/images/pieces/white_king.png  (thay file này)
```

### Bước 3: Rebuild Project (Nếu cần)

Nếu đang chạy trong IntelliJ:
1. `Build > Rebuild Project`
2. Hoặc nhấn `Ctrl + Shift + F9`
3. Chạy lại game

**Lưu ý:** Maven sẽ tự động copy file từ `src/main/resources/` sang `target/classes/` khi build.

---

## Các File Code Liên Quan

Nếu bạn muốn thay đổi **KÍCH THƯỚC** (không phải hình ảnh), hãy sửa các file sau:

### 1. Kích Thước Bàn Cờ

**File:** `src/main/java/nhom16oop/constants/GameConstants.java`

```java
public static final class Board {
    public static final int BOARD_SIZE = 8;           // Số ô (8x8)
    public static final int SQUARE_SIZE = 100;        // Kích thước 1 ô (pixels)
    public static final int BOARD_WIDTH = BOARD_SIZE * SQUARE_SIZE;   // 800 pixels
    public static final int BOARD_HEIGHT = BOARD_SIZE * SQUARE_SIZE;  // 800 pixels
}
```

**Để thay đổi kích thước bàn cờ:** Sửa `SQUARE_SIZE`
- Ví dụ: `SQUARE_SIZE = 120` → Bàn cờ sẽ là 960x960 pixels

---

### 2. Kích Thước Quân Cờ Trên Bàn

**File:** `src/main/java/nhom16oop/ui/board/ChessTile.java`

```java
private static final int PIECE_SIZE = 95;  // Kích thước quân cờ trên bàn
```

**Để thay đổi kích thước quân cờ:** Sửa `PIECE_SIZE`
- Ví dụ: `PIECE_SIZE = 85` → Quân cờ nhỏ hơn, có khoảng cách với viền ô

---

### 3. Kích Thước Quân Cờ Bị Bắt

**File:** `src/main/java/nhom16oop/ui/components/panels/PlayerPanel.java`

```java
private final static int CAPTURED_PIECE_SIZE = 20;  // Kích thước quân bị bắt
```

---

### 4. Preload Hình Ảnh (Cache)

**File:** `src/main/java/nhom16oop/utils/ImageLoader.java`

```java
public static void preloadImages() {
    String[] chessPieceName = {
        "white_pawn.png", "black_pawn.png",
        "white_rook.png", "black_rook.png",
        // ... các quân khác
    };
    for (String name : chessPieceName) {
        getImage("images/pieces/" + name, 95, 95);
    }
    getImage("images/chessboard.png", GameConstants.Board.BOARD_WIDTH, GameConstants.Board.BOARD_HEIGHT);
}
```

➡️ **Lưu ý:** Nếu bạn thêm/xóa file hình ảnh quân cờ, hãy cập nhật mảng `chessPieceName` ở đây.

---

## Câu Hỏi Thường Gặp (FAQ)

### Q1: Tôi có file hình 2000x2000, có dùng được không?
**A:** Có! Hệ thống sẽ tự động scale về kích thước cần thiết. Tuy nhiên, file lớn sẽ tốn thời gian load ban đầu. Khuyến nghị tối ưu hình ảnh trước.

### Q2: Hình ảnh không vuông (chữ nhật) thì sao?
**A:** Hệ thống sẽ scale theo tỷ lệ `width x height` được chỉ định, có thể bị méo nếu tỷ lệ không khớp. Khuyến nghị dùng hình vuông cho bàn cờ và quân cờ.

### Q3: Tôi muốn dùng hình ảnh có nền trong suốt?
**A:** Hoàn toàn được! Dùng PNG với alpha channel. Hệ thống hỗ trợ đầy đủ.

### Q4: Sau khi thay file, game vẫn hiển thị hình cũ?
**A:** Rebuild project:
```
Build > Rebuild Project (Ctrl + Shift + F9)
```
Hoặc xóa thư mục `target/` và build lại.

### Q5: Tôi có thể thêm hình ảnh mới (không thay thế)?
**A:** Có! Thêm file vào `src/main/resources/images/`, sau đó load bằng:
```java
Image myImage = ImageLoader.getImage("images/your_image.png", width, height);
```

### Q6: Tôi muốn thay đổi màu sắc ô cờ thay vì dùng hình nền?
**A:** Xem file `ChessTile.java` - phương thức `paintComponent()`. Có thể comment phần vẽ hình nền và dùng `g.setColor()` + `g.fillRect()` để vẽ màu.

---

## Tóm Tắt

| Loại Hình | Vị Trí File | Kích Thước Mục Tiêu | Tự Động Scale? |
|-----------|-------------|---------------------|----------------|
| Nền bàn cờ | `images/chessboard.png` | 800x800 px | ✅ Có |
| Texture gỗ | `images/wood_texture.png` | Theo dialog | ✅ Có |
| Quân cờ trên bàn | `images/pieces/*.png` | 95x95 px | ✅ Có |
| Quân cờ bị bắt | `images/pieces/*.png` | 20x20 px | ✅ Có |
| Icon toolbar | `images/*.png` | ~32x32 px | ✅ Có |

**✅ KẾT LUẬN:** Bạn có thể thay thế hình ảnh bất kỳ kích thước nào, hệ thống sẽ tự động xử lý scale và chia tile. Không cần configure thêm gì!

---

**Lưu ý cuối cùng:** Luôn backup hình ảnh gốc trước khi thay thế, để dễ dàng quay lại nếu cần!

---

# Hướng Dẫn Thay Đổi Màu Sắc & Giao Diện UI

## Tổng Quan UI Components

Project này có nhiều thành phần UI có thể custom về màu sắc, font chữ, border, và hiệu ứng. Dưới đây là danh sách đầy đủ các file và các thuộc tính có thể thay đổi.

---

## 1. Bàn Cờ (Chess Tile)

**File:** `src/main/java/nhom16oop/ui/board/ChessTile.java`

### Các Màu Có Thể Thay Đổi:

#### a) Highlight Ô Được Chọn (Selected Tile)
```java
private void drawSelectionHighlight(Graphics2D g2d) {
    if (isLeftClickSelected) {
        g2d.setColor(new Color(56, 72, 79, 160));  // Xám xanh trong suốt
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}
```
**Thay đổi:** Sửa `new Color(56, 72, 79, 160)` → `new Color(R, G, B, Alpha)`
- R, G, B: 0-255 (màu)
- Alpha: 0-255 (độ trong suốt, 0 = hoàn toàn trong suốt)

#### b) Highlight Nước Đi Vừa Thực Hiện (Last Move)
```java
private void drawLastMoveIndicator(Graphics2D g2d) {
    if (isInLastMove) {
        // Nền tối
        g2d.setColor(new Color(0, 0, 0, 50));  // Đen nhạt
        g2d.fillRect(0, 0, tileSize, tileSize);
        
        // Viền màu theo lượt chơi
        g2d.setColor(chessController.getBoardManager().getCurrentBoardState()
            .getCurrentPlayerColor().isBlack() 
            ? new Color(0, 211, 255)      // Xanh dương nếu lượt đen
            : new Color(255, 24, 62));    // Đỏ nếu lượt trắng
        g2d.setStroke(new BasicStroke(4));  // Độ dày viền
        g2d.drawRect(0, 0, tileSize, tileSize);
    }
}
```
**Thay đổi:**
- Màu nền: `new Color(0, 0, 0, 50)` 
- Màu viền lượt đen: `new Color(0, 211, 255)` (xanh dương)
- Màu viền lượt trắng: `new Color(255, 24, 62)` (đỏ)
- Độ dày viền: `new BasicStroke(4)` → số khác

#### c) Indicator Nước Đi Hợp Lệ (Valid Move)
```java
private void drawValidMoveIndicator(Graphics2D g2d) {
    if (piece != null) {
        // Nếu ô có quân cờ (có thể bắt) - vẽ vòng tròn đỏ
        g2d.setColor(new Color(222, 47, 31, 150));  // Đỏ trong suốt
        g2d.setStroke(new BasicStroke(4));
        int circleSize = 90;
        g2d.drawOval(...);
    } else {
        // Nếu ô trống - vẽ chấm tròn trắng
        g2d.setColor(new Color(255, 255, 255, 100));  // Trắng trong suốt
        int circleSize = 30;  // Kích thước chấm tròn
        g2d.fillOval(...);
    }
}
```
**Thay đổi:**
- Màu vòng tròn (bắt quân): `new Color(222, 47, 31, 150)` (đỏ)
- Kích thước vòng tròn: `circleSize = 90`
- Màu chấm tròn (ô trống): `new Color(255, 255, 255, 100)` (trắng)
- Kích thước chấm: `CIRCLE_SIZE = 30` (constant ở đầu file)

#### d) Highlight Gợi Ý (Hint)
```java
private void drawHintHighlight(Graphics2D g2d) {
    if (isHintHighlightedSquare) {
        ChessPiece piece = getPiece();
        g2d.setColor(new Color(0, 196, 255, 
            piece != null && piece.getColor() != chessController.getBoardManager().getCurrentPlayerColor() 
            ? 100   // Alpha khi có quân địch
            : 50)); // Alpha khi ô trống
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}
```
**Thay đổi:**
- Màu: `new Color(0, 196, 255, ...)` (xanh dương)
- Độ trong suốt: 100 (có quân địch) hoặc 50 (ô trống)

---

## 2. Panel Người Chơi (Player Panel)

**File:** `src/main/java/nhom16oop/ui/components/panels/PlayerPanel.java`

### Các Thuộc Tính Có Thể Thay Đổi:

#### a) Kích Thước Panel
```java
private final static int FRAME_WIDTH = 250;      // Chiều rộng
private final static int FRAME_HEIGHT = 800;     // Chiều cao
private final static int AVATAR_WIDTH = 120;     // Kích thước avatar
private final static int AVATAR_HEIGHT = 120;
private final static int CAPTURED_PIECE_SIZE = 20; // Kích thước quân bị bắt
```

#### b) Background Gradient (Nền Gradient)
```java
@Override
protected void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g.create();
    
    // Gradient từ màu nâu sáng đến nâu đậm
    GradientPaint gradient = new GradientPaint(
        0, 0, new Color(89, 45, 13),           // Màu đầu (góc trên-trái)
        getWidth(), getHeight(), 
        new Color(138, 66, 17)                 // Màu cuối (góc dưới-phải)
    );
    g2d.setPaint(gradient);
    g2d.fillRect(0, 0, getWidth(), getHeight());
    
    // Viền vàng khi đến lượt chơi
    if (isActiveTurn) {
        g2d.setColor(new Color(255, 255, 0, 100));  // Vàng trong suốt
        g2d.setStroke(new BasicStroke(5));           // Độ dày viền
        g2d.drawRect(2, 2, getWidth() - 4, getHeight() - 4);
    }
}
```
**Thay đổi:**
- Màu gradient bắt đầu: `new Color(89, 45, 13)` (nâu sáng)
- Màu gradient kết thúc: `new Color(138, 66, 17)` (nâu đậm)
- Màu viền lượt chơi: `new Color(255, 255, 0, 100)` (vàng)
- Độ dày viền: `new BasicStroke(5)`

#### c) Font Chữ & Màu
```java
// Tên người chơi
nameLabel.setFont(new Font("Georgia", Font.BOLD, 22));
nameLabel.setForeground(Color.WHITE);

// Timer
timerLabel.setFont(new Font("Monospaced", Font.BOLD, 32));
timerLabel.setForeground(Color.WHITE);

// Timer khi sắp hết thời gian
if (timeLow) {
    timerLabel.setForeground(new Color(255, 50, 50)); // Đỏ
} else {
    timerLabel.setForeground(Color.WHITE);
}

// Điểm số
scoreLabel.setFont(new Font("Georgia", Font.PLAIN, 13));
scoreLabel.setForeground(Color.WHITE);
```

#### d) Border Timer
```java
timerLabel.setBorder(BorderFactory.createCompoundBorder(
    BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2, true),
    BorderFactory.createEmptyBorder(10, 20, 10, 20)
));
```
**Thay đổi:**
- Màu viền: `new Color(255, 255, 255, 100)` (trắng trong suốt)
- Độ dày viền: `2`
- Padding: `(10, 20, 10, 20)` = (top, left, bottom, right)

---

## 3. Toolbar (Thanh Công Cụ)

**File:** `src/main/java/nhom16oop/ui/components/panels/ChessToolbar.java`

### Các Thuộc Tính:

```java
private static final int ICON_SIZE = 24;                      // Kích thước icon
private static final int TOOLBAR_HEIGHT = 50;                 // Chiều cao toolbar
private static final Color BACKGROUND_COLOR = new Color(40, 40, 40); // Màu nền
private static final int BORDER_SIZE = 5;                     // Khoảng cách viền
```

**Các nút có thể ẩn/hiện:**
- Back (Quay lại launcher)
- Flip Board (Lật bàn cờ)
- Resign (Đầu hàng)
- Show Hint (Gợi ý - chỉ hiện ở Player vs AI)
- Move Back (Undo)
- Move Forward (Redo)

**Cách ẩn nút:** Comment dòng `buttonConfigs.add(...)` tương ứng trong method `initializeButtonConfigs()`

---

## 4. Dialog Windows

### a) Game Mode Selection Dialog

**File:** `src/main/java/nhom16oop/ui/components/dialogs/GameModeSelectionDialog.java`

#### Kích Thước & Màu
```java
private static final int FRAME_WIDTH = 400;
private static final int FRAME_HEIGHT = 600;

// Background
getContentPane().setBackground(new Color(30, 30, 30)); // Nền tối

// Texture nền (nếu có)
Image woodTexture = ImageLoader.getImage("images/wood_texture.png", 
                                         FRAME_WIDTH, FRAME_HEIGHT);

// Viền bo tròn
g2d.setColor(new Color(80, 40, 20));
g2d.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, 
                                     getHeight() - 1, 20, 20));
```

#### Font & Button
```java
// Tiêu đề
messageLabel.setFont(new Font("Arial", Font.BOLD, 20));
messageLabel.setForeground(Color.WHITE);

// Button
button.setFont(new Font("Arial", Font.PLAIN, 16));

// Màu button (trong paintComponent)
g2d.setColor(getModel().isPressed() 
    ? new Color(92, 51, 23)       // Khi nhấn
    : getModel().isRollover() 
        ? new Color(160, 82, 45)  // Khi hover
        : new Color(139, 69, 19)); // Mặc định
```

**Kích thước button:**
```java
return new Dimension(200, 50);  // width x height
```

---

### b) Resign Dialog

**File:** `src/main/java/nhom16oop/ui/components/dialogs/ResignDialog.java`

#### Background Gradient
```java
GradientPaint gradient = new GradientPaint(
    0, 0, new Color(139, 69, 19),      // Nâu sáng
    getWidth(), getHeight(), 
    new Color(92, 51, 23)              // Nâu đậm
);
```

#### Viền
```java
g2d.setColor(new Color(80, 40, 20));   // Màu viền
g2d.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, 
                                     getHeight() - 1, 20, 20));
```

#### Button
```java
// Font
button.setFont(new Font("Georgia", Font.PLAIN, 14));
button.setForeground(Color.WHITE);

// Kích thước
return new Dimension(120, 40);

// Màu nền button
g2d.setColor(getModel().isPressed() 
    ? new Color(92, 51, 23)       // Nhấn
    : getModel().isRollover() 
        ? new Color(160, 82, 45)  // Hover
        : new Color(139, 69, 19)); // Mặc định
```

---

### c) Game Over Dialog

**File:** `src/main/java/nhom16oop/ui/components/dialogs/GameOverDialog.java`

Tương tự ResignDialog, có cùng style và màu sắc.

---

### d) Promotion Dialog

**File:** `src/main/java/nhom16oop/ui/components/dialogs/PromotionDialog.java`

Dialog chọn quân phong cấp (Queen, Rook, Bishop, Knight).

```java
// Font
messageLabel.setFont(new Font("Arial", Font.BOLD, 18));

// Button border
button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

// Icon size
ImageLoader.getImage(iconPath, 95, 95);  // Kích thước icon quân cờ
```

---

## 5. Move History Panel (Panel Lịch Sử Nước Đi)

**File:** `src/main/java/nhom16oop/ui/components/panels/MoveHistoryPanel.java`

### Các Thuộc Tính:

```java
private static final int BUTTON_WIDTH = 40;
private static final int BUTTON_HEIGHT = 40;
private static final int PANEL_HEIGHT = 75;
private static final int VIEWPORT_WIDTH = 740;

// Màu nền
setBackground(new Color(139, 69, 19));  // Nâu

// Font & màu chữ
moveNumberLabel.setFont(new Font("Roboto", Font.BOLD, 16));
moveNumberLabel.setForeground(new Color(245, 245, 220)); // Vàng kem

whiteMoveLabel.setFont(new Font("Roboto", Font.PLAIN, 16));
whiteMoveLabel.setForeground(new Color(245, 245, 220));

// Nút điều hướng (mũi tên trái/phải)
leftArrow.setBackground(new Color(92, 51, 23));  // Nâu đậm
rightArrow.setBackground(new Color(92, 51, 23));
```

---

## 6. Main Chess UI

**File:** `src/main/java/nhom16oop/ui/ChessUI.java`

### Background & Layout:

```java
// Main panel background
mainPanel.setBackground(new Color(30, 30, 30));  // Xám đen

// Puzzle mode info panel
panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

// Fonts
titleLabel.setFont(new Font("Georgia", Font.BOLD, 22));
movesLabel.setFont(new Font("Roboto", Font.BOLD, 18));
objectiveLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
```

---

## Bảng Tóm Tắt Màu Sắc Chính

| Thành Phần | Màu Chính | RGB | Mục Đích |
|------------|-----------|-----|----------|
| Ô được chọn | Xám xanh | (56, 72, 79, 160) | Highlight tile |
| Nước đi vừa thực hiện (đen) | Xanh dương | (0, 211, 255) | Viền last move |
| Nước đi vừa thực hiện (trắng) | Đỏ | (255, 24, 62) | Viền last move |
| Nước đi hợp lệ (bắt quân) | Đỏ | (222, 47, 31, 150) | Vòng tròn |
| Nước đi hợp lệ (ô trống) | Trắng | (255, 255, 255, 100) | Chấm tròn |
| Hint highlight | Xanh dương | (0, 196, 255) | Gợi ý |
| Player panel gradient 1 | Nâu sáng | (89, 45, 13) | Nền |
| Player panel gradient 2 | Nâu đậm | (138, 66, 17) | Nền |
| Active turn border | Vàng | (255, 255, 0, 100) | Viền lượt chơi |
| Timer warning | Đỏ | (255, 50, 50) | Cảnh báo hết giờ |
| Toolbar background | Xám đen | (40, 40, 40) | Nền toolbar |
| Dialog background | Xám đen | (30, 30, 30) | Nền dialog |
| Button default | Nâu | (139, 69, 19) | Nút bấm |
| Button hover | Nâu sáng | (160, 82, 45) | Nút hover |
| Button pressed | Nâu đậm | (92, 51, 23) | Nút nhấn |
| Move history background | Nâu | (139, 69, 19) | Nền lịch sử |
| Move text | Vàng kem | (245, 245, 220) | Chữ nước đi |

---

## Hướng Dẫn Thay Đổi Màu Sắc Toàn Bộ Theme

### Ví Dụ: Thay Đổi Theme Từ Nâu Sang Xanh Lá

1. **Player Panel:** Thay gradient từ nâu (89, 45, 13) → xanh lá (34, 139, 34)
2. **Buttons:** Thay màu button từ (139, 69, 19) → (60, 179, 113)
3. **Move History:** Thay background từ (139, 69, 19) → (46, 125, 50)
4. **Dialog:** Giữ nguyên nền tối (30, 30, 30) hoặc đổi sang xanh đen (25, 42, 40)

### Các Bước:

1. Sử dụng tính năng **Find & Replace** của IDE
2. Tìm: `new Color(139, 69, 19)` → Thay: `new Color(60, 179, 113)`
3. Tìm: `new Color(89, 45, 13)` → Thay: `new Color(34, 139, 34)`
4. Tìm: `new Color(138, 66, 17)` → Thay: `new Color(46, 125, 50)`
5. Rebuild project và test

**⚠️ Lưu ý:** Nên backup code trước khi thay đổi hàng loạt!

---

## Các Font Chữ Được Sử Dụng

| Vị Trí | Font Family | Style | Size |
|--------|-------------|-------|------|
| Player name | Georgia | BOLD | 22 |
| Timer | Monospaced | BOLD | 32 |
| Score | Georgia | PLAIN | 13 |
| Move number | Roboto | BOLD | 16 |
| Move notation | Roboto | PLAIN | 16 |
| Dialog title | Arial | BOLD | 18-20 |
| Button text | Arial/Georgia | PLAIN | 14-16 |
| Puzzle title | Georgia | BOLD | 22 |
| Puzzle moves | Roboto | BOLD | 18 |
| Puzzle objective | Roboto | PLAIN | 14 |

**Thay đổi font:** Sửa trong constructor của các label:
```java
label.setFont(new Font("TênFont", Font.BOLD/PLAIN, size));
```

---

## Tóm Tắt File Cần Sửa Để Thay Đổi Giao Diện

1. **ChessTile.java** - Màu highlight, nước đi, hint
2. **PlayerPanel.java** - Gradient panel, timer, score
3. **ChessToolbar.java** - Màu toolbar, kích thước nút
4. **GameModeSelectionDialog.java** - Dialog chọn mode
5. **ResignDialog.java** - Dialog xác nhận resign
6. **GameOverDialog.java** - Dialog kết thúc game
7. **PromotionDialog.java** - Dialog phong cấp
8. **MoveHistoryPanel.java** - Panel lịch sử nước đi
9. **ChessUI.java** - Layout tổng thể

**Không cần sửa:**
- **ImageLoader.java** - Chỉ load hình ảnh
- **ChessBoard.java** - Chỉ vẽ board image
- Các file logic game (không liên quan UI)

---

## Tips & Best Practices

1. **Luôn backup code trước khi thay đổi**
2. **Sử dụng alpha channel** (độ trong suốt) để tạo hiệu ứng mượt mà
3. **Test trên nhiều mode** (PvP, PvAI, Puzzle) sau khi thay đổi
4. **Giữ contrast tốt** giữa text và background để dễ đọc
5. **Sử dụng gradient** để tạo chiều sâu cho UI
6. **Bo tròn góc** (RoundRectangle2D) để UI trông hiện đại hơn
7. **Màu sắc nhất quán** - dùng bảng màu chung cho toàn project

---

**Chúc bạn customize giao diện thành công!** 🎨

