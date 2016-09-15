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

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import java.awt.Color;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Logan
 */
public class styleList {
    MainForm m_MainForm = null;
    
    //initailizes the Hashtables that store the data for each individual object
    Hashtable<String, Object> Flow = new Hashtable<>();
    Hashtable<String, Object> Stock = new Hashtable<>();
    Hashtable<String, Object> Arrow = new Hashtable<>();
    Hashtable<String, Object> Variable = new Hashtable<>();
    Hashtable<String, Object> ArrowLU = new Hashtable<>();
    Hashtable<String, Object> ArrowRU = new Hashtable<>();
    Hashtable<String, Object> ArrowLL = new Hashtable<>();
    Hashtable<String, Object> ArrowRL = new Hashtable<>();
    Hashtable<String, Object> ArrowU = new Hashtable<>();
    Hashtable<String, Object> ArrowD = new Hashtable<>();
    Hashtable<String, Object> ArrowL = new Hashtable<>();
    Map<String, Object> edgeStyle = new HashMap<String, Object>();
    Map<String, Object> edgeConStyle = new HashMap<String, Object>();
    
    styleList(MainForm mainform)
    {        
        //creates a style for a flow. 
        Flow.put(mxConstants.STYLE_SHAPE, mxConstants.STYLE_IMAGE);
        Flow.put(mxConstants.STYLE_IMAGE, "file:Images/FlowImg.png");
        Flow.put(mxConstants.STYLE_STROKEWIDTH, 1.5);
        Flow.put(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(new Color(0, 0, 170)));
        Flow.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM);
        
