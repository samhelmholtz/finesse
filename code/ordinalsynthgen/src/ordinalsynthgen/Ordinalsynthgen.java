/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ordinalsynthgen;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import smfcore.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * @author sam
 */
public class Ordinalsynthgen {

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // Arguments
        // n: number of rows in data matrix (default 8000 in paper)
        // m: number of columns in data matrix (default 100 in paper)
        // k: num cols of usage matrix = num rows of basis matrix (default 16 in paper)
        // o: noise percent (integer)...we do the division by 100 in the code (default 10 in paper)
        // u: By default this parameter is not used. If, however, you wish to generate a data matrix based on a ground-truth usage matrix that takes
        // entries from the FULL scale, set this to the "rho" value desired for the distribution in that matrix (e.g. -1.1 as with the basis matrix rho).
        // This parameter wasn't ever used in the paper.
        // b: The "rho" in the paper. This controls the probability distribution for basis matrix. Lower values give "sparser" data matrices (right-skewed). Default -1.1 in the paper.
        // s: number of elements in the scale (including zero). Default 7 in the paper
        // f: output filename
        // r: format for output file: 0 for normal format (useful for Asso and GreEss), 1 for SMF format
        // l: lambda value, the average number of TRUE entries in a row of the usage matrix (only relevant if a is 1). Default of 3 in the paper
        // a: 1 if the usage matrix should be binary, otherwise 0. Default of 1 in the paper.


        Options options = new Options();
        options.addOption("n", true, "Option n");
        options.addOption("m", true, "Option m");
        options.addOption("k", true, "Option k");
        options.addOption("o", true, "Option o");
        options.addOption("u", true, "Option u");
        options.addOption("b", true, "Option b");
        options.addOption("s", true, "Option s");
        options.addOption("f", true, "Option f");
        options.addOption("r", true, "Option r");
        options.addOption("l", true, "Option l");
        options.addOption("a", true, "Option a");


        CommandLineParser parser = new ExtendedGnuParser(true);
        CommandLine cmd = parser.parse(options, args);

        int n = Integer.parseInt(cmd.getOptionValue("n"));
        int m = Integer.parseInt(cmd.getOptionValue("m"));
        int k = Integer.parseInt(cmd.getOptionValue("k"));
        short noisePercent = Short.parseShort(cmd.getOptionValue("o"));
        double usageProbabilityDistribution = Double.parseDouble(cmd.getOptionValue("u"));
        double basisProbabilityDistribution = Double.parseDouble(cmd.getOptionValue("b"));
        int numElementsInScale = Short.parseShort(cmd.getOptionValue("s"));
        boolean enforceBinary = Integer.parseInt(cmd.getOptionValue("a")) > 0;
        int averageNumberTrueEntriesInUsageMatrix = Integer.parseInt(cmd.getOptionValue("l"));
        String outputFilePath = cmd.getOptionValue("f");
        int outputFormat = Integer.parseInt(cmd.getOptionValue("r"));

        IStochastic stochastic = new Stochastic();
        OrdinalSyntheticMatrixGenerator gen = new OrdinalSyntheticMatrixGenerator(stochastic);

        SemiringObject[][] matrix;
        if (enforceBinary) {
            matrix = gen.GenerateWithBooleanUsageMatrix(n, m, k, numElementsInScale, basisProbabilityDistribution, averageNumberTrueEntriesInUsageMatrix, noisePercent);
        } else {
            matrix = gen.GenerateWithSemiringUsageMatrix(n, m, k, numElementsInScale, basisProbabilityDistribution, usageProbabilityDistribution, noisePercent);
        }


//        // temp: assume the matrix is binary and generate using normal probabilities.
//        double basisMatrixProbability = 0.1;
//        double usageMatrixProbability = 4.0 / k;
//
//        // Now we can build the clean basis matrix (transposed)
//        SemiringObject[][] basisMatrixTransposed = new SemiringObject[m][k];
//
//        for (int i = 0; i < k; i++) {
//            for (int j = 0; j < m; j++) {
//                double r = stochastic.NextUniformlyDistributedDouble();
//                basisMatrixTransposed[j][i] = new OrdinalSemiringObject(r < basisMatrixProbability ? (int) 1 : (int) 0);
//            }
//        }
//
//        // And build the clean usage matrix (not transposed)
//        SemiringObject[][] usageMatrix = new SemiringObject[n][k];
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < k; j++) {
//                double r = stochastic.NextUniformlyDistributedDouble();
//                usageMatrix[i][j] = new OrdinalSemiringObject(r < usageMatrixProbability ? (int) 1 : (int) 0);
//            }
//        }
//
//        SemiringObject[][] resultMatrix = new SemiringObject[n][m]; // semiringMatrixProductCalculator.CalculateMatrixProductWithTightMultiplication(usageMatrix, basisMatrix, semiringFeatures);
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < m; j++) {
//                int entryResult = 0;
//                for (int l = 0; l < k; l++) {
//                    SemiringObject first = usageMatrix[i][l];
//                    SemiringObject second = basisMatrixTransposed[j][l];
//                    int sum = ((OrdinalSemiringObject) first).IntegerValue + ((OrdinalSemiringObject) second).IntegerValue - (numElementsInScale - 1);
//                    int multResult = sum > 0 ? sum : 0;
//                    entryResult = entryResult > multResult ? entryResult : multResult;
//                }
//                resultMatrix[i][j] = new OrdinalSemiringObject(entryResult);
//            }
//        }


        // Generate the features
        ISemiringFeature[] semiringFeatures = new ISemiringFeature[m];
        for (
                int i = 0;
                i < m; i++)

        {
            semiringFeatures[i] = enforceBinary ? new OrdinalBinUSemiringFeature((int) numElementsInScale) : new OrdinalSemiringFeature((int) numElementsInScale);
        }

        // Get the distribution counts (for debugging purposes)
        int[] scaleValueCounts = new int[numElementsInScale];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int scaleValue = ((OrdinalSemiringObject) matrix[i][j]).IntegerValue;
                scaleValueCounts[scaleValue]++;
            }
        }
        for (int i = 0; i < scaleValueCounts.length; i++) {
            sb.append(scaleValueCounts[i] + (i == scaleValueCounts.length - 1 ? "" : ","));
        }

        boolean debug = false;
        if (debug) {
            PrintWriter writer = new PrintWriter(new FileOutputStream(new File("/home/sam/tmp/dist.txt"), false));
            writer.println(sb.toString());
            writer.close();
        } else {
            System.out.println("Distribution counts (from smallest scale value to the largest. Sum of all counts is " + (n * m) + "): " + sb.toString());
        }

        if (outputFormat == 1)

        {
            new SemiringMatrixIo().WriteToSmfFile(matrix, semiringFeatures, outputFilePath, false);
        } else

        {
            new SemiringMatrixIo().WriteToOrdinalFile(matrix, outputFilePath, numElementsInScale);
        }
    }
}
