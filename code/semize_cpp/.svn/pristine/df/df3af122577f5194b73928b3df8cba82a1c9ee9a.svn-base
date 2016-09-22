/* 
 * File:   GreedyCoverCalculator.h
 * Author: sam
 *
 * Created on April 24, 2015, 11:52 AM
 */

#ifndef GREEDYCOVERCALCULATOR_H
#define	GREEDYCOVERCALCULATOR_H

#include "DiscreteMatrix.h"
#include "DiscreteVectorBitMagician.h"


class GreedyCoverCalculator {
public:
    GreedyCoverCalculator();
    GreedyCoverCalculator(const GreedyCoverCalculator& orig);
    virtual ~GreedyCoverCalculator();
    int CalculateGreedyCover(unsigned long long* dataVector, DiscreteMatrix* matrixBeingOptimized, DiscreteMatrix* otherFactorMatrix, int indexOfColumnBeingOptimized, DiscreteMatrix* valueMasks, DiscreteVectorStorageInfo* storageInfoForDataVector, DiscreteVectorStorageInfo* storageInfoForMatrixBeingOptimized, bool binaryOnly);
private:
    DiscreteVectorBitMagician* discreteVectorBitMagician;
};

#endif	/* GREEDYCOVERCALCULATOR_H */

