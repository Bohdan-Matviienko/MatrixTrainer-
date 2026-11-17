package org.example.ui;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class NumberField extends TextField {

    private static final String DEFAULT_STYLE =
            "-fx-background-color: white; -fx-border-color: #ced4da; -fx-border-radius: 4; -fx-background-radius: 4; -fx-font-size: 13px;";
    private static final String CORRECT_STYLE =
            "-fx-background-color: #d4edda; -fx-border-color: #28a745; -fx-border-width: 1.5; -fx-border-radius: 4; -fx-background-radius: 4; -fx-font-size: 13px;";
    private static final String ERROR_STYLE =
            "-fx-background-color: #f8d7da; -fx-border-color: #dc3545; -fx-border-width: 1.5; -fx-border-radius: 4; -fx-background-radius: 4; -fx-font-size: 13px;";
    private static final String HINT_STYLE =
            "-fx-background-color: #fff3cd; -fx-border-color: #ffc107; -fx-border-width: 1.5; -fx-border-radius: 4; -fx-background-radius: 4; -fx-font-size: 13px;";

    public NumberField() {
        setAlignment(Pos.CENTER);
        setPrefWidth(56);
        setPrefHeight(34);
        setStyle(DEFAULT_STYLE);

        setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?\\d{0,4}")) {
                return change;
            }
            return null;
        }));
    }

    public boolean hasValidInteger() {
        String text = getText().trim();
        return !text.isEmpty() && !text.equals("-");
    }

    public int getIntValue() {
        if (!hasValidInteger()) {
            return 0;
        }
        return Integer.parseInt(getText().trim());
    }

    public void markCorrect(String tooltipText) {
        setStyle(CORRECT_STYLE);
        attachInstantTooltip(tooltipText);
    }

    public void markError(String tooltipText) {
        setStyle(ERROR_STYLE);
        attachInstantTooltip(tooltipText);
    }

    public void markHint(String tooltipText) {
        setStyle(HINT_STYLE);
        attachInstantTooltip(tooltipText);
    }

    public void resetValidation() {
        setStyle(DEFAULT_STYLE);
        setTooltip(null);
    }

    private void attachInstantTooltip(String message) {
        Tooltip tooltip = new Tooltip(message);
        tooltip.setShowDelay(Duration.millis(150));
        tooltip.setStyle("-fx-font-size: 13px;");
        setTooltip(tooltip);
    }
}