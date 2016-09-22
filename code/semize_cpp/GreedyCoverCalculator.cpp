/* 
 * File:   GreedyCoverCalculator.cpp
 * Author: sam
 * 
 * Created on April 24, 2015, 11:52 AM
 */

#include "GreedyCoverCalculator.h"
#include <string.h>
#include <cstdlib>
#include "omp.h"
#include <stdio.h>
#include <iostream>

GreedyCoverCalculator::GreedyCoverCalculator() {
    this->discreteVectorBitMagician = new DiscreteVectorBitMagician();
}

GreedyCoverCalculator::GreedyCoverCalculator(const GreedyCoverCalculator& orig) {
}

GreedyCoverCalculator::~GreedyCoverCalculator() {
    delete this->discreteVectorBitMagician;
}

int GreedyCoverCalculator::CalculateGreedyCover(unsigned long long* dataVector, DiscreteMatrix* matrixBeingOptimized, DiscreteMatrix* otherFactorMatrix, int indexOfColumnBeingOptimized, DiscreteMatrix* valueMasks, DiscreteVectorStorageInfo* storageInfoForVectorsWithSameLengthAsData, DiscreteVectorStorageInfo* storageInfoForMatrixBeingOptimizedVectors, bool binaryOnly) {

    // Initialize our solution to zero
    int* vectorVHoldingTheNewlyOptimizedColumn = new int[matrixBeingOptimized->numRows](); // the parentheses initialize to zero

    // Get the initial error, which is simply the sum of the values in the data vector
    int e_b = this->discreteVectorBitMagician->PopCount(dataVector, storageInfoForVectorsWithSameLengthAsData);

    // Initialize an 'incremental' copy of our data vector, which represents the result
    // of multiplying our solution vector with the other matrix
    // We know this is zero at the start, because our solution vector is zero
    // Here again we use parentheses to initialize the vector to zero
    unsigned long long* incrementalVector = new unsigned long long[storageInfoForVectorsWithSameLengthAsData->numUlonglongElementsRequiredToRepresentVectorInTotal]();

    // Here two temporary full-length vectors which we use as intermediate vectors in the following loops
    unsigned long long* w = new unsigned long long[storageInfoForVectorsWithSameLengthAsData->numUlonglongElementsRequiredToRepresentVectorInTotal]();
    unsigned long long* resultantDataVectorAfterCandidateChange = new unsigned long long[storageInfoForVectorsWithSameLengthAsData->numUlonglongElementsRequiredToRepresentVectorInTotal]();
    unsigned long long* xorResult = new unsigned long long[storageInfoForVectorsWithSameLengthAsData->numUlonglongElementsRequiredToRepresentVectorInTotal]();

    int delta = -1;
    while (delta < 0) {

        // Assume we don't do anything useful in this iteration
        delta = 0;

        // Here we want to make a SINGLE change to V
        // There is a max of k*numScaleValues ways we can do this
        int mostProfitableIndex = -1;
        int mostProfitableValue = -1;

        for (int i = 0; i < matrixBeingOptimized->numRows; i++) {

            for (int v = vectorVHoldingTheNewlyOptimizedColumn[i] + 1; v < storageInfoForVectorsWithSameLengthAsData->numDiscreteValues; v++) {

                // If we're only looking for a binary matrix, we can skip all but the top value
                // Todo: this can be done more efficiently
                if(binaryOnly && (v < (storageInfoForVectorsWithSameLengthAsData->numDiscreteValues - 1))){
                    continue;
                }
                
                // Here our candidate index is i, and our candidate value is v. That is, we're considering seting vectorVHoldingTheNewlyOptimizedColumn[i] = v;
                // The first thing we need to do is AND the corresponding full-length vector from the other matrix with this value
                unsigned long long* pointerToRowIOfOtherFactorMatrix = otherFactorMatrix->GetPointerToRow(i, storageInfoForVectorsWithSameLengthAsData);
                unsigned long long* maskForValueV = valueMasks->GetPointerToRow(v, storageInfoForVectorsWithSameLengthAsData);
                this->discreteVectorBitMagician->BitwiseAnd(pointerToRowIOfOtherFactorMatrix, maskForValueV, w, storageInfoForVectorsWithSameLengthAsData);

                // Now the question is: How have we incremented our resultant data vector (which would be obtained by multiplying our solution vector with the other matrix)
                // with this new change?
                // To find out, we can just do an OR
                this->discreteVectorBitMagician->BitwiseOr(incrementalVector, w, resultantDataVectorAfterCandidateChange, storageInfoForVectorsWithSameLengthAsData);

                // Now we can see what the new error is if we were to persist this change
                // This is simply the XOR between the result and the data vector, followed by a popcount                
                this->discreteVectorBitMagician->BitwiseXor(resultantDataVectorAfterCandidateChange, dataVector, xorResult, storageInfoForVectorsWithSameLengthAsData);
                int newError = this->discreteVectorBitMagician->PopCount(xorResult, storageInfoForVectorsWithSameLengthAsData);

                // If it's profitable, mark this candidate as the best
                int newDelta = newError - e_b;
                if (newDelta < 0) {
                    delta = newDelta;
                    e_b = newError;
                    mostProfitableIndex = i;
                    mostProfitableValue = v;                    
                }
            }
        }

        // Now we can set the change we want in our end result
        if (mostProfitableIndex > -1) {
            vectorVHoldingTheNewlyOptimizedColumn[mostProfitableIndex] = mostProfitableValue;

            // Get the new incremental vector
            this->discreteVectorBitMagician->BitwiseAnd(otherFactorMatrix->GetPointerToRow(mostProfitableIndex, storageInfoForVectorsWithSameLengthAsData), valueMasks->GetPointerToRow(mostProfitableValue, storageInfoForVectorsWithSameLengthAsData), w, storageInfoForVectorsWithSameLengthAsData);
            this->discreteVectorBitMagician->BitwiseOr(incrementalVector, w, resultantDataVectorAfterCandidateChange, storageInfoForVectorsWithSameLengthAsData);
            memcpy(incrementalVector, resultantDataVectorAfterCandidateChange, storageInfoForVectorsWithSameLengthAsData->numBytesRequiredToRepresentOneRowVectorInTotal);
        }
    }

    // Now that we're done, we have our optimized result in vectorVHoldingTheNewlyOptimizedColumn
    // We can now set the appropriate column of matrixInFocus with these values
    for (int i = 0; i < otherFactorMatrix->numRows; i++) {
        matrixBeingOptimized->SetValue(i, indexOfColumnBeingOptimized, vectorVHoldingTheNewlyOptimizedColumn[i], storageInfoForMatrixBeingOptimizedVectors);
    }

    delete[] w;
    delete[] vectorVHoldingTheNewlyOptimizedColumn;
    delete[] incrementalVector;
    delete[] xorResult;
    delete[] resultantDataVectorAfterCandidateChange;

    // Return the error with respect to the data vector
    return e_b;
}