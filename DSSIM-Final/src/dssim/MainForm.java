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
import com.orsoncharts.util.json.JSONArray;
import com.orsoncharts.util.json.JSONObject;
import dssim.gui.ArrowObject;
import dssim.gui.FlowObject;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jfree.data.xy.XYSeriesCollection;
import dssim.gui.StockObject;
import dssim.gui.VariableObject;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.parser.JSONParser;


public class MainForm extends javax.swing.JFrame {

    private mxGraphComponent graphComponent;
    protected static mxGraph graph = new mxGraph();
    private Object cell;
    int objectLoc = -1;
    private String methodChoice = "";
    private String XTitle = "";
    private String YTitle = "";
    private String graphTitle = "";
    XYSeriesCollection data;
    DefaultTableModel tableModel;
    int amountOfStocks;
    final JFileChooser fc = new JFileChooser();
    String style = "";
    styleList stylelist = new styleList(this);
    ModelSettings modelSettings = new ModelSettings();

    String inputname;
    String inputsymbol;
    String inputinitial;
    String inputequation;
    String inputValue;

    /*these ArrayLists are for keeping track of the graph objects placed by the user. 
     another method could be to keep track
     by using a hashtable. Yet, arraylists have very useful methods. */
    public ArrayList<StockObject> stockArrayList = new ArrayList<StockObject>();
    public ArrayList<FlowObject> flowArrayList = new ArrayList<FlowObject>();
    public ArrayList<VariableObject> variableArrayList = new ArrayList<VariableObject>();
    public ArrayList<ArrowObject> arrowArrayList = new ArrayList<ArrowObject>();

    /**
     * Creates new form MainForm
     */
    public MainForm() {

        initComponents();
        //This sets up the inital graph. This is what all the objects are added to. 
        graph = new mxGraph() {
            public boolean isCellMovable(Object cell) {
                return isCellsMovable() && !isCellLocked(cell) && !getModel().isVertex(getModel().getParent(cell));
            }
        ;
        };
        graph.setCellsResizable(false);
        graph.setCellsEditable(false);
        graph.setCellsDisconnectable(false);

        Object parent = graph.getDefaultParent();
        //update graph object
        graph.getModel().beginUpdate();
        try {
            mxStylesheet stylesheet = graph.getStylesheet();
        } finally {
            graph.getModel().endUpdate();
        }

        graphComponent = new mxGraphComponent(graph);
        graphComponent.setGridVisible(true);
        jScrollPane1.setViewportView(graphComponent); // might be causing issue with cursor PMC

        // allow the user to use the mouse to navigate
        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                cell = graphComponent.getCellAt(e.getX(), e.getY());
            }

            //sets up what happens when a user clicks on the graph to place an object
            public void mousePressed(MouseEvent e) {
                cell = graphComponent.getCellAt(e.getX(), e.getY());
                // different objectLoc will call different Add functions
                if (objectLoc == 1) {
                    stockInfoForm();
                    AddStock(inputname, style, inputsymbol, inputinitial, inputequation, e.getX(), e.getY());
                    objectLoc = -1;
                } else if (objectLoc == 2) {
                    AddFlow(inputname, style, e.getX(), e.getY());
                    objectLoc = -1;
                } else if (objectLoc == 3) {
                    AddArrowRight(style, e.getX(), e.getY());
                    objectLoc = -1;
                } else if (objectLoc == 4) {
                    AddVariable(style, inputname, inputsymbol, inputValue, e.getX(), e.getY());
                    objectLoc = -1;
                } else if (objectLoc == 5) {
                    AddArrowUpLft(style, e.getX(), e.getY());
                    objectLoc = -1;
                } else if (objectLoc == 6) {
                    AddArrowUpRght(style, e.getX(), e.getY());
                    objectLoc = -1;
                } else if (objectLoc == 7) {
                    AddArrowLwLft(style, e.getX(), e.getY());
                    objectLoc = -1;
                } else if (objectLoc == 8) {
                    AddArrowLwRght(style, e.getX(), e.getY());
                    objectLoc = -1;
                } else if (objectLoc == 9) {
                    AddArrowUp(style, e.getX(), e.getY());
                    objectLoc = -1;
                } else if (objectLoc == 10) {
                    AddArrowDwn(style, e.getX(), e.getY());
                    objectLoc = -1;
                } else if (objectLoc == 11) {
                    AddArrowLft(style, e.getX(), e.getY());
                    objectLoc = -1;
                } else {
                    //objectLoc should always be equal to -1 when not placing an object
                    objectLoc = -1;
                }
            }
        });

        //This initalizes the stylesheets from stylelist.java class for each object
        graph.getStylesheet().putCellStyle("Stock", stylelist.getStock());
        graph.getStylesheet().putCellStyle("Flow", stylelist.getFlow());
        graph.getStylesheet().putCellStyle("Arrow", stylelist.getArrow());
        graph.getStylesheet().putCellStyle("ArrowLU", stylelist.getArrowLU());
        graph.getStylesheet().putCellStyle("ArrowRU", stylelist.getArrowRU());
        graph.getStylesheet().putCellStyle("ArrowLL", stylelist.getArrowLL());
        graph.getStylesheet().putCellStyle("ArrowRL", stylelist.getArrowRL());
        graph.getStylesheet().putCellStyle("ArrowU", stylelist.getArrowU());
        graph.getStylesheet().putCellStyle("ArrowD", stylelist.getArrowD());
        graph.getStylesheet().putCellStyle("ArrowL", stylelist.getArrowL());
        graph.getStylesheet().putCellStyle("Variable", stylelist.getVariable());
    }

    // paul added this PMC 9-27-16
    public void CustomCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image img = toolkit.getImage("lilcombo.png");
        Point point = new Point(0, 0);
        Cursor cursor = toolkit.createCustomCursor(img, point, "Cursor");

        graphComponent.setCursor(cursor); // still doesn't work PMC
    }

//This method will add a stock to the graph
   void AddStock(String name, String styleName,
            String inputsymbol, String inputinitial, String inputequation, int x, int y) {
        Object parent = graph.getDefaultParent();
        graphComponent.setConnectable(false);
        graph.setCellsBendable(false);
        graph.setCellsCloneable(false);
        graph.getModel().beginUpdate();
        //stockObject is created and added to the stockArrayList
        Object node = graph.insertVertex(parent, null, name, x, y, 100, 50, styleName);//draw the node

        graph.getModel().endUpdate();
        StockObject stockobject = new StockObject(node, name, inputsymbol, inputinitial,x+"",y+"");
        stockArrayList.add(stockobject);
    }
   // This method will add a stock to the graph from a save file
    void AddStock(StockObject stock) {
        //Object parent = graph.getDefaultParent();
        graphComponent.setConnectable(false);
        graph.setCellsBendable(false);
        graph.setCellsCloneable(false);
        graph.getModel().beginUpdate();
        //stockObject is created and added to the stockArrayList
       /* Object node = graph.insertVertex(parent, null, stock.getStockName(), Integer.parseInt(stock.getStockX()), 
                Integer.parseInt(stock.getStockY()), 100, 50, "Stock");//draw the node
        */
        graph.getModel().endUpdate();
        /*StockObject stockobject = new StockObject(node,stock.getStockName(), stock.getStockDescrip(), 
                stock.getStockInitial(),stock.getStockX(),stock.getStockY());
        stockArrayList.add(stockobject);*/
    }
