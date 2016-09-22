/* 
 * File:   RandomizationRoundRunner.cpp
 * Author: sam
 * 
 * Created on April 30, 2015, 9:06 AM
 */

#include <limits>
#include <string.h>

#include "RandomizationRoundRunner.h"
#include "limits.h"
#include "Semize.h"
#include <iostream>


RandomizationRoundRunner::RandomizationRoundRunner(Semize* semize) {
    this->semize = semize;
}

RandomizationRoundRunner::RandomizationRoundRunner(const RandomizationRoundRunner& orig) {
}

RandomizationRoundRunner::~RandomizationRoundRunner() {
}

void RandomizationRoundRunner::Run(DiscreteMatrix* dataMatrix, DiscreteMatrix* dataMatrixTransposed, DiscreteMatrix* usageMatrixTransposed, DiscreteMatrix* basisMatrix, double* objectiveMeasure, int numRandomizationRounds, DiscreteVectorStorageInfo* storageInfoForDataMatrix, DiscreteVectorStorageInfo* storageInfoForUsageMatrix, DiscreteMatrix* rowValueMasks, DiscreteMatrix* columnValueMasks){
    DiscreteMatrix* usageMatrixTransposedCopy = new DiscreteMatrix(usageMatrixTransposed->numRows, usageMatrixTransposed->numCols, storageInfoForUsageMatrix);
    DiscreteMatrix* basisMatrixCopy = new DiscreteMatrix(basisMatrix->numRows, basisMatrix->numCols, storageInfoForDataMatrix);
    double objectiveMeasureCopy;
    
    *objectiveMeasure = std::numeric_limits<double>::max();
    for (int i = 0; i < numRandomizationRounds; i++) {
        this->semize->Run(dataMatrix, dataMatrixTransposed, usageMatrixTransposedCopy, basisMatrixCopy, &objectiveMeasureCopy, storageInfoForDataMatrix, storageInfoForUsageMatrix, rowValueMasks, columnValueMasks);
        std::cout << "Iteration " << i << ": " << objectiveMeasureCopy;
        if(objectiveMeasureCopy < *objectiveMeasure){
            std::cout << " (best so far)";
            memcpy(usageMatrixTransposed->entries, usageMatrixTransposedCopy->entries, usageMatrixTransposed->numBytesUsedToRepresentAllEntries);
            memcpy(basisMatrix->entries, basisMatrixCopy->entries, basisMatrix->numBytesUsedToRepresentAllEntries);
            *objectiveMeasure = objectiveMeasureCopy;            
        }
        std::cout << std::endl;
    }
    
    delete usageMatrixTransposedCopy;
    delete basisMatrixCopy;
}