/*
 * The MIT License
 *
 * Copyright 2016 kamre_000.
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
package dssim;

import static dssim.MainForm.graph;
import org.json.simple.JSONObject;
import dssim.gui.FlowObject;
import dssim.gui.StockObject;
import dssim.gui.VariableObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author kamre_000
 */
public class JSONRead {

    public static ArrayList<StockObject> readStock(JSONParser parser, File filename) {
        ArrayList<StockObject> stockArrayList = new ArrayList();

        try {
            Object obj = parser.parse(new FileReader(filename));
            JSONObject jStock = (JSONObject) obj;
            JSONArray jStockArray = (JSONArray) jStock.get("Stocks");
            Iterator it = jStockArray.iterator();
            while (it.hasNext()) {
                JSONObject Stock = (JSONObject) it.next();
                //String arg = (String) jsonObject.get("arg");
                String name = (String) Stock.get("name");
                String desc = (String) Stock.get("desc");
                String init = (String) Stock.get("init");
                String x = (String) Stock.get("x");
                x = x.substring(0, x.length() - 2);
                String y = (String) Stock.get("y");
                y = y.substring(0, y.length() - 2);
                //Object jobj = jStock.get("obj");
                Object parent = graph.getDefaultParent();
                Object node = graph.insertVertex(parent, null, name, Integer.parseInt(x),
                        Integer.parseInt(y), 100, 50, "Stock");//draw the node
                StockObject stock = new StockObject(node, name, desc, init, x, y);
                stockArrayList.add(stock);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return stockArrayList;
    }

    public static ArrayList<FlowObject> readFlow(JSONParser parser, File filename) {
        ArrayList<FlowObject> flowArrayList = new ArrayList();

        try {
            Object obj = parser.parse(new FileReader(filename));
            JSONObject jFlow = (JSONObject) obj;
            JSONArray jFlowArray = (JSONArray) jFlow.get("Flows");
            Iterator it = jFlowArray.iterator();
            while (it.hasNext()) {
                JSONObject Flow = (JSONObject) it.next();
                String name = (String) Flow.get("name");
                String eq = (String) Flow.get("eq");
                String x = (String) Flow.get("x");
                x = x.substring(0, x.length() - 2);
                String y = (String) Flow.get("y");
                y = y.substring(0, y.length() - 2);
                //Object jobj = jsonObject.get("obj");
                Object parent = graph.getDefaultParent();
                Object node = graph.insertVertex(parent, null, name, Integer.parseInt(x),
                        Integer.parseInt(y), 100, 50, "Flow");//draw the node
                
                //Flow object no longer has x and y, it is a from and to
              //  FlowObject flow = new FlowObject(node, name, eq, from, to);
               // flowArrayList.add(flow);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return flowArrayList;
    }

    public static ArrayList<VariableObject> readVar(JSONParser parser, File filename) {
        ArrayList<VariableObject> varArrayList = new ArrayList();

        try {
            Object obj = parser.parse(new FileReader(filename));
            JSONObject jVar = (JSONObject) obj;
            JSONArray jVarArray = (JSONArray) jVar.get("Variables");
            Iterator it = jVarArray.iterator();
            while (it.hasNext()) {
                JSONObject Var = (JSONObject) it.next();
                String name = (String) Var.get("name");
                String desc = (String) Var.get("desc");
                String init = (String) Var.get("init");
                String x = (String) Var.get("x");
                x = x.substring(0, x.length() - 2);
                String y = (String) Var.get("y");
                y = y.substring(0, y.length() - 2);
                //Object jobj = jsonObject.get("obj");
                Object parent = graph.getDefaultParent();
                Object node = graph.insertVertex(parent, null, name, Integer.parseInt(x),
                        Integer.parseInt(y), 100, 50, "Variable");//draw the node
                VariableObject var = new VariableObject(node, name, desc, init, x, y);
                varArrayList.add(var);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return varArrayList;
    }
    /*
     public ArrowObject readArrow(){
     Object obj = parser.parse(new FileReader(filename));

     JSONObject ms = (JSONObject) obj;
        
     return arr;
     }*/

    public static String[] readSettings(JSONParser parser, File filename) {
        String[] settings = new String[3];
        try {
            Object obj = parser.parse(new FileReader(filename));
            JSONObject mSettings = (JSONObject) obj;
            JSONObject ms = (JSONObject) mSettings.get("Model Settings");
            settings[0] = (String) ms.get("init");
            settings[1] = (String) ms.get("final");
            settings[2] = (String) ms.get("timestep");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return settings;
    }
}