// This method will add a flow to the graph from the user input
    void AddFlow(String name, String styleName, int x, int y) {
        Object parent = graph.getDefaultParent();
        graphComponent.setConnectable(false);
        graph.setCellsBendable(false);
        graph.setCellsCloneable(false);
        graph.getModel().beginUpdate();
        try {
            //flowObject is added to the flowArrayList
            Object node = graph.insertVertex(parent, null, name, x, y, 100, 50, styleName);//draw the node
            FlowObject flowobject = new FlowObject(node, inputname, inputequation,x+"",y+"");
            flowArrayList.add(flowobject);
            
        } finally {
            graph.getModel().endUpdate();
        }
    }
// This method will add a flow to the graph from a save file
    void AddFlow(FlowObject flow) {
        //Object parent = graph.getDefaultParent();
        graphComponent.setConnectable(false);
        graph.setCellsBendable(false);
        graph.setCellsCloneable(false);
        graph.getModel().beginUpdate();
            graph.getModel().endUpdate();
        
    }
// This method will add a right pointing arrow to the graph

    void AddArrowRight(String styleName, int x, int y) {
        Object parent = graph.getDefaultParent();
        graphComponent.setConnectable(false);
        graph.setCellsCloneable(false);
        graph.setCellsBendable(false);

        graph.getModel().beginUpdate();
        try {
            Object node = graph.insertVertex(parent, null, null, x, y, 100, 50, styleName);//draw the node
        } finally {
            graph.getModel().endUpdate();
        }
    }
//This method will add a upper left pointing arrow

    void AddArrowUpLft(String styleName, int x, int y) {
        Object parent = graph.getDefaultParent();
        graphComponent.setConnectable(false);
        graph.setCellsCloneable(false);
        graph.setCellsBendable(false);

        graph.getModel().beginUpdate();
        try {
            Object node = graph.insertVertex(parent, null, null, x, y, 100, 50, styleName);//draw the node
        } finally {
            graph.getModel().endUpdate();
        }
    }

//This method will add an upper right pointing arrow
    void AddArrowUpRght(String styleName, int x, int y) {
        Object parent = graph.getDefaultParent();
        graphComponent.setConnectable(false);
        graph.setCellsCloneable(false);
        graph.setCellsBendable(false);

        graph.getModel().beginUpdate();
        try {
            Object node = graph.insertVertex(parent, null, null, x, y, 100, 50, styleName);//draw the node
        } finally {
            graph.getModel().endUpdate();
        }
    }
//This method will add a lower left pointing arrow

    void AddArrowLwLft(String styleName, int x, int y) {
        Object parent = graph.getDefaultParent();
        graphComponent.setConnectable(false);
        graph.setCellsCloneable(false);
        graph.setCellsBendable(false);

        graph.getModel().beginUpdate();
        try {
            Object node = graph.insertVertex(parent, null, null, x, y, 100, 50, styleName);//draw the node
        } finally {
            graph.getModel().endUpdate();
        }
    }
//This method will add a lower right pointing arrow

    void AddArrowLwRght(String styleName, int x, int y) {
        Object parent = graph.getDefaultParent();
        graphComponent.setConnectable(false);
        graph.setCellsCloneable(false);
        graph.setCellsBendable(false);

        graph.getModel().beginUpdate();
        try {
            Object node = graph.insertVertex(parent, null, null, x, y, 100, 50, styleName);//draw the node
        } finally {
            graph.getModel().endUpdate();
        }
    }
//This method will add an up pointing arrow

    void AddArrowUp(String styleName, int x, int y) {
        Object parent = graph.getDefaultParent();
        graphComponent.setConnectable(false);
        graph.setCellsCloneable(false);
        graph.setCellsBendable(false);

        graph.getModel().beginUpdate();
        try {
            Object node = graph.insertVertex(parent, null, null, x, y, 100, 50, styleName);//draw the node
        } finally {
            graph.getModel().endUpdate();
        }
    }
//This method will add a down pointing arrow

    void AddArrowDwn(String styleName, int x, int y) {
        Object parent = graph.getDefaultParent();
        graphComponent.setConnectable(false);
        graph.setCellsCloneable(false);
        graph.setCellsBendable(false);

        graph.getModel().beginUpdate();
        try {
            Object node = graph.insertVertex(parent, null, null, x, y, 100, 50, styleName);//draw the node
        } finally {
            graph.getModel().endUpdate();

        }
    }
//This method will add a left pointing arrow

    void AddArrowLft(String styleName, int x, int y) {
        Object parent = graph.getDefaultParent();
        graphComponent.setConnectable(false);
        graph.setCellsCloneable(false);
        graph.setCellsBendable(false);

        graph.getModel().beginUpdate();
        try {
            Object node = graph.insertVertex(parent, null, null, x, y, 100, 50, styleName);//draw the node
        } finally {
            graph.getModel().endUpdate();
        }
    }
