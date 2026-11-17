package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import org.example.model.Matrix;
import org.example.model.MatrixService;
import org.example.model.MatrixServiceImpl;
import org.example.model.OperationMode;
import org.example.ui.NumberField;

import java.util.Random;

public class Controller {

    @FXML private ComboBox<OperationMode> modeComboBox;
    @FXML private Label ruleLabel;
    @FXML private Label operationSymbolLabel;
    @FXML private Label resultSizeLabel;
    @FXML private Label statusLabel;

    @FXML private Spinner<Integer> rowsASpinner;
    @FXML private Spinner<Integer> colsASpinner;
    @FXML private Spinner<Integer> rowsBSpinner;
    @FXML private Spinner<Integer> colsBSpinner;

    @FXML private GridPane gridA;
    @FXML private GridPane gridB;
    @FXML private GridPane gridResult;

    private final MatrixService matrixService = new MatrixServiceImpl();
    private final Random random = new Random();

    private NumberField[][] fieldsA;
    private NumberField[][] fieldsB;
    private NumberField[][] fieldsResult;
    private boolean updatingSpinners = false;

    @FXML
    public void initialize() {
        modeComboBox.getItems().setAll(OperationMode.values());
        modeComboBox.setValue(OperationMode.MULTIPLICATION);
        modeComboBox.valueProperty().addListener((obs, oldMode, newMode) -> onModeChanged(newMode));

        setupSpinner(rowsASpinner, 2);
        setupSpinner(colsASpinner, 2);
        setupSpinner(rowsBSpinner, 2);
        setupSpinner(colsBSpinner, 2);

        updateModeUI(OperationMode.MULTIPLICATION);
        rebuildGrids();
    }

