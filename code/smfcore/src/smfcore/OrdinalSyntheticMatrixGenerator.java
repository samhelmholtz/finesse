/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smfcore;

public class OrdinalSyntheticMatrixGenerator implements IOrdinalSyntheticMatrixGenerator {

    private IStochastic stochastic;

    public OrdinalSyntheticMatrixGenerator(IStochastic stoch) {
        stochastic = stoch;
    }

    @Override
    public SemiringObject[][] GenerateWithSemiringUsageMatrix(int n, int m, int k, int numElementsInScale, double probDistBasis, double probDistUsage, int noisePercent) {
        double noiseFraction = (double) noisePercent / 100.0;

        // Build the cumulative probability distribution. Here we have some constraints.
        // The last number in the prob distribution must be 1.
        // The first number must be zero.
        // Must be monotonically increasing.
        // The number of elements in the cumulative probability distribution array is numElementsInScale
        // e.g. scale 0,1,2,3
        // => the unskewed cumulative prob dist: 0.25, 0.5, 0.75, 1.0
        // Say we get a value of 0.9. Is it smaller than 0.25? No, so we don't pick element 0. Continue...last one is 'is it smaller than
        // 1.0'? Yes...so we pick scale value 3.
        // If it's skewed we'll just say that it's linearly skewed, i.e. pr(a) + pr(b) + pr(c) + pr(d) = 1
        // pr(a) = pr(b) + delta
        // pr(b) = pr(c) + delta
        // pr(c) = pr(d) + delta
        // pr(d) = delta
        // i.e. an arithmetic sequence. We can calculate the delta easily
        // Our distribution types are called:
        // 0: positive skew
        // 1: no skew
        // 2: negative skew

//        // First calculate the CDF for the basis matrix
//        double[] cdfForBasis = new double[numElementsInScale];
//        if (probDistBasis == 0) {
//            // positive ("higher on the left") skew
//            double delta = 1.0 / (numElementsInScale * (1.0 + numElementsInScale) / 2.0);
//            cdfForBasis[cdfForBasis.length - 1] = 1;
//            for (int i = cdfForBasis.length - 2; i >= 0; i--) {
//                // Take the previous one, then add the probability of the current one, which is the previous one plus delta
//                // This should give us a one at the end
//                cdfForBasis[i] = cdfForBasis[i + 1] - ((cdfForBasis.length - i - 1) * delta);
//            }
//        } else if (probDistBasis == 2) {
//            // negative ("higher on the right") skew
//            double delta = 1.0 / (numElementsInScale * (1.0 + numElementsInScale) / 2.0);
//            cdfForBasis[0] = delta;
//            for (int i = 1; i < cdfForBasis.length; i++) {
//                // Take the previous one, then add the probability of the current one, which is the previous one plus delta
//                // This should give us a one at the end
//                cdfForBasis[i] = cdfForBasis[i - 1] + (i + 1) * delta;
//            }
//        } else {
//            // uniform probability distribution
//            double delta = 1.0 / numElementsInScale;
//            for (int i = 0; i < cdfForBasis.length; i++) {
//                cdfForBasis[i] = (i + 1) * delta;
//            }
//        }
//
//        // Now for the usage matrix
//        double[] cdfForUsage = new double[numElementsInScale];
//        if (probDistUsage == 0) {
//            // positive ("higher on the left") skew
//            double delta = 1.0 / (numElementsInScale * (1.0 + numElementsInScale) / 2.0);
//            cdfForUsage[cdfForUsage.length - 1] = 1;
//            for (int i = cdfForUsage.length - 2; i >= 0; i--) {
//                // Take the previous one, then add the probability of the current one, which is the previous one plus delta
//                // This should give us a one at the end
//                cdfForUsage[i] = cdfForUsage[i + 1] - ((cdfForUsage.length - i - 1) * delta);
//            }
//        } else if (probDistUsage == 2) {
//            // negative ("higher on the right") skew
//            double delta = 1.0 / (numElementsInScale * (1.0 + numElementsInScale) / 2.0);
//            cdfForUsage[0] = delta;
//            for (int i = 1; i < cdfForUsage.length; i++) {
//                // Take the previous one, then add the probability of the current one, which is the previous one plus delta
//                // This should give us a one at the end
//                cdfForUsage[i] = cdfForUsage[i - 1] + (i + 1) * delta;
//            }
//        } else {
//            // uniform probability distribution
//            double delta = 1.0 / numElementsInScale;
//            for (int i = 0; i < cdfForUsage.length; i++) {
//                cdfForUsage[i] = (i + 1) * delta;
//            }
//        }

        // CDF for basis matrix
        double[] probabilityDensityFunctionBasis = new double[numElementsInScale];
        for (int i = 0; i < numElementsInScale; i++) {
            probabilityDensityFunctionBasis[i] = Math.pow((i + 1), probDistBasis);
        }
        // normalize
        double sumOfProbabilities = 0;
        for (int i = 0; i < numElementsInScale; i++) {
            sumOfProbabilities += probabilityDensityFunctionBasis[i];
        }
        double[] cumulativeDistributionBasis = new double[numElementsInScale];
        for (int i = 0; i < numElementsInScale; i++) {
            cumulativeDistributionBasis[i] = (i == 0 ? 0 : cumulativeDistributionBasis[i - 1]) + probabilityDensityFunctionBasis[i] / sumOfProbabilities;
        }


        // CDF for usage matrix
        double[] probabilityDensityFunctionUsage = new double[numElementsInScale];
        for (int i = 0; i < numElementsInScale; i++) {
            probabilityDensityFunctionUsage[i] = Math.pow((i + 1), probDistUsage);
        }
        // normalize
        sumOfProbabilities = 0;
        for (int i = 0; i < numElementsInScale; i++) {
            sumOfProbabilities += probabilityDensityFunctionUsage[i];
        }
        double[] cumulativeDistributionUsage = new double[numElementsInScale];
        for (int i = 0; i < numElementsInScale; i++) {
            cumulativeDistributionUsage[i] = (i == 0 ? 0 : cumulativeDistributionUsage[i - 1]) + probabilityDensityFunctionUsage[i] / sumOfProbabilities;
        }


        // Now we can build the clean basis matrix (transposed)
        SemiringObject[][] basisMatrixTransposed = new SemiringObject[m][k];

        for (int i = 0; i < k; i++) {
            for (int j = 0; j < m; j++) {
                double r = stochastic.NextUniformlyDistributedDouble();
                for (short l = 0; l < cumulativeDistributionBasis.length; l++) {
                    if (r <= cumulativeDistributionBasis[l]) {
                        basisMatrixTransposed[j][i] = new OrdinalSemiringObject((int) l);
                        break;
                    }
                }
            }
        }

        // And build the clean usage matrix (not transposed)
        SemiringObject[][] usageMatrix = new SemiringObject[n][k];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                double r = stochastic.NextUniformlyDistributedDouble();
                for (short l = 0; l < cumulativeDistributionUsage.length; l++) {
                    if (r <= cumulativeDistributionUsage[l]) {
                        usageMatrix[i][j] = new OrdinalSemiringObject((int) l);
                        break;
                    }
                }
            }
        }

