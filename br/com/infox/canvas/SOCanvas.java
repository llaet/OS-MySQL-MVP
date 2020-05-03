/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infox.canvas;

import java.sql.*;
import br.com.infox.dal.ConnectionModule;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import java.util.*;

/**
 *
 * @author Lucas Laet
 */
public class SOCanvas extends javax.swing.JInternalFrame {

    private Connection connection = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    //store the radio button selection
    private String serviceType = null;

    /**
     * Creates new form OSCanvas
     */
    public SOCanvas() {
        initComponents();
        connection = ConnectionModule.connector();
        fillCboSOTechnician();
    }

    /*
    * fill the technicians names combo box when the SOCanvas was openned 
     */
    private void fillCboSOTechnician() {
        String read = "select userName from tbUsers";
        try {
            pst = connection.prepareStatement(read);
            rs = pst.executeQuery();
            while (rs.next()) {
                cboSOTechnician.addItem(rs.getString(1));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /*
    * search on database the customer by name
    * the extern lib rs2xml is also used to auto fill the columns of the table on so canvas form
     */
    private void customerSearch() {
        String read = "select customerID as ID, customerName as NAME, customerPhone as PHONE_NUMBER from tbCustomers where customerName like ?";
        try {
            pst = connection.prepareStatement(read);
            pst.setString(1, txtSOSearch.getText() + "%");
            rs = pst.executeQuery();
            tblSOClients.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /*
    * get the customer ID, by the selected row on so canvas form
     */
    private void fieldSet() {
        int set = tblSOClients.getSelectedRow();
        txtSOID.setText(tblSOClients.getModel().getValueAt(set, 0).toString());
    }

    /*
    * create on database a new service order
     */
    private void createSO() {
        if (txtSOID.getText().isEmpty() || txtSOProduct.getText().isEmpty() || txtSOIssue.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "There are one or more required fields not filled in!");
        } else {
            String create = "insert into tbSO (serviceType,statusSO,product,productProblem,service,technician,price,customerID)"
                    + " values (?,?,?,?,?,?,?,?)";
            try {
                pst = connection.prepareStatement(create);
                pst.setString(1, serviceType);
                pst.setString(2, cboSOStatus.getSelectedItem().toString());
                pst.setString(3, txtSOProduct.getText());
                pst.setString(4, txtSOIssue.getText());
                pst.setString(5, txtSOService.getText());
                pst.setString(6, cboSOTechnician.getSelectedItem().toString());
                pst.setString(7, txtSOPrice.getText().replace(",", "."));
                pst.setString(8, txtSOID.getText());
                int instruction = pst.executeUpdate();
                if (instruction > 0) {
                    JOptionPane.showMessageDialog(null, "Applied service order.");
                    txtSOProduct.setText(null);
                    txtSOIssue.setText(null);
                    txtSOService.setText(null);
                    txtSOPrice.setText("0");
                    txtSOID.setText(null);
                } else {
                    JOptionPane.showMessageDialog(null, "Please verify if the price field is filled even with '0' value.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    /*
     * Create an ListCanvas
     */
    private void showListCanvas() {
        ListCanvas listCanvas = new ListCanvas();
        listCanvas.setVisible(true);
        listCanvas.showTable("so");
    }

    /*
     * search on database the os data and bring to so canvas form
     */
    public static void readSO(ArrayList<String> list) {
        try {
            txtSO.setText(list.get(0));
            txtSODate.setText(list.get(1));
            if (list.get(2).equals("service order")) {
                rbtSO.setSelected(true);
            } else {
                rbtSOBudget.setSelected(true);
            }
            cboSOStatus.setSelectedItem(list.get(3));
            txtSOProduct.setText(list.get(4));
            txtSOIssue.setText(list.get(5));
            txtSOService.setText(list.get(6));
            cboSOTechnician.setSelectedItem(list.get(7));
            txtSOPrice.setText(list.get(8));
            txtSOID.setText(list.get(9));
            // disabling functions that may bring errors to an existent SO
            txtSOSearch.setEnabled(false);
            tblSOClients.setVisible(false);
            btnCreate.setEnabled(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /*
    * update on dabase the os data
     */
    private void updateSO() {
        if (txtSOID.getText().isEmpty() || txtSOProduct.getText().isEmpty() || txtSOIssue.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "There are one or more required fields not filled in!");
        } else {
            String update = "update tbSO set serviceType = ?, statusSO = ?, product = ?, productProblem = ?, service = ?, technician = ?, price = ?"
                    + " where so = ?";
            try {
                pst = connection.prepareStatement(update);
                pst.setString(1, serviceType);
                pst.setString(2, cboSOStatus.getSelectedItem().toString());
                pst.setString(3, txtSOProduct.getText());
                pst.setString(4, txtSOIssue.getText());
                pst.setString(5, txtSOService.getText());
                pst.setString(6, cboSOTechnician.getSelectedItem().toString());
                pst.setString(7, txtSOPrice.getText().replace(",", "."));
                pst.setString(8, txtSO.getText());
                int instruction = pst.executeUpdate();
                if (instruction > 0) {
                    JOptionPane.showMessageDialog(null, "Service order updated successfully");
                    txtSOProduct.setText(null);
                    txtSOIssue.setText(null);
                    txtSOService.setText(null);
                    txtSOPrice.setText("0");
                    txtSOID.setText(null);
                    txtSODate.setText(null);
                    txtSO.setText(null);
                    // enable the objects bellow (they are disabled on searchSO method)
                    btnCreate.setEnabled(true);
                    txtSOSearch.setEnabled(true);
                    tblSOClients.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Please verify if the price field is filled even with '0' value.");
                }
            } catch (com.mysql.jdbc.MysqlDataTruncation e) {
                JOptionPane.showMessageDialog(null, "The price field must be filled with a numeric value!");
            } catch (Exception e2) {
                JOptionPane.showMessageDialog(null, e2);
            }
        }
    }

    /*
    * delete the service order on database
     */
    private void deleteSO() {
        if (txtSOID.getText().isEmpty() || txtSOProduct.getText().isEmpty() || txtSOIssue.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "There are one or more required fields not filled in!");
            // Pane to confirmation of deletion selected
        } else {
            int confirmationPane = JOptionPane.showConfirmDialog(null, "Confirm service order deletion?", "Attention", JOptionPane.YES_NO_OPTION);
            if (confirmationPane == JOptionPane.YES_OPTION) {
                // create the delete sql statement on a String
                String delete = "delete from tbSO where so = ?";
                try {
                    //prepare the delete statement with the data received on form, as sql query
                    pst = connection.prepareStatement(delete);
                    pst.setString(1, txtSO.getText());
                    //execute sql update operation
                    int instruction = pst.executeUpdate();
                    //verify if the query returned 1
                    if (instruction > 0) {
                        JOptionPane.showMessageDialog(null, "Service order deleted successfully.");
                        txtSOProduct.setText(null);
                        txtSOIssue.setText(null);
                        txtSOService.setText(null);
                        txtSOPrice.setText("0");
                        txtSOID.setText(null);
                        txtSODate.setText(null);
                        txtSO.setText(null);
                        // enable the objects bellow (they are disabled on searchSO method)
                        btnCreate.setEnabled(true);
                        txtSOSearch.setEnabled(true);
                        tblSOClients.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Please verify if the price field is filled even with '0' value.");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                    System.out.println(e);
                }
            }
        }
    }

    /*
    * Print the so data by with the jasper report framework
     */
    private void printSO() {
        // generate a SO report
        int confirmationPane = JOptionPane.showConfirmDialog(null, "Confirm a service order emission?", "Attention", JOptionPane.YES_NO_OPTION);
        if (confirmationPane == JOptionPane.YES_OPTION) {
            // print the report with the jasper report framework
            try {
                //HashMap to filter the so number on the form
                HashMap filter = new HashMap<>();
                filter.put("so", Integer.parseInt(txtSO.getText()));
                //the jasper print class is used bellow
                JasperPrint print = JasperFillManager.fillReport("C:src/br/com/infox/reports/so.jasper", filter, connection);
                // the JasperViewer shows the report
                JasperViewer.viewReport(print, false);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please fill the SO informations in form to print the report.");
            } catch (Exception e2) {
                JOptionPane.showMessageDialog(null, e2);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtSO = new javax.swing.JTextField();
        txtSODate = new javax.swing.JTextField();
        rbtSOBudget = new javax.swing.JRadioButton();
        rbtSO = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtSOSearch = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtSOID = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSOClients = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        cboSOStatus = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtSOProduct = new javax.swing.JTextField();
        txtSOIssue = new javax.swing.JTextField();
        txtSOService = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtSOPrice = new javax.swing.JTextField();
        btnCreate = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        btnRead = new javax.swing.JButton();
        cboSOTechnician = new javax.swing.JComboBox<>();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("SERVICES ORDER");
        setToolTipText("");
        setPreferredSize(new java.awt.Dimension(600, 501));
        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("SO");

        jLabel2.setText("Date");

        txtSO.setEditable(false);
        txtSO.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        txtSO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSOActionPerformed(evt);
            }
        });

        txtSODate.setEditable(false);
        txtSODate.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N

        buttonGroup1.add(rbtSOBudget);
        rbtSOBudget.setText("Budget");
        rbtSOBudget.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtSOBudgetActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtSO);
        rbtSO.setText("Service Order");
        rbtSO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtSOActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(57, 57, 57)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(rbtSOBudget)
                        .addGap(18, 18, 18)
                        .addComponent(rbtSO)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(txtSO, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSODate, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtSODate)
                        .addGap(1, 1, 1)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtSOBudget)
                    .addComponent(rbtSO))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setText("Required Fields *");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("CUSTOMER"));

        txtSOSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSOSearchKeyReleased(evt);
            }
        });

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icons/search.png"))); // NOI18N

        jLabel6.setText("ID*");

        txtSOID.setEditable(false);
        txtSOID.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        tblSOClients.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "NAME", "PHONE NUMBER"
            }
        ));
        tblSOClients.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSOClientsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSOClients);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtSOSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSOID, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 30, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSOSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtSOID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setText("SO Status");

        cboSOStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Withdrawal ready", "Reproved budget", "Waiting for approvement", "Waiting product parts", "Not picked up by customer", "Avaliable on store", "Product comeback" }));

        jLabel7.setText("PRODUCT*");

        jLabel8.setText("PRODUCT ISSUE*");

        jLabel9.setText("SERVICE");

        jLabel10.setText("TECHNICIAN");

        jLabel11.setText("PRICE");

        txtSOPrice.setText("0");

        btnCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icons/plus.png"))); // NOI18N
        btnCreate.setToolTipText("Add SO");
        btnCreate.setBorderPainted(false);
        btnCreate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCreate.setPreferredSize(new java.awt.Dimension(50, 50));
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });

        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icons/pencil.png"))); // NOI18N
        btnUpdate.setToolTipText("Update SO");
        btnUpdate.setBorderPainted(false);
        btnUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdate.setPreferredSize(new java.awt.Dimension(50, 50));
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icons/x.png"))); // NOI18N
        btnDelete.setToolTipText("Delete SO");
        btnDelete.setBorderPainted(false);
        btnDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelete.setPreferredSize(new java.awt.Dimension(50, 50));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icons/print.png"))); // NOI18N
        btnPrint.setToolTipText("Print SO");
        btnPrint.setBorderPainted(false);
        btnPrint.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPrint.setPreferredSize(new java.awt.Dimension(50, 50));
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        btnRead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icons/searchbig.png"))); // NOI18N
        btnRead.setToolTipText("Search SO");
        btnRead.setBorderPainted(false);
        btnRead.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRead.setPreferredSize(new java.awt.Dimension(50, 50));
        btnRead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadActionPerformed(evt);
            }
        });

        cboSOTechnician.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select a Technician" }));
        cboSOTechnician.setToolTipText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboSOStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jLabel10)
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(cboSOTechnician, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 130, Short.MAX_VALUE)
                                    .addComponent(jLabel11)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtSOPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(btnCreate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnRead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(104, 104, 104))))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jLabel9)
                            .addGap(18, 18, 18)
                            .addComponent(txtSOService))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addGap(18, 18, 18)
                            .addComponent(txtSOIssue))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addGap(18, 18, 18)
                            .addComponent(txtSOProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(12, 12, 12)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboSOStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtSOProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtSOIssue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtSOService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(txtSOPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboSOTechnician, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCreate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50))
        );

        setBounds(0, 0, 600, 510);
    }// </editor-fold>//GEN-END:initComponents

    private void txtSOSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSOSearchKeyReleased
        // call clientSearch method
        customerSearch();
    }//GEN-LAST:event_txtSOSearchKeyReleased

    private void tblSOClientsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSOClientsMouseClicked
        // call fieldSet method
        fieldSet();
    }//GEN-LAST:event_tblSOClientsMouseClicked

    private void rbtSOBudgetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtSOBudgetActionPerformed
        // insert the budget radio button option at the var below
        serviceType = "budget";
    }//GEN-LAST:event_rbtSOBudgetActionPerformed

    private void rbtSOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtSOActionPerformed
        // insert the service order radio button option at the var below
        serviceType = "service order";
    }//GEN-LAST:event_rbtSOActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // set the budget radio button has selected for the canvas start
        rbtSOBudget.setSelected(true);
        serviceType = "budget";
    }//GEN-LAST:event_formInternalFrameOpened

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        // call the createSO method
        createSO();
    }//GEN-LAST:event_btnCreateActionPerformed

    private void btnReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadActionPerformed
        // call the searchSO method
        showListCanvas();
    }//GEN-LAST:event_btnReadActionPerformed

    private void txtSOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSOActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSOActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // call the updateSO method
        updateSO();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // call the deleteSO method
        deleteSO();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // call the printSO method
        printSO();
    }//GEN-LAST:event_btnPrintActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnRead;
    private javax.swing.JButton btnUpdate;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private static javax.swing.JComboBox<String> cboSOStatus;
    private static javax.swing.JComboBox<String> cboSOTechnician;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JRadioButton rbtSO;
    private static javax.swing.JRadioButton rbtSOBudget;
    private static javax.swing.JTable tblSOClients;
    private static javax.swing.JTextField txtSO;
    private static javax.swing.JTextField txtSODate;
    private static javax.swing.JTextField txtSOID;
    private static javax.swing.JTextField txtSOIssue;
    private static javax.swing.JTextField txtSOPrice;
    private static javax.swing.JTextField txtSOProduct;
    private static javax.swing.JTextField txtSOSearch;
    private static javax.swing.JTextField txtSOService;
    // End of variables declaration//GEN-END:variables
}
