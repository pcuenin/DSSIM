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
public class VariableObject extends SuperObject {

    private String sVarDescrip;
    private String sVarInitial;
    //public String sFlowEquation;
    private Argument aVarArg;

    public VariableObject(Object graphobject, String inputname, String inputsymbol, String inputinitial) {
        super(inputname, graphobject);
//sObjJgraphName = graphobject.toString();
        //sObjName = inputname;
        sVarDescrip = inputsymbol;
        sVarInitial = inputinitial;
        //oObj = graphobject;
        //will cause issue if user inputs into variable a string like "x*54"
        //may use if statement to check for what type of argument constructor to use to avoid errors
        aVarArg = new Argument(inputsymbol, Double.parseDouble(inputinitial));
    }

    public Argument getVarArg() {
        return aVarArg;
    }

    public void setVarArg(String symbol, String initial) {
        aVarArg = new Argument(symbol, Double.parseDouble(initial));
    }

    public String getVarDescrip() {
        return sVarDescrip;
    }

    public void setVarDescrip(String newsymbol) {
        sVarDescrip = newsymbol;
    }

    public String getVarInitial() {
        return sVarInitial;
    }

    public void setVarInitial(String newinitial) {
        sVarInitial = newinitial;
        setVarArg(sVarDescrip, sVarInitial);
    }

}
