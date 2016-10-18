/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nutribem.gui;

import br.com.nutribem.dao.RepositoryDao;
import br.com.nutribem.fachada.Fachada;
import br.com.nutribem.gui.util.SessionUtil;
import br.com.nutribem.utils.DataUtil;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author PauLinHo
 */
public class FrmInicio extends javax.swing.JFrame {

    private RepositoryDao repositoryDao;

    public FrmInicio() {
        initComponents();
        repositoryDao = new RepositoryDao();

        verificarPermissao();
        

    }

    /**
     * Verifica a permissao do Colaborado que logou se for Usuario n�o sera
     * mostrada alguns MENUS
     */
    private void verificarPermissao() {

        if (SessionUtil.getInstance().getColaborador().getNome() != null) {
            if (!SessionUtil.getInstance().getColaborador().getUsuario().getPermissao().getNivel().equals("ADMINISTRADOR")) {
                menuColaborador.setVisible(false);
                menuFormasDePagamentos.setVisible(false);
                MenuLojaBar.setVisible(false);
                menuRelatorio.setVisible(false);
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

        barMenu = new javax.swing.JMenuBar();
        barCadastros = new javax.swing.JMenu();
        menuProduto = new javax.swing.JMenuItem();
        menuFornecedor = new javax.swing.JMenuItem();
        menuColaborador = new javax.swing.JMenuItem();
        menuCategoria = new javax.swing.JMenuItem();
        menuUnidadeDeMedida = new javax.swing.JMenuItem();
        menuFormasDePagamentos = new javax.swing.JMenuItem();
        menuBarPDV = new javax.swing.JMenu();
        menuPDV = new javax.swing.JMenuItem();
        menuRelatorio = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        MenuLojaBar = new javax.swing.JMenu();
        menuLoja = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        barCadastros.setText("Cadastros");

        menuProduto.setText("Produtos");
        menuProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuProdutoActionPerformed(evt);
            }
        });
        barCadastros.add(menuProduto);

