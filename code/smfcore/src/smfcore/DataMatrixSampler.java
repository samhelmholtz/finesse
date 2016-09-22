/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smfcore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sam
 */
public class DataMatrixSampler implements IDataMatrixSampler {

    private IStochastic stochastic;

    public DataMatrixSampler(IStochastic stochastic) {

        this.stochastic = stochastic;
    }

    @Override
    public SemiringObject[][] SampleK(SemiringObject[][] population, int k, ISemiringFeature[] semirings) {

//        // Naive approach: select the first k
//        int m = population[0].length;
//
//        // Allocate memory for our sample
//        SemiringObject[][] result = new SemiringObject[m][k];
//        for (int i = 0; i < k; i++) {
//            SemiringObject[] basisVector = population[i];
//            for (int j = 0; j < m; j++) {
//                result[j][i] = basisVector[j];
//            }
//        }
//
//        return result;

        int n = population.length;
        int m = population[0].length;

        // Allocate memory for our sample
        SemiringObject[][] result = new SemiringObject[m][k];
        List<Integer> selectedBasisVectors = new ArrayList<>();

        // Select the first at random
        selectedBasisVectors.add(stochastic.NextInt(population.length));

        // Select the next ones based on max Hamming distance to the existing ones
        while (selectedBasisVectors.size() < k) {
            selectedBasisVectors.add(GetIndexOfVectorWithMaxDifference(population, selectedBasisVectors, semirings));
        }

        // Now we have the k vectors that we need.
        // Set them as the columns of our transformed basis matrix (i.e. the rows of our normal basis matrix)
        for (int i = 0; i < k; i++) {
            SemiringObject[] basisVector = population[selectedBasisVectors.get(i)];
            for (int j = 0; j < m; j++) {
                result[j][i] = basisVector[j];
            }
        }

        return result;
    }

    private int GetIndexOfVectorWithMaxDifference(SemiringObject[][] population, List<Integer> indicesAlreadySelected, ISemiringFeature[] semiringFeatures) {
        int indexToAdd = -1;
        double maxDistance = -1;
        for (int i = 0; i < population.length; i++) {
            if (indicesAlreadySelected.contains(i)) {
                continue;
            }

            SemiringObject[] candidateBasisVector = population[i];

            double dissimilarity = 0;
            for (Integer integer : indicesAlreadySelected) {
                SemiringObject[] alreadySelectedVector = population[integer];

                for (int j = 0; j < population[0].length; j++) {
                    dissimilarity += semiringFeatures[j].Dissimilarity(candidateBasisVector[j], alreadySelectedVector[j]);
                }
            }

            if (dissimilarity > maxDistance) {
                indexToAdd = i;
                maxDistance = dissimilarity;
            }
        }

        return indexToAdd;
    }
}
