/**
 * Created with IntelliJ IDEA.
 * User: cliff
 * Date: 10/06/13
 * Time: 22:05
 * To change this template use File | Settings | File Templates.
 */

import au.com.bytecode.opencsv.CSVWriter;
import org.apache.pdfbox.ExtractText;
import org.apache.pdfbox.PDFReader;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.*;

public class PreProcess
{
    String InputFileFolder;
    String myLoc;
    String OutputFileFolder;
    String LDAScalaLearnScript;
    String LDAScalaInferScript;
    String FileFolder;
    String LDAtype;
    String Slash;

    public PreProcess(String InputFileFolderi, String myLoci, String OutputFileFolderi, String FileFolderi, String Slashi)
    {
        InputFileFolder=InputFileFolderi;
        myLoc=myLoci;
        OutputFileFolder=OutputFileFolderi;
        //LDAScalaLearnScript=LDAScalaLearnScripti;
        //LDAScalaInferScript=LDAScalaInferScripti;
        FileFolder=FileFolderi;
        //LDAtype=LDAtypei;
        Slash=Slashi;
    }

    public String[][] run()
    {
        String[][] mainoutput=null;
        try
        {
            File f = new File(InputFileFolder);
            File[] matchingFiles = f.listFiles();
            mainoutput = new String[matchingFiles.length][9];
            String fileExtension = "";

            String[] tempres1 = new String[5];
            String[] tempres2 = new String[2];

            CSVWriter csvout = new CSVWriter(new FileWriter(FileFolder + Slash + "stats.csv"));
            for(int j=0 ; j<matchingFiles.length ; j++)
            {
                String corpus = "";
                System.out.println("Reading file " + matchingFiles[j] + " ... ");

                if(matchingFiles[j].getName().contains(".txt"))
                {
                    BufferedReader reader = new BufferedReader(new FileReader(matchingFiles[j]));
                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        corpus = corpus + line;
                    }
                    fileExtension = ".txt";
                }

                if(matchingFiles[j].getName().contains(".pdf"))
                {
                    PDFParser parser;
                    String parsedText=null;
                    PDFTextStripper pdfStripper = null;
                    PDDocument pdDoc = null;
                    COSDocument cosDoc = null;

                    parser = new PDFParser(new FileInputStream(matchingFiles[j]));
                    parser.parse();
                    cosDoc = parser.getDocument();
                    pdfStripper = new PDFTextStripper();
                    pdDoc = new PDDocument(cosDoc);
                    pdfStripper.setStartPage(1);
                    pdfStripper.setEndPage(5);
                    parsedText = pdfStripper.getText(pdDoc);

                    cosDoc.close();
                    pdDoc.close();

                    corpus=parsedText;
                    fileExtension = ".pdf";
                }


                String InputFileName = matchingFiles[j].toString();
                System.out.println(corpus);
                mainoutput[j][0]=InputFileName.replace(InputFileFolder + Slash, "").replace(fileExtension, "");
                StanCoreNLP corenlp = new StanCoreNLP(corpus, myLoc, OutputFileFolder, InputFileName.replace(InputFileFolder + Slash, "").replace(fileExtension, ""), Slash);
                tempres1=corenlp.run();

                String csvPath = corenlp.getcsvPath();


                String LDAType="lda";
                LDAModel lda = new LDAModel(csvPath, FileFolder, mainoutput[j][0], LDAType, Slash);
                lda.learn("1-lda-learn.scala");
                lda.infer("2-lda-infer.scala");

                AnalyseResults res = new AnalyseResults(FileFolder, mainoutput[j][0], LDAType, Slash);
                tempres2=res.getMeanAndSD();

                mainoutput[j][1]=tempres1[0];
                mainoutput[j][2]=tempres1[1];
                mainoutput[j][3]=tempres1[2];
                mainoutput[j][4]=tempres1[3];
                mainoutput[j][5]=tempres1[4];
                mainoutput[j][6]=tempres2[0];
                mainoutput[j][7]=tempres2[1];
                mainoutput[j][8]=LDAType;

                for(String[] t : mainoutput)
                {
                    csvout.writeNext(t);
                }

                deleteFilesInFolder(FileFolder + Slash + "out", ".gz");

                LDAType="llda";
                LDAModel llda = new LDAModel(csvPath, FileFolder, mainoutput[j][0], LDAType, Slash);
                llda.learn("1-llda-learn.scala");
                llda.infer("2-llda-infer.scala");

                res = new AnalyseResults(FileFolder, mainoutput[j][0], LDAType, Slash);
                tempres2=res.getMeanAndSD();

                mainoutput[j][1]=tempres1[0];
                mainoutput[j][2]=tempres1[1];
                mainoutput[j][3]=tempres1[2];
                mainoutput[j][4]=tempres1[3];
                mainoutput[j][5]=tempres1[4];
                mainoutput[j][6]=tempres2[0];
                mainoutput[j][7]=tempres2[1];
                mainoutput[j][8]=LDAType;

                for(String[] t : mainoutput)
                {
                    csvout.writeNext(t);
                }

                deleteFilesInFolder(FileFolder + Slash + "out", ".gz");

                LDAType="plda";
                LDAModel plda = new LDAModel(csvPath, FileFolder, mainoutput[j][0], LDAType, Slash);
                plda.learn("1-plda-learn.scala");
                plda.infer("2-plda-infer.scala");

                res = new AnalyseResults(FileFolder, mainoutput[j][0], LDAType, Slash);
                tempres2=res.getMeanAndSD();

                mainoutput[j][1]=tempres1[0];
                mainoutput[j][2]=tempres1[1];
                mainoutput[j][3]=tempres1[2];
                mainoutput[j][4]=tempres1[3];
                mainoutput[j][5]=tempres1[4];
                mainoutput[j][6]=tempres2[0];
                mainoutput[j][7]=tempres2[1];
                mainoutput[j][8]=LDAType;

                for(String[] t : mainoutput)
                {
                    csvout.writeNext(t);
                }

                deleteFilesInFolder(FileFolder + Slash + "out", ".gz");
            }
            csvout.close();
        }
        catch (Exception ex)
        {
            System.out.println("Error:-" + ex.toString() + ", " + ex.getMessage() + ", " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return mainoutput;
    }

    private void deleteFilesInFolder(String folder, String containing)
    {
        File f2 = new File(folder);
        File[] matchingFiles2 = f2.listFiles();

        for(File fi : matchingFiles2)
        {
            if(fi.getName().contains(containing))
            {
                fi.delete();
            }
        }
    }
}
