/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package videojuegocrud;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 *
 * @author CARLOS
 */
public class JFramevideojuegos extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(JFramevideojuegos.class.getName());
    private ProductoDAO productoDAO;
    private DefaultTableModel modeloTabla;
    

    /**
     * Creates new form JFramevideojuegos
     */
    public JFramevideojuegos() {
        initComponents();
        productoDAO = new ProductoDAO();
        configurarTabla();
        cargarProductos();
    }
private void restaurarTablaPrincipal() {
    System.out.println(" Restaurando tabla principal...");
    cargarProductos();
    JOptionPane.showMessageDialog(this, 
        "Mostrando todos los productos", 
        "Vista Completa", 
        JOptionPane.INFORMATION_MESSAGE);
}
    private void configurarTabla() {
        modeloTabla = new DefaultTableModel(
            new Object [][] {},
            new String [] {
                "ID Producto", "Nombre", "Género", "Precio"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
        tblProductos.setModel(modeloTabla);
        
        tblProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarProductoDesdeTabla();
            }
        });
    }

    private void cargarProductos() {
        modeloTabla.setRowCount(0); 
        List<Producto> productos = productoDAO.obtenerTodosLosProductos();
        
        for (Producto producto : productos) {
            Object[] fila = {
                producto.getIdProducto(),
                producto.getNombre(),
                producto.getGenero(),
                producto.getPrecio()
            };
            modeloTabla.addRow(fila);
        }
    }

   private void insertarProducto() {
    try {
        System.out.println("=== INICIANDO INSERCIÓN ===");
        
        Producto producto = obtenerProductoDesdeFormulario();
        if (producto != null) {
            System.out.println(" Producto a insertar:");
            System.out.println("   ID: " + producto.getIdProducto());
            System.out.println("   Nombre: " + producto.getNombre());
            System.out.println("   Género: " + producto.getGenero());
            System.out.println("   Precio: " + producto.getPrecio());
            
            boolean resultado = productoDAO.insertarProducto(producto);
            System.out.println(" Resultado de inserción: " + resultado);
            
            if (resultado) {
                JOptionPane.showMessageDialog(this, " Producto insertado correctamente");
                cargarProductos();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, 
                    " Error al insertar producto" +
                    "Revisa la consola para más detalles");
            }
        } else {
            System.out.println(" Producto es null - validación falló");
        }
        
    } catch (Exception ex) {
        System.out.println("Error en insertarProducto: " + ex.getMessage());
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
    }

    private void actualizarProducto() {
        try {
            Producto producto = obtenerProductoDesdeFormulario();
            if (producto != null) {
                if (productoDAO.actualizarProducto(producto)) {
                    JOptionPane.showMessageDialog(this, "Producto actualizado correctamente");
                    cargarProductos();
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar producto");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminarProducto() {
        String idProducto = txtId.getText().trim();
        if (idProducto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID de producto para eliminar");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar el producto " + idProducto + "?",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (productoDAO.eliminarProducto(idProducto)) {
                JOptionPane.showMessageDialog(this, "Producto eliminado correctamente");
                cargarProductos();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar producto");
            }
        }
    }

    private void buscarUniversal() {
    String consulta = txtconsultar.getText().trim();
    
    if (consulta.isEmpty()) {
        JOptionPane.showMessageDialog(this, 
            "Ingrese un término para buscar", 
            "Búsqueda Vacía", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    System.out.println(" Búsqueda universal: '" + consulta + "'");
    
    try {
        List<Producto> resultados = productoDAO.buscarProductos(consulta);
        
        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No se encontraron productos con: '" + consulta + "'", 
                "Sin Resultados", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Mostrar resultados en la tabla
            modeloTabla.setRowCount(0);
            for (Producto producto : resultados) {
                Object[] fila = {
                    producto.getIdProducto(),
                    producto.getNombre(),
                    producto.getGenero(),
                    producto.getPrecio()
                };
                modeloTabla.addRow(fila);
            }
            
            JOptionPane.showMessageDialog(this, 
                "Se encontraron " + resultados.size() + " producto(s)", 
                "Resultados", 
                JOptionPane.INFORMATION_MESSAGE);
        }
        
    } catch (Exception ex) {
        System.out.println("Error en búsqueda universal: " + ex.getMessage());
        JOptionPane.showMessageDialog(this, 
            "Error al realizar la búsqueda", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtGenero.setText("");
        txtPrecio.setText("");
    }

    private Producto obtenerProductoDesdeFormulario() {
    try {
        String idProducto = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String genero = txtGenero.getText().trim();
        String textoPrecio = txtPrecio.getText().trim();

        if (idProducto.isEmpty() || nombre.isEmpty() || genero.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
            return null;
        }

      
        double precio;
        try {
            textoPrecio = textoPrecio.replace(",", ".");
            precio = Double.parseDouble(textoPrecio);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "El precio debe ser un número válido\n" +
                "Ejemplos: 15.50 o 15,50");
            return null;
        }
     

        return new Producto(idProducto, nombre, genero, precio);
        
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "El precio debe ser un número válido");
        return null;
    }
    }

    private void cargarProductoEnFormulario(Producto producto) {
        txtId.setText(producto.getIdProducto());
        txtNombre.setText(producto.getNombre());
        txtGenero.setText(producto.getGenero());
        txtPrecio.setText(String.valueOf(producto.getPrecio()));
    }

    private void cargarProductoDesdeTabla() {
        int filaSeleccionada = tblProductos.getSelectedRow();
        if (filaSeleccionada >= 0) {
            String idProducto = modeloTabla.getValueAt(filaSeleccionada, 0).toString();
            Producto producto = productoDAO.obtenerProductoPorId(idProducto);
            if (producto != null) {
                cargarProductoEnFormulario(producto);
            }
        }
    }
     @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnInsertar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        txtId = new javax.swing.JTextField();
        txtGenero = new javax.swing.JTextField();
        txtPrecio = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtconsultar = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblProductos = new javax.swing.JTable();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("ID PRODUCTO");

        jLabel2.setText("NOMBRE");

        jLabel3.setText("GENERO");

        jLabel4.setText("PRECIO");

        btnInsertar.setText("INSERTAR");
        btnInsertar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertarActionPerformed(evt);
            }
        });

        btnActualizar.setText("ACTUALIZAR");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnBuscar.setText("BUSCAR");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnLimpiar.setText("LIMPIAR");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnEliminar.setText("ELIMINAR");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        txtId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdActionPerformed(evt);
            }
        });

        jLabel5.setText("CONSULTAR");

        javax.swing.GroupLayout jScrollPane1Layout = new javax.swing.GroupLayout(jScrollPane1);
        jScrollPane1.setLayout(jScrollPane1Layout);
        jScrollPane1Layout.setHorizontalGroup(
            jScrollPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jScrollPane1Layout.createSequentialGroup()
                .addGroup(jScrollPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jScrollPane1Layout.createSequentialGroup()
                        .addGroup(jScrollPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jScrollPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jScrollPane1Layout.createSequentialGroup()
                                    .addGap(19, 19, 19)
                                    .addComponent(jLabel5))
                                .addGroup(jScrollPane1Layout.createSequentialGroup()
                                    .addGap(25, 25, 25)
                                    .addGroup(jScrollPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jScrollPane1Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jLabel1)))
                        .addGap(54, 54, 54)
                        .addGroup(jScrollPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGenero, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtconsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jScrollPane1Layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(btnInsertar)
                        .addGap(118, 118, 118)
                        .addComponent(btnEliminar))
                    .addGroup(jScrollPane1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59)
                        .addComponent(btnActualizar)
                        .addGap(75, 75, 75)
                        .addComponent(btnLimpiar)))
                .addContainerGap(79, Short.MAX_VALUE))
        );
        jScrollPane1Layout.setVerticalGroup(
            jScrollPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jScrollPane1Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addGroup(jScrollPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jScrollPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jScrollPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(21, 21, 21)
                .addGroup(jScrollPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jScrollPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtconsultar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(jScrollPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscar)
                    .addComponent(btnActualizar)
                    .addComponent(btnLimpiar))
                .addGap(18, 18, 18)
                .addGroup(jScrollPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInsertar)
                    .addComponent(btnEliminar))
                .addGap(51, 51, 51))
        );

        tblProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tblProductos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnInsertarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertarActionPerformed
        // TODO add your handling code here:
            insertarProducto();
    }//GEN-LAST:event_btnInsertarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        // TODO add your handling code here:
        actualizarProducto();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
             String consulta = txtconsultar.getText().trim();
    
    if (consulta.isEmpty()) {
        cargarProductos();
        JOptionPane.showMessageDialog(this, 
            " Mostrando todos los productos", 
            "Vista Completa", 
            JOptionPane.INFORMATION_MESSAGE);
    } else {
        buscarUniversal();
    }



   
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
         eliminarProducto();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void txtIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new JFramevideojuegos().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnInsertar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable tblProductos;
    private javax.swing.JTextField txtGenero;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtconsultar;
    // End of variables declaration//GEN-END:variables
}
