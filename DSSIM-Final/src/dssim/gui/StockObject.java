/*
 * The MIT License
 *
 * Copyright 2016 paulcuenin.
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
package dssim.gui;

import org.mariuszgromada.math.mxparser.Argument;

/**
 *
 * @author paulcuenin
 */
public class StockObject {

        public String sStockName; //for keeping the name given from the user
        public String sStockSymbol; //for keeping the user entered symbol
        public String sStockInitial; //for keeping the initial value the user enters
        public Object oStockJgraph; //the JGraph object that is created by placing the object
        public Argument aStockArg; //Argument data type is from the mxparser library
        public String sStockJgraphName; //JGraph object name 

        //stock object is created for the purposes right now as a variable that is represented in the generated graphs and tables
        public StockObject(Object graphobject, String inputname, String inputsymbol, String inputinitial) {

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
        public Argument getStockArg() {
           // Argument newStockArg = new Argument( aStockArg.getSymbol,aStockArg.getArgumentValue());
            return aStockArg.clone();
        }

        //sets the argument
        public void setStockArg(String symbol, String initial) {
            aStockArg = new Argument(symbol, Double.parseDouble(initial));
        }

        //sets gets the stock name
        public String getStockName() {
            return sStockName;
        }

        //for use by mainform class
        public void setStockName(String newname) {
            sStockName = newname;
        }

        //get stock symbol
        public String getStockSymbol() {
            return sStockSymbol;
        }

        //set stock symbol
        public void setStockSymbol(String newsymbol) {
            sStockSymbol = newsymbol;
        }

        //return initial value as a string
        public String getStockInitial() {
            return sStockInitial;
        }

        //set stock initial value
        public void setStockInitial(String newinitial) {
            sStockInitial = newinitial;
        }

        //returns the stock object of jgraph object type
        public Object getStockObject() {

            return oStockJgraph;
        }

        //returns the graph object name this and the stockobject builds a reference table 
        public String getStockJgraphName() {
            return sStockJgraphName;
        }

    }
