package smfcore;

/**
 * Created by sam on 21.01.15.
 */
public interface ISemiringMatrixErrorCalculator {
    double CalculateErrorWithTightMultiplication(SemiringObject[][] expected, SemiringObject[][] usageMatrix, SemiringObject[][] basisMatrixTransposed, ISemiringFeature[] features);
    double CalculateErrorWithLooseMultiplication(SemiringObject[][] expected, boolean[][] usageMatrix, SemiringObject[][] basisMatrixTransposed, ISemiringFeature[] features);
    double CalculateError(SemiringObject[][] expected, SemiringObject[][] actual, ISemiringFeature[] features);
}
