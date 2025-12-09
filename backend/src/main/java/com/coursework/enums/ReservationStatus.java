package com.coursework.enums;

public enum ReservationStatus {
    CONFIRMED,
    CANCELLED,
    DENIED,
    DONE;

    public String getReservationStatus() {
        switch (this) {
            case CANCELLED:
                return "Отменено";
            case DENIED:
                return "Отклонено";
            case CONFIRMED:
                return "Подтверждено";
            case DONE:
                return "Выполнено";
            default:
                throw new IllegalArgumentException("Unknown status: " + this);
        }
    }
}
