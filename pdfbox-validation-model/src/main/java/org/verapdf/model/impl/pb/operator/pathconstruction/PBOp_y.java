package org.verapdf.model.impl.pb.operator.pathconstruction;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosNumber;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.operator.Op_y;

import java.util.List;

/**
 * Operator which appends a cubic Bézier curve to the current path
 *
 * @author Timur Kamalov
 */
public class PBOp_y extends PBOpPathConstruction implements Op_y {

	/** Type name for {@code PBOp_y} */
    public static final String OP_Y_TYPE = "Op_y";

    public PBOp_y(List<COSBase> arguments) {
        super(arguments, OP_Y_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (CONTROL_POINTS.equals(link)) {
            return this.getControlPoints();
        }
        return super.getLinkedObjects(link);
    }

    private List<CosNumber> getControlPoints() {
        return this.getListOfNumbers();
    }

}
