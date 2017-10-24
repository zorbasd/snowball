package com.algorithms;

/**
 *
 * @author ammar
 */

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceGML;
import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.ConnectedComponents.ConnectedComponent;
import static org.graphstream.algorithm.Toolkit.*;

import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Random;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.converters.ConverterUtils;

public class Snowball {    
    /**
    * This function returns a breadth-first iterator for particular Subtree of our
    * correlation maximum spanning tree; such that, the breadth-first traversal starts form
    * the node with the maximum weighted degree.
    * It has on parameter, which is the subtree of the correlation maximum spanning tree,
    * which is in-turn presented in a form of a ConnectedComponent.
    */
    public Iterator<Node> getSubtreeBreadthFirstIterator(ConnectedComponent subTree){
        /* get any random node in subTree */
        Node root = subTree.iterator().next();
        for(Node n : subTree.getEachNode() ) {
            if(weightedDegree(root, "weight",0) < weightedDegree(n, "weight",0)){   
                root = n;
            }
        }
        /* get the childs of the root */
        return root.getBreadthFirstIterator();
    }

    /**
    * This function loads a graph from a .gml file.
    * It has one parameters, which is the path to the .gml file.
    */
    public Graph getGraphFromFile(String filePath) throws IOException{
        /* initilizing a simply, undirected, weighted graph */
        Graph graph = new SingleGraph("tutorial 1");
        
        /* preparing the file source */
        FileSource fs = new FileSourceGML();

        /* reading the file to the graph */
        fs.addSink(graph); 
        fs.readAll(filePath);
        fs.removeSink(graph);
        
        return graph;
    }

    /**
    * This function displays a particular graph.
    * It has two parameters: the first parameter, is the graph itself; the second
    * parameter, is the a boolean flag indication whether to show the edge weights or not.
    */
    public void displayGraph(Graph graph, boolean showEdgeWeights){
        /* preparing node and edge lables for the graph*/
        for(Node n: graph.getEachNode()){
            n.addAttribute("ui.label", n.getAttribute("name"));
        }
        if(showEdgeWeights){
            for(Edge e : graph.getEachEdge() ){
            e.addAttribute("ui.label", e.getAttribute("weight"));
        }
        }
        /* displaying graph*/
        graph.display();
    }
    
   /**
    * This function removes a set of attributes from a collection of instances.
    * It has two parameters: the first parameter, is the collection of instances; the second
    * parameter, is the indeces of the the attributes to be removed.
    */
   public Instances getTrainingData(Instances inst, String selectedAttributesIndecies ) throws Exception {
        /* declaring the returnded instances */  
        Instances resultingInstances;

        /* declaring a remove filter */
        Remove remove = new Remove();;

        remove.setAttributeIndices(selectedAttributesIndecies);
        remove.setInvertSelection(true);
        remove.setInputFormat(inst);

        resultingInstances = Filter.useFilter(inst, remove);

        return resultingInstances;
   }
       
   /**
    * This function rounds a double values to two decimal places.
    * It has one parameter, which the double value to be rounded.
    */ 
    public double RoundTo4Decimals(double val) {
            DecimalFormat df2 = new DecimalFormat("###.####");
        return Double.valueOf(df2.format(val));
    }
     
    /**
    * This function returns the url for the root working directory of the application.
    * It has two overloads:
    * The first one takes now parameters, and returns the applications root working directory url, and
    * The second one takes one parameters, and returns the url to a child of the applications root working directory.
    */
    public String getWorkingDirectory() throws IOException{
        File currentDirectory = new File(new File(".").getAbsolutePath());
        return currentDirectory.getParentFile().getParentFile().getCanonicalPath();
    }
    
    public String getWorkingDirectory(String childFolderName) throws IOException{
        File currentDirectory = new File(new File(".").getAbsolutePath());
        return currentDirectory.getParentFile().getParentFile().getCanonicalPath() + "/"+ childFolderName;
    }
       
   /**
    * This takes a full path (string) for a specific file, and returns a BufferedWriter object for that file.
    */
    public static BufferedWriter getBufferedFileWriter(String filePath, boolean append) throws IOException{       
        File file = new File(filePath);
        
        // if file doesnt exists, then create it.
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
            
        FileWriter fileWriter = new FileWriter( file.getAbsoluteFile(), append );
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
         
        return bufferedWriter;
    }
      
