package eu.bbmri.eric.csit.utils.pdfconverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import java.io.*;
import java.nio.file.*;

public class DocToPDF {

    private static final Logger logger = LogManager.getLogger(DocToPDF.class);

    public void initilaceDirectory(String folderPath) {
        logger.info("Start initial folder convertion: " + folderPath);
        try {

            FilenameFilter filterDocxFiles = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    return name.endsWith(".docx");
                }
            };

            File folder = new File(folderPath);
            File[] files = folder.listFiles(filterDocxFiles);
            for(File file : files) {
                File checkIfPdfExists = new File(folderPath, file.getName() + ".pdf");
                if(!checkIfPdfExists.exists()) {
                    convertFile(folderPath, file.getName());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void startWatcher(String folderPath) {
        logger.info("Start watcher for folder: " + folderPath);
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(folderPath);
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    String affectedFile = event.context().toString();
                    logger.info("File event detected for: " + affectedFile);
                    if(affectedFile.endsWith(".docx")) {
                        convertFile(folderPath, affectedFile);
                    }
                }
                key.reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void convertFile(String path, String inputFileName) {
        try (InputStream is = new FileInputStream(new File(path, inputFileName));
             OutputStream out = new FileOutputStream(new File(path, inputFileName + ".pdf"));) {
            // 1) Load DOCX into XWPFDocument
            XWPFDocument document = new XWPFDocument(is);
            //XSSFWorkbook
            // 2) Prepare Pdf options
            PdfOptions options = PdfOptions.create();
            // 3) Convert XWPFDocument to Pdf
            PdfConverter.getInstance().convert(document, out, options);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}