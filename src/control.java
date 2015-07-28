/**
 * Created with IntelliJ IDEA.
 * User: cliff
 * Date: 10/06/13
 * Time: 21:48
 * To change this template use File | Settings | File Templates.
 */

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;

public class control
{
    public static void main(String[] args)
    {
        String Slash="\\";

        String homeamd = "G:\\Shared_2\\Cliff\\Dropbox\\Dropbox";
        String college = "C:\\Users\\coreila\\Dropbox";
        String macosx = "/Users/cliff/Desktop/Dropbox";
        String macwin8 = "C:\\Users\\cliffo\\Dropbox";
        String adhoc = "G:\\Shared_2\\Cliff\\MSD\\adhocexperiments\\v4\\i19";
        String myLoc = homeamd;
        String thisLoc = Slash + "MSD" + Slash + "Tech" + Slash + "codename" + Slash + "v4" + Slash + "i16";
        //String FileFolder = myLoc + thisLoc;
        String FileFolder = adhoc;
        String InputFileFolder = FileFolder + Slash + "in";
        String OutputFileFolder = FileFolder +  Slash + "out";
        //String LDAFileFolder = FileFolder +  Slash + "llda";

        String[][] mainoutput=null;

        try
        {
            long start = new Date().getTime();

            PreProcess pp = new PreProcess(InputFileFolder, myLoc, OutputFileFolder, FileFolder, Slash);
            mainoutput=pp.run();

            /*
            CSVWriter csvout = new CSVWriter(new FileWriter(FileFolder + "\\stats.csv"));
            String[] headings = new String[8];
            headings[0]="filename";
            headings[1]="sentencecount";
            headings[2]="framecount";
            headings[3]="dependencycount";
            headings[4]="corefpropercount";
            headings[5]="corefpronominalcount";
            headings[6]="mean";
            headings[7]="variance";
            csvout.writeNext(headings);

            for(String[] t : mainoutput)
            {
                csvout.writeNext(t);
            }

            csvout.close();

            /*
            List<String[]> tempout = new ArrayList<String[]>();
            String[] tempres = new String[2];

            String tempfolder="G:\\Shared_2\\Cliff\\MSD\\adhocexperiments\\v4\\i7";
            try
            {

                File f = new File(tempfolder + "\\out");
                File[] matchingFiles = f.listFiles();

                //String filefolder2 = "G:\\Shared_2\\Cliff\\MSD\\adhocexperiments\\v4\\i7";
                for(int j=0 ; j<matchingFiles.length ; j++)
                {
                    String filename = matchingFiles[j].getName().replace(tempfolder + "\\", "").replace(".csv", "").replace("output_", "");
                    String[] tempouti=new String[3];

                    //System.out.println(matchingFiles[j].getName());
                    //System.out.println(tempfolder);
                    String thisFile=tempfolder + "\\out\\" + matchingFiles[j].getName().toString();

                    LDAModel adhoclda = new LDAModel(thisFile, tempfolder, filename, LDAType);
                    adhoclda.learn(LLDAScalaLearnScript);
                    adhoclda.infer(LLDAScalaInferScript);

                    AnalyseResults res = new AnalyseResults(tempfolder, filename, LDAType);
                    tempres=res.getMeanAndSD();

                    System.out.println(filename + "," + tempres[0] + "," + tempres[1]);
                    tempouti[0]=filename;
                    tempouti[1]=tempres[0];
                    tempouti[2]=tempres[1];
                    tempout.add(tempouti);
                }

                CSVWriter csvout = new CSVWriter(new FileWriter(tempfolder + "\\stats1.csv"));
                for(String[] t : tempout)
                {
                    csvout.writeNext(t);
                }
                csvout.close();


            }
            catch (Exception ex)
            {
                System.out.println("Error:-" + ex.toString() + ", " + ex.getMessage() + ", " + ex.getLocalizedMessage());
                ex.printStackTrace();
            }
                */
            long end = new Date().getTime();
            long elapsedtime=end-start;
            System.out.println("Time taken: " + elapsedtime/1000 + " seconds");

        }
        catch (Exception ex)
        {
            System.out.println("Error:-" + ex.toString() + ", " + ex.getMessage() + ", " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }

    }
}
