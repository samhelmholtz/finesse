/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smf;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import smfcore.*;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.commons.cli.ParseException;

/**
 *
 * @author sam
 */
public class Smf {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        Options options = new Options();
        options.addOption("k", true, "Option k");
        options.addOption("s", true, "Option s");
        options.addOption("b", true, "Option b");
        options.addOption("D", true, "Option D");

        CommandLineParser parser = new ExtendedGnuParser(true);
        CommandLine cmd = parser.parse(options, args);

        int k = Integer.parseInt(cmd.getOptionValue("k"));
        String basisFilePath = cmd.getOptionValue("b");
        String usageFilePath = cmd.getOptionValue("D");
        String dataFilePath = cmd.getOptionValue("s");

        // Instantiate all our dependencies
        IStochastic stochastic = new Stochastic();
        ISemiringScalarProductCalculator semiringScalarProductCalculator = new SemiringScalarProductCalculator();
        IDataMatrixSampler dataMatrixSampler = new DataMatrixSampler(stochastic);
        IGreedyCoverUsage greedyCoverUsage = new GreedyCoverUsage(semiringScalarProductCalculator);
        IGreedyCoverBasis greedyCoverBasis = new GreedyCoverBasis(semiringScalarProductCalculator);
        IUsageMatrixSetTypeDeterminer usageMatrixSetTypeDeterminer = new UsageMatrixSetTypeDeterminer();
        ISemiringMatrixErrorCalculator semiringMatrixErrorCalculator = new SemiringMatrixErrorCalculator(semiringScalarProductCalculator);
        ISemize semize = new Semize(dataMatrixSampler, greedyCoverUsage, greedyCoverBasis, usageMatrixSetTypeDeterminer, semiringScalarProductCalculator, semiringMatrixErrorCalculator);
        ISemizeRandomizationRoundRunner semizeRandomizationRoundRunner = new SemizeRandomizationRoundRunner(semize);
        ISemiringMatrixIo semiringMatrixIo = new SemiringMatrixIo();

        SmfFileReadResult dataFileReadResult = semiringMatrixIo.ReadFromSmfFile(dataFilePath, false);

        UsageBasisMatrixPair usageBasisMatrixPair = semizeRandomizationRoundRunner.RunSemizeForPRandomizationRoundsAndGetBestResult(dataFileReadResult.Matrix, dataFileReadResult.SemiringFeatures, k, 20).UsageBasisMatrixPair;

        boolean usageMatrixCannotUseFeatureObjects = usageMatrixSetTypeDeterminer.UsageMatrixCannotUseFeatureObjects(dataFileReadResult.SemiringFeatures);
        ISemiringFeature usageMatrixSemiringFeature = usageMatrixCannotUseFeatureObjects ? new CommonSemiringFeature() : dataFileReadResult.SemiringFeatures[0];

        ISemiringFeature[] usageMatrixSemiringFeatures = new ISemiringFeature[k];
        for (int i = 0; i < k; i++) {
            usageMatrixSemiringFeatures[i] = usageMatrixSemiringFeature;
        }

        semiringMatrixIo.WriteToSmfFile(usageBasisMatrixPair.UsageMatrix, usageMatrixSemiringFeatures, usageFilePath, false);
        semiringMatrixIo.WriteToSmfFile(usageBasisMatrixPair.BasisMatrixTransposed, dataFileReadResult.SemiringFeatures, basisFilePath,true);
    }
}
