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
public class FlowObject {

        public String sFlowName;
        public String sFlowSymbol;
        public Object oFlow;
        public String sFlowEquation;
        public String sFlowJgraphName;

        public FlowObject(Object graphobject, String inputname, String inputequation) {

            //flow name from user
            sFlowName = inputname;
            //jgraph object cell name
            sFlowJgraphName = graphobject.toString();
            //object given from jgraph
            oFlow = graphobject;
            //input equation from user. for rhs data building later
            sFlowEquation = inputequation;

        }

        public String getFlowName() {
            return sFlowName;
        }

        //for use later by gui
        public void setFlowName(String newname) {
            sFlowName = newname;
        }

        public Object getFlowObject() {

            return oFlow;
        }

        public String getFlowJgraphName() {
            return sFlowJgraphName;
        }

        public String getflowEquation() {
            return sFlowEquation;
        }

        public void setFlowEquation(String newequation) {
            sFlowEquation = newequation;
        }
    }
