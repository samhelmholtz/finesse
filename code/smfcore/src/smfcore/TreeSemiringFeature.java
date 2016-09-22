/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smfcore;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author sam
 */
public class TreeSemiringFeature extends SemiringFeatureBase {

    public static final String NAME = "Tree";
    private final String _masterTreeFilename;
    private final Tree _masterTree;
    private final Gson _gson;

    
    public TreeSemiringFeature(Tree masterTree, String masterTreeFilename){
        _gson = new Gson();
        _masterTree = masterTree;
        _masterTreeFilename = masterTreeFilename;
    }

    public Tree GetMasterTree(){
        return _masterTree;
    }

    @Override
    public boolean HasTightMultiplication() {
        return false;
    }

    @Override
    public SemiringObject GetIdentityElementForAddition() {
        return new TreeSemiringObject(new HashSet<String>());
    }

    @Override
    public SemiringObject Addition(SemiringObject first, SemiringObject second) {
        // Union
        TreeSemiringObject firstTree = (TreeSemiringObject) first;
        TreeSemiringObject secondTree = (TreeSemiringObject) second;
        Set<String> clonedFirst = new HashSet<>(firstTree.TreeValue);
        clonedFirst.addAll(secondTree.TreeValue);
        return new TreeSemiringObject(clonedFirst);
    }

    @Override
    public SemiringObject TightMultiplication(SemiringObject first, SemiringObject second) {
        throw new UnsupportedOperationException("Not supported."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double Dissimilarity(SemiringObject expected, SemiringObject reconstructed) {
        TreeSemiringObject expectedTree = (TreeSemiringObject) expected;
        TreeSemiringObject reconstructedTree = (TreeSemiringObject) reconstructed;
        Set<String> copyForIntersection = new HashSet<>(expectedTree.TreeValue);
        Set<String> copyForUnion = new HashSet<>(expectedTree.TreeValue);
        copyForIntersection.retainAll(reconstructedTree.TreeValue); // intersection
        copyForUnion.addAll(reconstructedTree.TreeValue); // union
        if(copyForUnion.size() == 0){
            return 0;
        }
        return 1.0 - ((double) copyForIntersection.size() / (double) copyForUnion.size());
    }

    @Override
    public List<SemiringObject> GetIncrementalSubset(SemiringObject baseObject) {
        // We expect here that the base object is quite small, i.e. not many nodes in our latent trees

        // Our goal here is to return an array of trees. These are all the trees with a single-child addition to the base.
        // The first step is to find all the leaf nodes.
        // Once we have a collection of leaf nodes, we simply need to clone the base object n times, and add one of the n children to each clone.
        // Step 1: Find the leaf nodes. We can do this by traversing the tree and stopping on the nodes which have no children in our set.
        // First case: handle the empty tree.
        TreeSemiringObject baseTree = (TreeSemiringObject) baseObject;
        Set<String> baseTreeValue = baseTree.TreeValue;
        if (baseTreeValue.isEmpty()) {
            Set<String> rootSet = new HashSet<>();
            rootSet.add(_masterTree.Id);
            return Arrays.asList((SemiringObject) new TreeSemiringObject(rootSet));
        }

        // Get those nodes that are a direct child of the base tree
        Set<String> childrenOfLeafNodes = new HashSet<>();
        AppendLeafNodes(baseTreeValue, childrenOfLeafNodes, _masterTree);

        // Now build the collection of next trees to try by appending each of those children to the base tree
        List<SemiringObject> incrementalSubset = new ArrayList<>();
        for (String childOfLeafNode : childrenOfLeafNodes) {
            Set<String> cloneOfBaseObject = new HashSet<>(baseTreeValue);
            cloneOfBaseObject.add(childOfLeafNode);
            incrementalSubset.add(new TreeSemiringObject(cloneOfBaseObject));
        }

        return incrementalSubset;
    }

    private void AppendLeafNodes(Set<String> baseObject, Set<String> candidateIds, Tree currentNode) {
        HashMap<String, Tree> currentNodeChildren = currentNode.Children;
        for (Map.Entry<String, Tree> entrySet : currentNodeChildren.entrySet()) {
            String key = entrySet.getKey();
            Tree childTree = entrySet.getValue();
            if(!baseObject.contains(key)){
                candidateIds.add(key);
            } else {
                // Keep traversing
                AppendLeafNodes(baseObject, candidateIds, childTree);
            }
        }
    }

    @Override
    public String Name() {
        return NAME;
    }

    @Override
    public SemiringObject DeserializeObject(String value) {
        String[] nodes = _gson.fromJson(value, String[].class);
        return new TreeSemiringObject(new HashSet<String>(Arrays.asList(nodes)));
    }

    @Override
    public String Serialize() {
        return NAME + "," + (_masterTreeFilename == null ? "THIS FEATURE WAS CONSTRUCTED WITHOUT SPECIFYING THE FILENAME OF THE MASTER ONTOLOGY." : _masterTreeFilename);
    }
}
