/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nutribem.gui;

import br.com.nutribem.dao.RepositoryDao;
import br.com.nutribem.dominio.EntidadeDominio;
import br.com.nutribem.dominio.FormaDePagamento;
import br.com.nutribem.dominio.Pagamento;
import br.com.nutribem.dominio.Pedido;
import br.com.nutribem.dominio.Produto;
import br.com.nutribem.fachada.Fachada;
import br.com.nutribem.fachada.Resultado;
import br.com.nutribem.gui.util.GuiUtil;
import br.com.nutribem.gui.util.SessionUtil;
import br.com.nutribem.utils.DataUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author PauLinHo
 */
public class FrmPagamento extends javax.swing.JFrame {

    /**
     * Creates new form FrmPagamento
     */
    private Pagamento pagamento;
    private RepositoryDao repositoryDao;
    private Fachada fachada;
    private Resultado resultado;
    private List<EntidadeDominio> formasDePagamentos;

    public FrmPagamento(java.awt.Frame parent, boolean modal) {

        pagamento = new Pagamento();
        repositoryDao = new RepositoryDao();
        fachada = new Fachada();
        formasDePagamentos = new ArrayList<EntidadeDominio>();
        resultado = new Resultado();

        initComponents();
        inicializarComponentes();

    }

    private void inicializarComponentes() {
        txtTotal.setEnabled(false);
        txtTroco.setEnabled(false);

        //recebe o pedido da SessionUtil
        if (pagamento != null) {
            pagamento.setPedido(SessionUtil.getInstance().getPedido());
        }

        txtTotal.setText(GuiUtil.getMoedaFormatada(String.valueOf(pagamento.getPedido().getValor())));

        //carregar o comboBox de Formas de Pagamento
        Vector<FormaDePagamento> vectorFormasDePagamentos = new Vector<FormaDePagamento>();

        formasDePagamentos = repositoryDao.findAll(new FormaDePagamento());
        for (EntidadeDominio f : formasDePagamentos) {
            if (f instanceof FormaDePagamento) {
                vectorFormasDePagamentos.add((FormaDePagamento) f);
            }
        }
        DefaultComboBoxModel model = new DefaultComboBoxModel(vectorFormasDePagamentos);
        cmbFormaDePagamento.setModel(model);
        cmbFormaDePagamento.setSelectedIndex(0);

        //setando com o formato moeda
        txtTroco.setText(GuiUtil.getMoedaFormatada("0.00"));
        txtValorPago.setText(GuiUtil.getMoedaFormatada("0.00"));
        txtValorPago.requestFocus();
        txtValorPago.selectAll();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtTroco = new javax.swing.JFormattedTextField();
        txtTotal = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        txtValorPago = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cmbFormaDePagamento = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnOK = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PAGAMENTO");

        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Exotc350 Bd BT", 1, 16)); // NOI18N
        jLabel1.setText("TOTAL:");

        txtTroco.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance())));
        txtTroco.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        txtTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance())));
        txtTotal.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Exotc350 Bd BT", 1, 16)); // NOI18N
        jLabel2.setText("TROCO:");

        txtValorPago.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance())));
        txtValorPago.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtValorPago.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtValorPagoFocusLost(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Exotc350 Bd BT", 1, 16)); // NOI18N
        jLabel4.setText("PAGO:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Exotc350 Bd BT", 1, 24)); // NOI18N
        jLabel8.setText("PAGAMENTO");

        cmbFormaDePagamento.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cmbFormaDePagamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbFormaDePagamentoActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Exotc350 Bd BT", 1, 16)); // NOI18N
        jLabel3.setText("FORMA:");

        btnCancel.setBackground(new java.awt.Color(255, 0, 0));
        btnCancel.setFont(new java.awt.Font("Exotc350 Bd BT", 1, 14)); // NOI18N
        btnCancel.setText("CANCEL");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnOK.setBackground(new java.awt.Color(0, 153, 0));
        btnOK.setFont(new java.awt.Font("Exotc350 Bd BT", 1, 18)); // NOI18N
        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel4)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addComponent(jLabel7))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTroco, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtValorPago, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(33, 33, 33)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cmbFormaDePagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel8)
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtValorPago, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTroco, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbFormaDePagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(31, 31, 31)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbFormaDePagamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFormaDePagamentoActionPerformed
        // TODO add your handling code here:
        btnOK.requestFocus();
    }//GEN-LAST:event_cmbFormaDePagamentoActionPerformed

    private void txtValorPagoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValorPagoFocusLost
        BigDecimal valorPago = new BigDecimal(GuiUtil.getMoedaSemFormatacao(txtValorPago.getText()));
        if (pagamento.getPedido().getValor().compareTo(valorPago) == 1) {
            JOptionPane.showMessageDialog(null, "O valor pago não pode ser Menor que o valor Total!", "Erro", JOptionPane.ERROR_MESSAGE);
            txtValorPago.requestFocus();
            txtValorPago.selectAll();
            return;
        }
        BigDecimal troco = new BigDecimal("0.00");
        troco = valorPago.subtract(pagamento.getPedido().getValor());
        txtTroco.setText(GuiUtil.getMoedaFormatada(String.valueOf(troco)));
        txtValorPago.setText(GuiUtil.getMoedaFormatada(String.valueOf(valorPago)));
        cmbFormaDePagamento.requestFocus();
    }//GEN-LAST:event_txtValorPagoFocusLost

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.setVisible(false);

    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed

        //verifica o valor pago
        BigDecimal valorPago = new BigDecimal(GuiUtil.getMoedaSemFormatacao(txtValorPago.getText()));
        if (pagamento.getPedido().getValor().compareTo(valorPago) == 1) {
            JOptionPane.showMessageDialog(null, "O valor pago não pode ser Menor que o valor Total!", "Erro", JOptionPane.ERROR_MESSAGE);
            txtValorPago.requestFocus();
            txtValorPago.selectAll();
            return;
        }

        int option = JOptionPane.showConfirmDialog(null, "Deseja Efetuar o Pagamento?", "AVISO",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

        if (option == JOptionPane.CANCEL_OPTION) {
            return;
        } else {
            efetuarPagamento();
            chamarImpressao();
        }


    }//GEN-LAST:event_btnOKActionPerformed

    private void efetuarPagamento() {

        FormaDePagamento forma = new FormaDePagamento();
        forma = (FormaDePagamento) cmbFormaDePagamento.getSelectedItem();
        if (forma == null) {
            JOptionPane.showMessageDialog(null, "Forma de Pagamento inválida", "Erro", JOptionPane.ERROR_MESSAGE);
            cmbFormaDePagamento.requestFocus();
            return;
        }
        pagamento.setFormaDePagamento(forma);
        pagamento.setDataPagamento(DataUtil.pegarDataAtualDoSistema());

        BigDecimal valorPago = new BigDecimal(GuiUtil.getMoedaSemFormatacao(txtValorPago.getText()));
        pagamento.setValorPago(valorPago);

        //SALVANDO ITEM DO PEDIDO NO BD
        for (int i = 0; i < pagamento.getPedido().getItens().size(); i++) {
            resultado = new Resultado();
            resultado = fachada.save(pagamento.getPedido().getItens().get(i));

            if (resultado.getEntidades().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Houve um erro ao processar item do pedido: \n"
                        + resultado.getMsg(), "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                pagamento.getPedido().getItens().get(i).setId(resultado.getEntidades().get(0).getId());
            }
        }

        //SALVANDO PEDIDOS NO BD
        resultado = new Resultado();
        resultado = fachada.save(pagamento.getPedido());

        if (resultado.getEntidades().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Houve um erro ao processar pedido: \n" + resultado.getMsg(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            pagamento.getPedido().setId(resultado.getEntidades().get(0).getId());
        }

        //REALIZANDO A DECREMENTAÇÃO DO PRODUTO NO ESTOQUE
        decrementarProdutoDoEstoque();

        //SALVANDO PAGAMENTO NO BD
        resultado = new Resultado();
        resultado = fachada.save(pagamento);

        if (resultado.getEntidades().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Houve um erro ao processar pagamento: \n" + resultado.getMsg(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ///atualiza os valores de Caixa
        addCaixa();

    }

    /**
     * realiza a decrementacao do produto no estoque
     */
    private void decrementarProdutoDoEstoque() {

        Produto p = null;
        for (int i = 0; i < pagamento.getPedido().getItens().size(); i++) {
            p = new Produto();

            p = pagamento.getPedido().getItens().get(i).getProduto();
            int qtde = pagamento.getPedido().getItens().get(i).getQuantidade();
            int temp = p.getQuantidadeEstoque() - qtde;

            p.setQuantidadeEstoque(temp);

            resultado = new Resultado();
            resultado = fachada.update(p);

            if (resultado.getEntidades().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Houve um erro ao decrementar pedido: \n" + resultado.getMsg(), "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

    private void chamarImpressao() {
        int option = JOptionPane.showConfirmDialog(null, "Deseja Imprimir o Comprovante?", "AVISO",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            //aqui imprime o pagamento
        }

        SessionUtil.getInstance().setPedido(new Pedido());
        SessionUtil.getInstance().setFlagLimparVenda(true);
        dispose();
    }

    /**
     * Atualiza dos valores dos caixas da Loja e do Funcionario
     */
    private void addCaixa() {

        SessionUtil sessionUtil = SessionUtil.getInstance();
        BigDecimal temp = new BigDecimal(0);
        temp = pagamento.getPedido().getValor();
        temp = temp.add(sessionUtil.getCaixaFuncionario().getValor());

        sessionUtil.getCaixaFuncionario().setValor(temp);

        temp = pagamento.getPedido().getValor();
        temp = temp.add(sessionUtil.getCaixaLoja().getValor());

        sessionUtil.getCaixaLoja().setValor(temp);

    }

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
            java.util.logging.Logger.getLogger(FrmPagamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmPagamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmPagamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmPagamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrmPagamento dialog = new FrmPagamento(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private javax.swing.JComboBox<String> cmbFormaDePagamento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JFormattedTextField txtTotal;
    private javax.swing.JFormattedTextField txtTroco;
    private javax.swing.JFormattedTextField txtValorPago;
    // End of variables declaration//GEN-END:variables
}
