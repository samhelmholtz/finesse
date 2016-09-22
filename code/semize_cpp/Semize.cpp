/* 
 * File:   Semize.cpp
 * Author: sam
 * 
 * Created on April 24, 2015, 1:49 PM
 */

#include "Semize.h"
#include <limits>
#include <string.h>
#include "omp.h"
#include "iostream"

Semize::Semize(Stochastic* stochastic, bool enforceBinaryUsageMatrix) {
    this->stochastic = stochastic;
    this->enforceBinaryUsageMatrix = enforceBinaryUsageMatrix;
}

Semize::Semize(const Semize& orig) {
}

Semize::~Semize() {    
}

void Semize::Run(DiscreteMatrix* dataMatrix, DiscreteMatrix* dataMatrixTransposed, DiscreteMatrix* usageMatrixTransposed, DiscreteMatrix* basisMatrix, double* objectiveMeasure, DiscreteVectorStorageInfo* storageInfoForVectorsOfLengthM, DiscreteVectorStorageInfo* storageInfoForVectorsOfLengthN, DiscreteMatrix* rowValueMasks, DiscreteMatrix* columnValueMasks) {

    SemizeInitializer* semizeInitializer = new SemizeInitializer(this->stochastic);
    semizeInitializer->Initialize(dataMatrix, basisMatrix, storageInfoForVectorsOfLengthM);
    GreedyCoverCalculator* greedyCoverCalculator = new GreedyCoverCalculator();

    // Create local copies of our usage and basis matrix before we go changing them
    DiscreteMatrix* usageMatrixTransposedCopy = new DiscreteMatrix(usageMatrixTransposed->numRows, usageMatrixTransposed->numCols, storageInfoForVectorsOfLengthN);
    DiscreteMatrix* basisMatrixCopy = new DiscreteMatrix(basisMatrix->numRows, basisMatrix->numCols, storageInfoForVectorsOfLengthM);

    // Alternate/iterate in EM-fashion
    int delta = -1;
    int errorBest = std::numeric_limits<int>::max();
    while (delta < 0) {

        // Copy the best solution so far into the 
        memcpy(usageMatrixTransposedCopy->entries, usageMatrixTransposed->entries, usageMatrixTransposed->numBytesUsedToRepresentAllEntries);
        memcpy(basisMatrixCopy->entries, basisMatrix->entries, basisMatrix->numBytesUsedToRepresentAllEntries);

        // Assume we don't do anything useful in this iteration
        delta = 0;
        int error = 0;

        // Solve for each "usage row" (which are indeed columns of usageMatrixTransposed) independently
#pragma omp parallel for
        for (int i = 0; i < dataMatrix->numRows; i++) {
            greedyCoverCalculator->CalculateGreedyCover(dataMatrix->GetPointerToRow(i, storageInfoForVectorsOfLengthM), usageMatrixTransposedCopy, basisMatrixCopy, i, rowValueMasks , storageInfoForVectorsOfLengthM, storageInfoForVectorsOfLengthN, enforceBinaryUsageMatrix);
        }
        
        // Solve for each "basis column" (which are indeed columns of basisMatrix) independently
        // Here we calculate the error column-wise as we optimize
#pragma omp parallel for reduction(+:error)
        for (int i = 0; i < dataMatrix->numCols; i++) {
            error += greedyCoverCalculator->CalculateGreedyCover(dataMatrixTransposed->GetPointerToRow(i, storageInfoForVectorsOfLengthN), basisMatrixCopy, usageMatrixTransposedCopy, i, columnValueMasks, storageInfoForVectorsOfLengthN, storageInfoForVectorsOfLengthM, false);
        }

        if (error < errorBest) {
            delta = error - errorBest;
            errorBest = error;
            memcpy(usageMatrixTransposed->entries, usageMatrixTransposedCopy->entries, usageMatrixTransposed->numBytesUsedToRepresentAllEntries);
            memcpy(basisMatrix->entries, basisMatrixCopy->entries, basisMatrix->numBytesUsedToRepresentAllEntries);
        }
    }

    delete usageMatrixTransposedCopy;
    delete basisMatrixCopy;
    delete semizeInitializer;
    delete greedyCoverCalculator;

    *objectiveMeasure = (double) (errorBest) / (storageInfoForVectorsOfLengthM->numDiscreteValues - 1);
}