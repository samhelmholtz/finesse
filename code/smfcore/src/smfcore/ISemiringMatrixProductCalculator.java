package smfcore;

/**
 * Created by sam on 20.01.15.
 */
public interface ISemiringMatrixProductCalculator {
    SemiringObject[][] CalculateMatrixProductWithTightMultiplication(SemiringObject[][] usageMatrix, SemiringObject[][] basisMatrixTransposed, ISemiringFeature[] semiringFeatures);
    SemiringObject[][] CalculateMatrixProductWithLooseMultiplication(boolean[][] usageMatrix, SemiringObject[][] basisMatrixTransposed, ISemiringFeature[] semiringFeatures);
}
