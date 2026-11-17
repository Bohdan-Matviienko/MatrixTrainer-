package org.example.model;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MatrixServiceTest {

    private MatrixService matrixService;

    @BeforeMethod
    public void setUp() {
        matrixService = new MatrixServiceImpl();
    }

    @Test
    public void testAddMatricesValid() {
        Matrix a = new Matrix(2, 3);
        a.setValue(0, 0, 4);   a.setValue(0, 1, -3);  a.setValue(0, 2, 0);
        a.setValue(1, 0, -7);  a.setValue(1, 1, 8);   a.setValue(1, 2, 5);

        Matrix b = new Matrix(2, 3);
        b.setValue(0, 0, 2);   b.setValue(0, 1, 5);   b.setValue(0, 2, -9);
        b.setValue(1, 0, 7);   b.setValue(1, 1, -2);  b.setValue(1, 2, 10);

        Matrix result = matrixService.add(a, b);

        Assert.assertEquals(result.getRows(), 2);
        Assert.assertEquals(result.getCols(), 3);
        Assert.assertEquals(result.getValue(0, 0), 6);
        Assert.assertEquals(result.getValue(0, 1), 2);
        Assert.assertEquals(result.getValue(0, 2), -9);
        Assert.assertEquals(result.getValue(1, 0), 0);
        Assert.assertEquals(result.getValue(1, 1), 6);
        Assert.assertEquals(result.getValue(1, 2), 15);
        Assert.assertTrue(result.getFormula(0, 1).contains("(-3)"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddIncompatibleDimensionsThrowsException() {
        Matrix a = new Matrix(2, 3);
        Matrix b = new Matrix(3, 2);
        matrixService.add(a, b);
    }

    @Test
    public void testMultiplyMatricesValid() {
        Matrix a = new Matrix(2, 3);
        a.setValue(0, 0, 1);  a.setValue(0, 1, 2);  a.setValue(0, 2, 3);
        a.setValue(1, 0, 4);  a.setValue(1, 1, 5);  a.setValue(1, 2, 6);

        Matrix b = new Matrix(3, 2);
        b.setValue(0, 0, 7);  b.setValue(0, 1, 8);
        b.setValue(1, 0, 9);  b.setValue(1, 1, 1);
        b.setValue(2, 0, 2);  b.setValue(2, 1, 3);

        Matrix result = matrixService.multiply(a, b);

        Assert.assertEquals(result.getRows(), 2);
        Assert.assertEquals(result.getCols(), 2);
        Assert.assertEquals(result.getValue(0, 0), 31); // 1*7 + 2*9 + 3*2 = 31
        Assert.assertEquals(result.getValue(0, 1), 19); // 1*8 + 2*1 + 3*3 = 19
        Assert.assertEquals(result.getValue(1, 0), 85); // 4*7 + 5*9 + 6*2 = 85
        Assert.assertEquals(result.getValue(1, 1), 55); // 4*8 + 5*1 + 6*3 = 55
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testMultiplyIncompatibleDimensionsThrowsException() {
        Matrix a = new Matrix(2, 3);
        Matrix b = new Matrix(2, 3);
        matrixService.multiply(a, b);
    }

    @Test
    public void testCalculateSwitchesModesCorrectly() {
        Matrix a = new Matrix(2, 2);
        a.setValue(0, 0, 2); a.setValue(0, 1, 3);
        a.setValue(1, 0, 4); a.setValue(1, 1, 5);

        Matrix b = new Matrix(2, 2);
        b.setValue(0, 0, 1); b.setValue(0, 1, 2);
        b.setValue(1, 0, 3); b.setValue(1, 1, 4);

        Matrix sum = matrixService.calculate(a, b, OperationMode.ADDITION);
        Matrix product = matrixService.calculate(a, b, OperationMode.MULTIPLICATION);

        Assert.assertEquals(sum.getValue(0, 0), 3);      // 2 + 1
        Assert.assertEquals(product.getValue(0, 0), 11); // 2*1 + 3*3
    }
}