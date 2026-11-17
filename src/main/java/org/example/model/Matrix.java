package org.example.model;

import java.util.Arrays;
import java.util.Objects;

public class Matrix {
    private final int rows;
    private final int cols;
    private final int[][] values;
    private final String[][] formulas;

    public Matrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Розмірності матриці мають бути додатними числами.");
        }
        this.rows = rows;
        this.cols = cols;
        this.values = new int[rows][cols];
        this.formulas = new String[rows][cols];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getValue(int row, int col) {
        checkBounds(row, col);
        return values[row][col];
    }

    public void setValue(int row, int col, int value) {
        checkBounds(row, col);
        values[row][col] = value;
    }

    public String getFormula(int row, int col) {
        checkBounds(row, col);
        return formulas[row][col];
    }

    public void setFormula(int row, int col, String formula) {
        checkBounds(row, col);
        formulas[row][col] = formula;
    }

    private void checkBounds(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException(
                    String.format("Індекс [%d][%d] виходить за межі матриці %dx%d", row, col, rows, cols)
            );
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Matrix matrix)) return false;
        return rows == matrix.rows
                && cols == matrix.cols
                && Arrays.deepEquals(values, matrix.values);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(rows, cols);
        result = 31 * result + Arrays.deepHashCode(values);
        return result;
    }
}