   /**
    * This takes appends a line to the csv file.
    */
    public void appendCSVFile(String filePath, List<String> line) throws IOException{       
        File csvFile = new File(filePath);
        
        FileWriter csvFileWriter;
        BufferedWriter csvBufferedWriter;
        CSVPrinter csvFilePrinter;
        
        //Create the CSVFormat object with "\n" as a record delimiter
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator("\n");
        csvFileFormat.DEFAULT.withDelimiter(',');
                        
        /* if file doesnt exists, then create it, and write the header. */
        if (!csvFile.exists()) {            
            csvFile.getParentFile().mkdirs();
            csvFile.createNewFile();
            
            /* preparing the CSV file printer */
            csvFileWriter = new FileWriter( csvFile.getAbsoluteFile(), true );
            csvBufferedWriter = new BufferedWriter(csvFileWriter);

            /* initialize CSVPrinter object */
            csvFilePrinter = new CSVPrinter( csvBufferedWriter, csvFileFormat);

            /* now printing the csv header */
            csvFilePrinter.printRecord( CSVHeader );
            
            /* flushing and closing csv file printer */
            csvBufferedWriter.flush();
            csvBufferedWriter.close();
            csvFilePrinter.close();
        }
        
        /* now writing a line into the csv file */
        csvFileWriter     = new FileWriter( csvFile.getAbsoluteFile(), true );
        csvBufferedWriter = new BufferedWriter(csvFileWriter);

        /* initialize CSVPrinter object */
        csvFilePrinter = new CSVPrinter( csvBufferedWriter, csvFileFormat);

        /* now printing the line in the csv file */
        csvFilePrinter.printRecord( line );

        /* flushing and closing csv file printer */
        csvBufferedWriter.flush();
        csvBufferedWriter.close();
        csvFilePrinter.close();       
    }
      
   /**
    * This function trains and evaluates a classifier for a profile attribute.
    */    
    public Double evaluateAttributeClassifier(Instances trainingData, String datasetsType, 
            String dataset, String profileAttribute, String methodology 
    ) throws Exception{
        if (trainingData.numAttributes() > 0 && trainingData.numInstances() > 0 ){    
            /** initializing the content of the Evaluation Summary File **/ 
            String evalSummaryFileContent = "";

            /** Appending Relations Name to fileString **/
            evalSummaryFileContent += "** Dataset Used for Training **\n" + trainingData.relationName() + "\n\n";
            
            /** Appending Class and Feature Attributes to Evaluation Summary File  **/ 
            evalSummaryFileContent += "**  Feature Attibutes **\n";
            for(int i=0; i < trainingData.numAttributes(); i++){
                if( trainingData.attribute(i).name() != trainingData.classAttribute().name() ){
                    evalSummaryFileContent += Integer.toString(i+1) + "- " + trainingData.attribute(i).name() + "\n";
                }
            }
            evalSummaryFileContent += "\n**  Class Attibutes **\n" + trainingData.classAttribute().name() + "\n";
            
            //Appending Data summary to Evaluation Summary File
            evalSummaryFileContent += "\nCurrent Dataset have: " + ( trainingData.numAttributes()-1 ) + 
            " Features and "+ trainingData.numInstances()  + " Instances\n\n";
            
            /* Building the initial classifier */
            Classifier tree = new RandomForest();
            tree.buildClassifier(trainingData);

            /* Appending The Tree to Evaluation Summary File */
            evalSummaryFileContent += "=== Classifier model (full training set) ===\n";
            evalSummaryFileContent += tree.toString() + "\n\n";
            
            /* Evaluating the classifier - 10-fold cross validation */
            Evaluation eval = new Evaluation(trainingData); 
            eval.crossValidateModel(tree, trainingData, 10, new Random(1));
            evalSummaryFileContent += eval.toSummaryString() + "\n\n";
            evalSummaryFileContent += eval.toClassDetailsString() + "\n\n";
            evalSummaryFileContent += eval.toMatrixString() + "\n";
            
            
            if(methodology != "Extended Features"){
                /** Opening Evaluation Summary File **/
                String eavlSummaryFilePath = getWorkingDirectory("data/" + datasetsType + "/" + dataset + "/"+methodology+"/" +
                        profileAttribute + "/RandomForest-" +"Evaluation-Summary@" + dataset  + "-Records.txt"
                );

                // Assigning a bufferedWriter to the file.
                BufferedWriter eavlSummaryFileBufferedWriter = getBufferedFileWriter( eavlSummaryFilePath, false );

                // Writing the evalSummaryFileContent into the Evaluation Summary File.
                eavlSummaryFileBufferedWriter.write(evalSummaryFileContent );
                //System.out.println("ok "+methodology );

                //closing the eavlSummaryFileBufferedWriter.
                eavlSummaryFileBufferedWriter.close();
            }
            
            // getting the classifier accuracy.
            Double accuracy = RoundTo4Decimals(eval.pctCorrect());
            return accuracy;
            
        }else{                
            throw new Exception("Error in evalAttributeClassifierInDataset: Current Dataset is Empty!");
        }
    }
        