        menuFornecedor.setText("Fornecedores");
        menuFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFornecedorActionPerformed(evt);
            }
        });
        barCadastros.add(menuFornecedor);

        menuColaborador.setText("Colaboradores");
        menuColaborador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuColaboradorActionPerformed(evt);
            }
        });
        barCadastros.add(menuColaborador);

        menuCategoria.setText("Categorias");
        menuCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCategoriaActionPerformed(evt);
            }
        });
        barCadastros.add(menuCategoria);

        menuUnidadeDeMedida.setText("Unidades De Medidas");
        menuUnidadeDeMedida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuUnidadeDeMedidaActionPerformed(evt);
            }
        });
        barCadastros.add(menuUnidadeDeMedida);

        menuFormasDePagamentos.setText("Formas De Pagamentos");
        menuFormasDePagamentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFormasDePagamentosActionPerformed(evt);
            }
        });
        barCadastros.add(menuFormasDePagamentos);

        barMenu.add(barCadastros);

        menuBarPDV.setText("Venda");

        menuPDV.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        menuPDV.setText("PDV");
        menuPDV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPDVActionPerformed(evt);
            }
        });
        menuBarPDV.add(menuPDV);

        barMenu.add(menuBarPDV);

        menuRelatorio.setText("Relat�rios");

        jMenuItem1.setText("Vendas");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        menuRelatorio.add(jMenuItem1);

        jMenuItem2.setText("Caixas");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        menuRelatorio.add(jMenuItem2);

        barMenu.add(menuRelatorio);

        MenuLojaBar.setText("Loja");

        menuLoja.setText("Dados");
        menuLoja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuLojaActionPerformed(evt);
            }
        });
        MenuLojaBar.add(menuLoja);

        barMenu.add(MenuLojaBar);

        setJMenuBar(barMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 731, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 479, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuProdutoActionPerformed
        // TODO add your handling code here:
        FrmProduto frmProduto = new FrmProduto(this, true);
        frmProduto.setLocationRelativeTo(null);
        frmProduto.setResizable(false);
        frmProduto.setVisible(true);
    }//GEN-LAST:event_menuProdutoActionPerformed

    private void menuCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCategoriaActionPerformed
        // TODO add your handling code here:
        FrmCategoria frmCategoria = new FrmCategoria(this, true);
        frmCategoria.setLocationRelativeTo(null);
        frmCategoria.setResizable(false);
        frmCategoria.setVisible(true);
    }//GEN-LAST:event_menuCategoriaActionPerformed

    private void menuUnidadeDeMedidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuUnidadeDeMedidaActionPerformed
        // TODO add your handling code here:
        FrmUnidadeDeMedida frmUnidadeDeMedida = new FrmUnidadeDeMedida(this, true);
        frmUnidadeDeMedida.setLocationRelativeTo(null);
        frmUnidadeDeMedida.setResizable(false);
        frmUnidadeDeMedida.setVisible(true);

    }//GEN-LAST:event_menuUnidadeDeMedidaActionPerformed

    private void menuFormasDePagamentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFormasDePagamentosActionPerformed
        // TODO add your handling code here:
        FrmFormaDePagamento frmFormaDePagamento = new FrmFormaDePagamento(this, true);
        frmFormaDePagamento.setLocationRelativeTo(null);
        frmFormaDePagamento.setResizable(false);
        frmFormaDePagamento.setVisible(true);
    }//GEN-LAST:event_menuFormasDePagamentosActionPerformed

    private void menuLojaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuLojaActionPerformed
        // TODO add your handling code here:
        FrmLoja frmLoja = new FrmLoja(this, true);
        frmLoja.setLocationRelativeTo(null);
        frmLoja.setResizable(false);
        frmLoja.setVisible(true);
    }//GEN-LAST:event_menuLojaActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        if (!SessionUtil.getInstance().getColaborador().getNome().equals("")) {
            int option = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja SAIR?", "AVISO",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

            if (option == JOptionPane.CANCEL_OPTION) {
                this.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
            } else {
                
                this.dispose();
            }

        }

    }//GEN-LAST:event_formWindowClosing

    private void menuColaboradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuColaboradorActionPerformed
        // TODO add your handling code here:
        FrmColaborador frmColaborador = new FrmColaborador(this, true);
        frmColaborador.setLocationRelativeTo(null);
        frmColaborador.setResizable(false);
        frmColaborador.setVisible(true);
    }//GEN-LAST:event_menuColaboradorActionPerformed

    private void menuFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFornecedorActionPerformed
        // TODO add your handling code here:
        FrmFornecedor frmFornecedor = new FrmFornecedor(this, true);
        frmFornecedor.setLocationRelativeTo(null);
        frmFornecedor.setResizable(false);
        frmFornecedor.setVisible(true);
    }//GEN-LAST:event_menuFornecedorActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated

        verificarPermissao();

    }//GEN-LAST:event_formWindowActivated

    private void menuPDVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPDVActionPerformed
        // TODO add your handling code here:
        FrmVenda frmVenda = new FrmVenda(this, true);
        frmVenda.setLocationRelativeTo(null);
        frmVenda.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        frmVenda.setResizable(false);
        frmVenda.setVisible(true);
    }//GEN-LAST:event_menuPDVActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        FrmRelatorioVendas frmRelatorioVendas = new FrmRelatorioVendas(this, true);
        frmRelatorioVendas.setLocationRelativeTo(null);
        frmRelatorioVendas.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frmRelatorioVendas.setVisible(true);
        
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        FrmRelatorioCaixas frmRelatorioCaixas = new FrmRelatorioCaixas(this, true);
        frmRelatorioCaixas.setLocationRelativeTo(null);
        frmRelatorioCaixas.setVisible(true);
        
    }//GEN-LAST:event_jMenuItem2ActionPerformed

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
            java.util.logging.Logger.getLogger(FrmInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrmInicio frmPrincipal = new FrmInicio();
                frmPrincipal.setLocationRelativeTo(null);
                frmPrincipal.setTitle("NutriBem");
                frmPrincipal.setVisible(true);

                //Mostrando a tela de Login
                FrmLogin frmLogin = new FrmLogin(frmPrincipal, true);
                frmLogin.setLocationRelativeTo(null);
                frmLogin.setResizable(false);
                frmLogin.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                frmLogin.setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu MenuLojaBar;
    private javax.swing.JMenu barCadastros;
    private javax.swing.JMenuBar barMenu;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenu menuBarPDV;
    private javax.swing.JMenuItem menuCategoria;
    private javax.swing.JMenuItem menuColaborador;
    private javax.swing.JMenuItem menuFormasDePagamentos;
    private javax.swing.JMenuItem menuFornecedor;
    private javax.swing.JMenuItem menuLoja;
    private javax.swing.JMenuItem menuPDV;
    private javax.swing.JMenuItem menuProduto;
    private javax.swing.JMenu menuRelatorio;
    private javax.swing.JMenuItem menuUnidadeDeMedida;
    // End of variables declaration//GEN-END:variables
}
