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
public class GreedyCoverUsage implements IGreedyCoverUsage {

    private ISemiringScalarProductCalculator semiringScalarProductCalculator;
    private boolean useNewApproach;

    public GreedyCoverUsage(ISemiringScalarProductCalculator semiringScalarProductCalculator) {
        this.semiringScalarProductCalculator = semiringScalarProductCalculator;
        this.useNewApproach = true;
    }

    @Override
    public boolean[] GreedyCoverUsageWithLooseConjunction(SemiringObject[] dataRowVector, SemiringObject[][] basisMatrixTransposed, ISemiringFeature[] semiringFeatures) {

        if (useNewApproach) {
            // The error can only be a multiple of 1/n, where n is the length of the data column vector.
            // We hence calculate 1/(2n) for solving the floating-point value comparison problem
            double errorEps = 1.0 / (2 * dataRowVector.length);

            // Copy the usage matrix into a boolean matrix for performance reasons
            int dataRowVectorLength = dataRowVector.length;
            int k = basisMatrixTransposed[0].length;

            // Allocate memory for our result
            boolean[] result = new boolean[k];

            // Initialize all entries to "zero"
            for (int i = 0; i < result.length; i++) {
                result[i] = false;
            }

            SemiringObject[] dataLengthVectorIncremental = new SemiringObject[dataRowVectorLength];
            for (int i = 0; i < dataRowVectorLength; i++) {
                dataLengthVectorIncremental[i] = semiringFeatures[i].GetIdentityElementForAddition();
            }

            // Some temp vectors for intermeditate computation
            SemiringObject[] resultAfterAnd = new SemiringObject[dataRowVectorLength];
            SemiringObject[] resultAfterOr = new SemiringObject[dataRowVectorLength];
            SemiringObject[] correspondingRowVectorBasisMatrix = new SemiringObject[dataRowVectorLength];

            while (true) {

                double errorFromIncremental = 0;
                for (int i = 0; i < dataRowVectorLength; i++) {
                    errorFromIncremental += semiringFeatures[i].Dissimilarity(dataLengthVectorIncremental[i], dataRowVector[i]);
                }

                int bestIndexSoFar = -1;
                boolean bestValueSoFar = false;

                for (int i = 0; i < k; i++) {

                    if (result[i]) {
                        continue; // no more to try
                    }

                    // Get the corresponding usage matrix column vector
                    for (int j = 0; j < dataRowVectorLength; j++) {
                        correspondingRowVectorBasisMatrix[j] = basisMatrixTransposed[j][i];
                    }

                    // Now that we have set our candidate value, we can get the corresponding vector which would contribute to the eventual supremum operation
                    // by ANDING our corresponding column vector from the usage matrix with a vector having a copy-paste of the current value.
                    // Practically, we can just do a min() over the correspondingColumnVector

                    for (int j = 0; j < dataRowVectorLength; j++) {
                        resultAfterAnd[j] = semiringFeatures[j].LooseMultiplication(true, correspondingRowVectorBasisMatrix[j]);
                    }

                    // Now we can do an OR with our incremental data vector to see how it's changed
                    for (int j = 0; j < dataRowVectorLength; j++) {
                        resultAfterOr[j] = semiringFeatures[j].Addition(resultAfterAnd[j], dataLengthVectorIncremental[j]);
                    }

                    // Now we can calculate the error for this changed data vector, and see how it compares
                    double newError = 0;
                    for (int j = 0; j < dataRowVectorLength; j++) {
                        newError += semiringFeatures[j].Dissimilarity(resultAfterOr[j], dataRowVector[j]);
                    }

                    if (errorFromIncremental > (newError + errorEps)) {
                        errorFromIncremental = newError;
                        bestIndexSoFar = i;
                        bestValueSoFar = true;
                    }

                }

                if (bestIndexSoFar > -1) {
                    // Get the corresponding usage matrix column vector
                    for (int j = 0; j < dataRowVectorLength; j++) {
                        correspondingRowVectorBasisMatrix[j] = basisMatrixTransposed[j][bestIndexSoFar];
                    }

                    // Set our result value as well as the incremental vector
                    result[bestIndexSoFar] = bestValueSoFar;
                    for (int j = 0; j < dataRowVectorLength; j++) {
                        resultAfterAnd[j] = semiringFeatures[j].LooseMultiplication(bestValueSoFar, correspondingRowVectorBasisMatrix[j]);
                    }

                    // Now we can do an OR with our incremental data vector to see how it's changed
                    for (int j = 0; j < dataRowVectorLength; j++) {
                        dataLengthVectorIncremental[j] = semiringFeatures[j].Addition(resultAfterAnd[j], dataLengthVectorIncremental[j]);
                    }
                } else {
                    // No changes useful
                    break;
                }

            }

            return result;
        } else {
            int dataRowVectorLength = dataRowVector.length;
            int k = basisMatrixTransposed[0].length;

            // Allocate memory for our result
            boolean[] result = new boolean[k];

            // Initialize all entries to "zero"
            for (int i = 0; i < result.length; i++) {
                result[i] = false;
            }

            // Calculate the initial error
            double errorBest = 0;
            for (int i = 0; i < dataRowVectorLength; i++) {
                SemiringObject scalarProductResult = semiringScalarProductCalculator.CalculateScalarProductWithLooseMultiplication(result, basisMatrixTransposed[i], semiringFeatures[i]);
                errorBest += semiringFeatures[i].Dissimilarity(dataRowVector[i], scalarProductResult);
            }

            double delta = Double.NEGATIVE_INFINITY;
            while (delta < 0) {
                // Clone the optimum
                boolean[] localOptimalVector = result.clone();
                double localError = errorBest;

                for (int i = 0; i < k; i++) {
                    if (result[i]) {
                        continue; // no further values to try
                    }
                    boolean[] vectorToModify = result.clone();
                    vectorToModify[i] = true;

                    double errorAfterModification = 0;
                    for (int j = 0; j < dataRowVectorLength; j++) {
                        SemiringObject scalarProductResult = semiringScalarProductCalculator.CalculateScalarProductWithLooseMultiplication(vectorToModify, basisMatrixTransposed[j], semiringFeatures[j]);
                        errorAfterModification += semiringFeatures[j].Dissimilarity(dataRowVector[j], scalarProductResult);
                    }

                    if (errorAfterModification < localError) {
                        localOptimalVector = vectorToModify.clone();
                        localError = errorAfterModification;
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
    public SemiringObject[] GreedyCoverUsageWithTightConjunction(SemiringObject[] dataRowVector, SemiringObject[][] basisMatrixTransposed, ISemiringFeature[] semiringFeatures) {

        if(useNewApproach){
            // The error can only be a multiple of 1/n, where n is the length of the data column vector.
            // We hence calculate 1/(2n) for solving the floating-point value comparison problem
            double errorEps = 1.0 / (2 * dataRowVector.length);

            int dataRowVectorLength = dataRowVector.length;
            int k = basisMatrixTransposed[0].length;

            ISemiringFeature exampleSemiringFeature = semiringFeatures[0];

            // Allocate memory for our result
            SemiringObject[] result = new SemiringObject[k];

            // Initialize all entries to "zero"
            for (int i = 0; i < result.length; i++) {
                result[i] = exampleSemiringFeature.GetIdentityElementForAddition();
            }

            SemiringObject[] dataLengthVectorIncremental = new SemiringObject[dataRowVectorLength];
            for (int i = 0; i < dataRowVectorLength; i++) {
                dataLengthVectorIncremental[i] = semiringFeatures[i].GetIdentityElementForAddition();
            }

            // Some temp vectors for intermeditate computation
            SemiringObject[] resultAfterAnd = new SemiringObject[dataRowVectorLength];
            SemiringObject[] resultAfterOr = new SemiringObject[dataRowVectorLength];
            SemiringObject[] correspondingRowVectorBasisMatrix = new SemiringObject[dataRowVectorLength];

            while (true) {

                double errorFromIncremental = 0;
                for (int i = 0; i < dataRowVectorLength; i++) {
                    errorFromIncremental += semiringFeatures[i].Dissimilarity(dataLengthVectorIncremental[i], dataRowVector[i]);
                }

                int bestIndexSoFar = -1;
                SemiringObject bestValueSoFar = null;

                for (int i = 0; i < k; i++) {

                    List<SemiringObject> valuesToTry = exampleSemiringFeature.GetIncrementalSubset(result[i]);

                    // Get the corresponding basis matrix row vector
                    for (int j = 0; j < dataRowVectorLength; j++) {
                        correspondingRowVectorBasisMatrix[j] = basisMatrixTransposed[j][i];
                    }

                    for (SemiringObject valueToTry : valuesToTry) {

                        // Now that we have set our candidate value, we can get the corresponding vector which would contribute to the eventual supremum operation
                        // by ANDING our corresponding column vector from the usage matrix with a vector having a copy-paste of the current value.
                        // Practically, we can just do a min() over the correspondingColumnVector

                        for (int j = 0; j < dataRowVectorLength; j++) {
                            resultAfterAnd[j] = semiringFeatures[j].TightMultiplication(valueToTry, correspondingRowVectorBasisMatrix[j]);
                        }

                        // Now we can do an OR with our incremental data vector to see how it's changed
                        for (int j = 0; j < dataRowVectorLength; j++) {
                            resultAfterOr[j] = semiringFeatures[j].Addition(resultAfterAnd[j], dataLengthVectorIncremental[j]);
                        }

                        // Now we can calculate the error for this changed data vector, and see how it compares
                        double newError = 0;
                        for (int j = 0; j < dataRowVectorLength; j++) {
                            newError += semiringFeatures[j].Dissimilarity(resultAfterOr[j], dataRowVector[j]);
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
                    for (int j = 0; j < dataRowVectorLength; j++) {
                        correspondingRowVectorBasisMatrix[j] = basisMatrixTransposed[j][bestIndexSoFar];
                    }

                    // Set our result value as well as the incremental vector
                    result[bestIndexSoFar] = bestValueSoFar;
                    for (int j = 0; j < dataRowVectorLength; j++) {
                        resultAfterAnd[j] = semiringFeatures[j].TightMultiplication(bestValueSoFar, correspondingRowVectorBasisMatrix[j]);
                    }

                    // Now we can do an OR with our incremental data vector to see how it's changed
                    for (int j = 0; j < dataRowVectorLength; j++) {
                        dataLengthVectorIncremental[j] = semiringFeatures[j].Addition(resultAfterAnd[j], dataLengthVectorIncremental[j]);
                    }
                } else {
                    // No changes useful
                    break;
                }

            }

            return result;
        } else{
            int dataRowVectorLength = dataRowVector.length;
            int k = basisMatrixTransposed[0].length;

            ISemiringFeature exampleSemiringFeature = semiringFeatures[0];

            // Allocate memory for our result
            SemiringObject[] result = new SemiringObject[k];

            // Initialize all entries to "zero"
            SemiringObject zeroElement = exampleSemiringFeature.GetIdentityElementForAddition();
            for (int i = 0; i < result.length; i++) {
                result[i] = zeroElement;
            }

            // Calculate the initial error
            double errorBest = 0;
            for (int i = 0; i < dataRowVectorLength; i++) {
                SemiringObject scalarProductResult = semiringScalarProductCalculator.CalculateScalarProductWithTightMultiplication(result, basisMatrixTransposed[i], semiringFeatures[i]);
                errorBest += semiringFeatures[i].Dissimilarity(dataRowVector[i], scalarProductResult);
            }

            double delta = Double.NEGATIVE_INFINITY;
            while (delta < 0) {
                // Clone the optimum
                SemiringObject[] localOptimalVector = result.clone();

                double localError = errorBest;

                for (int i = 0; i < k; i++) {

                    List<SemiringObject> valuesToTry = exampleSemiringFeature.GetIncrementalSubset(result[i]);
                    SemiringObject[] vectorToModify = result.clone();

                    for (SemiringObject valueToTry : valuesToTry) {
                        vectorToModify[i] = valueToTry;
                        double errorAfterModification = 0;
                        for (int j = 0; j < dataRowVectorLength; j++) {
                            SemiringObject scalarProductResult = semiringScalarProductCalculator.CalculateScalarProductWithTightMultiplication(vectorToModify, basisMatrixTransposed[j], semiringFeatures[j]);
                            errorAfterModification += semiringFeatures[j].Dissimilarity(dataRowVector[j], scalarProductResult);
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
