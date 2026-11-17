package org.example.model;

public class MatrixServiceImpl implements MatrixService {

    @Override
    public boolean canMultiply(Matrix a, Matrix b) {
        if (a == null || b == null) return false;
        return a.getCols() == b.getRows();
    }

    @Override
    public boolean canAdd(Matrix a, Matrix b) {
        if (a == null || b == null) return false;
        return a.getRows() == b.getRows() && a.getCols() == b.getCols();
    }

    @Override
    public boolean isCompatible(Matrix a, Matrix b, OperationMode mode) {
        if (mode == null) return false;
        return switch (mode) {
            case MULTIPLICATION -> canMultiply(a, b);
            case ADDITION -> canAdd(a, b);
        };
    }

    @Override
    public Matrix multiply(Matrix a, Matrix b) {
        if (!canMultiply(a, b)) {
            throw new IllegalArgumentException(
                    String.format("Неможливо перемножити матриці: кількість стовпців A (%d) не дорівнює кількості рядків B (%d).",
                            a != null ? a.getCols() : 0, b != null ? b.getRows() : 0)
            );
        }

        int rows = a.getRows();
        int cols = b.getCols();
        int common = a.getCols();
        Matrix result = new Matrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int sum = 0;
                StringBuilder expr = new StringBuilder();

                for (int k = 0; k < common; k++) {
                    int valA = a.getValue(i, k);
                    int valB = b.getValue(k, j);
                    sum += valA * valB;

                    if (k > 0) {
                        expr.append(" + ");
                    }
                    expr.append(formatNum(valA)).append("·").append(formatNum(valB));
                }

                result.setValue(i, j, sum);
                result.setFormula(i, j, expr.toString());
            }
        }
        return result;
    }

    @Override
    public Matrix add(Matrix a, Matrix b) {
        if (!canAdd(a, b)) {
            throw new IllegalArgumentException(
                    String.format("Неможливо додати матриці різних розмірностей: A(%dx%d) та B(%dx%d).",
                            a != null ? a.getRows() : 0, a != null ? a.getCols() : 0,
                            b != null ? b.getRows() : 0, b != null ? b.getCols() : 0)
            );
        }

        int rows = a.getRows();
        int cols = a.getCols();
        Matrix result = new Matrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int valA = a.getValue(i, j);
                int valB = b.getValue(i, j);
                int sum = valA + valB;

                String expr = String.format("A[%d][%d] + B[%d][%d] = %s + %s",
                        i + 1, j + 1, i + 1, j + 1, formatNum(valA), formatNum(valB));

                result.setValue(i, j, sum);
                result.setFormula(i, j, expr);
            }
        }
        return result;
    }

    @Override
    public Matrix calculate(Matrix a, Matrix b, OperationMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("Режим операції не обрано.");
        }
        return switch (mode) {
            case MULTIPLICATION -> multiply(a, b);
            case ADDITION -> add(a, b);
        };
    }

    private String formatNum(int n) {
        return n < 0 ? "(" + n + ")" : String.valueOf(n);
    }
}