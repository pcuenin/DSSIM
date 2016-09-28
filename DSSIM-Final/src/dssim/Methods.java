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

import dssim.gui.FlowObject;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.mariuszgromada.math.mxparser.*;
import dssim.gui.StockObject;
import dssim.gui.VariableObject;

public final class Methods  {

    //this data type is from the jfreechart library
    public XYSeriesCollection data;
    //as well as this one, this one allows an x y type table setup
    public DefaultTableModel tableModel = new DefaultTableModel(0, 2);
    //these array lists are instantiated here to be used as global values to be passed to the mainform
    public ArrayList<Argument> argumentList = new ArrayList<Argument>();
    public ArrayList<Argument> variableArgList = new ArrayList<Argument>();
    public ArrayList<Double> y0 = new ArrayList<Double>();

    public Methods() {
        // what is this doing? -PMC
    }

    public Methods(ArrayList<StockObject> stockArrayList,

            ArrayList<FlowObject> flowArrayList,
            ArrayList<VariableObject> variableArrayList, double t0,
            double tf, double stepSize, String choice) {
       


        StockObject stock; 
        VariableObject var;

        //for each stock get the stock initial values and add to the argumentList
        for (StockObject stockArrayList1 : stockArrayList) {
            stock = (StockObject) stockArrayList1; //get initial value
            argumentList.add(stock.getStockArg()); // add stock arge to argument list
        }
        //for each variable object, add to the argument array list for variables
        for (VariableObject variableArrayList1 : variableArrayList) {
            var = (VariableObject) variableArrayList1;
            variableArgList.add(var.getVarArg());
        }
        //the user choice of numerical analysis method is sent to these if statements
        if ("rk4".equals(choice)) {
            data = rk4(t0, tf, stepSize, argumentList, variableArgList, stockArrayList, flowArrayList);
        } else if ("rk2".equals(choice)) {
            data = rk2(t0, tf, stepSize, argumentList, variableArgList, stockArrayList, flowArrayList);
        } else {
            data = eulers(t0, tf, stepSize, argumentList, variableArgList, stockArrayList, flowArrayList);
        }
         // label the string array for columns
        String stockNames="Time,";
        for(int x=0;x<stockArrayList.size();x++)
        {
            stockNames+=argumentList.get(x).getArgumentName()+",";
        }
        
        String columns[]=stockNames.split(",");
        DefaultTableModel tableModel = new DefaultTableModel(0, stockArrayList.size()+1);
        tableModel.setColumnIdentifiers(columns); // set the labels
    }
    

    public XYSeriesCollection returnData() {
        return data;
    }

    public DefaultTableModel getTable() {
        return tableModel;
    }

    //this method is an attempt to reset data. doesnt help the bug yet...
    /*public void ResetData() {
        data = new XYSeriesCollection();
    }/*/

