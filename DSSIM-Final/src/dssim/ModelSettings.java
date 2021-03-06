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
package dssim;

import javax.swing.JFrame;

/**
 *
 * @author Logan
 */
public class ModelSettings extends javax.swing.JPanel {

    /**
     * Creates new form ModelSettings
     */
    //Initial values for the inital time, final time, and time step variables
    public String initialTime;
    public String finalTime;
    public String timeStep;
    JFrame frame = null;

    public ModelSettings() {

        initComponents();
    }

    public boolean isFrameAvailable() {
        return frame != null;
    }

    public void setFrame(JFrame modelFrame) {
        frame = modelFrame;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setInitialTime(String initT) {
        initialTime = initT;
    }

    //Returns the text from the inital time text field to the MainForm
    public String getInitialTime() {
        return initialTime;
    }

    public void setFinalTime(String finT) {
        finalTime = finT;
    }

    //Returns the text from the final time text field to the MainForm
    public String getFinalTime() {
        return finalTime;
    }

    //Returns the text from the time step text field to the MainForm
    public void setTimeStep(String init) {
        initialTime = init;
    }

    public String getTimeStep() {
        return timeStep;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        initialTimeLabel = new javax.swing.JLabel();
        finalTimeLabel = new javax.swing.JLabel();
        timeStepLabel = new javax.swing.JLabel();
        initialTimeTField = new javax.swing.JTextField();
        finalTimeTField = new javax.swing.JTextField();
        timeStepTField = new javax.swing.JTextField();
        modelSettingLabel = new javax.swing.JLabel();
        saveBtn = new javax.swing.JButton();

        initialTimeLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        initialTimeLabel.setText("Initial Time");

        finalTimeLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        finalTimeLabel.setText("Final Time");

        timeStepLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        timeStepLabel.setText("Time Step");

        initialTimeTField.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                initialTimeTFieldInputMethodTextChanged(evt);
            }
        });

        finalTimeTField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finalTimeTFieldActionPerformed(evt);
            }
        });

        timeStepTField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timeStepTFieldActionPerformed(evt);
            }
        });

        modelSettingLabel.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        modelSettingLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        modelSettingLabel.setText("Model Settings");

        saveBtn.setText("Save");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(modelSettingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(finalTimeLabel)
                            .addComponent(initialTimeLabel)
                            .addComponent(timeStepLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(timeStepTField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(finalTimeTField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                .addComponent(initialTimeTField, javax.swing.GroupLayout.Alignment.TRAILING))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(119, 119, 119)
                        .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(modelSettingLabel)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(initialTimeTField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(initialTimeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(finalTimeLabel)
                    .addComponent(finalTimeTField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timeStepTField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timeStepLabel))
                .addGap(18, 18, 18)
                .addComponent(saveBtn)
                .addContainerGap(13, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        // TODO add your handling code here:
        //pulls text from the text fields and saves them
        initialTime = initialTimeTField.getText();
        finalTime = finalTimeTField.getText();
        timeStep = timeStepTField.getText();
        frame.setVisible(false);

    }//GEN-LAST:event_saveBtnActionPerformed

    private void finalTimeTFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finalTimeTFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_finalTimeTFieldActionPerformed

    private void timeStepTFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeStepTFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_timeStepTFieldActionPerformed

    private void initialTimeTFieldInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_initialTimeTFieldInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_initialTimeTFieldInputMethodTextChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel finalTimeLabel;
    public javax.swing.JTextField finalTimeTField;
    private javax.swing.JLabel initialTimeLabel;
    public javax.swing.JTextField initialTimeTField;
    private javax.swing.JLabel modelSettingLabel;
    private javax.swing.JButton saveBtn;
    private javax.swing.JLabel timeStepLabel;
    public javax.swing.JTextField timeStepTField;
    // End of variables declaration//GEN-END:variables
}
