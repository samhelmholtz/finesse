/* 
 * File:   SemizeInitializer.h
 * Author: sam
 *
 * Created on April 28, 2015, 1:03 PM
 */

#ifndef SEMIZEINITIALIZER_H
#define	SEMIZEINITIALIZER_H

#include "Stochastic.h"
#include "DiscreteMatrix.h"


class SemizeInitializer {
public:
    SemizeInitializer(Stochastic* stochastic);
    SemizeInitializer(const SemizeInitializer& orig);
    virtual ~SemizeInitializer();
    void Initialize(DiscreteMatrix* dataMatrix, DiscreteMatrix* basisMatrix, DiscreteVectorStorageInfo* storageInfo);
private:
    Stochastic* stochastic;
};

#endif	/* SEMIZEINITIALIZER_H */

