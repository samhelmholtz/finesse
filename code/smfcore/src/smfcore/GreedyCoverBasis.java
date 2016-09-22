/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smfcore;

import java.util.List;

/**
 * @author sam
 */
public class GreedyCoverBasis implements IGreedyCoverBasis {

    private ISemiringScalarProductCalculator semiringScalarProductCalculator;
    private boolean useNewApproach;

    public GreedyCoverBasis(ISemiringScalarProductCalculator semiringScalarProductCalculator) {
        this.semiringScalarProductCalculator = semiringScalarProductCalculator;
        this.useNewApproach = true;
    }

    @Override
    public SemiringObject[] GreedyCoverBasisWithLooseConjunction(SemiringObject[] dataColumnVector, boolean[][] usageMatrix, ISemiringFeature semiringFeature) {

        if (useNewApproach) {
            // The error can only be a multiple of 1/n, where n is the length of the data column vector.
            // We hence calculate 1/(2n) for solving the floating-point value comparison problem
            double errorEps = 1.0 / (2 * dataColumnVector.length);

            // Copy the usage matrix into a boolean matrix for performance reasons
            int dataColVectorLength = dataColumnVector.length;
            int k = usageMatrix[0].length;

            // Allocate memory for our result
            SemiringObject[] result = new SemiringObject[k];

            // Initialize all entries to "zero"
            SemiringObject zeroElement = semiringFeature.GetIdentityElementForAddition();
            for (int i = 0; i < result.length; i++) {
                result[i] = zeroElement;
            }

            SemiringObject[] dataLengthVectorIncremental = new SemiringObject[dataColVectorLength];
            for (int i = 0; i < dataColVectorLength; i++) {
                dataLengthVectorIncremental[i] = zeroElement;
            }

            // Some temp vectors for intermeditate computation
            SemiringObject[] resultAfterAnd = new SemiringObject[usageMatrix.length];
            SemiringObject[] resultAfterOr = new SemiringObject[usageMatrix.length];
            boolean[] correspondingColumnVectorUsageMatrix = new boolean[usageMatrix.length];

            while (true) {

                double errorFromIncremental = 0;
                for (int i = 0; i < dataColVectorLength; i++) {
                    errorFromIncremental += semiringFeature.Dissimilarity(dataLengthVectorIncremental[i], dataColumnVector[i]);
                }

                int bestIndexSoFar = -1;
                SemiringObject bestValueSoFar = null;

                for (int i = 0; i < k; i++) {

                    List<SemiringObject> valuesToTry = semiringFeature.GetIncrementalSubset(result[i]);

                    // Get the corresponding usage matrix column vector
                    for (int j = 0; j < usageMatrix.length; j++) {
                        correspondingColumnVectorUsageMatrix[j] = usageMatrix[j][i];
                    }

                    for (SemiringObject valueToTry : valuesToTry) {

                        // Now that we have set our candidate value, we can get the corresponding vector which would contribute to the eventual supremum operation
                        // by ANDING our corresponding column vector from the usage matrix with a vector having a copy-paste of the current value.
                        // Practically, we can just do a min() over the correspondingColumnVector

                        for (int j = 0; j < dataColVectorLength; j++) {
                            resultAfterAnd[j] = correspondingColumnVectorUsageMatrix[j] ? valueToTry : zeroElement;
                        }

                        // Now we can do an OR with our incremental data vector to see how it's changed
                        for (int j = 0; j < dataColVectorLength; j++) {
                            resultAfterOr[j] = semiringFeature.Addition(resultAfterAnd[j], dataLengthVectorIncremental[j]);
                        }

                        // Now we can calculate the error for this changed data vector, and see how it compares
                        double newError = 0;
                        for (int j = 0; j < dataColVectorLength; j++) {
                            newError += semiringFeature.Dissimilarity(resultAfterOr[j], dataColumnVector[j]);
                        }

                        if (errorFromIncremental > (newError + errorEps)) {
                            errorFromIncremental = newError;
                            bestIndexSoFar = i;
                            bestValueSoFar = valueToTry;
                        }
                    }
                }

                if (bestIndexSoFar > -1) {
                    // Get the corresponding usage matrix column vector
                    for (int j = 0; j < dataColVectorLength; j++) {
                        correspondingColumnVectorUsageMatrix[j] = usageMatrix[j][bestIndexSoFar];
                    }

                    // Set our result value as well as the incremental vector
                    result[bestIndexSoFar] = bestValueSoFar;
                    for (int j = 0; j < dataColVectorLength; j++) {
                        resultAfterAnd[j] = correspondingColumnVectorUsageMatrix[j] ? bestValueSoFar : zeroElement;
                    }

                    // Now we can do an OR with our incremental data vector to see how it's changed
                    for (int j = 0; j < dataColVectorLength; j++) {
                        dataLengthVectorIncremental[j] = semiringFeature.Addition(resultAfterAnd[j], dataLengthVectorIncremental[j]);
                    }
                } else {
                    // No changes useful
                    break;
                }

            }

            return result;
        } else {
// Copy the usage matrix into a boolean matrix for performance reasons
            int dataColVectorLength = dataColumnVector.length;
            int k = usageMatrix[0].length;

            // Allocate memory for our result
            SemiringObject[] result = new SemiringObject[k];

            // Initialize all entries to "zero"
            SemiringObject zeroElement = semiringFeature.GetIdentityElementForAddition();
            for (int i = 0; i < result.length; i++) {
                result[i] = zeroElement;
            }

            // Calculate the initial error
            double errorBest = 0;
            for (int i = 0; i < dataColVectorLength; i++) {
                SemiringObject scalarProductResult = semiringScalarProductCalculator.CalculateScalarProductWithLooseMultiplication(usageMatrix[i], result, semiringFeature);
                errorBest += semiringFeature.Dissimilarity(dataColumnVector[i], scalarProductResult);
            }

            double delta = Double.NEGATIVE_INFINITY;
            while (delta < 0) {
                // Clone the optimum
                SemiringObject[] localOptimalVector = result.clone();
                double localError = errorBest;

                for (int i = 0; i < k; i++) {

                    List<SemiringObject> valuesToTry = semiringFeature.GetIncrementalSubset(result[i]);
                    SemiringObject[] vectorToModify = result.clone();

                    for (SemiringObject valueToTry : valuesToTry) {
                        vectorToModify[i] = valueToTry;
                        double errorAfterModification = 0;
                        for (int j = 0; j < dataColVectorLength; j++) {
                            SemiringObject scalarProductResult = semiringScalarProductCalculator.CalculateScalarProductWithLooseMultiplication(usageMatrix[j], vectorToModify, semiringFeature);
                            errorAfterModification += semiringFeature.Dissimilarity(dataColumnVector[j], scalarProductResult);
                        }

                        if (errorAfterModification < localError) {
                            localOptimalVector = vectorToModify.clone();
                            localError = errorAfterModification;
                        }
                    }
                }

                delta = localError - errorBest;
                if (delta < 0) {
                    result = localOptimalVector.clone();
                    errorBest = localError;
                }
            }

            return result;
        }
    }