   /**
    * This function trains and evaluates the classifier of each profile attribute.
    */     
    public void doSnowballForDataset(String datasetsType, String dataset) throws Exception{    
        String indeciesOfFeatureAttributes = "1-3";

        if(datasetsType == "simulated"){
            indeciesOfFeatureAttributes = "1-8";
        }
        
        String demographicsOnly = indeciesOfFeatureAttributes;
        
        String correlationMaximumSpanningTreeFilePath = getWorkingDirectory("data/"+ datasetsType +
        "/Policies-Only/correlationSummary/"+ dataset +"/cramersv-correlation-maximum-spanning-tree-"+ dataset +".gml");

        /** loading data from the dataset file **/
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(getWorkingDirectory("data/" + datasetsType + "/" + dataset + ".arff") );
        Instances rawData = source.getDataSet();
        
        /* getting the training data with demographics only.
        /* getting the correlation maximum spanning tree from the .GML file */
        Graph correlationMaximumSpanningTree = getGraphFromFile(correlationMaximumSpanningTreeFilePath);
                    
        /* getting the number of nodes in the maximum spanning tree */
        int numberOfnodes = correlationMaximumSpanningTree.getNodeCount();
        
        /* getting the subtress within the correlation maximum spanning tree (if any) */
        ConnectedComponents Subtrees = new ConnectedComponents();
        Subtrees.init(correlationMaximumSpanningTree);
        Subtrees.compute();

        /* getting a list of breadth-first iterators for all Subtrees */
        List< Iterator<Node> > allSubtreesBreadthFirstIterators = new ArrayList< Iterator<Node> >();
        
        for(ConnectedComponent Subtree : Subtrees) {
            allSubtreesBreadthFirstIterators.add(getSubtreeBreadthFirstIterator(Subtree) );
        }
        
        /* adding the root of every subtree to the initial feature vector */
        for(Iterator<Node> subtreeBreadthFirstIterator : allSubtreesBreadthFirstIterators){
            if(subtreeBreadthFirstIterator.hasNext()){
                Node subtreeRoot  = subtreeBreadthFirstIterator.next();
                String subtreeRootName = subtreeRoot.getAttribute("name").toString();
                Integer subtreeRootIndex = rawData.attribute(getPrivacyPolicyAttribute(subtreeRootName, datasetsType)).index()+1;
                
                /* updating the feature vector*/
                indeciesOfFeatureAttributes = indeciesOfFeatureAttributes + "," + subtreeRootIndex.toString();
                
                /* updating the node counter */
                numberOfnodes--;
            }
        }
        
        /* now traing the classifiers of the nodes of each subree accumelativly */
        while ( numberOfnodes > 0 ) {
            for(Iterator<Node> subtreeBreadthFirstIterator : allSubtreesBreadthFirstIterators){
                if(subtreeBreadthFirstIterator.hasNext()){
                    Node childNode = subtreeBreadthFirstIterator.next();
                    String childNodeName = childNode.getAttribute("name").toString();
                    Integer childNodeIndex = rawData.attribute(getPrivacyPolicyAttribute(childNodeName, datasetsType)).index()+1;
                    
                    /* updating the feature vector */
                    indeciesOfFeatureAttributes = indeciesOfFeatureAttributes + "," + childNodeIndex.toString();
                    String IndiceisOfDemographicsOnly = demographicsOnly;
                    IndiceisOfDemographicsOnly = IndiceisOfDemographicsOnly + "," + childNodeIndex.toString(); 
                    
                    /* getting the training data */
                    Instances trainingData = getTrainingData(rawData, indeciesOfFeatureAttributes);
                    Instances demographicsOnlyTrainingData = getTrainingData(rawData, IndiceisOfDemographicsOnly);
                    
                    /* selecting the class feature of the training data */
                    trainingData.setClassIndex(trainingData.numAttributes()-1);
                    demographicsOnlyTrainingData.setClassIndex(demographicsOnlyTrainingData.numAttributes()-1);
                    rawData.setClassIndex(childNodeIndex-1);
                    
                    /* Now, training the child node attribute classifier, and recording the results */
                    String nodeCSVFilePath = getWorkingDirectory("data/"+ datasetsType +"/experimentalResults/SnowBall/"+ childNodeName +".csv" );

                    List csvLine = new ArrayList();
                    csvLine.add(dataset);
                    csvLine.add(evaluateAttributeClassifier(rawData, datasetsType, dataset, childNodeName, "Extended Features" ));
                    csvLine.add(evaluateAttributeClassifier(trainingData, datasetsType, dataset, childNodeName, "SnowBall" ));
                    csvLine.add(evaluateAttributeClassifier(demographicsOnlyTrainingData, datasetsType, dataset, childNodeName, "Demographics Only" ));                        
                            
                    appendCSVFile(nodeCSVFilePath, csvLine);
                    
                    /* updating the node counter */
                    numberOfnodes--;
                }                    
            }
        }
    }