    public XYSeriesCollection rk4(double t0, double tF, double stepSize, 
            ArrayList<Argument> argumentList, ArrayList<Argument> variableArgList,
            ArrayList<StockObject> stockArrayList, ArrayList<FlowObject> flowArrayList) {

        //aVarList is an argument array that is created from the argument ArrayList given to it. 
        Argument[] aVarList = argumentList.toArray(new Argument[argumentList.size()]);
        //aTempVarList only created to later create the temparg array list
        Argument[] aTempVarList = new Argument[argumentList.size()];
        for (int j = 0; j < aVarList.length; j++) {
            aTempVarList[j] = aVarList[j].clone();
        }
        int numSteps = (int)((tF - t0) / stepSize);
        double t = t0;
        ArrayList<Argument> aTempArgArrayList = new ArrayList<Argument>();
        for (int j = 0; j < aVarList.length; j++) {
            aTempArgArrayList.add(aTempVarList[j]);
        }
        //
        double[] dydt = new double[argumentList.size()];

        ArrayList<Double> k1 = new ArrayList<Double>();

        ArrayList<Double> k2 = new ArrayList<Double>();

        ArrayList<Double> k3 = new ArrayList<Double>();

        ArrayList<Double> k4 = new ArrayList<Double>();

        //idea is to set k1 through k4 ArrayLists to double values of 0
        //used to have stockArrayList.size()
        for (int x = 0; x < stockArrayList.size(); x++) {
            k1.add(0.0);
            k2.add(0.0);
            k3.add(0.0);
            k4.add(0.0);
        }

        //array list length of amount of stocks
        ArrayList<XYSeries> series = new ArrayList<XYSeries>();

        //create series to hold graph data
        for (int i = 0; i < stockArrayList.size(); i++) {
            XYSeries tempSeries = new XYSeries(stockArrayList.get(i).getObjName());
            series.add(tempSeries);
        }

        final XYSeriesCollection data = new XYSeriesCollection();
        //create strings to hold table data
        ArrayList<String[]> tableStrings = new ArrayList<String[]>();
        String[] temp = {" ", " "};
        for (int i = 0; i <= stockArrayList.size(); i++) {
            tableStrings.add(temp);
        }

        //add initial values to series
        for (int i = 0; i < stockArrayList.size(); i++) {
            series.get(i).add(0, argumentList.get(i).getArgumentValue());
        }

        int numOfStocks = stockArrayList.size();
        double value=0.0;

        for (int n = 0; n < numSteps; n++) {

            t = t + stepSize;

            //Let's find k1:
            dydt = RightHandSide(variableArgList, argumentList, flowArrayList);

            for (int i = 0; i < numOfStocks; i++) {

                k1.set(i, stepSize * dydt[i]);
            }

            //next let's find k2:
            for (int i = 0; i < numOfStocks; i++) {

                value = (argumentList.get(i).getArgumentValue() + (k1.get(i)*stepSize/2));
                aTempArgArrayList.get(i).setArgumentValue(value);

                dydt = RightHandSide(variableArgList, aTempArgArrayList, flowArrayList);
            }
            for (int i = 0; i < numOfStocks; i++) {
                k2.set(i, stepSize * dydt[i]);
            }

            //next let's find k3:
            for (int i = 0; i < numOfStocks; i++) {

                value = argumentList.get(i).getArgumentValue() + (k2.get(i)*stepSize/2);
                aTempArgArrayList.get(i).setArgumentValue(value);
                dydt = RightHandSide(variableArgList, aTempArgArrayList, flowArrayList);
            }
            for (int i = 0; i < numOfStocks; i++) {
                k3.set(i, stepSize * dydt[i]);
            }

            //next let's find k4:
            for (int i = 0; i < numOfStocks; i++) {

                value = argumentList.get(i).getArgumentValue() + (k3.get(i)*stepSize);
                aTempArgArrayList.get(i).setArgumentValue(value);
                dydt = RightHandSide(variableArgList, aTempArgArrayList, flowArrayList);
            }
            for (int i = 0; i < numOfStocks; i++) {
                k4.set(i, stepSize * dydt[i]);
            }

            //now we update y
            for (int i = 0; i < numOfStocks; i++) {

                value = argumentList.get(i).getArgumentValue() + ((k1.get(i) + (2 * k2.get(i)) + (2 * k3.get(i)) + k4.get(i)) / 6);
                argumentList.get(i).setArgumentValue(value);

            }

            double x = n + 1;
            for (int i = 0; i < stockArrayList.size(); i++) {
                series.get(i).add(x*stepSize, argumentList.get(i).getArgumentValue());
            }
            for(int k = 0; k < argumentList.size();k++){
                for (int i = 0; i < stockArrayList.size(); i++) {
                    tableStrings.get(i)[0] = Double.toString(x*stepSize);
                    tableStrings.get(i)[1] = Double.toString(argumentList.get(i).getArgumentValue());
                    tableModel.addRow(tableStrings.get(i));
                }
            }
        }
        for (int i = 0; i < stockArrayList.size(); i++) {
            data.addSeries(series.get(i));
        }
        return data;
    }