    private void setupSpinner(Spinner<Integer> spinner, int initialValue) {
        SpinnerValueFactory<Integer> factory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 6, initialValue);
        spinner.setValueFactory(factory);
        spinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (!updatingSpinners) {
                rebuildGrids();
            }
        });
    }

    private void onModeChanged(OperationMode mode) {
        if (mode == null) return;
        updateModeUI(mode);
        rebuildGrids();
    }

    private void updateModeUI(OperationMode mode) {
        operationSymbolLabel.setText(mode.getSymbol());
        ruleLabel.setText(mode.getRuleDescription());
    }

    @FXML
    public void rebuildGrids() {
        int rA = rowsASpinner.getValue();
        int cA = colsASpinner.getValue();
        int rB = rowsBSpinner.getValue();
        int cB = colsBSpinner.getValue();

        fieldsA = createMatrixGrid(gridA, rA, cA, "0");
        fieldsB = createMatrixGrid(gridB, rB, cB, "0");
        gridResult.getChildren().clear();

        OperationMode mode = modeComboBox.getValue();

        if (mode == OperationMode.MULTIPLICATION) {
            if (cA != rB) {
                fieldsResult = null;
                resultSizeLabel.setText("Матриця C (несумісні розміри)");
                showStatus(String.format(
                        "Неможливо виконати множення: кількість стовпців A (%d) не дорівнює кількості рядків B (%d). Натисніть «Підібрати розміри».",
                        cA, rB), true);
                return;
            }
            fieldsResult = createMatrixGrid(gridResult, rA, cB, "");
            resultSizeLabel.setText(String.format("Матриця C (%d × %d)", rA, cB));
            showStatus("Режим «Множення матриць»: заповніть дані або згенеруйте приклад та обчисліть елементи матриці C.", false);

        } else if (mode == OperationMode.ADDITION) {
            if (rA != rB || cA != cB) {
                fieldsResult = null;
                resultSizeLabel.setText("Матриця C (несумісні розміри)");
                showStatus(String.format(
                        "Неможливо виконати додавання: розмірність A (%d×%d) відрізняється від B (%d×%d). Натисніть «Підібрати розміри».",
                        rA, cA, rB, cB), true);
                return;
            }
            fieldsResult = createMatrixGrid(gridResult, rA, cA, "");
            resultSizeLabel.setText(String.format("Матриця C (%d × %d)", rA, cA));
            showStatus("Режим «Додавання матриць»: додайте відповідні елементи матриць A і B та введіть відповіді у матрицю C.", false);
        }
    }

    private NumberField[][] createMatrixGrid(GridPane grid, int rows, int cols, String defaultText) {
        grid.getChildren().clear();
        NumberField[][] fields = new NumberField[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                NumberField field = new NumberField();
                field.setText(defaultText);
                grid.add(field, j, i);
                fields[i][j] = field;
            }
        }
        return fields;
    }

    @FXML
    public void onAdjustDimensions() {
        OperationMode mode = modeComboBox.getValue();
        int rA = rowsASpinner.getValue();
        int cA = colsASpinner.getValue();

        updatingSpinners = true;
        try {
            if (mode == OperationMode.MULTIPLICATION) {
                rowsBSpinner.getValueFactory().setValue(cA);
            } else if (mode == OperationMode.ADDITION) {
                rowsBSpinner.getValueFactory().setValue(rA);
                colsBSpinner.getValueFactory().setValue(cA);
            }
        } finally {
            updatingSpinners = false;
        }
        rebuildGrids();
    }

    @FXML
    public void onFillRandom() {
        if (fieldsResult == null) {
            onAdjustDimensions();
        }
        fillGridRandomly(fieldsA);
        fillGridRandomly(fieldsB);
        clearResultGrid();
        showStatus("Згенеровано новий навчальний приклад! Введіть відповіді у матрицю C та натисніть «Перевірити».", false);
    }

    private void fillGridRandomly(NumberField[][] fields) {
        for (NumberField[] row : fields) {
            for (NumberField field : row) {
                field.setText(String.valueOf(random.nextInt(19) - 9));
                field.resetValidation();
            }
        }
    }

    @FXML
    public void onShowHints() {
        if (fieldsResult == null) {
            showStatus("Спочатку узгодьте розмірності матриць A та B!", true);
            return;
        }

        Matrix matrixA = readAndNormalizeInputMatrix(fieldsA);
        Matrix matrixB = readAndNormalizeInputMatrix(fieldsB);
        Matrix expected = matrixService.calculate(matrixA, matrixB, modeComboBox.getValue());

        for (int i = 0; i < expected.getRows(); i++) {
            for (int j = 0; j < expected.getCols(); j++) {
                String hintText = "Підказка для C[" + (i + 1) + "][" + (j + 1) + "]: "
                        + expected.getFormula(i, j) + " = ?";
                fieldsResult[i][j].markHint(hintText);
            }
        }
        showStatus("Підказки активовано! Наведіть курсор миші на будь-яку клітинку матриці C, щоб побачити формулу.", false);
    }

    @FXML
    public void onCheckResult() {
        if (fieldsResult == null) {
            showStatus("Перевірка неможлива: розмірності матриць A та B несумісні для обраної операції!", true);
            return;
        }

        try {
            Matrix matrixA = readAndNormalizeInputMatrix(fieldsA);
            Matrix matrixB = readAndNormalizeInputMatrix(fieldsB);
            OperationMode mode = modeComboBox.getValue();

            Matrix expected = matrixService.calculate(matrixA, matrixB, mode);

            int total = expected.getRows() * expected.getCols();
            int correct = 0;

            for (int i = 0; i < expected.getRows(); i++) {
                for (int j = 0; j < expected.getCols(); j++) {
                    NumberField cell = fieldsResult[i][j];
                    int expectedVal = expected.getValue(i, j);
                    String fullEquation = expected.getFormula(i, j) + " = " + expectedVal;

                    if (cell.hasValidInteger() && cell.getIntValue() == expectedVal) {
                        cell.markCorrect("Правильно! " + fullEquation);
                        correct++;
                    } else {
                        cell.markError("Помилка! Правильний розрахунок: " + fullEquation);
                    }
                }
            }

            if (correct == total) {
                showStatus(String.format("Відмінно! Усі елементи обчислено правильно (%d з %d).", correct, total), false);
            } else {
                showStatus(String.format(
                        "Результат: %d з %d правильно. Наведіть курсор на червоні клітинки, щоб розібрати помилки.",
                        correct, total), true);
            }
        } catch (IllegalArgumentException ex) {
            showStatus(ex.getMessage(), true);
        }
    }

    @FXML
    public void onClear() {
        resetInputGrid(fieldsA);
        resetInputGrid(fieldsB);
        clearResultGrid();
        showStatus("Поля очищено.", false);
    }

    private void resetInputGrid(NumberField[][] fields) {
        if (fields == null) return;
        for (NumberField[] row : fields) {
            for (NumberField field : row) {
                field.setText("0");
                field.resetValidation();
            }
        }
    }

    private void clearResultGrid() {
        if (fieldsResult == null) return;
        for (NumberField[] row : fieldsResult) {
            for (NumberField field : row) {
                field.setText("");
                field.resetValidation();
            }
        }
    }

    private Matrix readAndNormalizeInputMatrix(NumberField[][] fields) {
        int rows = fields.length;
        int cols = fields[0].length;
        Matrix matrix = new Matrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!fields[i][j].hasValidInteger()) {
                    fields[i][j].setText("0");
                }
                matrix.setValue(i, j, fields[i][j].getIntValue());
            }
        }
        return matrix;
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setStyle(isError
                ? "-fx-text-fill: #dc3545; -fx-font-weight: bold; -fx-font-size: 13px;"
                : "-fx-text-fill: #155724; -fx-font-weight: bold; -fx-font-size: 13px;");
    }
}