// This method will add a variable to the graph

    void AddVariable(String styleName, String inputName,
            String inputSymbol, String inputInitial, int x, int y) {
        Object parent = graph.getDefaultParent();
        graphComponent.setConnectable(true);
        graph.setCellsCloneable(false);
        graph.setCellsBendable(false);

        graph.getModel().beginUpdate();
        try {
            Object node = graph.insertVertex(parent, null, inputName, x, y, 100, 50, styleName);//draw the node
            VariableObject variableobject = new VariableObject(node, inputName,
                    inputSymbol, inputInitial,x+"",y+"");
            variableArrayList.add(variableobject);
        } finally {
            graph.getModel().endUpdate();
        }
    }
    void AddVariable(VariableObject var) {
        
        graphComponent.setConnectable(true);
        graph.setCellsCloneable(false);
        graph.setCellsBendable(false);
        graph.getModel().beginUpdate();

            graph.getModel().endUpdate();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")   // what does this do? -PMC
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jSeparator4 = new javax.swing.JSeparator();
        stockBtn = new javax.swing.JButton();
        flowBtn = new javax.swing.JButton();
        arrowComboBox = new javax.swing.JComboBox<String>();
        variableBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JToggleButton();
        modelSettingsBtn = new javax.swing.JButton();
        runSimBtn = new javax.swing.JButton();
        viewGraphButton = new javax.swing.JButton();
        viewTableButton = new javax.swing.JButton();
        analysisMethodLabel = new javax.swing.JLabel();
        methodChoiceCombo = new javax.swing.JComboBox();
        showAllVariableBtn = new javax.swing.JButton();
        closeBtn = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        FileMenu = new javax.swing.JMenu();
        SaveMenuItem = new javax.swing.JMenuItem();
        OpenMenuItem = new javax.swing.JMenuItem();
        PrintMenuItem = new javax.swing.JMenuItem();
        EditMenu = new javax.swing.JMenu();
        DeleteMenuItem = new javax.swing.JMenuItem();
        AddMenu = new javax.swing.JMenu();
        StockMenuItem = new javax.swing.JMenuItem();
        FlowMenuItem = new javax.swing.JMenuItem();
        arrowTypes = new javax.swing.JMenu();
        arrowRMenuItem = new javax.swing.JMenuItem();
        arrowLMenuItem = new javax.swing.JMenuItem();
        arrowRUMenuItem = new javax.swing.JMenuItem();
        arrowLUMenuItem = new javax.swing.JMenuItem();
        arrowRLMenuItem = new javax.swing.JMenuItem();
        arrowLLMenuItem = new javax.swing.JMenuItem();
        arrowUMenuItem = new javax.swing.JMenuItem();
        arrowDMenuItem = new javax.swing.JMenuItem();
        VariableMenuItem = new javax.swing.JMenuItem();
        HelpMenu = new javax.swing.JMenu();
        AboutMenuItem = new javax.swing.JMenuItem();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jButton3.setText("jButton3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jScrollPane1.setForeground(new java.awt.Color(255, 255, 255));

        jSeparator4.setBackground(new java.awt.Color(51, 51, 51));
        jSeparator4.setForeground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator4))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );

        stockBtn.setText("Stock");
        stockBtn.setToolTipText("Create Stock");
        stockBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stockBtnActionPerformed(evt);
            }
        });

        flowBtn.setText("Flow");
        flowBtn.setToolTipText("Create Flow");
        flowBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                flowBtnActionPerformed(evt);
            }
        });

        arrowComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Right Arrow", "Upper Left Arrow", "Upper Right Arrow", "Lower Left Arrow", "Lower Right Arrow", "Up Arrow", "Down Arrow", "Left Arrow" }));
        arrowComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrowComboBoxActionPerformed(evt);
            }
        });

        variableBtn.setText("Variable");
        variableBtn.setToolTipText("Add Variable");
        variableBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                variableBtnActionPerformed(evt);
            }
        });

        deleteBtn.setText("Delete");
        deleteBtn.setToolTipText("Delete Selected Item");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        modelSettingsBtn.setText("<html> <div align=\"center\">\nModel <br>  Settings </div></html>");
        modelSettingsBtn.setToolTipText("Change Model Settings");
        modelSettingsBtn.setAlignmentY(0.0F);
        modelSettingsBtn.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        modelSettingsBtn.setIconTextGap(0);
        modelSettingsBtn.setMargin(new java.awt.Insets(0, 0, 0, 0));
        modelSettingsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modelSettingsBtnActionPerformed(evt);
            }
        });

        runSimBtn.setText("Run");
        runSimBtn.setToolTipText("Run Simulation");
        runSimBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runSimBtnActionPerformed(evt);
            }
        });

        viewGraphButton.setText("Graph");
        viewGraphButton.setToolTipText("View Graph");
        viewGraphButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewGraphButtonActionPerformed(evt);
            }
        });

        viewTableButton.setText("Table");
        viewTableButton.setToolTipText("View Table");
        viewTableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewTableButtonActionPerformed(evt);
            }
        });

        analysisMethodLabel.setText("Analysis Method");

        methodChoiceCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Eulers", "Runge-Kutta 2", "Runge Kutta 4" }));
        methodChoiceCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                methodChoiceComboActionPerformed(evt);
            }
        });

        showAllVariableBtn.setText("<html>  <div align=\"center\"> All<br> Inputs</div></html>");
        showAllVariableBtn.setToolTipText("Show All Inputs");
        showAllVariableBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showAllVariableBtnActionPerformed(evt);
            }
        });

        closeBtn.setText("Close");
        closeBtn.setToolTipText("Close Program");
        closeBtn.setMaximumSize(new java.awt.Dimension(107, 23));
        closeBtn.setMinimumSize(new java.awt.Dimension(107, 23));
        closeBtn.setPreferredSize(new java.awt.Dimension(107, 23));
        closeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeBtnActionPerformed(evt);
            }
        });

        FileMenu.setText("File");

        SaveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        SaveMenuItem.setText("Save");
        SaveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(SaveMenuItem);

        OpenMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        OpenMenuItem.setText("Open");
        OpenMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(OpenMenuItem);

        PrintMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        PrintMenuItem.setText("Print");
        PrintMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrintMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(PrintMenuItem);

        jMenuBar1.add(FileMenu);

        EditMenu.setText("Edit");

        DeleteMenuItem.setText("Delete");
        DeleteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteMenuItemActionPerformed(evt);
            }
        });
        EditMenu.add(DeleteMenuItem);

        AddMenu.setText("Add");
        AddMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddMenuActionPerformed(evt);
            }
        });

        StockMenuItem.setText("Stock");
        StockMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StockMenuItemActionPerformed(evt);
            }
        });
        AddMenu.add(StockMenuItem);

        FlowMenuItem.setText("Flow");
        FlowMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FlowMenuItemActionPerformed(evt);
            }
        });
        AddMenu.add(FlowMenuItem);

        arrowTypes.setText("Arrow");

        arrowRMenuItem.setText("Right Arrow");
        arrowRMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrowRMenuItemActionPerformed(evt);
            }
        });
        arrowTypes.add(arrowRMenuItem);

        arrowLMenuItem.setText("Left Arrow");
        arrowLMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrowLMenuItemActionPerformed(evt);
            }
        });
        arrowTypes.add(arrowLMenuItem);

        arrowRUMenuItem.setText("Upper Right Arrow");
        arrowRUMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrowRUMenuItemActionPerformed(evt);
            }
        });
        arrowTypes.add(arrowRUMenuItem);

        arrowLUMenuItem.setText("Upper Left Arrow");
        arrowLUMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrowLUMenuItemActionPerformed(evt);
            }
        });
        arrowTypes.add(arrowLUMenuItem);

        arrowRLMenuItem.setText("Lower Right Arrow");
        arrowRLMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrowRLMenuItemActionPerformed(evt);
            }
        });
        arrowTypes.add(arrowRLMenuItem);

        arrowLLMenuItem.setText("Lower Left Arrow");
        arrowLLMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrowLLMenuItemActionPerformed(evt);
            }
        });
        arrowTypes.add(arrowLLMenuItem);

        arrowUMenuItem.setText("Up Arrow");
        arrowUMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrowUMenuItemActionPerformed(evt);
            }
        });
        arrowTypes.add(arrowUMenuItem);

        arrowDMenuItem.setText("Down Arrow");
        arrowDMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrowDMenuItemActionPerformed(evt);
            }
        });
        arrowTypes.add(arrowDMenuItem);

        AddMenu.add(arrowTypes);

        VariableMenuItem.setText("Variable");
        VariableMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VariableMenuItemActionPerformed(evt);
            }
        });
        AddMenu.add(VariableMenuItem);

        EditMenu.add(AddMenu);

        jMenuBar1.add(EditMenu);

        HelpMenu.setText("About");

        AboutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        AboutMenuItem.setText("About this program");
        AboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AboutMenuItemActionPerformed(evt);
            }
        });
        HelpMenu.add(AboutMenuItem);

        jMenuBar1.add(HelpMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(stockBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(flowBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(arrowComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(variableBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(modelSettingsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(runSimBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(viewGraphButton, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(viewTableButton, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(analysisMethodLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(methodChoiceCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(showAllVariableBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(closeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1131, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(stockBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(flowBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(arrowComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(variableBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(closeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(runSimBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(viewGraphButton, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(viewTableButton, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(modelSettingsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(showAllVariableBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(analysisMethodLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(methodChoiceCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)))
                .addContainerGap())
        );

        runSimBtn.getAccessibleContext().setAccessibleName("Run \nSimulation");
        methodChoiceCombo.getAccessibleContext().setAccessibleName("");
        methodChoiceCombo.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SaveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveMenuItemActionPerformed
        // TODO add your handling code here:
        //Brings up file chooser as to where to save objects
        JSONSave savefile = new JSONSave();
        int returnVal = fc.showSaveDialog(MainForm.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {  
              
            // Writing to a file  
           JFileChooser chooser=new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.showSaveDialog(null);

            String path=chooser.getSelectedFile().getAbsolutePath();
            String filename=chooser.getSelectedFile().getName();
            File newSaveFile=new File(path,filename);  
            newSaveFile.createNewFile(); 
            FileWriter fileWriter = new FileWriter(newSaveFile);  //temp hardcoded file location
            
            fileWriter.write("{ \n"); 
            fileWriter.write("\"Stocks\" : \n"); 
            System.out.println("Writing JSON objects to file");  
            System.out.println("-----------------------"); 
            JSONArray stockList = new JSONArray();
            for (int i = 0; i < stockArrayList.size(); i++) {
                JSONObject stockObj = savefile.saveStock(stockArrayList.get(i));
                stockList.add(stockObj);
            }        
            fileWriter.write(stockList.toJSONString()+", \n"); 
            fileWriter.write("\n");
            fileWriter.write("\"Flows\" : \n"); 
            fileWriter.flush();
            JSONArray flowList = new JSONArray();
            for (int i = 0; i < flowArrayList.size(); i++)
            {
               JSONObject flowObj = savefile.saveFlow(flowArrayList.get(i));
               flowList.add(flowObj);
            }
            
            fileWriter.write(flowList.toJSONString()+", \n"); 
            fileWriter.write("\n"); 
            fileWriter.write("\"Variables\" : \n"); 
            fileWriter.flush();
            JSONArray varList = new JSONArray();
            for (int i = 0; i < variableArrayList.size(); i++)
            {
               JSONObject varObj = savefile.saveVar(variableArrayList.get(i));
               varList.add(varObj);
            }
            fileWriter.write(varList.toJSONString()+"\n");  
            fileWriter.flush();
            for (int i = 0; i < arrowArrayList.size(); i++)
            {
               
                
            }   
            fileWriter.write("\n"); 
            fileWriter.write("\"Model Settings\" : \n"); 
            JSONObject settings = savefile.saveSettings(modelSettings);
            fileWriter.write(settings.toJSONString()+", \n");  
            fileWriter.write("}");   
            fileWriter.close();
            System.out.println("Finished writing file");
  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
            
        }
    }//GEN-LAST:event_SaveMenuItemActionPerformed

    private void AboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AboutMenuItemActionPerformed
        // TODO add your handling code here:
        //Displays the dialog box with information about the program
        JOptionPane.showMessageDialog(null, "This program was developed by the super awesome talents of"
                + " \nLogan Bautista, Jeleshia Freeman, TJ Shedd and Taylor Wilcox. "
                + "\nThey are part of the graduating class of April 2016! Now Paul and Kamren are working on it.");
    }//GEN-LAST:event_AboutMenuItemActionPerformed

    private void runSimBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runSimBtnActionPerformed

        Methods method2 = new Methods();
        
        for (int i = 0; i < stockArrayList.size(); i++) {
            String s = stockArrayList.get(i).getObjName();
            final JTextField stockName = new JTextField(s);
            final JTextField stockSymbol = new JTextField(stockArrayList.get(i).getStockDescrip());
            final JTextField stockInitial = new JTextField(stockArrayList.get(i).getStockInitial());
            int cnt = i;
            stockArrayList.get(cnt).setObjName(stockName.getText());
            stockArrayList.get(cnt).setStockDescrip(stockSymbol.getText());
            stockArrayList.get(cnt).setStockInitial(stockInitial.getText());
            stockArrayList.get(cnt).setStockArg(stockSymbol.getText(), stockInitial.getText());
        }
        //one improvement is to make things like Double.parseDouble(modelSettings.getFinalTime() static variables
       /* Object node = graph.createVertex(null, null, "t", 0, 0, 100, 50, "Time");
        VariableObject time = new VariableObject(node,"t","t",modelSettings.getInitialTime(),"0","0");
        variableArrayList.add(time);*/
        method2 = new Methods((ArrayList) stockArrayList, flowArrayList, variableArrayList,
                Double.parseDouble(modelSettings.getInitialTime()),
                Double.parseDouble(modelSettings.getFinalTime()), Double.parseDouble(modelSettings.getTimeStep()),
                methodChoice);
        //try to reset data
        tableModel = null;
        //call the table model
        tableModel = method2.getTable();
        //try to reset data
        data = null;
        //call the data model
        data = method2.returnData();
    }//GEN-LAST:event_runSimBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        // TODO add your handling code here:
        graph.getModel().remove(cell);
        System.out.println(cell.toString());
        //each of these forloops is a way for the system to go through each graph object and check if it is the one that
        //needs to be deleted. The main key references here are the graphobject cell name kept in the arraylist of that object.
        for (int i = 0; i < stockArrayList.size(); i++) {

            if (stockArrayList.get(i).getObjJgraphName().equals(cell.toString())) {
                stockArrayList.remove(i);
            }
        }
        for (int i = 0; i < flowArrayList.size(); i++) {

            if (flowArrayList.get(i).getObjJgraphName().equals(cell.toString())) {
                flowArrayList.remove(i);
            }
        }
        for (int i = 0; i < arrowArrayList.size(); i++) {

            if (arrowArrayList.get(i).getObjJgraphName().equals(cell.toString())) {
                arrowArrayList.remove(i);
            }
        }
        for (int i = 0; i < variableArrayList.size(); i++) {

            if (variableArrayList.get(i).getObjJgraphName().equals(cell.toString())) {
                variableArrayList.remove(i);
            }
        }
        deleteBtn.setSelected(false);
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void stockBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stockBtnActionPerformed
        // TODO add your handling code here: PMC
        //increment amount of stocks, used for panels and default names
        //changed amount of stocks a different way, like by taking the size of the stockArrayList

        style = "Stock";
        CustomCursor();

        /* I commented this out to see if I could move the form later in the flow
         //check if the user would like to input information
         JPanel check = new JPanel(); //create box
         check.setLayout(new BoxLayout(check, BoxLayout.PAGE_AXIS)); // center the box
         check.add(new JLabel("Would you like to input information for this stock?")); // ask them question
         int yN = JOptionPane.showConfirmDialog(null, check);
         if (yN == 0) {
         //if yes, get info
         //Base values in case someone hits enter to fast, so it wont throw errors
         JTextField stockName = new JTextField("Stock" + stockArrayList.size());
         JTextField stockInitial = new JTextField(Double.toString(0.0));
         JTextField stockDescrip = new JTextField("Stock" + Integer.toString(stockArrayList.size()));
         JPanel addStock = new JPanel();
         addStock.setLayout(new BoxLayout(addStock, BoxLayout.PAGE_AXIS));
         addStock.add(new JLabel("Stock Name"));
         addStock.add(stockName);
         addStock.add(new JLabel("Stock Symbol"));
         addStock.add(stockDescrip);
         addStock.add(new JLabel("Stock Initial Value"));
         addStock.add(stockInitial);

         int option = JOptionPane.showConfirmDialog(null, addStock);
         inputname = stockName.getText();
         inputsymbol = stockDescrip.getText();
         inputinitial = stockInitial.getText();
         } //if no, set blank or default
         else {
         //blank values, stock name increments
         inputname = "Stock" + stockArrayList.size();
         //these can NOT be "", they show as null and go haywire
         inputinitial = Double.toString(0.0);
         inputsymbol = "Stock" + Integer.toString(stockArrayList.size());
         }
         //updates objectLoc for mouse event on where to place the object on the graph
         */
        objectLoc = 1;

    }//GEN-LAST:event_stockBtnActionPerformed
    private void stockInfoForm() {

        //check if the user would like to input information
        /*JPanel check = new JPanel(); //create box
         check.setLayout(new BoxLayout(check, BoxLayout.PAGE_AXIS)); // center the box
         check.add(new JLabel("Would you like to input information for this stock?")); // ask them question
         int yN = JOptionPane.showConfirmDialog(null, check);
         if (yN == 0) {*/
        //if yes, get info
        //Base values in case someone hits enter to fast, so it wont throw errors
        JTextField stockName = new JTextField("Stock" + stockArrayList.size());
        JTextField stockInitial = new JTextField(Double.toString(0.0));
        JTextField stockDescrip = new JTextField("Stock" + Integer.toString(stockArrayList.size()));
        JPanel addStock = new JPanel();
        addStock.setLayout(new BoxLayout(addStock, BoxLayout.PAGE_AXIS));
        addStock.add(new JLabel("Stock Name"));
        addStock.add(stockName);
        addStock.add(new JLabel("Stock Description"));
        addStock.add(stockDescrip);
        addStock.add(new JLabel("Stock Initial Value"));
        addStock.add(stockInitial);

        int option = JOptionPane.showConfirmDialog(null, addStock);
        inputname = stockName.getText();
        inputsymbol = stockDescrip.getText();
        inputinitial = stockInitial.getText();

        /*} //if no, set blank or default
         else {
         //blank values, stock name increments
         inputname = "Stock" + stockArrayList.size();
         //these can NOT be "", they show as null and go haywire
         inputinitial = Double.toString(0.0);
         inputsymbol = "Stock" + Integer.toString(stockArrayList.size());
         }*/
        //updates objectLoc for mouse event on where to place the object on the graph
    }
    private void modelSettingsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modelSettingsBtnActionPerformed
        // TODO add your handling code here:
        //Creates a new JFrame that displays the GUI from ModelSettings.java class

        if (!modelSettings.isFrameAvailable()) {
            JFrame jf = new JFrame();
            jf.add(modelSettings);
            modelSettings.setFrame(jf);
            jf.setSize(340, 270);
            jf.setResizable(false);
            jf.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }

        modelSettings.getFrame().setVisible(true);

    }//GEN-LAST:event_modelSettingsBtnActionPerformed

    private void variableBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_variableBtnActionPerformed
        // TODO add your handling code here:
        style = "Variable";
        //updates objectLoc for mouse event on where to place the object on the graph
        objectLoc = 4;
        inputValue = "0.0";
        //check if the user would like to input the information about the variable
        JPanel check = new JPanel();
        check.setLayout(new BoxLayout(check, BoxLayout.PAGE_AXIS));
        check.add(new JLabel("Would you like to input information for this Variable?"));
        int yN = JOptionPane.showConfirmDialog(null, check);
        if (yN == 0) {
            //if yes, get info
            //Base values in case someone hits enter to fast, so it wont throw errors
            JTextField varName = new JTextField("Variable" + variableArrayList.size());
            JTextField varSymbol = new JTextField("Variable" + variableArrayList.size());
            JTextField varValue = new JTextField("0.0");
            JPanel addVar = new JPanel();
            addVar.setLayout(new BoxLayout(addVar, BoxLayout.PAGE_AXIS));
            addVar.add(new JLabel("Variable Name"));
            addVar.add(varName);
            addVar.add(new JLabel("Variable Symbol"));
            addVar.add(varSymbol);
            addVar.add(new JLabel("Variable Value"));
            addVar.add(varValue);
            int option = JOptionPane.showConfirmDialog(null, addVar);
            inputname = varName.getText();
            inputValue = varValue.getText();
            inputsymbol = varSymbol.getText();
        } else {
            //blank values, variable name increments
            inputname = "Variable" + variableArrayList.size();
            //these can NOT be "", they show as null and go haywire
            inputValue = "0.0";
            inputsymbol = "v" + +variableArrayList.size();
        }
    }//GEN-LAST:event_variableBtnActionPerformed

    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeBtnActionPerformed
        // TODO add your handling code here:
        //Closes the GUI
        System.exit(0);
    }//GEN-LAST:event_closeBtnActionPerformed

    private void StockMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StockMenuItemActionPerformed
        /*
         // TODO add your handling code here: What is this here? PMC
         //method is the same as the StockBtn above. It is just an extra one in the top menus
         //increment amount of stocks, used for panels and default names
         style = "Stock";
         //check if the user would like to input information
         JPanel check = new JPanel();
         check.setLayout(new BoxLayout(check, BoxLayout.PAGE_AXIS));
         check.add(new JLabel("Would you like to input information for this stock?"));
         int yN = JOptionPane.showConfirmDialog(null, check);
         if (yN == 0) {
         //if yes, get info
         //Base values in case someone hits enter to fast, so it wont throw errors
         JTextField stockName = new JTextField("Stock" + stockArrayList.size());
         JTextField stockInitial = new JTextField(Double.toString(0.0));
         JTextField stockDescrip = new JTextField("Stock" + Integer.toString(stockArrayList.size()));
         JPanel addStock = new JPanel();
         addStock.setLayout(new BoxLayout(addStock, BoxLayout.PAGE_AXIS));
         addStock.add(new JLabel("Stock Name"));
         addStock.add(stockName);
         addStock.add(new JLabel("Stock Symbol"));
         addStock.add(stockDescrip);
         addStock.add(new JLabel("Stock Initial Value"));
         addStock.add(stockInitial);
         int option = JOptionPane.showConfirmDialog(null, addStock);
         inputname = stockName.getText();
         inputsymbol = stockDescrip.getText();
         inputinitial = stockInitial.getText();
         } //if no, set blank or default
         else {
         //blank values, stock name increments
         inputname = "Stock" + stockArrayList.size();
         //these can NOT be "", they show as null and go haywire
         inputinitial = Double.toString(0.0);
         inputsymbol = "Stock" + Integer.toString(stockArrayList.size());
         }
         objectLoc = 1;
         //old code for posterety */
        stockBtnActionPerformed(evt);

    }//GEN-LAST:event_StockMenuItemActionPerformed
    public static mxGraph getGraph(){
        return graph;
    }
    
    private void DeleteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteMenuItemActionPerformed
        // TODO add your handling code here:

        //delete method is the same as the delete above. It is just an extra one in the top menus
        graph.getModel().remove(cell);
        System.out.println(cell.toString());

        for (int i = 0; i < stockArrayList.size(); i++) {

            if (stockArrayList.get(i).getObjJgraphName().equals(cell.toString())) {
                stockArrayList.remove(i);
            }
        }
        for (int i = 0; i < flowArrayList.size(); i++) {

            if (flowArrayList.get(i).getObjJgraphName().equals(cell.toString())) {
                flowArrayList.remove(i);
            }
        }
        for (int i = 0; i < arrowArrayList.size(); i++) {

            if (arrowArrayList.get(i).getObjJgraphName().equals(cell.toString())) {
                arrowArrayList.remove(i);
            }
        }
        for (int i = 0; i < variableArrayList.size(); i++) {

            if (variableArrayList.get(i).getObjJgraphName().equals(cell.toString())) {
                variableArrayList.remove(i);
            }
        }
        deleteBtn.setSelected(false);
    }//GEN-LAST:event_DeleteMenuItemActionPerformed

    private void methodChoiceComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_methodChoiceComboActionPerformed
        if (methodChoiceCombo.getSelectedIndex() == 0) {
            methodChoice = "eulers";
        } else if (methodChoiceCombo.getSelectedIndex() == 1) {
            methodChoice = "rk2";
        } else if (methodChoiceCombo.getSelectedIndex() == 2) {
            methodChoice = "rk4";
        }
    }//GEN-LAST:event_methodChoiceComboActionPerformed

    private void viewGraphButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewGraphButtonActionPerformed
        //See if user would like to name the graph labels
        //if blank, ask if they would like to input

        //if the graph titles are default, ask if they would like to input information
        if (XTitle.equals("") && YTitle.equals("") && graphTitle.equals("")) {
            JPanel check = new JPanel();
            check.setLayout(new BoxLayout(check, BoxLayout.PAGE_AXIS));
            check.add(new JLabel("Would you like to label the graph?"));
            int yN = JOptionPane.showConfirmDialog(null, check);
            if (yN == 0) {
                //if yes get info
                JTextField graphTitleField = new JTextField(25);
                JTextField XTitleField = new JTextField(25);
                JTextField YTitleField = new JTextField(25);
                JPanel graphLabels = new JPanel();
                graphLabels.setLayout(new BoxLayout(graphLabels, BoxLayout.PAGE_AXIS));
                graphLabels.add(new JLabel("graph's title."));
                graphLabels.add(graphTitleField);
                graphLabels.add(new JLabel("X axis title."));
                graphLabels.add(XTitleField);
                graphLabels.add(new JLabel("Y axis title."));
                graphLabels.add(YTitleField);
                JOptionPane.showConfirmDialog(null, graphLabels);
                graphTitle = graphTitleField.getText();
                XTitle = XTitleField.getText();
                YTitle = YTitleField.getText();
                final GraphThis graph = new GraphThis("DSSIM Graph", data, graphTitle, XTitle, YTitle, tableModel);
            } //if no, just run with the blank values or preset values
            else {
                final GraphThis graph = new GraphThis("DSSIM Graph", data, graphTitle, XTitle, YTitle, tableModel);
            }
        } else {
            //if there are values, ask if they would like to change them
            JPanel check = new JPanel();
            check.setLayout(new BoxLayout(check, BoxLayout.PAGE_AXIS));
            check.add(new JLabel("Would you like to change the labels on the graph?"));
            int yN = JOptionPane.showConfirmDialog(null, check);
            if (yN == 0) {
                //if yes get info
                JTextField graphTitleField = new JTextField(25);
                JTextField XTitleField = new JTextField(25);
                JTextField YTitleField = new JTextField(25);
                JPanel graphLabels = new JPanel();
                graphLabels.setLayout(new BoxLayout(graphLabels, BoxLayout.PAGE_AXIS));
                graphLabels.add(new JLabel("graph's title."));
                graphLabels.add(graphTitleField);
                graphLabels.add(new JLabel("X axis title."));
                graphLabels.add(XTitleField);
                graphLabels.add(new JLabel("Y axis title."));
                graphLabels.add(YTitleField);
                JOptionPane.showConfirmDialog(null, graphLabels);
                graphTitle = graphTitleField.getText();
                XTitle = XTitleField.getText();
                YTitle = YTitleField.getText();
                final GraphThis graph = new GraphThis("DSSIM Graph", data, graphTitle, XTitle, YTitle, tableModel);
            } //if no, just run with the blank values or preset values
            else {
                final GraphThis graph = new GraphThis("DSSIM Graph", data, graphTitle, XTitle, YTitle, tableModel);
            }
        }
    }//GEN-LAST:event_viewGraphButtonActionPerformed

    private void viewTableButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewTableButtonActionPerformed
        final Table table = new Table(tableModel);
        //call table model and send it the global table variable
    }//GEN-LAST:event_viewTableButtonActionPerformed

    private void flowBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_flowBtnActionPerformed
        // TODO add your handling code here:
        style = "Flow";
        JPanel check = new JPanel();
        ////see if user would like to input information
        check.setLayout(new BoxLayout(check, BoxLayout.PAGE_AXIS));
        check.add(new JLabel("Would you like to input information for this Flow?"));
        int yN = JOptionPane.showConfirmDialog(null, check);
        if (yN == 0) {
            //if yes, get info
            //Base values in case someone hits enter to fast, so it wont throw errors
            JTextField flowName = new JTextField("Flow" + flowArrayList.size());
            JTextField flowEquation = new JTextField("");
            JPanel addFlow = new JPanel();
            addFlow.setLayout(new BoxLayout(addFlow, BoxLayout.PAGE_AXIS));
            addFlow.add(new JLabel("Flow Name"));
            addFlow.add(flowName);
            addFlow.add(new JLabel("Flow Equation"));
            addFlow.add(flowEquation);
            int option = JOptionPane.showConfirmDialog(null, addFlow);
            inputname = flowName.getText();
            inputequation = flowEquation.getText();
        } else {
            //blank values, stock name increments
            inputname = "Flow" + flowArrayList.size();
            //these can NOT be "", they show as null and go haywire
            inputequation = "";
        }
        objectLoc = 2;
        //AddFlow(response, style);
    }//GEN-LAST:event_flowBtnActionPerformed

    private void AddMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AddMenuActionPerformed

    private void FlowMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FlowMenuItemActionPerformed
        // TODO add your handling code here:
        //This method is the same ad the flowBtn above. It is just an extra one in the top menus
        style = "Flow";
        JPanel check = new JPanel();
        //see if user would like to input information
        check.setLayout(new BoxLayout(check, BoxLayout.PAGE_AXIS));
        check.add(new JLabel("Would you like to input information for this Flow?"));
        int yN = JOptionPane.showConfirmDialog(null, check);
        if (yN == 0) {
            //if yes, get info
            //Base values in case someone hits enter to fast, so it wont throw errors
            JTextField flowName = new JTextField("Flow" + flowArrayList.size());
            JTextField flowEquation = new JTextField("");
            JPanel addFlow = new JPanel();
            addFlow.setLayout(new BoxLayout(addFlow, BoxLayout.PAGE_AXIS));
            addFlow.add(new JLabel("Flow Name"));
            addFlow.add(flowName);
            addFlow.add(new JLabel("Flow Equation"));
            addFlow.add(flowEquation);
            int option = JOptionPane.showConfirmDialog(null, addFlow);
            inputname = flowName.getText();
            inputequation = flowEquation.getText();
        } else {
            //blank values, stock name increments
            inputname = "Flow" + flowArrayList.size();
            //these can NOT be "", they show as null and go haywire
            inputequation = "";
        }
        objectLoc = 2;
    }//GEN-LAST:event_FlowMenuItemActionPerformed

    private void showAllVariableBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showAllVariableBtnActionPerformed
        // TODO add your handling code here:
        //Displays the window that displays all the objects on the graph
        JPanel stockOverallPanel = new JPanel();
        stockOverallPanel.setLayout(new BoxLayout(stockOverallPanel, BoxLayout.Y_AXIS));
        JPanel flowOverallPanel = new JPanel();
        flowOverallPanel.setLayout(new BoxLayout(flowOverallPanel, BoxLayout.Y_AXIS));
        JPanel variableOverallPanel = new JPanel();

        //create a holder for all the save buttons for save all button based on the size of each array list
        int amountOfSaveButtons = stockArrayList.size() + flowArrayList.size() + variableArrayList.size();
        final ArrayList<JButton> buttons = new ArrayList<JButton>();

        //for the amount of stocks, create a panel and allow edit fields
        for (int i = 0; i < stockArrayList.size(); i++) {
            //Panel per stock
            final int cnt = i;
            String s = stockArrayList.get(i).getObjName();
            JPanel stock = new JPanel();
            stock.setName(s);
            //pre filling fields. stockname is s because its the same as the panel name
            final JTextField stockName = new JTextField(s);
            final JTextField stockSymbol = new JTextField(stockArrayList.get(i).getStockDescrip());
            final JTextField stockInitial = new JTextField(stockArrayList.get(i).getStockInitial());

            stock.setLayout(new BoxLayout(stock, BoxLayout.PAGE_AXIS));
            stock.add(new JLabel(" "));
            stock.add(new JLabel("Stock options for " + stockArrayList.get(i).getObjName()));
            stock.add(new JLabel("Stock Name"));
            stock.add(stockName);
            stock.add(new JLabel("Stock Symbol"));
            stock.add(stockSymbol);
            stock.add(new JLabel("Stock Initial Value"));
            stock.add(stockInitial);
            //save button per panel
            JButton saveBtn = new JButton("Save");
            //create save button and add it to the overall button array
            buttons.add(saveBtn);
            saveBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    stockArrayList.get(cnt).setObjName(stockName.getText());
                    stockArrayList.get(cnt).setStockDescrip(stockSymbol.getText());
                    stockArrayList.get(cnt).setStockInitial(stockInitial.getText());
                    stockArrayList.get(cnt).setStockArg(stockSymbol.getText(), stockInitial.getText());
                    //now it saves, but i want it to update the jgraph with the names
                }
            });
            stock.add(saveBtn);
            //get info back out
            //adding to overall pannel
            stockOverallPanel.add(stock);
        }
        //for the amount of flows, create a panel and allow editing
        for (int i = 0; i < flowArrayList.size(); i++) {
            //Panel per stock
            final int cnt = i;
            String s = flowArrayList.get(i).getObjName();
            JPanel flow = new JPanel();
            flow.setName(s);
            //pre filling fields. stockname is s because its the same as the panel name
            final JTextField flowName = new JTextField(s);
            final JTextField flowEquation = new JTextField(flowArrayList.get(i).getFlowEquation());
            flow.setLayout(new BoxLayout(flow, BoxLayout.PAGE_AXIS));
            flow.add(new JLabel(" "));
            flow.add(new JLabel("Flow options for " + flowArrayList.get(i).getObjName()));
            flow.add(new JLabel("Flow Name"));
            flow.add(flowName);
            flow.add(new JLabel("Flow Equation"));
            flow.add(flowEquation);
            //save button per panel
            JButton saveBtn = new JButton("Save");
            //create save button and add it to the overall button array
            buttons.add(saveBtn);
            saveBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    flowArrayList.get(cnt).setObjName(flowName.getText());
                    flowArrayList.get(cnt).setFlowEquation(flowEquation.getText());
                    //flowArrayList.get(cnt).setVarArg(stockDescrip.getText(), stockInitial.getText());
                    //now it saves, but i want it to update the jgraph with the names
                }
            });
            flow.add(saveBtn);
            //get info back out
            //adding to overall pannel
            flowOverallPanel.add(flow);
        }
        //for the amount of variables, create a panel
        for (int i = 0; i < variableArrayList.size(); i++) {
            //Panel per stock
            final int cnt = i;
            String s = variableArrayList.get(i).getObjName();
            JPanel var = new JPanel();
            var.setName(s);
            //pre filling fields. stockname is s because its the same as the panel name
            final JTextField varName = new JTextField(s);
            final JTextField varSymbol = new JTextField(variableArrayList.get(i).getVarDescrip());
            final JTextField varValue = new JTextField(variableArrayList.get(i).getVarInitial());

            var.setLayout(new BoxLayout(var, BoxLayout.PAGE_AXIS));
            var.add(new JLabel(" "));
            var.add(new JLabel("Variable options for " + variableArrayList.get(i).getObjName()));
            var.add(new JLabel("Variable Name"));
            var.add(varName);
            var.add(new JLabel("Variable Symbol"));
            var.add(varSymbol);
            var.add(new JLabel("Variable Value"));
            var.add(varValue);
            //save button per panel
            JButton saveBtn = new JButton("Save");
            //create save button and add it to the overall button array
            buttons.add(saveBtn);
            saveBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    variableArrayList.get(cnt).setObjName(varName.getText());
                    variableArrayList.get(cnt).setVarDescrip(varSymbol.getText());
                    variableArrayList.get(cnt).setVarInitial(varValue.getText());
                    //variableArrayList.get(cnt).setVarArg(varSymbol.getText(), varValue.getText());
                    //now it saves, but i want it to update the jgraph with the names
                }
            });
            var.add(saveBtn);
            //get info back out
            //adding to overall pannel
            variableOverallPanel.add(var);
        }
        //create overall button
        JButton saveAllBtn = new JButton("Save All");
        saveAllBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //for the amount of save buttons, click them
                for (int i = 0; i < buttons.size(); i++) {
                    buttons.get(i).doClick();
                }
            }
        });

        //making overall panels scrollable
        JScrollPane stockContainer = new JScrollPane(stockOverallPanel);
        JScrollPane flowContainer = new JScrollPane(flowOverallPanel);
        JScrollPane varContainer = new JScrollPane(variableOverallPanel);
        //add it to frame
        JFrame frame = new JFrame();
        frame.setTitle("Edit Stock Options");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLayout(new GridLayout(0, 2));
        frame.add(stockContainer);
        frame.add(flowContainer);
        frame.add(varContainer);
        frame.add(saveAllBtn);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }//GEN-LAST:event_showAllVariableBtnActionPerformed

    private void PrintMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrintMenuItemActionPerformed
        // TODO add your handling code here:
        //This method will allow the user to print the graph when completed
    }//GEN-LAST:event_PrintMenuItemActionPerformed

    private void VariableMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VariableMenuItemActionPerformed
        // TODO add your handling code here:

        //create variable through the menu at the top of the screen same as above
        style = "Variable";
        objectLoc = 4;
        inputValue = "0.0";
        JPanel check = new JPanel();
        check.setLayout(new BoxLayout(check, BoxLayout.PAGE_AXIS));
        check.add(new JLabel("Would you like to input information for this Variable?"));
        int yN = JOptionPane.showConfirmDialog(null, check);
        if (yN == 0) {
            //if yes, get info
            //Base values in case someone hits enter to fast, so it wont throw errors
            JTextField varName = new JTextField("Variable" + variableArrayList.size());
            JTextField varSymbol = new JTextField("Variable" + variableArrayList.size());
            JTextField varValue = new JTextField("");
            JPanel addVar = new JPanel();
            addVar.setLayout(new BoxLayout(addVar, BoxLayout.PAGE_AXIS));
            addVar.add(new JLabel("Variable Name"));
            addVar.add(varName);
            addVar.add(new JLabel("Variable Symbol"));
            addVar.add(varSymbol);
            addVar.add(new JLabel("Variable Value"));
            addVar.add(varValue);
            int option = JOptionPane.showConfirmDialog(null, addVar);
            inputname = varName.getText();
            inputValue = varValue.getText();
            inputsymbol = varSymbol.getText();
        } else {
            //blank values, variable name increments
            inputname = "Variable" + variableArrayList.size();
            //these can NOT be "", they show as null and go haywire
            inputValue = "0.0";
            inputsymbol = "v" + +variableArrayList.size();
        }
    }//GEN-LAST:event_VariableMenuItemActionPerformed

    private void arrowComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrowComboBoxActionPerformed
        // TODO add your handling code here:
        //Allows the selection of different arrow types.
        String arrowType = arrowComboBox.getSelectedItem().toString();
        if (arrowType == "Right Arrow") {
            style = "Arrow";
            objectLoc = 3;
        } else if (arrowType == "Upper Left Arrow") {
            style = "ArrowLU";
            objectLoc = 5;
        } else if (arrowType == "Upper Right Arrow") {
            style = "ArrowRU";
            objectLoc = 6;
        } else if (arrowType == "Lower Left Arrow") {
            style = "ArrowLL";
            objectLoc = 7;
        } else if (arrowType == "Lower Right Arrow") {
            style = "ArrowRL";
            objectLoc = 8;
        } else if (arrowType == "Up Arrow") {
            style = "ArrowU";
            objectLoc = 9;
        } else if (arrowType == "Down Arrow") {
            style = "ArrowD";
            objectLoc = 10;
        } else if (arrowType == "Left Arrow") {
            style = "ArrowL";
            objectLoc = 11;
        }
    }//GEN-LAST:event_arrowComboBoxActionPerformed

    private void arrowLUMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrowLUMenuItemActionPerformed
        // TODO add your handling code here:
        //sets style and objectLoc variable for a upper left pointing arrow
        style = "ArrowLU";
        objectLoc = 5;
    }//GEN-LAST:event_arrowLUMenuItemActionPerformed

    private void arrowRMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrowRMenuItemActionPerformed
        // TODO add your handling code here:
        //sets style and objectLoc variable for right pointing arrow
        style = "Arrow";
        objectLoc = 3;
    }//GEN-LAST:event_arrowRMenuItemActionPerformed

    private void arrowLMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrowLMenuItemActionPerformed
        // TODO add your handling code here:
        //sets style and objectLoc variable for a left pointing arrow
        style = "ArrowL";
        objectLoc = 11;
    }//GEN-LAST:event_arrowLMenuItemActionPerformed

    private void arrowRUMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrowRUMenuItemActionPerformed
        // TODO add your handling code here:
        //sets style and objectLoc variable for a upper right pointing arrow
        style = "ArrowRU";
        objectLoc = 6;
    }//GEN-LAST:event_arrowRUMenuItemActionPerformed

    private void arrowRLMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrowRLMenuItemActionPerformed
        // TODO add your handling code here:
        //sets style and objectLoc variable for a lower right pointing arrow
        style = "ArrowRL";
        objectLoc = 8;
    }//GEN-LAST:event_arrowRLMenuItemActionPerformed

    private void arrowLLMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrowLLMenuItemActionPerformed
        // TODO add your handling code here:
        //sets style and objectLoc variable for a lower left pointing arrow
        style = "ArrowLL";
        objectLoc = 7;
    }//GEN-LAST:event_arrowLLMenuItemActionPerformed

    private void arrowUMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrowUMenuItemActionPerformed
        // TODO add your handling code here:
        //sets style and objectLoc variable for a up pointing arrow
        style = "ArrowU";
        objectLoc = 9;
    }//GEN-LAST:event_arrowUMenuItemActionPerformed

    private void arrowDMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrowDMenuItemActionPerformed
        // TODO add your handling code here:
        //sets style and objectLoc variable for a down pointing arrow
        style = "ArrowD";
        objectLoc = 10;
    }//GEN-LAST:event_arrowDMenuItemActionPerformed

    private void OpenMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenMenuItemActionPerformed
        // TODO add your handling code here:
        //JSONParser parser = new JSONParser();
        
        JSONParser parser = new JSONParser();
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);
        //String srcName = "";
        if (returnVal != JFileChooser.APPROVE_OPTION)
            return;
        
        File srcFile = fc.getSelectedFile();
        //String filename = "srcFile";
        graph.removeCells(graph.getChildVertices(graph.getDefaultParent()));
        
        stockArrayList = JSONRead.readStock(parser, srcFile);
        for(int i=0;i<stockArrayList.size();i++)
        {
            AddStock(stockArrayList.get(i));
        }
        flowArrayList = JSONRead.readFlow(parser, srcFile);
        for(int i=0;i<flowArrayList.size();i++)
        {
            AddFlow(flowArrayList.get(i));
        }
        variableArrayList = JSONRead.readVar(parser, srcFile);
        for(int i=0;i<variableArrayList.size();i++)
        {
            AddVariable(variableArrayList.get(i));
        }
        String[] mSettings = JSONRead.readSettings(parser, srcFile);
        modelSettings.initialTime=mSettings[0];
        modelSettings.finalTime=mSettings[1];
        modelSettings.timeStep=mSettings[2];
    }//GEN-LAST:event_OpenMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AboutMenuItem;
    private javax.swing.JMenu AddMenu;
    private javax.swing.JMenuItem DeleteMenuItem;
    private javax.swing.JMenu EditMenu;
    private javax.swing.JMenu FileMenu;
    private javax.swing.JMenuItem FlowMenuItem;
    private javax.swing.JMenu HelpMenu;
    private javax.swing.JMenuItem OpenMenuItem;
    private javax.swing.JMenuItem PrintMenuItem;
    private javax.swing.JMenuItem SaveMenuItem;
    private javax.swing.JMenuItem StockMenuItem;
    private javax.swing.JMenuItem VariableMenuItem;
    private javax.swing.JLabel analysisMethodLabel;
    private javax.swing.JComboBox<String> arrowComboBox;
    private javax.swing.JMenuItem arrowDMenuItem;
    private javax.swing.JMenuItem arrowLLMenuItem;
    private javax.swing.JMenuItem arrowLMenuItem;
    private javax.swing.JMenuItem arrowLUMenuItem;
    private javax.swing.JMenuItem arrowRLMenuItem;
    private javax.swing.JMenuItem arrowRMenuItem;
    private javax.swing.JMenuItem arrowRUMenuItem;
    private javax.swing.JMenu arrowTypes;
    private javax.swing.JMenuItem arrowUMenuItem;
    private javax.swing.JButton closeBtn;
    private javax.swing.JToggleButton deleteBtn;
    private javax.swing.JButton flowBtn;
    private javax.swing.JButton jButton3;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JComboBox methodChoiceCombo;
    private javax.swing.JButton modelSettingsBtn;
    private javax.swing.JButton runSimBtn;
    private javax.swing.JButton showAllVariableBtn;
    private javax.swing.JButton stockBtn;
    private javax.swing.JButton variableBtn;
    private javax.swing.JButton viewGraphButton;
    private javax.swing.JButton viewTableButton;
    // End of variables declaration//GEN-END:variables
}
