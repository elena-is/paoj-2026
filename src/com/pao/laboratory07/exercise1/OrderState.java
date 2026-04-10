package com.pao.laboratory07.exercise1;

public enum OrderState {
    PLACED,
    PROCESSED,
    SHIPPED,
    DELIVERED,
    CANCELED;

    public boolean esteFinala() {
        return this == DELIVERED || this == CANCELED;
    }
}