    public XYSeriesCollection rk2(double t0, double tF, double stepSize, 
            ArrayList<Argument> argumentList, ArrayList<Argument> variableArgList, 
            ArrayList<StockObject> stockArrayList, ArrayList<FlowObject> flowArrayList) {

        //Used to help create the tempvarlist
        Argument[] aVarList = argumentList.toArray(new Argument[argumentList.size()]);
        //aTempVarList only created to later create the temparg array list
        Argument[] aTempVarList = new Argument[argumentList.size()];
        for (int j = 0; j < aVarList.length; j++) {
            aTempVarList[j] = aVarList[j].clone();
        }
        //double numSteps = (tF - t0) / stepSize;
        int numSteps = (int)((tF - t0) / stepSize);
        double t = t0;
        ArrayList<Argument> aTempArgArrayList = new ArrayList<Argument>();
        for (int j = 0; j < aVarList.length; j++) {
            aTempArgArrayList.add(aTempVarList[j]);
        }
        double[] dydt = new double[argumentList.size()];

        ArrayList<Double> k1 = new ArrayList<Double>();

        ArrayList<Double> k2 = new ArrayList<Double>();

        //idea is to set k1 through k4 ArrayLists to double values of 0
        //used to have stockArrayList.size()
        for (int x = 0; x < stockArrayList.size(); x++) {
            k1.add(0.0);
            k2.add(0.0);
        }

        //array list length of amount of stocks
        ArrayList<XYSeries> series = new ArrayList<XYSeries>();

        //create series to hold graph data
        for (int i = 0; i < stockArrayList.size(); i++) {
            XYSeries tempSeries = new XYSeries(stockArrayList.get(i).getObjName());
            series.add(tempSeries);
        }

        final XYSeriesCollection data = new XYSeriesCollection();
        //create strings to hold table data
        ArrayList<String[]> tableStrings = new ArrayList<String[]>();
        String[] temp = {" ", " "};
        for (int i = 0; i <= stockArrayList.size(); i++) {
            tableStrings.add(temp);
        }

        //add initial values to series
        for (int i = 0; i < stockArrayList.size(); i++) {
            series.get(i).add(0, argumentList.get(i).getArgumentValue());
        }

        int numOfStocks = stockArrayList.size();
        double value;

        for (int n = 0; n < numSteps; n++) {
            t = t + stepSize;

            //Let's find k1:
            dydt = RightHandSide(variableArgList, argumentList, flowArrayList);

            for (int i = 0; i < numOfStocks; i++) {

                k1.set(i, stepSize * dydt[i]);
            }

            //next let's find k2:
            for (int i = 0; i < numOfStocks; i++) {

                value = (argumentList.get(i).getArgumentValue() + (k1.get(i)*stepSize));
                aTempArgArrayList.get(i).setArgumentValue(value);

                dydt = RightHandSide(variableArgList, aTempArgArrayList, flowArrayList);
            }
            for (int i = 0; i < numOfStocks; i++) {
                k2.set(i, stepSize * dydt[i]);
            }
            for (int i = 0; i < numOfStocks; i++) {

                value = (argumentList.get(i).getArgumentValue() + ((k1.get(i) + k2.get(i)) / 2));
                argumentList.get(i).setArgumentValue(value);

            }
            double x = n + 1;

            for (int i = 0; i < stockArrayList.size(); i++) {
                series.get(i).add(x*stepSize, argumentList.get(i).getArgumentValue());
            }

            for (int i = 0; i < stockArrayList.size(); i++) {
                tableStrings.get(i)[0] = Double.toString(x*stepSize);
                tableStrings.get(i)[1] = Double.toString(argumentList.get(0).getArgumentValue());
                tableModel.addRow(tableStrings.get(i));
            }
        }
        for (int i = 0; i < stockArrayList.size(); i++) {
            data.addSeries(series.get(i));
        }
        return data;
    }

