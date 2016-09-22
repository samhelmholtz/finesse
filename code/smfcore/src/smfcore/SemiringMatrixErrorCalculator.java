package smfcore;

/**
 * Created by sam on 21.01.15.
 */
public class SemiringMatrixErrorCalculator implements ISemiringMatrixErrorCalculator {

    private ISemiringScalarProductCalculator semiringScalarProductCalculator;

    public SemiringMatrixErrorCalculator(ISemiringScalarProductCalculator semiringScalarProductCalculator) {
        this.semiringScalarProductCalculator = semiringScalarProductCalculator;
    }

    @Override
    public double CalculateErrorWithTightMultiplication(SemiringObject[][] expected, SemiringObject[][] usageMatrix, SemiringObject[][] basisMatrixTransposed, ISemiringFeature[] features) {

        int n = expected.length;
        int m = expected[0].length;

        double error = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                SemiringObject scalarProductResult = semiringScalarProductCalculator.CalculateScalarProductWithTightMultiplication(usageMatrix[i], basisMatrixTransposed[j], features[j]);
                error += features[j].Dissimilarity(expected[i][j], scalarProductResult);
            }
        }

        return error;
    }

    @Override
    public double CalculateErrorWithLooseMultiplication(SemiringObject[][] expected, boolean[][] usageMatrix, SemiringObject[][] basisMatrixTransposed, ISemiringFeature[] features) {
        int n = expected.length;
        int m = expected[0].length;
        double error =  0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                SemiringObject scalarProductResult = semiringScalarProductCalculator.CalculateScalarProductWithLooseMultiplication(usageMatrix[i], basisMatrixTransposed[j], features[j]);
                error += features[j].Dissimilarity(expected[i][j], scalarProductResult);
            }
        }
        return error;
    }

    @Override
    public double CalculateError(SemiringObject[][] expected, SemiringObject[][] actual, ISemiringFeature[] features) {
        int n = expected.length;
        int m = expected[0].length;
        double error =  0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                error += features[j].Dissimilarity(expected[i][j], actual[i][j]);
            }
        }
        return error;
    }
}
