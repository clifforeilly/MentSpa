/**
 * Created with IntelliJ IDEA.
 * User: cliff
 * Date: 11/06/13
 * Time: 13:14
 * To change this template use File | Settings | File Templates.
 */

import au.com.bytecode.opencsv.CSVReader;
import java.io.FileReader;
import java.util.List;

public class AnalyseResults
{
    String[] output = new String[2];
    List inputdata;
    double[][] csvDoubles;
    int numCols;
    String Slash;

    public AnalyseResults(String FileFolderi, String ldaInferredFileName, String type, String Slashi)
    {
        Slash=Slashi;
        String filename = FileFolderi + Slash + type + Slash + ldaInferredFileName + Slash + "output_" + ldaInferredFileName + "-document-topic-distributions.csv";
        processCSV(filename);
    }

    public String[] getMeanAndSD()
    {
        double[] meannsd = new double[2];
        try
        {
            Double tempCalc = 0.0;
            int valCount = 0;

            Double[][] outputmatrix = new Double[inputdata.size()][inputdata.size()];

            for(int k=0 ; k<inputdata.size() ; k++)
            {
                for(int j=0 ; j<inputdata.size() ; j++)
                {
                    tempCalc = 0.0;
                    for(int i=0 ; i<numCols ; i++)
                    {
                        if(j>k)
                        {
                            tempCalc = tempCalc + csvDoubles[k][i]*csvDoubles[j][i];
                        }
                    }
                    outputmatrix[j][k]=tempCalc;
                }
            }

            tempCalc=0.0;
            String[] tempRow = new String[inputdata.size()];
            for(Double[] t : outputmatrix)
            {
                for(int p=0 ; p<inputdata.size() ; p++)
                {
                    if(!String.valueOf(t[p]).equals("NaN"))
                    {
                        if(t[p]!=0.0)
                        {
                            tempCalc=tempCalc + t[p];
                            valCount++;
                        }
                    }
                }
            }

            meannsd[0]=tempCalc/valCount;
            //System.out.println(meannsd[0]);

            tempCalc=0.0;
            for(Double[] t : outputmatrix)
            {
                for(int p=0 ; p<inputdata.size() ; p++)
                {
                    if(!String.valueOf(t[p]).equals("NaN"))
                    {
                        if(t[p]!=0.0)
                        {
                            tempCalc=tempCalc +(t[p]-meannsd[0])*(t[p]-meannsd[0]);

                        }
                    }
                }
            }

            meannsd[1]=tempCalc/valCount;
            //System.out.println(meannsd[1]);

            output[0]=String.valueOf(meannsd[0]);
            output[1]=String.valueOf(meannsd[1]);

        }
        catch (Exception ex)
        {
            System.out.println("Error:-" + ex.toString() + ", " + ex.getMessage() + ", " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return output;
    }

    private void processCSV(String csvfile)
    {
        try
        {
            String outedFile="";
            outedFile=csvfile;
            CSVReader csvdata = new CSVReader(new FileReader(outedFile));
            inputdata = csvdata.readAll();
            csvdata.close();

            numCols=0;

            String[] row = null;
            int rcount;

            for(Object inputrow : inputdata)
            {
                row = (String[]) inputrow;
                numCols = row.length-1;
            }

            csvDoubles = new double[inputdata.size()][numCols];

            rcount=0;
            for(Object inputrow2 : inputdata)
            {
                row = (String[]) inputrow2;
                for(int i=0; i<row.length-1; i++)
                {
                    csvDoubles[rcount][i]=Double.valueOf(row[i+1]);
                }
                rcount++;
            }
        }
        catch (Exception ex)
        {
            System.out.println("Error:-" + ex.toString() + ", " + ex.getMessage() + ", " + ex.getLocalizedMessage());
            ex.printStackTrace();

        }
    }

}
