package com.mca.pdfgen.beans.pdf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.geom.Rectangle;
import com.mca.pdfgen.constants.AppConstants;

import lombok.Data;

@Data
public final class PDFSignatureLocationInfo 
{
	private static final String SIGFIELD = "sigfield";
    private static final Logger LOGGER = LoggerFactory.getLogger(PDFSignatureLocationInfo.class);

    private int pageNumber;
    private Rectangle location;
    private String  locationLabelTxt;
    private String  signFieldLabelTxt;

    public PDFSignatureLocationInfo(final int pageNo, final Rectangle rect, final int fieldIndex,
                                    final String label, final String suffix)
    {
        super();
        
        LOGGER.debug("Field Label = {}, fieldIndex = {} Suffix = {}", label, fieldIndex, suffix);

        pageNumber = pageNo;
        locationLabelTxt = label;
        signFieldLabelTxt = SIGFIELD + fieldIndex + AppConstants.UNDERSCORE + suffix;

        // In legacy system, width and height are hard-coded to 50 & 20, using same values
        location = new Rectangle(rect.getX() - 1, rect.getY() - 1, 50, 20);
    }
}
