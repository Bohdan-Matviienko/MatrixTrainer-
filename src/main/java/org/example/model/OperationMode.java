package org.example.model;

public enum OperationMode {
    MULTIPLICATION(
            "Множення матриць",
            "×",
            "Правило: Кількість стовпців матриці A має дорівнювати кількості рядків матриці B (colsA = rowsB). Розмір C = rowsA × colsB."
    ),
    ADDITION(
            "Додавання матриць",
            "+",
            "Правило: Матриці A та B повинні мати однакову розмірність (rowsA = rowsB та colsA = colsB). Додаються відповідні елементи A[i][j] + B[i][j]."
    );

    private final String displayName;
    private final String symbol;
    private final String ruleDescription;

    OperationMode(String displayName, String symbol, String ruleDescription) {
        this.displayName = displayName;
        this.symbol = symbol;
        this.ruleDescription = ruleDescription;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getRuleDescription() {
        return ruleDescription;
    }

    @Override
    public String toString() {
        return displayName;
    }
}