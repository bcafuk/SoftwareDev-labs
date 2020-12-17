package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.DoubleBinaryOperator;

public class CalcModelImpl implements CalcModel {
    private static final char DECIMAL_POINT = '.';
    private static final char NEGATIVE_SYMBOL = '-';

    private boolean isEditable = true;
    private boolean isNegative = false;
    private double currentValue = 0.0;
    private String currentString = "";
    private String frozenString = null;
    private Double activeOperand = null;
    private DoubleBinaryOperator pendingOperation = null;

    private final List<CalcValueListener> listeners = new LinkedList<>();

    @Override
    public void addCalcValueListener(CalcValueListener l) {
        listeners.add(Objects.requireNonNull(l, "The listener must not be null"));
    }

    @Override
    public void removeCalcValueListener(CalcValueListener l) {
        listeners.remove(Objects.requireNonNull(l, "The listener must not be null"));
    }

    private void notifyListeners() {
        for (CalcValueListener listener : listeners)
            listener.valueChanged(this);
    }

    @Override
    public String toString() {
        if (frozenString != null)
            return frozenString;

        if (currentString.isEmpty())
            return isNegative ? "-0" : "0";

        if (currentString.equals("NaN"))
            return currentString;

        return isNegative ? NEGATIVE_SYMBOL + currentString : currentString;
    }

    @Override
    public double getValue() {
        return currentValue;
    }

    @Override
    public void setValue(double value) {
        frozenString = null;
        currentValue = value;

        double sign = Math.copySign(1.0, value);
        this.isNegative = sign < 0.0;
        this.currentString = Double.toString(Math.abs(value));

        this.isEditable = false;
        notifyListeners();
    }

    @Override
    public boolean isEditable() {
        return isEditable;
    }

    @Override
    public void clear() {
        isEditable = true;
        isNegative = false;
        currentValue = 0.0;
        currentString = "";

        notifyListeners();
    }

    @Override
    public void clearAll() {
        isEditable = true;
        isNegative = false;
        currentValue = 0.0;
        currentString = "";
        frozenString = null;
        activeOperand = null;
        pendingOperation = null;

        notifyListeners();
    }

    @Override
    public void swapSign() throws CalculatorInputException {
        if (!isEditable)
            throw new CalculatorInputException("The calculator is not editable");

        isNegative = !isNegative;
        updateValue();
    }

    @Override
    public void insertDecimalPoint() throws CalculatorInputException {
        if (!isEditable)
            throw new CalculatorInputException("The calculator is not editable");
        if (currentString.isEmpty())
            throw new CalculatorInputException("No digits have been entered");
        if (currentString.indexOf(DECIMAL_POINT) != -1)
            throw new CalculatorInputException("The number already contains a decimal point");

        currentString += DECIMAL_POINT;
        frozenString = null;

        notifyListeners();
    }

    @Override
    public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
        if (!isEditable)
            throw new CalculatorInputException("The calculator is not editable");
        if (digit < 0 || digit > 9)
            throw new IllegalArgumentException("The digit must be in the range [0, 9], but was " + digit);

        if (currentString.equals("0"))
            if (digit == 0)
                return;
            else
                currentString = "";

        String newString = currentString + digit;
        double newValue = Double.parseDouble(newString);

        if (!Double.isFinite(newValue))
            throw new CalculatorInputException("The new number is not representable as a double: " + newString);

        currentString = newString;

        updateValue();
    }

    private void updateValue() {
        double absoluteValue = currentString.isEmpty() ? 0 : Double.parseDouble(currentString);
        currentValue = isNegative ? -absoluteValue : absoluteValue;
        frozenString = null;

        notifyListeners();
    }

    @Override
    public boolean isActiveOperandSet() {
        return activeOperand != null;
    }

    @Override
    public double getActiveOperand() throws IllegalStateException {
        if (activeOperand == null)
            throw new IllegalStateException("The active operand has not been set");

        return activeOperand;
    }

    @Override
    public void setActiveOperand(double activeOperand) {
        this.activeOperand = activeOperand;
    }

    @Override
    public void clearActiveOperand() {
        activeOperand = null;
    }

    @Override
    public DoubleBinaryOperator getPendingBinaryOperation() {
        return pendingOperation;
    }

    @Override
    public void setPendingBinaryOperation(DoubleBinaryOperator op) {
        pendingOperation = op;
    }

    @Override
    public void freezeValue(String value) {
        frozenString = value;
    }

    @Override
    public boolean hasFrozenValue() {
        return frozenString != null;
    }
}
