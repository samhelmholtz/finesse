package smfcore;

/**
 * Created by sam on 20.01.15.
 */
public class SemiringMatrixProductCalculator implements ISemiringMatrixProductCalculator {

    private final ISemiringScalarProductCalculator semiringScalarProductCalculator;

    public SemiringMatrixProductCalculator(ISemiringScalarProductCalculator semiringScalarProductCalculator) {
        this.semiringScalarProductCalculator = semiringScalarProductCalculator;
    }

    @Override
    public SemiringObject[][] CalculateMatrixProductWithTightMultiplication(SemiringObject[][] usageMatrix, SemiringObject[][] basisMatrixTransposed, ISemiringFeature[] semiringFeatures) {

        SemiringObject[][] result = new SemiringObject[usageMatrix.length][basisMatrixTransposed.length];

        for (int i = 0; i < usageMatrix.length; i++) {
            for (int j = 0; j < basisMatrixTransposed.length; j++) {
                result[i][j] = semiringScalarProductCalculator.CalculateScalarProductWithTightMultiplication(usageMatrix[i], basisMatrixTransposed[j],semiringFeatures[j]);
            }
        }

        return result;
    }

    @Override
    public SemiringObject[][] CalculateMatrixProductWithLooseMultiplication(boolean[][] usageMatrix, SemiringObject[][] basisMatrixTransposed, ISemiringFeature[] semiringFeatures) {
        SemiringObject[][] result = new SemiringObject[usageMatrix.length][basisMatrixTransposed.length];

        for (int i = 0; i < usageMatrix.length; i++) {
            for (int j = 0; j < basisMatrixTransposed.length; j++) {
                result[i][j] = semiringScalarProductCalculator.CalculateScalarProductWithLooseMultiplication(usageMatrix[i], basisMatrixTransposed[j],semiringFeatures[j]);
            }
        }

        return result;
    }
}
