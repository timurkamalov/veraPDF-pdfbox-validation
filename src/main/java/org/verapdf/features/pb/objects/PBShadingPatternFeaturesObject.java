package org.verapdf.features.pb.objects;

import org.apache.pdfbox.pdmodel.graphics.pattern.PDShadingPattern;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.util.Set;

/**
 * Features object for shading pattern
 *
 * @author Maksim Bezrukov
 */
public class PBShadingPatternFeaturesObject implements IFeaturesObject {

    private static final String ID = "id";

    private PDShadingPattern shadingPattern;
    private String id;
    private String shadingChild;
    private String extGStateChild;
    private Set<String> pageParent;
    private Set<String> patternParent;
    private Set<String> xobjectParent;
    private Set<String> fontParent;

    /**
     * Constructs new tilling pattern features object
     *
     * @param shadingPattern - PDTilingPattern which represents tilling pattern for feature report
     * @param id             - id of the object
     * @param extGStateChild - external graphics state id which contains in this shading pattern
     * @param shadingChild   - shading id which contains in this shading pattern
     * @param pageParent     - set of page ids which contains the given extended graphics state as its resources
     * @param patternParent  - set of pattern ids which contains the given extended graphics state as its resources
     * @param xobjectParent  - set of xobject ids which contains the given extended graphics state as its resources
     * @param fontParent     - set of font ids which contains the given extended graphics state as its resources
     */
    public PBShadingPatternFeaturesObject(PDShadingPattern shadingPattern, String id, String shadingChild, String extGStateChild, Set<String> pageParent, Set<String> patternParent, Set<String> xobjectParent, Set<String> fontParent) {
        this.shadingPattern = shadingPattern;
        this.id = id;
        this.shadingChild = shadingChild;
        this.extGStateChild = extGStateChild;
        this.pageParent = pageParent;
        this.patternParent = patternParent;
        this.xobjectParent = xobjectParent;
        this.fontParent = fontParent;
    }

    /**
     * @return PATTERN instance of the FeaturesObjectTypesEnum enumeration
     */
    @Override
    public FeaturesObjectTypesEnum getType() {
        return FeaturesObjectTypesEnum.PATTERN;
    }

    /**
     * Reports featurereport into collection
     *
     * @param collection - collection for feature report
     * @return FeatureTreeNode class which represents a root node of the constructed collection tree
     * @throws FeaturesTreeNodeException - occurs when wrong features tree node constructs
     */
    @Override
    public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeaturesTreeNodeException {
        if (shadingPattern != null) {
            FeatureTreeNode root = FeatureTreeNode.newRootInstance("pattern");
            root.addAttribute(ID, id);
            root.addAttribute("type", "shading");

            parseParents(root);

            if (shadingChild != null) {
                FeatureTreeNode shading = FeatureTreeNode.newChildInstance("shading", root);
                shading.addAttribute(ID, shadingChild);
            }

            parseFloatMatrix(shadingPattern.getMatrix().getValues(), FeatureTreeNode.newChildInstance("matrix", root));

            if (extGStateChild != null) {
                FeatureTreeNode exGState = FeatureTreeNode.newChildInstance("graphicsState", root);
                exGState.addAttribute(ID, extGStateChild);
            }

            collection.addNewFeatureTree(FeaturesObjectTypesEnum.PATTERN, root);
            return root;
        }

        return null;
    }

    private void parseFloatMatrix(float[][] array, FeatureTreeNode parent) throws FeaturesTreeNodeException {
        for (int i = 0; i < array.length; ++i) {
            for (int j = 0; j < array.length - 1; ++j) {
                FeatureTreeNode element = FeatureTreeNode.newChildInstance("element", parent);
                element.addAttribute("row", String.valueOf(i));
                element.addAttribute("column", String.valueOf(j));
                element.addAttribute("value", String.valueOf(array[i][j]));
            }
        }
    }

    private void parseParents(FeatureTreeNode root) throws FeaturesTreeNodeException {
        if ((pageParent != null && !pageParent.isEmpty()) ||
                (patternParent != null && !patternParent.isEmpty()) ||
                (xobjectParent != null && !xobjectParent.isEmpty()) ||
                (fontParent != null && !fontParent.isEmpty())) {
            FeatureTreeNode parents = FeatureTreeNode.newChildInstance("parents", root);

            if (pageParent != null) {
                for (String id : pageParent) {
                    FeatureTreeNode node = FeatureTreeNode.newChildInstance("page", parents);
                    node.addAttribute(ID, id);
                }
            }
            if (patternParent != null) {
                for (String id : patternParent) {
                    FeatureTreeNode node = FeatureTreeNode.newChildInstance("pattern", parents);
                    node.addAttribute(ID, id);
                }
            }
            if (xobjectParent != null) {
                for (String id : xobjectParent) {
                    FeatureTreeNode node = FeatureTreeNode.newChildInstance("xobject", parents);
                    node.addAttribute(ID, id);
                }
            }
            if (fontParent != null) {
                for (String id : fontParent) {
                    FeatureTreeNode node = FeatureTreeNode.newChildInstance("font", parents);
                    node.addAttribute(ID, id);
                }
            }
        }
    }
}
