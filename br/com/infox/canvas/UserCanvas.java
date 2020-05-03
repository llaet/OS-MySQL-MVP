/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.infox.canvas;

import java.sql.*;
import br.com.infox.dal.ConnectionModule;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Lucas Laet
 */
public class UserCanvas extends javax.swing.JInternalFrame {

    private Connection connection = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;

    /**
     * Creates new form UserCanvas
     */
    public UserCanvas() {
        initComponents();
        connection = ConnectionModule.connector();
    }

    private void createUser() {
        // create the insert sql statement on a String
        String create = "insert into tbUsers(userID, userName, userPhone, userLogin, userPassword, userProfile) values(?,?,?,?,?,?)";
        try {
            if (txtUserID.getText().isEmpty() || txtUserName.getText().isEmpty() || txtLogin.getText().isEmpty()
                    || txtPassword.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "There are one or more required fields not filled in!");
            } else {
                //prepare the statement for the database operation
                pst = connection.prepareStatement(create);
                pst.setString(1, txtUserID.getText());
                pst.setString(2, txtUserName.getText());
                pst.setString(3, txtUserPhone.getText());
                pst.setString(4, txtLogin.getText());
                pst.setString(5, txtPassword.getText());
                pst.setString(6, cboUserProfile.getSelectedItem().toString());
                //use the statement to create a user on database
                int instruction = pst.executeUpdate();
                if (instruction > 0) {
                    JOptionPane.showMessageDialog(null, "New user registered successfully.");
                    // clean the fields of the user form
                    txtUserID.setText(null);
                    txtUserName.setText(null);
                    txtUserPhone.setText(null);
                    txtLogin.setText(null);
                    txtPassword.setText(null);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "The number ID or/and login already exists!");
        }
    }

    /*
     * Create an ListCanvas
     */
    private void showListCanvas() {
        ListCanvas listCanvas = new ListCanvas();
        listCanvas.setVisible(true);
        listCanvas.showTable("user");
    }

