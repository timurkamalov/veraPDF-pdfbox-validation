package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosFileSpecification;
import org.verapdf.model.external.EmbeddedFile;
import org.verapdf.model.impl.pb.external.PBoxEmbeddedFile;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represent a specific type of Dictionary - File Specification Dictionary.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosFileSpecification extends PBCosDict implements
        CosFileSpecification {

    /** Type name for PBCosFileSpecification */
    public static final String COS_FILE_SPECIFICATION_TYPE = "CosFileSpecification";

	public static final String EF = "EF";

	private final String f;
	private final String uf;
	private final String afrelationship;

    /**
     * Default constructor
     * @param dictionary pdfbox COSDictionary
     */
    public PBCosFileSpecification(COSDictionary dictionary, PDDocument document, PDFAFlavour flavour) {
        super(dictionary, COS_FILE_SPECIFICATION_TYPE, document, flavour);
		this.f = this.getStringValue(COSName.F);
		this.uf = this.getStringValue(COSName.UF);
		this.afrelationship = this.getNameValue(COSName.getPDFName("AFRelationship"));
    }

	@Override
	public String getF() {
		return this.f;
	}

	@Override
	public String getUF() {
		return this.uf;
	}

	@Override
	public String getAFRelationship() {
		return this.afrelationship;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		if (EF.equals(link)) {
			return this.getEFFile();
		}
		return super.getLinkedObjects(link);
	}

	private List<EmbeddedFile> getEFFile() {
		COSBase efDictionary = ((COSDictionary) this.baseObject)
				.getDictionaryObject(COSName.EF);
		if (efDictionary instanceof COSDictionary) {
			ArrayList<EmbeddedFile> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(new PBoxEmbeddedFile((COSDictionary) efDictionary));
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}

	private String getStringValue(COSName key) {
		COSBase value = ((COSDictionary) this.baseObject).getDictionaryObject(key);
		return value instanceof COSString ? ((COSString) value).getString() : null;
	}

	private String getNameValue(COSName key) {
		COSBase value = ((COSDictionary) this.baseObject).getDictionaryObject(key);
		return value instanceof COSName ? ((COSName) value).getName() : null;
	}
}
