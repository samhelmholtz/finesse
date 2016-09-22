/* 
 * File:   RandomizationRoundRunner.h
 * Author: sam
 *
 * Created on April 30, 2015, 9:06 AM
 */

#ifndef RANDOMIZATIONROUNDRUNNER_H
#define	RANDOMIZATIONROUNDRUNNER_H

#include "Semize.h"


class RandomizationRoundRunner {
public:
    RandomizationRoundRunner(Semize* semize);
    RandomizationRoundRunner(const RandomizationRoundRunner& orig);
    virtual ~RandomizationRoundRunner();
    void Run(DiscreteMatrix* dataMatrix, DiscreteMatrix* dataMatrixTransposed, DiscreteMatrix* usageMatrixTransposed, DiscreteMatrix* basisMatrix, double* objectiveMeasure, int numRandomizationRounds, DiscreteVectorStorageInfo* storageInfoForDataMatrix, DiscreteVectorStorageInfo* storageInfoForUsageMatrix, DiscreteMatrix* rowValueMasks, DiscreteMatrix* columnValueMasks);
private:
    Semize* semize;    
};

#endif	/* RANDOMIZATIONROUNDRUNNER_H */

