package pokemon_online.world_builder;


import java.awt.Window;

import pokemon_online.land.Land;

public class Sposta extends javax.swing.JDialog {
    

    public Sposta(java.awt.Frame parent, boolean modal, Land land, Window formChiamante) {
        super(parent, modal);
        initComponents();
        this.landPrima = land;
        this.centraFrameSuFrame(formChiamante);
        this.setVisible(true);
    }

    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jButtonSN = new javax.swing.JButton();
        jButtonDWN = new javax.swing.JButton();
        jButtonUP = new javax.swing.JButton();
        pannelloSposta1 = new PannelloSposta();
        jButtonDX = new javax.swing.JButton();
        jButtonOk = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sposta");
        setModal(true);
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jButtonSN.setText("SN");
        jButtonSN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSNActionPerformed(evt);
            }
        });

        jButtonDWN.setText("DWN");
        jButtonDWN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDWNActionPerformed(evt);
            }
        });

        jButtonUP.setText("UP");
        jButtonUP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUPActionPerformed(evt);
            }
        });

        pannelloSposta1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        org.jdesktop.layout.GroupLayout pannelloSposta1Layout = new org.jdesktop.layout.GroupLayout(pannelloSposta1);
        pannelloSposta1.setLayout(pannelloSposta1Layout);
        pannelloSposta1Layout.setHorizontalGroup(
            pannelloSposta1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 92, Short.MAX_VALUE)
        );
        pannelloSposta1Layout.setVerticalGroup(
            pannelloSposta1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 92, Short.MAX_VALUE)
        );

        jButtonDX.setText("DX");
        jButtonDX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDXActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jButtonSN, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jButtonDWN, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jButtonUP, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, pannelloSposta1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonDX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jButtonUP, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jButtonDX, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jButtonSN, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, pannelloSposta1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonDWN, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 48, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButtonOk.setText("OK");
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(223, Short.MAX_VALUE)
                .add(jButtonOk)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonOk)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
        this.landPrima = this.spostaLand(landPrima);
        this.dispose();
    }//GEN-LAST:event_jButtonOkActionPerformed

    private void jButtonUPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUPActionPerformed
        this.ySpostamento--;
        this.pannelloSposta1.setYSpostamento(ySpostamento * 4);
        repaint();
    }//GEN-LAST:event_jButtonUPActionPerformed

    private void jButtonDWNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDWNActionPerformed
        this.ySpostamento++;
        this.pannelloSposta1.setYSpostamento(ySpostamento * 4);
        repaint();
    }//GEN-LAST:event_jButtonDWNActionPerformed

    private void jButtonSNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSNActionPerformed
        this.xSpostamento--;
        this.pannelloSposta1.setXSpostamento(xSpostamento * 4);
        repaint();
    }//GEN-LAST:event_jButtonSNActionPerformed

    private void jButtonDXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDXActionPerformed
        this.xSpostamento++;
        this.pannelloSposta1.setXSpostamento(xSpostamento * 4);
        repaint();
    }//GEN-LAST:event_jButtonDXActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDWN;
    private javax.swing.JButton jButtonDX;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JButton jButtonSN;
    private javax.swing.JButton jButtonUP;
    private javax.swing.JPanel jPanel1;
    private PannelloSposta pannelloSposta1;
    // End of variables declaration//GEN-END:variables
    Land landPrima;
    int xSpostamento = 0;
    int ySpostamento = 0;
    
    Land spostaLand (Land land){
        Land landDopo = new Land(land.getName(), land.getColsCount(), land.getRowsCount());
        for (int c = 0; c < landDopo.getColsCount(); c++){//Per ogni colonna
            if ((c < landPrima.getRowsCount() + this.xSpostamento)&&(c - this.xSpostamento >= 0)){
                for (int r = 0; r < landDopo.getRowsCount(); r++){//Per ogni righa
                    if ((r < landPrima.getRowsCount() + this.ySpostamento)&&(r - this.ySpostamento >= 0))
                        landDopo.componenti[c][r] = landPrima.componenti[c - this.xSpostamento][r - this.ySpostamento];
                }
            }
        }
        return landDopo;
    }
    
    void centraFrameSuFrame(Window frame2) { //Posiziona il frame corrente al centro di frame2
        int x = frame2.getX();
        int y = frame2.getY();
        int larghezza = frame2.getWidth();
        int altezza = frame2.getHeight();
        this.setLocation((x + larghezza / 2) - this.getWidth()/2, (y + altezza / 2) - this.getHeight() / 2);
    }
}
