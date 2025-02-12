package com.local.portforwader;

import javafx.scene.control.Alert;

public class AlertBuilder {
    public String message;

    public static int ERROR = 3;
    public static int CONFIRMATION = 1;
    public static int WARNING = 2;

    public int type;

    public AlertBuilder message(String message) {
        this.message = message;
        return this;
    }

    public AlertBuilder type(int type) {
        this.type = type;
        return this;
    }

    public void build() {
        Alert alert = new Alert(getType());
        alert.setTitle("Mensagem");
        alert.setContentText(this.message);
        alert.show();
    }

    private Alert.AlertType getType() {
        return switch (this.type) {
            case 1 -> Alert.AlertType.CONFIRMATION;
            case 2 -> Alert.AlertType.WARNING;
            case 3 -> Alert.AlertType.ERROR;
            default -> throw new IllegalStateException("Unexpected value: " + this.type);
        };
    }
}