    /**
     * Search on database the user informations and bring it to the UserCanvas
     * form
     */
    public static void searchUser(ArrayList<String> list) {
        try {
            txtUserID.setText(list.get(0));
            txtUserName.setText(list.get(1));
            txtUserPhone.setText(list.get(2));
            txtLogin.setText(list.get(3));
            txtPassword.setText(list.get(4));
            cboUserProfile.setSelectedItem(list.get(5));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * Update on database the user informations
     */
    private void updateUser() {
        //create update statement as a String
        String update = "update tbUsers set userName = ?, userPhone = ?, userLogin = ?, userPassword = ?, userProfile = ? where userID = ?";
        try {
            //prepare the update statement with the data received on form, as sql query
            pst = connection.prepareStatement(update);
            pst.setString(1, txtUserName.getText());
            pst.setString(2, txtUserPhone.getText());
            pst.setString(3, txtLogin.getText());
            pst.setString(4, txtPassword.getText());
            pst.setString(5, cboUserProfile.getSelectedItem().toString());
            pst.setString(6, txtUserID.getText());
            //verify the data entrance - if has any not filled in
            if (txtUserID.getText().isEmpty() || txtUserName.getText().isEmpty() || txtUserPhone.getText().isEmpty() || txtLogin.getText().isEmpty()
                    || txtPassword.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "There are one or more fields not filled in!");
            } else {
                //execute sql update operation
                int instruction = pst.executeUpdate();
                //verify if the query returned 1
                if (instruction > 0) {
                    JOptionPane.showMessageDialog(null, "Informations updated successfully.");
                    txtUserID.setText(null);
                    txtUserName.setText(null);
                    txtUserPhone.setText(null);
                    txtLogin.setText(null);
                    txtPassword.setText(null);
                } else {
                    JOptionPane.showMessageDialog(null, "The ID does not exist. Please try again.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void deleteUser() {
        int confirmationPane = JOptionPane.showConfirmDialog(null, "Confirm user profile deletion?", "Warning", JOptionPane.YES_NO_OPTION);
        if (confirmationPane == JOptionPane.YES_OPTION) {
            // prepare a delete estatement as a String
            String delete = "delete from tbUsers where userID = ?";
            try {
                pst = connection.prepareStatement(delete);
                pst.setString(1, txtUserID.getText());
                if (txtUserID.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "The ID filed is not filled in!");
                } else {
                    int instruction = pst.executeUpdate();
                    if (instruction > 0) {
                        JOptionPane.showMessageDialog(null, "User was deleted successfully.");
                        txtUserID.setText(null);
                        txtUserName.setText(null);
                        txtUserPhone.setText(null);
                        txtLogin.setText(null);
                        txtPassword.setText(null);
                    } else {
                        JOptionPane.showMessageDialog(null, "The ID does not exist. Please try again.");
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtUserID = new javax.swing.JTextField();
        txtUserName = new javax.swing.JTextField();
        txtLogin = new javax.swing.JTextField();
        txtPassword = new javax.swing.JTextField();
        cboUserProfile = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txtUserPhone = new javax.swing.JTextField();
        btnUserCreate = new javax.swing.JButton();
        btnUserRead = new javax.swing.JButton();
        btnUserUpdate = new javax.swing.JButton();
        btnUserDelete = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("USERS");
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(600, 501));

        jLabel1.setText("ID*");

        jLabel2.setText("NAME*");

        jLabel3.setText("LOGIN*");

        jLabel4.setText("PASSWORD*");

        jLabel5.setText("PROFILE");

        cboUserProfile.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "admin", "user" }));

        jLabel6.setText("PHONE NUMBER");

        btnUserCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icons/create.png"))); // NOI18N
        btnUserCreate.setToolTipText(" Include user");
        btnUserCreate.setBorderPainted(false);
        btnUserCreate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUserCreate.setPreferredSize(new java.awt.Dimension(75, 70));
        btnUserCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserCreateActionPerformed(evt);
            }
        });

        btnUserRead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icons/read.png"))); // NOI18N
        btnUserRead.setToolTipText("User info");
        btnUserRead.setBorderPainted(false);
        btnUserRead.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUserRead.setPreferredSize(new java.awt.Dimension(75, 70));
        btnUserRead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserReadActionPerformed(evt);
            }
        });

        btnUserUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icons/update.png"))); // NOI18N
        btnUserUpdate.setToolTipText("Update user");
        btnUserUpdate.setBorderPainted(false);
        btnUserUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUserUpdate.setPreferredSize(new java.awt.Dimension(75, 70));
        btnUserUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserUpdateActionPerformed(evt);
            }
        });

        btnUserDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/infox/icons/delete.png"))); // NOI18N
        btnUserDelete.setToolTipText("Delete user");
        btnUserDelete.setBorderPainted(false);
        btnUserDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUserDelete.setPreferredSize(new java.awt.Dimension(75, 70));
        btnUserDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserDeleteActionPerformed(evt);
            }
        });

        jLabel7.setText("Required fields *");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 491, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtUserID, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cboUserProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38)
                                .addComponent(jLabel6))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4)))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtUserPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addComponent(btnUserCreate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnUserRead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnUserUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnUserDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(53, 53, 53)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtUserID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cboUserProfile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtUserPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnUserCreate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUserRead, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUserUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUserDelete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(116, Short.MAX_VALUE))
        );

        setBounds(0, 0, 600, 510);
    }// </editor-fold>//GEN-END:initComponents

    private void btnUserReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserReadActionPerformed
        // calls the 'search' method
        showListCanvas();
    }//GEN-LAST:event_btnUserReadActionPerformed

    private void btnUserUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserUpdateActionPerformed
        // calls the 'update' method
        updateUser();
    }//GEN-LAST:event_btnUserUpdateActionPerformed

    private void btnUserDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserDeleteActionPerformed
        // calls the 'delete' method
        deleteUser();
    }//GEN-LAST:event_btnUserDeleteActionPerformed

    private void btnUserCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserCreateActionPerformed
        // calls the 'create' method
        createUser();
    }//GEN-LAST:event_btnUserCreateActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnUserCreate;
    private javax.swing.JButton btnUserDelete;
    private javax.swing.JButton btnUserRead;
    private javax.swing.JButton btnUserUpdate;
    private static javax.swing.JComboBox<String> cboUserProfile;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private static javax.swing.JTextField txtLogin;
    private static javax.swing.JTextField txtPassword;
    private static javax.swing.JTextField txtUserID;
    private static javax.swing.JTextField txtUserName;
    private static javax.swing.JTextField txtUserPhone;
    // End of variables declaration//GEN-END:variables
}
