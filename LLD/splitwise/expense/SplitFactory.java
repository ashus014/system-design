package LLD.splitwise.expense;

import LLD.splitwise.expense.split.EqualExpenseSplit;
import LLD.splitwise.expense.split.ExpenseSplit;
import LLD.splitwise.expense.split.PercentageExpenseSplit;
import LLD.splitwise.expense.split.UnequalExpenseSplit;

public class SplitFactory {

    public static ExpenseSplit getSplitObject(ExpenseSplitType splitType) {

        switch (splitType) {
            case EQUAL:
                return new EqualExpenseSplit();
            case UNEQUAL:
                return new UnequalExpenseSplit();
            case PERCENTAGE:
                return new PercentageExpenseSplit();
            default:
                return null;
        }
    }
}
