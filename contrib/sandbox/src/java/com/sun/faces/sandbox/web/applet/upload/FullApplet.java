/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

/**
 * 
 */
package com.sun.faces.sandbox.web.applet.upload;

import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

/**
 * @author Jason Lee
 *
 */
public class FullApplet extends BaseApplet {
    private static final long serialVersionUID = 1L;
    protected javax.swing.JButton btnAddFiles;
    protected javax.swing.JButton btnCancel;
    protected javax.swing.JButton btnRemove;
    protected javax.swing.JButton btnUpload;
    protected FileTableModel model = new FileTableModel();
    protected javax.swing.JPanel pnlButtons;
    protected javax.swing.JPanel pnlFiles;
    protected javax.swing.JScrollPane spFiles;
    protected javax.swing.JTable tblFiles;

    public boolean getResult() {
        return result;
    }

    public void init() {
        super.init();
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    initComponents();
                }
            });
        } catch (Exception e) {
            System.err.println("createGUI didn't successfully complete");
        }
    }

    private void btnAddFilesActionPerformed(java.awt.event.ActionEvent evt) {
        MultiFileUploadDialog dialog = new MultiFileUploadDialog(startDir, fileFilter);
        int returnVal = dialog.display();

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File files[] = dialog.getFiles();
            if (files != null) {
                for (File file : files) {
                    model.addFile(file);
                }
                tblFiles.tableChanged(null);
            }
        }
    }

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
        result = CANCEL;
    }

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {
        int[] selectedRows = tblFiles.getSelectedRows();
        for (int selectedRow : selectedRows) {
            model.removeFile(selectedRow);
        }
        tblFiles.tableChanged(null);
    }

    private void btnUploadActionPerformed(java.awt.event.ActionEvent evt) {
        debugDialog("Upload clicked");
        List<File> files = model.getFiles();
        debugDialog("Files = " + files.toString());
        uploadFiles(files);
        debugDialog("Files uploaded");
        setVisible(false);
        result = UPLOAD;
    }

    private void initComponents() {
        pnlButtons = new javax.swing.JPanel();
        btnAddFiles = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnUpload = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        pnlFiles = new javax.swing.JPanel();
        spFiles = new javax.swing.JScrollPane();
        tblFiles = new javax.swing.JTable();

//        setBounds(new java.awt.Rectangle(0, 0, 500, 200));
//        setMinimumSize(new java.awt.Dimension(200, 75));
        pnlButtons.setLayout(new java.awt.GridLayout(1, 0));

        btnAddFiles.setText("Add Files");
        btnAddFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddFilesActionPerformed(evt);
            }
        });

        pnlButtons.add(btnAddFiles);

        btnRemove.setText("Remove File(s)");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        pnlButtons.add(btnRemove);

        btnUpload.setText("Upload");
        btnUpload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUploadActionPerformed(evt);
            }
        });

        pnlButtons.add(btnUpload);

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

//        pnlButtons.add(btnCancel);

        getContentPane().add(pnlButtons, java.awt.BorderLayout.NORTH);

        pnlFiles.setLayout(new java.awt.CardLayout());

        pnlFiles.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                pnlFilesComponentResized(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                pnlFilesComponentShown(evt);
            }
        });

        tblFiles.setModel(model);
        tblFiles.setName("Files Tables");
        spFiles.setViewportView(tblFiles);

        pnlFiles.add(spFiles, "card2");

        getContentPane().add(pnlFiles, java.awt.BorderLayout.CENTER);
    }

    private void pnlFilesComponentResized(java.awt.event.ComponentEvent evt) {
        pnlFilesComponentShown(evt);
    }


    private void pnlFilesComponentShown(java.awt.event.ComponentEvent evt) {
        spFiles.setSize(spFiles.getParent().getSize());
    }
    
}
