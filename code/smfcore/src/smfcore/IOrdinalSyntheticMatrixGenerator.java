/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package smfcore;

/**
 *
 * @author sam
 */
public interface IOrdinalSyntheticMatrixGenerator {
    SemiringObject[][] GenerateWithSemiringUsageMatrix(int n, int m, int k, int numElementsInScale, double probDistBasis, double probDistUsage, int noisePercent);
    SemiringObject[][] GenerateWithBooleanUsageMatrix(int n, int m, int k, int numElementsInScale, double probDistBasis, int numTrueValuesPerRowOfUsageMatrix, int noisePercent);
}
