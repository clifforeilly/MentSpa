/**
 * Created with IntelliJ IDEA.
 * User: cliff
 * Date: 10/06/13
 * Time: 22:03
 * To change this template use File | Settings | File Templates.
 */


import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import au.com.bytecode.opencsv.CSVWriter;

public class StanCoreNLP
{
    String csvPath;
    String InputFileName;
    String myLoc;
    String OutputFileFolder;
    String corpus;
    String Slash;

    public StanCoreNLP(String corpusi, String myLoci, String OutputFileFolderi, String InputFileNamei, String Slashi)
    {
        InputFileName=InputFileNamei;
        myLoc=myLoci;
        OutputFileFolder=OutputFileFolderi;
        Slash=Slashi;
        csvPath = OutputFileFolder + Slash + "output_" + InputFileName + ".csv";
        corpus=corpusi;
    }

    public String getcsvPath()
    {
        return csvPath;
    }

    public String[] run()
    {
        String[] outputl1 = new String[5];
        try
        {
            List<String> nounNodeNames = new ArrayList<String>();
            nounNodeNames.add( "NP");
            nounNodeNames.add( "NP$");
            nounNodeNames.add( "NPS");
            nounNodeNames.add( "NN");
            nounNodeNames.add( "NN$");
            nounNodeNames.add( "NNS");
            nounNodeNames.add( "NNS$");
            nounNodeNames.add( "NNP");
            nounNodeNames.add( "NNPS");

            List<String> verbNodeNames = new ArrayList<String>();
            verbNodeNames.add( "VB");
            verbNodeNames.add( "VBD");
            verbNodeNames.add( "VBG");
            verbNodeNames.add( "VBN");
            verbNodeNames.add( "VBP");
            verbNodeNames.add( "VBZ");

            List<String> adjectiveNodeNames = new ArrayList<String>();
            adjectiveNodeNames.add( "JJ");
            adjectiveNodeNames.add( "JJR");
            adjectiveNodeNames.add( "JJS");

            List<String> adverbNodeNames = new ArrayList<String>();
            adverbNodeNames.add( "RB");
            adverbNodeNames.add( "RBR");
            adverbNodeNames.add( "RBS");

            List<String> determinerNodeNames = new ArrayList<String>();
            determinerNodeNames.add( "DT");

            List<String> prepositionNodeNames = new ArrayList<String>();
            prepositionNodeNames.add( "IN");

            List<String> conjunctionNodeNames = new ArrayList<String>();
            conjunctionNodeNames.add( "CC");

            List<String> interjectionNodeNames = new ArrayList<String>();
            interjectionNodeNames.add( "UH");

            List<String> pronounNodeNames = new ArrayList<String>();
            pronounNodeNames.add( "PRP");
            pronounNodeNames.add( "PRP$");

            Properties props = new Properties();
            props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
            //props.put("dcoref.score", true);
            StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
            List<String[]> mm = new ArrayList<String[]>();
            int s = 0;
            FNFrame fn = new FNFrame(myLoc, Slash);

            int MSCount = 0;

            Annotation doc = new Annotation(corpus);
            pipeline.annotate(doc);
            List<CoreMap> sentences = doc.get(CoreAnnotations.SentencesAnnotation.class);
            Map<Integer, CorefChain> graph = doc.get(CorefCoreAnnotations.CorefChainAnnotation.class);

            outputl1[0]=String.valueOf(sentences.size());
            int tempFrameCount=0;
            int tempDependencyCount=0;
            int tempCorefPronominalCount=0;
            int tempCorefProperCount=0;

            for(CoreMap sentence : sentences)
            {
                int w = 0;
                s++;
                System.out.println(sentence.toString());
                SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
                String Entities = "";
                String tmt = "";

                for(CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class))
                {
                    String word = token.get(CoreAnnotations.TextAnnotation.class);
                    String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                    if(pronounNodeNames.contains(pos))
                    {
                        Entities = Entities + " " + word;
                    }
                }

                for(CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class))
                {
                    w++;
                    String word = token.get(CoreAnnotations.TextAnnotation.class);
                    String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                    String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                    String lem = token.get(CoreAnnotations.LemmaAnnotation.class);
                    //tmt = tmt + " " + lem;
                    //do FrameNet check here and add frame elements etc to the tm[2] string
                    List<String[]> outputFrames = new ArrayList<String[]>();

                    String type = "";
                    if(nounNodeNames.contains(pos))
                    {
                        type="Noun";
                    }
                    if(verbNodeNames.contains(pos))
                    {
                        type="Verb";
                    }
                    if(adjectiveNodeNames.contains(pos))
                    {
                        type="Adjective";
                    }
                    if(adverbNodeNames.contains(pos))
                    {
                        type="Adverb";
                    }
                    if(conjunctionNodeNames.contains(pos))
                    {
                        type="Conjunction";
                    }
                    if(determinerNodeNames.contains(pos))
                    {
                        type="Determiner";
                    }
                    if(prepositionNodeNames.contains(pos))
                    {
                        type="Preposition";
                    }
                    if(interjectionNodeNames.contains(pos))
                    {
                        type="Interjection";
                    }

                    outputFrames=fn.getFrames(lem, type);
                    tempFrameCount=tempFrameCount + outputFrames.size();

                    System.out.println(s + ":" + w + " (" + word + "(" + lem + ")" + ", " + pos + (ne.equals("O") ? "" : "," + ne) + ") ");


                    for(String[] f : outputFrames)
                    {
                        if(!f[0].equals(" "))
                        {
                            //dependencies
                            String msdeps = "";
                            IndexedWord thisw = dependencies.getNodeByWordPattern(word);
                            if(dependencies.hasChildren(thisw))
                            {
                                List<IndexedWord> liw = dependencies.getChildList(thisw);

                                for(IndexedWord iw : liw)
                                {
                                    String thiswr = iw.word();
                                    msdeps=msdeps +  " " + thiswr;
                                    tempDependencyCount++;
                                }
                            }

                            MSCount++;
                            String[] tm = new String[19];
                            tm[0] = String.valueOf(MSCount);
                            tm[1]=String.valueOf(s);
                            tm[2]=sentence.toString();
                            tm[3]=word;
                            tm[4]=f[0];
                            tm[5]=f[1];
                            tm[6]=f[2];
                            tm[7]=f[3];
                            tm[8]=f[4];
                            tm[9]=f[5];
                            tm[10]=f[6];
                            tm[11]=f[7];
                            tm[12]=f[8];
                            tm[13]=f[9];
                            tm[14]=f[10];
                            tm[15]=Entities;
                            tm[16]=msdeps;
                            tm[17]=tm[4] + " " + tm[5] + " " + tm[6] + " " + tm[7] + " " + tm[8] + " " + tm[9] + " ";
                            tm[17]=tm[17] + " " + tm[10] + " " + tm[11] + " " + tm[12] + " " + tm[13] + " " + tm[14] + " ";
                            tm[17]=tm[17] + " " + tm[15] + " " + tm[16];
                            tm[18]=tm[5] + " " + tm[6] + " " + tm[7] + " " + tm[8] + " " + tm[9] + " ";
                            tm[18]=tm[18] + " " + tm[10] + " " + tm[11] + " " + tm[12] + " " + tm[13] + " " + tm[14] + " ";
                            tm[18]=tm[18] + " " + tm[15] + " " + tm[16];
                            mm.add(tm);
                        }
                    }
                }

                    /*
                    System.out.println(dependencies);
                    IndexedWord root = dependencies.getFirstRoot();
                    System.out.printf("ROOT(root-0, %s-%d)%n", root.word(), root.index());
                    System.out.println(dependencies.toString("readable"));
                    */

            }

