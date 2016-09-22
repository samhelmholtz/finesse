package smfcore;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by sam on 20.01.15.
 */
public class SemiringMatrixIo implements ISemiringMatrixIo {
    @Override
    public void WriteToSmfFile(SemiringObject[][] matrix, ISemiringFeature[] semiringFeatures, String filename, boolean writeTheTransposeInstead) throws Exception {

        // File format: first, the dimensions of the matrix, one on each line, e.g.
        // 80
        // 50
        // ...for a 80-row and 50-column matrix
        //
        // Then, the first feature type on the next line, with any "constructor" arguments required after it, separated by commas, e.g.
        // Ordinal,5
        // OR
        // Tree,"/home/user/masterTree.txt"
        // ...for an ordinal feature with 5 scale elements (including zero), or a tree feature with master tree given in the file specified
        //
        // Then, the values for that feature (n of them), one on each line, e.g.
        // 5
        // 3
        // 0
        // ...for an ordinal feature with 6 scale values, or...
        // {JSON representation of tree nodes}
        // {JSON representation of tree nodes}
        // ... for a tree feature

        StringBuilder stringBuilder = new StringBuilder();

        if(writeTheTransposeInstead){
            // Here we're assuming that the semiring features correspond to the rows in the given matrix
            if(semiringFeatures.length != matrix.length)
                throw new Exception("Num semiring features incorrect");

            stringBuilder.append(matrix[0].length + "\n");
            stringBuilder.append(matrix.length + "\n");

            // Iterate over each feature, printing the feature information and then the feature content (which is a row in the given matrix)
            for (int i = 0; i < matrix.length; i++) {
                stringBuilder.append(semiringFeatures[i].Serialize() + "\n");
                for (int j = 0; j < matrix[0].length; j++) {
                    stringBuilder.append(matrix[i][j].GetStringRepresentation() + "\n");
                }
            }
        } else{
            // Here we're assuming that the semiring features correspond to the columns in the given matrix
            if(semiringFeatures.length != matrix[0].length)
                throw new Exception("Num semiring features incorrect");

            stringBuilder.append(matrix.length + "\n");
            stringBuilder.append(matrix[0].length + "\n");

            // Iterate over each feature, printing the feature information and then the feature content (which is a column in the given matrix)
            for (int i = 0; i < semiringFeatures.length; i++) {
                stringBuilder.append(semiringFeatures[i].Serialize() + "\n");
                for (int j = 0; j < matrix.length; j++) {
                    stringBuilder.append(matrix[j][i].GetStringRepresentation() + "\n");
                }
            }
        }

        // Dump the string to the file
        PrintWriter writer = new PrintWriter(filename, "UTF-8");
        writer.println(stringBuilder.toString());
        writer.close();
    }

    @Override
    public void WriteToOrdinalFile(SemiringObject[][] matrix, String filename, Integer numScaleElements) throws Exception {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(matrix.length + "\n");
        stringBuilder.append(matrix[0].length + "\n");
        stringBuilder.append(numScaleElements + "\n");
        // Non-SMF format. Here we assume that the feature metadata is known to the consumer. We just print it directly
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                stringBuilder.append(matrix[i][j].GetStringRepresentation() + " ");
            }
            stringBuilder.append("\n");
        }

        // Dump the string to the file
        PrintWriter writer = new PrintWriter(filename, "UTF-8");
        writer.println(stringBuilder.toString());
        writer.close();
    }

    @Override
    public SmfFileReadResult ReadFromSmfFile(String filename, boolean readIntoTranspose) throws Exception {
        SmfFileReadResult result = new SmfFileReadResult();
        Scanner scanner = new Scanner(new File(filename));
        int n = Integer.parseInt(scanner.nextLine());
        int m = Integer.parseInt(scanner.nextLine());

        SemiringObject[][] matrix;
        if(readIntoTranspose){
            matrix = new SemiringObject[m][n];
        } else{
            matrix = new SemiringObject[n][m];
        }

        ISemiringFeature[] semiringFeatures = new ISemiringFeature[m];
        HashMap<String, Tree> masterTrees = new HashMap<>();

        // Now we loop through the features
        for (int i = 0; i < m; i++) {
            String featureLine = scanner.nextLine();
            String[] featureSplit = featureLine.split(",");
            ISemiringFeature semiringFeature = null;
            if(featureSplit[0].equals(TernarySemiringFeature.NAME)){
                semiringFeature = new TernarySemiringFeature();
            } else if(featureSplit[0].equals(BooleanSemiringFeature.NAME)){
                semiringFeature = new BooleanSemiringFeature();
            } else if(featureSplit[0].equals(OrdinalSemiringFeature.NAME)){
                semiringFeature = new OrdinalSemiringFeature(Integer.parseInt(featureSplit[1]));
            } else if(featureSplit[0].equals(OrdinalBinUSemiringFeature.NAME)){
                semiringFeature = new OrdinalBinUSemiringFeature(Integer.parseInt(featureSplit[1]));
            } else if(featureSplit[0].equals(TreeSemiringFeature.NAME)){
                String masterTreeFilename = featureSplit[1];
                if(masterTrees.containsKey(masterTreeFilename)){
                    semiringFeature = new TreeSemiringFeature(masterTrees.get(masterTreeFilename), masterTreeFilename);
                } else{
                    Tree masterTree = Tree.FromJson(masterTreeFilename);
                    semiringFeature = new TreeSemiringFeature(masterTree, masterTreeFilename);
                    masterTrees.put(masterTreeFilename, masterTree);
                }
            } else if(featureSplit[0].equals(CommonSemiringFeature.NAME)){
                semiringFeature = new CommonSemiringFeature();
            } else{
                throw new Exception("Can't support semiring type of " + featureSplit[0]);
            }
            semiringFeatures[i] = semiringFeature;

            // Now just parse all the values
            for (int j = 0; j < n; j++) {
                if(readIntoTranspose){
                    matrix[i][j] = semiringFeature.DeserializeObject(scanner.nextLine());
                } else{
                    matrix[j][i] = semiringFeature.DeserializeObject(scanner.nextLine());
                }
            }
        }

        scanner.close();
        result.Matrix = matrix;
        result.SemiringFeatures = semiringFeatures;
        return result;
    }

    @Override
    public SemiringObject[][] ReadFromOrdinalFile(String filename) throws Exception {
        Scanner scanner = new Scanner(new File(filename));
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int numScaleElements = scanner.nextInt(); // not used
        SemiringObject[][] result = new SemiringObject[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                result[i][j] = new OrdinalSemiringObject(scanner.nextInt());
            }
        }

        scanner.close();
        return result;
    }

    @Override
    public int GetNumScaleElementsFromOrdinalFile(String filename) throws Exception {
        Scanner scanner = new Scanner(new File(filename));
        scanner.nextInt();
        scanner.nextInt();
        int numScaleElements = scanner.nextInt();
        scanner.close();
        return numScaleElements;
    }
}
