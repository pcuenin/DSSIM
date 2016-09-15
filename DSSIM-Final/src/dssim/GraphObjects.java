package dssim;

/*
 * The MIT License
 *
 * Copyright 2016 Lander University.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/**
 *
 * @author Lander University
 */

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import org.jfree.data.xy.XYSeriesCollection;
import org.mariuszgromada.math.mxparser.*;

//The way mxparser is working is as follows: Argument data types take a symbol(that is lower case) and a double value number. 
//          aStockArg = new Argument(inputsymbol, Double.parseDouble(inputinitial));
//here is one way to instantiate your argument. It takes a string for the symbol, and a double value as the value. 
//you can start an argument with a string for the value, but you will not be able to get an accurate value out of the argument return methods
//the parser library has methods to get values from the argument objects. Very useful!

public class GraphObjects extends MainForm{

//posy and posx are for later use with position being saved to the objects    
    int posx;
    int posy;
    
    
    
    //Stockobject is the general class for all stocks created
    public static class StockObject{
        
        
        public String sStockName; //for keeping the name given from the user
        public String sStockSymbol; //for keeping the user entered symbol
        public String sStockInitial; //for keeping the initial value the user enters
        public Object oStockJgraph; //the JGraph object that is created by placing the object
        public Argument aStockArg; //Argument data type is from the mxparser library
        public String sStockJgraphName; //JGraph object name 
    
        //stock object is created for the purposes right now as a variable that is represented in the generated graphs and tables
        StockObject(Object graphobject, String inputname, String inputsymbol, String inputinitial){
            
            //stock name from user
            sStockName = inputname;
            //jgraph object cell name
            sStockJgraphName = graphobject.toString();
            //stock symbol from user
            sStockSymbol = inputsymbol;
            //stock initial value input from user. for reference later and building parser argument
            sStockInitial = inputinitial;
            //object given from jgraph
            oStockJgraph = graphobject;            
            
            aStockArg = new Argument(inputsymbol, Double.parseDouble(inputinitial));
            
        }
        //return stock arg of argument type
        Argument getStockArg(){
            return aStockArg;
        }
        //sets the argument
        void setStockArg(String symbol, String initial){
             aStockArg = new Argument(symbol, Double.parseDouble(initial));
        }
        //sets gets the stock name
        String getStockName(){
            return sStockName;
        }
        //for use by mainform class
        void setStockName(String newname){
            sStockName = newname;
        }
        //get stock symbol
        String getStockSymbol(){
            return sStockSymbol;
        }
        //set stock symbol
        void setStockSymbol(String newsymbol){
            sStockSymbol = newsymbol;
        }
        //return initial value as a string
        String getStockInitial(){
            return sStockInitial;
        }
        //set stock initial value
        void setStockInitial(String newinitial){
            sStockInitial = newinitial;
        }
        //returns the stock object of jgraph object type
        Object getStockObject(){
            
            return oStockJgraph;
        }
        //returns the graph object name this and the stockobject builds a reference table 
        String getJgraphName(){
            return sStockJgraphName;
        }
        
        
        
        
    }
    //all other objects are deviations of the stock
    //variables purpose is to allow ther user to enter numbers to be referenced but not to the generated graphs and tables
    public static class VariableObject{
        public String sVarName;
        public String sVarSymbol;
        public String sVarInitial;
        public Object oVariable;
        //public String sEquation;
        public Argument aVarArg;
        public String sVarJgraphName;
        
        VariableObject(Object graphobject, String inputname, String inputsymbol, String inputinitial){
            sVarJgraphName = graphobject.toString();
            sVarName = inputname;
            sVarSymbol = inputsymbol;
            sVarInitial = inputinitial;
            oVariable = graphobject;
            //will cause issue if user inputs into variable a string like "x*54"
            //may use if statement to check for what type of argument constructor to use to avoid errors
            aVarArg = new Argument(inputsymbol, Double.parseDouble(inputinitial));
        }

        void setAArg(String symbol, String initial){
             aVarArg = new Argument(symbol, Double.parseDouble(initial));
        }
        String getVarName(){
            return sVarName;
        }
        //for use later by gui
        void setVarName(String newname){
            sVarName = newname;
        }
        void setVarSymbol(String newsymbol){
            sVarSymbol = newsymbol;
        }
        void setVarInitial(String newinitial){
            sVarInitial = newinitial;
            setAArg(sVarSymbol, sVarInitial);
        }
        
     
        
        Object getVarObject(){
            
            return oVariable;
        }
        String getJgraphName(){
            return sVarJgraphName;
        }
        String getSymbol(){
            return sVarSymbol;
        }
        Argument getVarArg(){
            return aVarArg;
        }
   
        String getInitial(){
            return sVarInitial;
        }
    }
    //the idea behind the flow object is that it allows a user to enter an equation that will be updating the values of the 
    //stocks on the generated graphs and tables
    public static class FlowObject{
        public String sFlowName;
        public String sSymbol;
        public Object oFlow;
        public String sEquation;
        public String sJgraphName;
        
        FlowObject(Object graphobject, String inputname, String inputequation){
            
            //flow name from user
            sFlowName = inputname;
            //jgraph object cell name
            sJgraphName = graphobject.toString();                   
            //object given from jgraph
            oFlow = graphobject;
            //input equation from user. for rhs data building later
            sEquation = inputequation;
            
            
        }
        
        String getFlowName(){
            return sFlowName;
        }
        //for use later by gui
        void setFlowName(String newname){
            sFlowName = newname;
        }
        Object getFlowObject(){
            
            return oFlow;
        }
        String getJgraphName(){
            return sJgraphName;
        }
        String getEquation(){
            return sEquation;
        }
        void setEquation(String newequation){
            sEquation = newequation;
        }
    }
    //arrows right now in this version are purely visual. They do not allow the user to see the model objects available to
    //use in aspects of the model. Later versions will implement the use of local variables associated with specific models aspects
    public static class ArrowObject{
        public String sArrowName;
        public Object oArrow;
        public String sJgraphName;
        ArrowObject(String inputname, Object graphobject){
            sArrowName = inputname;
            sJgraphName = graphobject.toString();
            oArrow = graphobject;
            
        }
        String getArrowName(){
            return sArrowName;
        }
        void setArrowName(String newname){
            sArrowName = newname;
        }
        String getJgraphName(){
            return sJgraphName;
        }
    }
    
    
}