        //creates a style for a stock
        Stock.put(mxConstants.STYLE_SHAPE, mxConstants.STYLE_IMAGE);
        Stock.put(mxConstants.STYLE_IMAGE, "file:Images/StockImg.png");
        Stock.put(mxConstants.STYLE_STROKEWIDTH, 1.5);
        Stock.put(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(new Color(0, 0, 170)));
        Stock.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM);
        
        //creates a style for a right facing arrow
        Arrow.put(mxConstants.STYLE_SHAPE, mxConstants.STYLE_IMAGE);
        Arrow.put(mxConstants.STYLE_IMAGE, "file:Images/ArrowImg.png");
        Arrow.put(mxConstants.STYLE_STROKEWIDTH, 1.5);
        Arrow.put(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(new Color(0, 0, 170)));
        Arrow.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM);
        
        //creates an arrow that points to the upper left hand corner
        ArrowLU.put(mxConstants.STYLE_SHAPE, mxConstants.STYLE_IMAGE);
        ArrowLU.put(mxConstants.STYLE_IMAGE, "file:Images/ArrowImg.png");
        ArrowLU.put(mxConstants.STYLE_ROTATION,240);
        ArrowLU.put(mxConstants.STYLE_STROKEWIDTH, 1.5);
        ArrowLU.put(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(new Color(0, 0, 170)));
        ArrowLU.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM);
        
        //creates an arrow that points to the upper right hand corner
        ArrowRU.put(mxConstants.STYLE_SHAPE, mxConstants.STYLE_IMAGE);
        ArrowRU.put(mxConstants.STYLE_IMAGE, "file:Images/ArrowImg.png");
        ArrowRU.put(mxConstants.STYLE_ROTATION,300);
        ArrowRU.put(mxConstants.STYLE_STROKEWIDTH, 1.5);
        ArrowRU.put(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(new Color(0, 0, 170)));
        ArrowRU.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM);
        
        //creates an arrow that points to the lower left hand corner
        ArrowLL.put(mxConstants.STYLE_SHAPE, mxConstants.STYLE_IMAGE);
        ArrowLL.put(mxConstants.STYLE_IMAGE, "file:Images/ArrowImg.png");
        ArrowLL.put(mxConstants.STYLE_ROTATION,120);
        ArrowLL.put(mxConstants.STYLE_STROKEWIDTH, 1.5);
        ArrowLL.put(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(new Color(0, 0, 170)));
        ArrowLL.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM);
        
        //creates an arrow that points to the lower right hand corner
        ArrowRL.put(mxConstants.STYLE_SHAPE, mxConstants.STYLE_IMAGE);
        ArrowRL.put(mxConstants.STYLE_IMAGE, "file:Images/ArrowImg.png");
        ArrowRL.put(mxConstants.STYLE_ROTATION,50);
        ArrowRL.put(mxConstants.STYLE_STROKEWIDTH, 1.5);
        ArrowRL.put(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(new Color(0, 0, 170)));
        ArrowRL.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM);
        
        //creates an arrow that points straight up
        ArrowU.put(mxConstants.STYLE_SHAPE, mxConstants.STYLE_IMAGE);
        ArrowU.put(mxConstants.STYLE_IMAGE, "file:Images/ArrowImg.png");
        ArrowU.put(mxConstants.STYLE_ROTATION,270);
        ArrowU.put(mxConstants.STYLE_STROKEWIDTH, 1.5);
        ArrowU.put(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(new Color(0, 0, 170)));
        ArrowU.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM);
        
        //creates an arrow that points straight down 
        ArrowD.put(mxConstants.STYLE_SHAPE, mxConstants.STYLE_IMAGE);
        ArrowD.put(mxConstants.STYLE_IMAGE, "file:Images/ArrowImg.png");
        ArrowD.put(mxConstants.STYLE_ROTATION,90);
        ArrowD.put(mxConstants.STYLE_STROKEWIDTH, 1.5);
        ArrowD.put(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(new Color(0, 0, 170)));
        ArrowD.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM);
        
        //creates an arrow that points to the left
        ArrowL.put(mxConstants.STYLE_SHAPE, mxConstants.STYLE_IMAGE);
        ArrowL.put(mxConstants.STYLE_IMAGE, "file:Images/ArrowImg.png");
        ArrowL.put(mxConstants.STYLE_ROTATION,180);
        ArrowL.put(mxConstants.STYLE_STROKEWIDTH, 1.5);
        ArrowL.put(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(new Color(0, 0, 170)));
        ArrowL.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM);
        
        //creates a variable
        Variable.put(mxConstants.STYLE_SHAPE, mxConstants.STYLE_IMAGE);
        Variable.put(mxConstants.STYLE_IMAGE, "file:Images/VariableImg.png");
        Variable.put(mxConstants.STYLE_STROKEWIDTH, 1.5);
        Variable.put(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(new Color(0, 0, 170)));
        Variable.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_BOTTOM);
    }    
    
    public Hashtable<String, Object> getFlow(){
       Hashtable<String, Object> x = this.Flow;
        return x;
    }
    
    public Hashtable<String, Object> getStock(){
       Hashtable<String, Object> x = this.Stock;
        return x;
    }
    
    public Hashtable<String, Object> getArrow(){
       Hashtable<String, Object> x = this.Arrow;
        return x;
    }
    
    public Hashtable<String, Object> getVariable(){
        Hashtable<String, Object> x = this.Variable;
        return x;
    }
    
    public Hashtable<String, Object> getArrowLU(){
       Hashtable<String, Object> x = this.ArrowLU;
        return x;
    }
    public Hashtable<String, Object> getArrowRU(){
       Hashtable<String, Object> x = this.ArrowRU;
        return x;
    }
    
    public Hashtable<String, Object> getArrowLL(){
       Hashtable<String, Object> x = this.ArrowLL;
        return x;
    }
    
    public Hashtable<String, Object> getArrowRL(){
       Hashtable<String, Object> x = this.ArrowRL;
        return x;
    }
    
    public Hashtable<String, Object> getArrowU(){
       Hashtable<String, Object> x = this.ArrowU;
        return x;
    }
    
    public Hashtable<String, Object> getArrowD(){
       Hashtable<String, Object> x = this.ArrowD;
        return x;
    }
    
    public Hashtable<String, Object> getArrowL(){
       Hashtable<String, Object> x = this.ArrowL;
        return x;
    }
    
}

