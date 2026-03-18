package com.pao.laboratory03.enums;

// * PASUL 1 — Creează enum-ul Priority.java (fișier separat în același pachet):
// *   - Constante: LOW, MEDIUM, HIGH, CRITICAL
// *   - Câmpuri private: int level, String color
// *   - Constructor privat: Priority(int level, String color)
// *   - Getteri: getLevel(), getColor()
// *   - Metodă abstractă: String getEmoji() — fiecare constantă o implementează diferit
// *     LOW → "🟢", MEDIUM → "🟡", HIGH → "🟠", CRITICAL → "🔴"
// *   - Valorile sugerate:
// *     LOW(1, "green"), MEDIUM(2, "yellow"), HIGH(3, "orange"), CRITICAL(4, "red")
// *

public enum Priority {

    LOW(1, "green") {
        @Override
        public String getEmoji() { return "🟢"; }
    },
    MEDIUM(2, "yellow") {
        @Override
        public String getEmoji() { return "🟡"; }
    },
    HIGH(3, "orange") {
        @Override
        public String getEmoji() { return "🟠"; }
    },
    CRITICAL(4, "red") {
        @Override
        public String getEmoji() { return "🔴"; }
    };

    private final int level;
    private final String color;

    Priority(int level, String color) {
        this.level = level;
        this.color = color;
    }

    public int getLevel() { return level; }
    public String getColor() { return color; }

    public abstract String getEmoji();
}