    /*
    * This method train and evaluate all of the profile attribute classifier, using all datasets of
    * certain type (e.g. real, simulated ), and then exports the evaluation results into a CSV file
    * for each profile attribute in the dataset type.
    */
    public boolean exportResults( String datasetsType ) throws IOException{
        try{
            List<String> AttributesList = null; 
            String[] DatasetsList = null;

            if(datasetsType == "simulated"){
                DatasetsList = DatasetsSimulated;
            }
            else{
                DatasetsList = DatasetsReal;       
            }

            for( String dataset : DatasetsList ){
                doSnowballForDataset(datasetsType, dataset);
            }
        }catch(Exception e){
            System.out.println("Error in exportResults: " + e.getMessage() );
            return false;
        }
        
        // the end
        System.out.println("Done! Finished Exporting [" + datasetsType + "] datasets.");
        return true;
    }      
 
   /**
    * This function returns the real privacy attribute name, as registered in the arff files.
    * It has one parameter, which the display name of that privacy policy attribute.
    */
    public String getPrivacyPolicyAttribute(String displayName, String type) throws Exception{
        String PrivacyPolicyAttribute = "";
        
        if (displayName+type == "Agesimulated") {
            PrivacyPolicyAttribute = "age-visibility";
        } else if (displayName+type == "Gendersimulated") {
            PrivacyPolicyAttribute = "gender-visibility";
        } else if (displayName+type == "Universitysimulated") {
            PrivacyPolicyAttribute = "university-visibility";
        } else if (displayName+type == "Countrysimulated") {
            PrivacyPolicyAttribute = "country-visibility";   
        } else if (displayName+type == "Citysimulated") {
            PrivacyPolicyAttribute = "current-city-visibility";
        } else if (displayName+type == "Interestssimulated") {
            PrivacyPolicyAttribute = "interests-visibility";
        } else if (displayName+type == "Languagesimulated") {
            PrivacyPolicyAttribute = "language-visibility";
        } else if (displayName+type == "Relationshipsimulated") {
            PrivacyPolicyAttribute = "relationship-status-visibility";
        } else if (displayName+type == "ProfilePicreal") {
            PrivacyPolicyAttribute = "ProfilePicPrivacy";  
        } else if (displayName+type == "Namereal") {
            PrivacyPolicyAttribute = "NamePrivacy";
        } else if (displayName+type == "Emailreal") {
            PrivacyPolicyAttribute = "EmailPrivacy";
        } else if (displayName+type == "Genderreal") {
            PrivacyPolicyAttribute = "GenderPrivacy";
        } else if (displayName+type == "DOBreal") {
            PrivacyPolicyAttribute = "DOBPrivacy";
        } else if (displayName+type == "Locationreal") {
            PrivacyPolicyAttribute = "LocationPrivacy";
        } else if (displayName+type == "Educationreal") {
            PrivacyPolicyAttribute = "EducationPrivacy";
        } else if (displayName+type == "Relationshipreal") {
            PrivacyPolicyAttribute = "RelationShipPrivacy";
        } else if (displayName+type == "languagereal") {
            PrivacyPolicyAttribute = "languagePrivacy";
        } else if (displayName+type == "Religionreal") {
            PrivacyPolicyAttribute = "ReligionPrivacy";
        } else if (displayName+type == "Mobilereal") {
            PrivacyPolicyAttribute = "MobilePrivacy";               
        } else {
            throw new Exception("Error: this display name [" + displayName+type + "] is not associated with any privacy policy attribute!");
        }
        
        return PrivacyPolicyAttribute;
    }

    /**
    * This contains some global variables that will be used throughout this script.
    */
    public String [] vertixLabelsSimulated   = {"Age", "Gender", "University", "Country", "City", "Interests", "Language", "Relationship"};
    public String [] vertixLabelsReal        = {"ProfilePic", "Name", "Email", "Gender", "DOB", "Location", "Education", "Relationship", "language", "Religion", "Mobile"};
    public List<String> CSVHeader            = Arrays.asList(new String[] {"Size/Methodology", "Extended Features", "SnowBall",  "Demographic Only"});
    public static String[] DatasetsSimulated = {"100", "300", "500", "1000", "2000"};
    public static String[] DatasetsReal      = {"10", "20", "30", "40", "50", "60", "70", "80", "90", "100"};
}
