/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smfcore;

/**
 * @author sam
 */
public class Semize implements ISemize {

    private IDataMatrixSampler dataMatrixSampler;
    private IGreedyCoverUsage greedyCoverUsage;
    private IGreedyCoverBasis greedyCoverBasis;
    private IUsageMatrixSetTypeDeterminer usageMatrixSetTypeDeterminer;
    private ISemiringScalarProductCalculator semiringScalarProductCalculator;
    private ISemiringMatrixErrorCalculator semiringMatrixErrorCalculator;

    public Semize(IDataMatrixSampler dataMatrixSampler, IGreedyCoverUsage greedyCoverUsage, IGreedyCoverBasis greedyCoverBasis, IUsageMatrixSetTypeDeterminer usageMatrixSetTypeDeterminer, ISemiringScalarProductCalculator semiringScalarProductCalculator, ISemiringMatrixErrorCalculator semiringMatrixErrorCalculator) {
        this.dataMatrixSampler = dataMatrixSampler;
        this.greedyCoverUsage = greedyCoverUsage;
        this.greedyCoverBasis = greedyCoverBasis;
        this.usageMatrixSetTypeDeterminer = usageMatrixSetTypeDeterminer;
        this.semiringScalarProductCalculator = semiringScalarProductCalculator;
        this.semiringMatrixErrorCalculator = semiringMatrixErrorCalculator;
    }

    @Override
    public SemizeResult Semize(SemiringObject[][] dataMatrix, ISemiringFeature[] semirings, int k) {
        int n = dataMatrix.length;
        int m = dataMatrix[0].length;

        // We create a transpose of the data matrix, which makes it easier to get "columns" of the original data matrix
        SemiringObject[][] dataMatrixTransposed = new SemiringObject[m][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                dataMatrixTransposed[j][i] = dataMatrix[i][j];
            }
        }

        // The basis matrix is transposed
        SemiringObject[][] basisMatrixTransposed = dataMatrixSampler.SampleK(dataMatrix, k, semirings);
        SemiringObject[][] usageMatrix = new SemiringObject[n][k];
        boolean[][] usageMatrixBoolean = new boolean[n][k];

        boolean usageMatrixCannotUseFeatureObjects = usageMatrixSetTypeDeterminer.UsageMatrixCannotUseFeatureObjects(semirings);

        double errorBest = Double.POSITIVE_INFINITY;
        double delta;
        int numIterations = 0;
        if (usageMatrixCannotUseFeatureObjects) {
            System.out.println("Info: Using binary usage matrix");
            do {
                // Solve for U
                for (int i = 0; i < n; i++) {
                    usageMatrixBoolean[i] = greedyCoverUsage.GreedyCoverUsageWithLooseConjunction(dataMatrix[i], basisMatrixTransposed, semirings);
                }

                // Solve for B
                for (int i = 0; i < m; i++) {
                    basisMatrixTransposed[i] = greedyCoverBasis.GreedyCoverBasisWithLooseConjunction(dataMatrixTransposed[i], usageMatrixBoolean, semirings[i]);
                }

                // Calculate the error
                double error = semiringMatrixErrorCalculator.CalculateErrorWithLooseMultiplication(dataMatrix, usageMatrixBoolean, basisMatrixTransposed, semirings);
                delta = error - errorBest;
                if (delta < 0) {
                    errorBest = error;
                }
                numIterations++;
            } while (delta < 0);

        } else {
            System.out.println("Info: Using usage matrix with entries from full set");
            do {
                // Solve for U
                for (int i = 0; i < n; i++) {
                    usageMatrix[i] = greedyCoverUsage.GreedyCoverUsageWithTightConjunction(dataMatrix[i], basisMatrixTransposed, semirings);
                }

                // Solve for B
                for (int i = 0; i < m; i++) {
                    basisMatrixTransposed[i] = greedyCoverBasis.GreedyCoverBasisWithTightConjunction(dataMatrixTransposed[i], usageMatrix, semirings[i]);
                }

                // Calculate the error
                double error = semiringMatrixErrorCalculator.CalculateErrorWithTightMultiplication(dataMatrix, usageMatrix, basisMatrixTransposed, semirings);
                delta = error - errorBest;
                if (delta < 0) {
                    errorBest = error;
                }
                numIterations++;
            } while (delta < 0);
        }

        System.out.println("Randomization-round iteration finished. Error: " + errorBest + " (" + Math.round(100 * errorBest / (n * m)) + "% the max possible value of " + (n * m) + "). Iteration count: " + numIterations);

        UsageBasisMatrixPair usageBasisMatrixPair = new UsageBasisMatrixPair();
        usageBasisMatrixPair.BasisMatrixTransposed = basisMatrixTransposed;
        if (usageMatrixCannotUseFeatureObjects) {
            SemiringObject[][] booleanUsageMatrix = new SemiringObject[n][k];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < k; j++) {
                    booleanUsageMatrix[i][j] = new CommonSemiringObject(usageMatrixBoolean[i][j]);
                }
            }
            usageBasisMatrixPair.UsageMatrix = booleanUsageMatrix;
        } else {
            usageBasisMatrixPair.UsageMatrix = usageMatrix;
        }

        SemizeResult semizeResult = new SemizeResult();
        semizeResult.Error = errorBest;
        semizeResult.UsageBasisMatrixPair = usageBasisMatrixPair;
        return semizeResult;
    }
}