//        // Generate the features
//        ISemiringFeature[] semiringFeatures = new ISemiringFeature[m];
//        for (int i = 0; i < m; i++) {
//            semiringFeatures[i] = new OrdinalSemiringFeature(numElementsInScale);
//        }

        // Matrix product to generate the "noiseless" data matrix
//        SemiringMatrixProductCalculator semiringMatrixProductCalculator = new SemiringMatrixProductCalculator(new SemiringScalarProductCalculator());
        SemiringObject[][] resultMatrix = new SemiringObject[n][m]; // semiringMatrixProductCalculator.CalculateMatrixProductWithTightMultiplication(usageMatrix, basisMatrix, semiringFeatures);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int entryResult = 0;
                for (int l = 0; l < k; l++) {
                    SemiringObject first = usageMatrix[i][l];
                    SemiringObject second = basisMatrixTransposed[j][l];
                    int sum = ((OrdinalSemiringObject) first).IntegerValue + ((OrdinalSemiringObject) second).IntegerValue - (numElementsInScale - 1);
                    int multResult = sum > 0 ? sum : 0;
                    entryResult = entryResult > multResult ? entryResult : multResult;
                }
                resultMatrix[i][j] = new OrdinalSemiringObject(entryResult);
            }
        }

        // Now we can add noise at the given percentage to generate the dirty matrix
        // If an entry is chosen for adding noise, we simply "flip" it by setting that entry to either 0 or the maximum scale value, depending on which one it's furthest away from
        // E.g. if we have a scale 0,1,2,3 and the entry is currently 2, we set it to zero. If the entry is currently 1 we set it to 3, and so on.
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (stochastic.NextUniformlyDistributedDouble() <= noiseFraction) {
                    resultMatrix[i][j] = ((OrdinalSemiringObject) resultMatrix[i][j]).IntegerValue > ((numElementsInScale - 1.0) / 2.0) ? new OrdinalSemiringObject(0) : new OrdinalSemiringObject(numElementsInScale - 1);
                }
            }
        }

        return resultMatrix;
    }

    @Override
    public SemiringObject[][] GenerateWithBooleanUsageMatrix(int n, int m, int k, int numElementsInScale, double probDistBasis, int numTrueValuesPerRowOfUsageMatrix, int noisePercent) {
        double noiseFraction = (double) noisePercent / 100.0;

        // Build the cumulative probability distribution. Here we have some constraints.
        // The last number in the prob distribution must be 1.
        // The first number must be zero.
        // Must be monotonically increasing.
        // The number of elements in the cumulative probability distribution array is numElementsInScale
        // e.g. scale 0,1,2,3
        // => the unskewed cumulative prob dist: 0.25, 0.5, 0.75, 1.0
        // Say we get a value of 0.9. Is it smaller than 0.25? No, so we don't pick element 0. Continue...last one is 'is it smaller than
        // 1.0'? Yes...so we pick scale value 3.
        // If it's skewed we'll just say that it's linearly skewed, i.e. pr(a) + pr(b) + pr(c) + pr(d) = 1
        // pr(a) = pr(b) + delta
        // pr(b) = pr(c) + delta
        // pr(c) = pr(d) + delta
        // pr(d) = delta
        // i.e. an arithmetic sequence. We can calculate the delta easily
        // Our distribution types are called:
        // 0: positive skew
        // 1: no skew
        // 2: negative skew

        // First calculate the CDF for the basis matrix
        // CDF for basis matrix
        double[] probabilityDensityFunctionBasis = new double[numElementsInScale];
        for (int i = 0; i < numElementsInScale; i++) {
            probabilityDensityFunctionBasis[i] = Math.pow((i + 1), probDistBasis);
        }
        // normalize
        double sumOfProbabilities = 0;
        for (int i = 0; i < numElementsInScale; i++) {
            sumOfProbabilities += probabilityDensityFunctionBasis[i];
        }
        double[] cumulativeDistributionBasis = new double[numElementsInScale];
        for (int i = 0; i < numElementsInScale; i++) {
            cumulativeDistributionBasis[i] = (i == 0 ? 0 : cumulativeDistributionBasis[i - 1]) + probabilityDensityFunctionBasis[i] / sumOfProbabilities;
        }

        // Now we can build the clean basis matrix (transposed)
        SemiringObject[][] basisMatrixTransposed = new SemiringObject[m][k];
        int numNonZeroValuesInBasisMatrix = 0;
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < m; j++) {
                double r = stochastic.NextUniformlyDistributedDouble();
                for (short l = 0; l < cumulativeDistributionBasis.length; l++) {
                    if (r <= cumulativeDistributionBasis[l]) {
                        basisMatrixTransposed[j][i] = new OrdinalSemiringObject((int) l);
                        break;
                    }
                }
                if (((OrdinalSemiringObject) basisMatrixTransposed[j][i]).IntegerValue > 0) {
                    numNonZeroValuesInBasisMatrix++;
                }
            }
        }

        double probabilityForTrueInUsageMatrix = (double) numTrueValuesPerRowOfUsageMatrix / k;
        int numTrueValuesInUsageMatrix = 0;
        // And build the clean usage matrix (not transposed)
        SemiringObject[][] usageMatrix = new SemiringObject[n][k];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                double r = stochastic.NextUniformlyDistributedDouble();
                usageMatrix[i][j] = new OrdinalSemiringObject(r < probabilityForTrueInUsageMatrix ? numElementsInScale - 1 : 0);

                if (((OrdinalSemiringObject) usageMatrix[i][j]).IntegerValue > 0) {
                    numTrueValuesInUsageMatrix++;
                }
            }
        }

        // Report the number of ones total in the usage matrix (potentially useful statistic)
        System.out.println("NumNonZeroValuesInBasisMatrix=" + numNonZeroValuesInBasisMatrix + " (" + (100.0 * numNonZeroValuesInBasisMatrix / (m * k)) + "%)");
        System.out.println("NumTrueValuesInUsageMatrix=" + numTrueValuesInUsageMatrix + " (" + (100.0 * numTrueValuesInUsageMatrix / (n * k)) + "%)");

        // Matrix product to generate the "noiseless" data matrix
        SemiringObject[][] resultMatrix = new SemiringObject[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int entryResult = 0;
                for (int l = 0; l < k; l++) {
                    SemiringObject first = usageMatrix[i][l];
                    SemiringObject second = basisMatrixTransposed[j][l];
                    int sum = ((OrdinalSemiringObject) first).IntegerValue + ((OrdinalSemiringObject) second).IntegerValue - (numElementsInScale - 1);
                    int multResult = sum > 0 ? sum : 0;
                    entryResult = entryResult > multResult ? entryResult : multResult;
                }
                resultMatrix[i][j] = new OrdinalSemiringObject(entryResult);
            }
        }

        // Now we can add noise at the given percentage to generate the dirty matrix
        // For the given percentage of matrix entries, we randomly choose another value from the ordinal scale
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (stochastic.NextUniformlyDistributedDouble() <= noiseFraction) {
                    resultMatrix[i][j] = new OrdinalSemiringObject(stochastic.NextInt(numElementsInScale)); //  ((OrdinalSemiringObject) resultMatrix[i][j]).IntegerValue > ((numElementsInScale - 1.0) / 2.0) ? new OrdinalSemiringObject(0) : new OrdinalSemiringObject(numElementsInScale - 1);
                }
            }
        }

        return resultMatrix;
    }
}
