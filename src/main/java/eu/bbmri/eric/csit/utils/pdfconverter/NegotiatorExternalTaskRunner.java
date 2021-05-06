package eu.bbmri.eric.csit.utils.pdfconverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NegotiatorExternalTaskRunner {

    private static final Logger logger = LogManager.getLogger(NegotiatorExternalTaskRunner.class);

    public static void main(String[] args) {
        logger.info("Starting Negotiator External Task Runner");
        DocToPDF FileConversionWatcher = new DocToPDF();
        String watchFolder = args[0];
        FileConversionWatcher.initilaceDirectory(watchFolder);
        FileConversionWatcher.startWatcher(watchFolder);
    }
}
