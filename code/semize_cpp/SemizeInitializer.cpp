/* 
 * File:   SemizeInitializer.cpp
 * Author: sam
 * 
 * Created on April 28, 2015, 1:03 PM
 */

#include "SemizeInitializer.h"
#include "DiscreteVectorBitMagician.h"
#include <vector>
#include <algorithm>
#include <string.h>

SemizeInitializer::SemizeInitializer(Stochastic* stochastic) {
    this->stochastic = stochastic;    
}

SemizeInitializer::SemizeInitializer(const SemizeInitializer& orig) {
}

SemizeInitializer::~SemizeInitializer() {    
}

void SemizeInitializer::Initialize(DiscreteMatrix* dataMatrix, DiscreteMatrix* basisMatrix, DiscreteVectorStorageInfo* storageInfo) {

    std::vector<int> selectedBasisVectors;

    // Select the first basis vector at random
    selectedBasisVectors.push_back(this->stochastic->NextInt());

    unsigned long long* xorResult = new unsigned long long[storageInfo->numUlonglongElementsRequiredToRepresentVectorInTotal]();
    DiscreteVectorBitMagician* discreteVectorBitMagician = new DiscreteVectorBitMagician();
    while (selectedBasisVectors.size() < basisMatrix->numRows) {
        // Here we want to get the index of the data row vector that has the maximum difference to those selected
        int indexToAdd = -1;
        int maxDistance = -1;

        // Look through the collection and find the next one
        for (int i = 0; i < dataMatrix->numRows; i++) {
            
            // If we've already included this one, move on to the next
            if (std::find(selectedBasisVectors.begin(), selectedBasisVectors.end(), i) != selectedBasisVectors.end()) {
                continue;
            }

            unsigned long long* candidateBasisVector = dataMatrix->GetPointerToRow(i, storageInfo);

            int dissimilarity = 0;
            for (std::vector<int>::iterator it = selectedBasisVectors.begin(); it != selectedBasisVectors.end(); ++it) {
                unsigned long long* alreadySelectedVector = dataMatrix->GetPointerToRow(*it, storageInfo);
                discreteVectorBitMagician->BitwiseXor(candidateBasisVector, alreadySelectedVector, xorResult, storageInfo);                
                dissimilarity += discreteVectorBitMagician->PopCount(xorResult, storageInfo);
            }

            if (dissimilarity > maxDistance) {
                indexToAdd = i;
                maxDistance = dissimilarity;
            }
        }

        selectedBasisVectors.push_back(indexToAdd);
    }

    // Now we have the k vectors that we need
    // We can now set our basis matrix appropriately
    // Each vector of the basis matrix is of length k...each vector corresponds to a column of the basis matrix.
    for (int i = 0; i < basisMatrix->numRows; i++) {
        // Get the index of the data row we're using
        int dataRowIndex = selectedBasisVectors[i];
        memcpy(basisMatrix->GetPointerToRow(i, storageInfo), dataMatrix->GetPointerToRow(dataRowIndex, storageInfo), storageInfo->numBytesRequiredToRepresentOneRowVectorInTotal);        
    }
    
    delete[] xorResult;
    delete discreteVectorBitMagician;
}