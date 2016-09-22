package smfcore;

import java.util.*;

/**
 * Created by sam on 23.01.15.
 */
public class TreeSyntheticMatrixGenerator implements ITreeSyntheticMatrixGenerator {

    private IStochastic stochastic;
    private ISemiringMatrixProductCalculator semiringMatrixProductCalculator;

    public TreeSyntheticMatrixGenerator(IStochastic stochastic, ISemiringMatrixProductCalculator semiringMatrixProductCalculator) {
        this.stochastic = stochastic;
        this.semiringMatrixProductCalculator = semiringMatrixProductCalculator;
    }

    @Override
    public Tree GenerateMasterTree(int depth, int nary) {
        // Now generate the master ontology.
        Tree masterRootNode = new Tree("0");
        AppendNChildrenToDepthD(masterRootNode, nary, 1, depth);
        return masterRootNode;
    }

    @Override
    public SemiringObject[][] Generate(Tree masterTree, int n, int m, int k, int lambda, int averageNumberOfNodesSampledForTreeEntries, int noise) {

        // First the usage matrix...that's simple
        boolean[][] usageMatrix = new boolean[n][k];

        // The density of 'true' values is controlled by lambda.
        double probabilityForTrueInUsageMatrix = (double)lambda / (double)k;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                usageMatrix[i][j] = stochastic.NextUniformlyDistributedDouble() < probabilityForTrueInUsageMatrix;
            }
        }

        // Now that we have the master ontology, get a list of the nodes
        HashSet<String> nodeIds = new HashSet<>();
        AppendChildNodeIdsToSet(masterTree, nodeIds);

        // Now for each matrix entry we choose a subset of averageNumberOfNodesSampledForTreeEntries these nodes (i.e. choose without replacement). We then find
        // all nodes that are required to "fill in" the smallest connected tree containing these nodes, and we set that tree as the matrix value
        SemiringObject[][] basisMatrixTransposed = new SemiringObject[m][k];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < k; j++) {
                // Copy to list, then shuffle, then take first averageNumberOfNodesSampledForTreeEntries
                ArrayList<String> nodesList = new ArrayList<>(nodeIds);
                Collections.shuffle(nodesList);
                for (int l = nodesList.size()-1; l >= averageNumberOfNodesSampledForTreeEntries ; l--) {
                    nodesList.remove(l);
                }

                // Now we need to find the smallest tree that includes all of these nodes.
                // We include a node if it's in our collection, or an ancestor of one in our collection
                HashSet<String> valueTreeNodes = new HashSet<>();
                AppendNodesEqualToOrParentsOfGivenIds(masterTree, new HashSet<String>(nodesList), valueTreeNodes);
                basisMatrixTransposed[i][j] = new TreeSemiringObject(valueTreeNodes);
            }
        }

        ISemiringFeature[] semiringFeatures = new ISemiringFeature[m];
        for (int i = 0; i < m; i++) {
            semiringFeatures[i] = new TreeSemiringFeature(masterTree, null);
        }

        SemiringObject[][] product = semiringMatrixProductCalculator.CalculateMatrixProductWithLooseMultiplication(usageMatrix, basisMatrixTransposed, semiringFeatures);

        // Now add noise.
        double noiseFraction = (double)noise / 100.0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if(stochastic.NextUniformlyDistributedDouble() < noiseFraction){
                    // Equal random choice between a full tree and an empty tree
                    product[i][j] = stochastic.NextUniformlyDistributedDouble() < 0.5 ? new TreeSemiringObject(new HashSet<String>()) : new TreeSemiringObject(nodeIds);
                }
            }
        }

        return product;
    }

    private void AppendNodesEqualToOrParentsOfGivenIds(Tree baseTree, Set<String> ids, Set<String> resultCollection){
        if(IsOrHasDescendantIn(baseTree, ids)) {
            resultCollection.add(baseTree.Id);
            for(Map.Entry<String, Tree> entry : baseTree.Children.entrySet()){
                AppendNodesEqualToOrParentsOfGivenIds(entry.getValue(), ids, resultCollection);
            }
        }
    }

    private boolean IsOrHasDescendantIn(Tree tree, Set<String> candidates){
        if(candidates.contains(tree.Id))
            return true;

        for(Map.Entry<String, Tree> entry : tree.Children.entrySet()){
            if(IsOrHasDescendantIn(entry.getValue(), candidates)) return true;
        }
        return false;
    }

    private void AppendChildNodeIdsToSet(Tree tree, Set<String> nodeSet){
        nodeSet.add(tree.Id);
        for(Map.Entry<String, Tree> entry : tree.Children.entrySet()){
            AppendChildNodeIdsToSet(entry.getValue(), nodeSet);
        }
    }

    private void AppendNChildrenToDepthD(Tree tree, int n, int depthOfTree, int stopAtDepth){
        if(depthOfTree >= stopAtDepth)
            return;

        for (int i = 0; i < n; i++) {
            Tree child = new Tree(tree.Id + "_" + i);
            tree.PutChild(child);
            AppendNChildrenToDepthD(child, n, depthOfTree+1, stopAtDepth);
        }
    }
}
