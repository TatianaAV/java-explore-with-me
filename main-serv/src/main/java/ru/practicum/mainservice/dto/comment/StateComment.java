package ru.practicum.mainservice.dto.comment;

public enum StateComment {
    PENDING("PENDING"),
    PUBLISHED("PUBLISHED"),
    CANCELED("CANCELED");

    private final String value;

    StateComment(String value) {
        this.value = value;
    }

    public static StateComment fromValue(String text) {
        for (StateComment b : StateComment.values()) {
            if (b.value.equals(text)) {
                return b;
            }
        }
        return null;
    }
}