    public XYSeriesCollection eulers(double t0, double tF, double stepSize, ArrayList<Argument> argumentList,
            ArrayList<Argument> variableArgList, ArrayList<StockObject> stockArrayList, 
            ArrayList<FlowObject> flowArrayList) {

        //Used to help create the tempvarlist
        Argument[] aVarList = argumentList.toArray(new Argument[argumentList.size()]);
        //aTempVarList only created to later create the temparg array list
        Argument[] aTempVarList = new Argument[argumentList.size()];
        for (int j = 0; j < aVarList.length; j++) {
            aTempVarList[j] = aVarList[j].clone();
        }
        //double numSteps = (tF - t0) / stepSize;
        int numSteps = (int)((tF - t0) / stepSize);
        double t = t0;
        ArrayList<Argument> aTempArgArrayList = new ArrayList<Argument>();
        for (int j = 0; j < aVarList.length; j++) {
            aTempArgArrayList.add(aTempVarList[j]);
        }
        double[] dydt = new double[argumentList.size()];

        ArrayList<Double> k1 = new ArrayList<Double>();

        //idea is to set k1 through k4 ArrayLists to double values of 0
        for (int x = 0; x < stockArrayList.size(); x++) {
            k1.add(0.0);
        }

        //array list length of amount of stocks
        ArrayList<XYSeries> series = new ArrayList<XYSeries>();

        //create series to hold graph data
        for (int i = 0; i < stockArrayList.size(); i++) {
            XYSeries tempSeries = new XYSeries(stockArrayList.get(i).getObjName());
            series.add(tempSeries);
        }

        final XYSeriesCollection data = new XYSeriesCollection();
        //create strings to hold table data
        ArrayList<String[]> tableStrings = new ArrayList<String[]>();
        String[] temp = {" ", " "};
        for (int i = 0; i <= stockArrayList.size(); i++) {
            tableStrings.add(temp);
        }

        //add initial values to series
        for (int i = 0; i < stockArrayList.size(); i++) {
            series.get(i).add(0, argumentList.get(i).getArgumentValue());
        }
        int numOfStocks = stockArrayList.size();
        double value;

        for (int n = 0; n < numSteps; n++) {
            t = t + stepSize;

            //Let's find k1:
            dydt = RightHandSide(variableArgList, argumentList, flowArrayList);

            for (int i = 0; i < numOfStocks; i++) {

                k1.set(i, stepSize * dydt[i]);
            }

            for (int i = 0; i < numOfStocks; i++) {

                value = argumentList.get(i).getArgumentValue() + (k1.get(i));
                argumentList.get(i).setArgumentValue(value);

            }
            double x = n + 1;

            for (int i = 0; i < stockArrayList.size(); i++) {
                series.get(i).add(x*stepSize, argumentList.get(i).getArgumentValue());
            }

            for (int i = 0; i < stockArrayList.size(); i++) {
                tableStrings.get(i)[0] = Double.toString(x*stepSize);
                tableStrings.get(i)[1] = Double.toString(argumentList.get(0).getArgumentValue());
                tableModel.addRow(tableStrings.get(i));
            }
        }
        for (int i = 0; i < stockArrayList.size(); i++) {
            data.addSeries(series.get(i));
        }
        return data;
    }

    
    //This method handles the actual equation the user creates.
    public double[] RightHandSide(ArrayList<Argument> variableArgList, ArrayList<Argument> stockArgList, ArrayList<FlowObject> flowArrayList) {

        //set double array of size of stockArrayList
        double[] ret = new double[stockArgList.size()];

        ArrayList<Argument> globalArgList = new ArrayList<Argument>();
        globalArgList.addAll(stockArgList);
        globalArgList.addAll(variableArgList);
        Argument[] globalvariables = globalArgList.toArray(new Argument[globalArgList.size()]);

        Expression e;
        //for how ever many stocks there are, you get each stock and find the solution to each equation from the stock using
        // the variables array. it then returns that to the double ret array at the appropriate index
        for (int j = 0; j < flowArrayList.size(); j++) {

            for (int i = 0; i < stockArgList.size(); i++) {
                FlowObject flow = flowArrayList.get(j);
                //Think about having general expressions passed to this loop, if you
                //can actually change parts of the expressions using e.whatever
                e = new Expression(flow.getflowEquation(), globalvariables);
                
                ret[i] = e.calculate();

            }
        }
        return ret;
    }

}
