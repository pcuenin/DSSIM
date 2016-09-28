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

/**
 *
 * @author paulcuenin
 */
public class FlowObject extends SuperObject {

    private String sFlowDescrip;
    private String sFlowEquation;
    // add from and to super object types
    SuperObject superObjectTo = null;
    SuperObject superObjectFrom = null;

    public FlowObject(Object graphobject, String inputname, String inputequation) {

        //flow name from user
        //sObjName = inputname;
        //jgraph object cell name
        //sObjJgraphName = graphobject.toString();
        //object given from jgraph
        //oObj = graphobject;
        //input equation from user. for rhs data building later
        super(inputname, graphobject);
        sFlowEquation = inputequation;

    }

    //for use later by gui
  

    public String getflowEquation() {
        return sFlowEquation;
    }

    public void setFlowEquation(String newequation) {
        sFlowEquation = newequation;
    }
}
