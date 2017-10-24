package com.classification;

/**
 *
 * @author ammar
 */

import com.algorithms.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceGML;
import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.ConnectedComponents.ConnectedComponent;
import static org.graphstream.algorithm.Toolkit.*;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.*;

public class Execute {
    public static void main(String[] args) throws Exception {
        try{
            Snowball snowball = new Snowball();
            
            snowball.exportResults("real");
            snowball.exportResults("simulated");
                                                               
        }catch(IOException e){
            e.printStackTrace(); 
            System.out.println(e.getMessage());
        }
    }
}