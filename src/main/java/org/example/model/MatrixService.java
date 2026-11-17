package org.example.model;

public interface MatrixService {
    boolean canMultiply(Matrix a, Matrix b);
    boolean canAdd(Matrix a, Matrix b);
    boolean isCompatible(Matrix a, Matrix b, OperationMode mode);

    Matrix multiply(Matrix a, Matrix b);
    Matrix add(Matrix a, Matrix b);
    Matrix calculate(Matrix a, Matrix b, OperationMode mode);
}