import java.io.*;
import java.io.*;

import org.apache.commons.fileupload.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: cliff
 * Date: 10/06/13
 * Time: 22:27
 * To change this template use File | Settings | File Templates.
 */
public class LDAModel
{
    String filename;
    String FileFolder;
    String corpusname;
    String LDAType;
    String Slash;

    public LDAModel(String filenamei, String FileFolderi, String corpusnamei, String LDATypei, String Slashi)
    {
        filename=filenamei;
        FileFolder=FileFolderi;
        corpusname=corpusnamei;
        LDAType=LDATypei;
        Slash=Slashi;
    }

    public void learn(String learnscript)
    {
        try
        {
            String cmd="java -jar " + FileFolder + Slash + "tmt-0.4.0.jar \"" + FileFolder +  Slash + learnscript + "\" \"" + filename + "\" \"" + FileFolder +  Slash + LDAType +  Slash + corpusname + "\"";
            // + FileFolder + "\\" + learnscript + " " + filename + " " + FileFolder + "\\lda\\scottadams20130509"
            //cmd="\"C:\\Program Files\\Java\\jre7\\bin\\java.exe\" -version";
            System.out.println(cmd);

            try
            {
                Process p = Runtime.getRuntime().exec(cmd);
                InputStream is = p.getErrorStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                System.out.println("<" + LDAType + "-learn>");
                while ((line=br.readLine())!=null)
                {
                    System.out.println(line);
                }
                System.out.println("</" + LDAType + "-learn>");
                int exitVal = p.waitFor();
                System.out.println("Process exitValue: " + exitVal);

            }
            catch(Throwable t)
            {
                t.printStackTrace();
            }

        }
        catch (Exception ex)
        {
            System.out.println("Error:-" + ex.toString() + ", " + ex.getMessage() + ", " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

    public void infer(String inferscript)
    {
        try
        {
            String cmd="java -jar " + FileFolder +  Slash + "tmt-0.4.0.jar \"" + FileFolder +  Slash + inferscript + "\" \"" + filename + "\" \"" + FileFolder +  Slash + LDAType +  Slash + corpusname + "\"";
            System.out.println(cmd);

            try
            {
                Process p = Runtime.getRuntime().exec(cmd);
                InputStream is = p.getErrorStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                System.out.println("<" + LDAType + "-infer>");
                while ((line=br.readLine())!=null)
                {
                    System.out.println(line);
                }
                System.out.println("</" + LDAType + "-infer>");
                int exitVal = p.waitFor();
                System.out.println("Process exitValue: " + exitVal);

            }
            catch(Throwable t)
            {
                t.printStackTrace();
            }
        }
        catch (Exception ex)
        {
            System.out.println("Error:-" + ex.toString() + ", " + ex.getMessage() + ", " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }
}