            outputl1[1]=String.valueOf(tempFrameCount);
            outputl1[2]=String.valueOf(tempDependencyCount);

            String lastProper="";

            for(CorefChain crc : graph.values())
            {

                System.out.println(crc.getChainID() + ":" + crc.getRepresentativeMention());

                for(CorefChain.CorefMention crm : crc.getMentionsInTextualOrder())
                {
                    if(crm.mentionType.toString()=="PROPER")
                    {
                        lastProper=crm.mentionSpan;
                        for(String[] t : mm)
                        {
                            if(Integer.parseInt(t[1])==crm.sentNum)
                            {
                                t[15]=t[15] + " " + lastProper;
                                t[17]=t[17] + " " + lastProper;
                                t[18]=t[18] + " " + lastProper;
                                tempCorefProperCount++;
                            }
                        }
                    }

                    if(crm.mentionType.toString()=="PRONOMINAL")
                    {
                        if(lastProper.length()!=0)
                        {
                            for(String[] t : mm)
                            {
                                if(Integer.parseInt(t[1])==crm.sentNum)
                                {
                                    t[15]=t[15] + " " + lastProper;
                                    t[17]=t[17] + " " + lastProper;
                                    t[18]=t[18] + " " + lastProper;
                                    tempCorefPronominalCount++;
                                }
                            }
                        }
                        lastProper="";
                    }
                }

            }

            outputl1[3]=String.valueOf(tempCorefProperCount);
            outputl1[4]=String.valueOf(tempCorefPronominalCount);

            CSVWriter csvout = new CSVWriter(new FileWriter(csvPath));
            for(String[] t : mm)
            {
                csvout.writeNext(t);
            }

            System.out.println("Writing to " + csvPath);
            csvout.close();

        }
        catch (Exception ex)
        {
            System.out.println("Error:-" + ex.toString() + ", " + ex.getMessage() + ", " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return outputl1;
    }

}