    @Override
    public SemiringObject[] GreedyCoverBasisWithTightConjunction(SemiringObject[] dataColumnVector, SemiringObject[][] usageMatrix, ISemiringFeature semiringFeature) {
        if (useNewApproach) {
            // The error can only be a multiple of 1/n, where n is the length of the data column vector.
            // We hence calculate 1/(2n) for solving the floating-point value comparison problem
            double errorEps = 1.0 / (2 * dataColumnVector.length);

            // Copy the usage matrix into a boolean matrix for performance reasons
            int dataColVectorLength = dataColumnVector.length;
            int k = usageMatrix[0].length;

            // Allocate memory for our result
            SemiringObject[] result = new SemiringObject[k];

            // Initialize all entries to "zero"
            SemiringObject zeroElement = semiringFeature.GetIdentityElementForAddition();
            for (int i = 0; i < result.length; i++) {
                result[i] = zeroElement;
            }

            SemiringObject[] dataLengthVectorIncremental = new SemiringObject[dataColVectorLength];
            for (int i = 0; i < dataColVectorLength; i++) {
                dataLengthVectorIncremental[i] = zeroElement;
            }

            // Some temp vectors for intermeditate computation
            SemiringObject[] resultAfterAnd = new SemiringObject[usageMatrix.length];
            SemiringObject[] resultAfterOr = new SemiringObject[usageMatrix.length];
            SemiringObject[] correspondingColumnVectorUsageMatrix = new SemiringObject[usageMatrix.length];

            while (true) {

                double errorFromIncremental = 0;
                for (int i = 0; i < dataColVectorLength; i++) {
                    errorFromIncremental += semiringFeature.Dissimilarity(dataLengthVectorIncremental[i], dataColumnVector[i]);
                }

                int bestIndexSoFar = -1;
                SemiringObject bestValueSoFar = null;

                for (int i = 0; i < k; i++) {

                    List<SemiringObject> valuesToTry = semiringFeature.GetIncrementalSubset(result[i]);

                    // Get the corresponding usage matrix column vector
                    for (int j = 0; j < usageMatrix.length; j++) {
                        correspondingColumnVectorUsageMatrix[j] = usageMatrix[j][i];
                    }

                    for (SemiringObject valueToTry : valuesToTry) {

                        // Now that we have set our candidate value, we can get the corresponding vector which would contribute to the eventual supremum operation
                        // by ANDING our corresponding column vector from the usage matrix with a vector having a copy-paste of the current value.
                        // Practically, we can just do a min() over the correspondingColumnVector

                        for (int j = 0; j < dataColVectorLength; j++) {
                            resultAfterAnd[j] = semiringFeature.TightMultiplication(valueToTry, correspondingColumnVectorUsageMatrix[j]);
                        }

                        // Now we can do an OR with our incremental data vector to see how it's changed
                        for (int j = 0; j < dataColVectorLength; j++) {
                            resultAfterOr[j] = semiringFeature.Addition(resultAfterAnd[j], dataLengthVectorIncremental[j]);
                        }

                        // Now we can calculate the error for this changed data vector, and see how it compares
                        double newError = 0;
                        for (int j = 0; j < dataColVectorLength; j++) {
                            newError += semiringFeature.Dissimilarity(resultAfterOr[j], dataColumnVector[j]);
                        }

                        if (errorFromIncremental > (newError + errorEps)) {
                            errorFromIncremental = newError;
                            bestIndexSoFar = i;
                            bestValueSoFar = valueToTry;
                        }
                    }
                }

                if (bestIndexSoFar > -1) {
                    // Get the corresponding usage matrix column vector
                    for (int j = 0; j < dataColVectorLength; j++) {
                        correspondingColumnVectorUsageMatrix[j] = usageMatrix[j][bestIndexSoFar];
                    }

                    // Set our result value as well as the incremental vector
                    result[bestIndexSoFar] = bestValueSoFar;
                    for (int j = 0; j < dataColVectorLength; j++) {
                        resultAfterAnd[j] = semiringFeature.TightMultiplication(bestValueSoFar, correspondingColumnVectorUsageMatrix[j]);
                    }

                    // Now we can do an OR with our incremental data vector to see how it's changed
                    for (int j = 0; j < dataColVectorLength; j++) {
                        dataLengthVectorIncremental[j] = semiringFeature.Addition(resultAfterAnd[j], dataLengthVectorIncremental[j]);
                    }
                } else {
                    // No changes useful
                    break;
                }
            }

            return result;
        } else {
            int dataColVectorLength = dataColumnVector.length;
            int k = usageMatrix[0].length;

            // Allocate memory for our result
            SemiringObject[] result = new SemiringObject[k];

            // Initialize all entries to "zero"
            SemiringObject zeroElement = semiringFeature.GetIdentityElementForAddition();
            for (int i = 0; i < result.length; i++) {
                result[i] = zeroElement;
            }

            // Calculate the initial error
            double errorBest = 0;
            for (int i = 0; i < dataColVectorLength; i++) {
                SemiringObject scalarProductResult = semiringScalarProductCalculator.CalculateScalarProductWithTightMultiplication(result, usageMatrix[i], semiringFeature);
                errorBest += semiringFeature.Dissimilarity(dataColumnVector[i], scalarProductResult);
            }

            double delta = Double.NEGATIVE_INFINITY;
            while (delta < 0) {
                // Clone the optimum
                SemiringObject[] localOptimalVector = result.clone();

                double localError = errorBest;

                for (int i = 0; i < k; i++) {

                    List<SemiringObject> valuesToTry = semiringFeature.GetIncrementalSubset(result[i]);
                    SemiringObject[] vectorToModify = result.clone();

                    for (SemiringObject valueToTry : valuesToTry) {
                        vectorToModify[i] = valueToTry;
                        double errorAfterModification = 0;
                        for (int j = 0; j < dataColVectorLength; j++) {
                            SemiringObject scalarProductResult = semiringScalarProductCalculator.CalculateScalarProductWithTightMultiplication(vectorToModify, usageMatrix[j], semiringFeature);
                            errorAfterModification += semiringFeature.Dissimilarity(dataColumnVector[j], scalarProductResult);
                        }

                        if (errorAfterModification < localError) {
                            localOptimalVector = vectorToModify.clone();
                            localError = errorAfterModification;
                        }
                    }
                }

                delta = localError - errorBest;
                if (delta < 0) {
                    result = localOptimalVector.clone();
                    errorBest = localError;
                }
            }

            return result;
        }

    }

}
