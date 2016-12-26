/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nutribem.gui;

import br.com.nutribem.dao.RepositoryDao;
import br.com.nutribem.dominio.CaixaFuncionario;
import br.com.nutribem.dominio.CaixaLoja;
import br.com.nutribem.dominio.EntidadeDominio;
import br.com.nutribem.dominio.ItemPedido;
import br.com.nutribem.dominio.Pedido;
import br.com.nutribem.dominio.PedidoStatus;
import br.com.nutribem.dominio.Produto;
import br.com.nutribem.fachada.Fachada;
import br.com.nutribem.fachada.Resultado;
import br.com.nutribem.gui.util.GuiUtil;
import br.com.nutribem.gui.util.SessionUtil;
import br.com.nutribem.tablesutil.PedidoTableModel;
import br.com.nutribem.utils.DataUtil;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author PauLinHo
 */
public class FrmVenda extends javax.swing.JFrame implements IForm {

    private Produto produto;
    private Pedido pedido;
    private ItemPedido itemPedido;
    private Fachada fachada;
    private RepositoryDao repositoryDao;
    private List<ItemPedido> itens;
    private BigDecimal valorTotal;
    private Boolean podeFecharFrame;
    private Map<String, Map<Integer, Integer>> mapEstoque = new HashMap<>();

    private Resultado resultado;

    public FrmVenda(java.awt.Frame parent, boolean modal) {

        initComponents();

        podeFecharFrame = false;
        produto = new Produto();
        fachada = new Fachada();
        pedido = new Pedido();
        itemPedido = new ItemPedido();
        repositoryDao = new RepositoryDao();
        itens = new ArrayList<ItemPedido>();
        valorTotal = new BigDecimal(GuiUtil.getMoedaSemFormatacao("0.00"));

        int i = inicializarCaixa();

        //Houve erro nos dados digitados
        if (i == -1) {
            podeFecharFrame = true;
            return;
        }

        ativarParaCadastro(false);

        limparComponentes();
        cadastrarTeclasDeAtalhos();
        preencherTabelas();

    }

    /**
     * Metodo chamado ao fechar o frame
     */
    private void fecharCaixa() {

        SessionUtil sessionUtil = SessionUtil.getInstance();

        sessionUtil.getCaixaFuncionario().setData(DataUtil.pegarDataAtualDoSistema());
        sessionUtil.getCaixaFuncionario().setIdentificacao("FECHAMENTO");

        sessionUtil.getCaixaLoja().setData(DataUtil.pegarDataAtualDoSistema());
        sessionUtil.getCaixaLoja().setIdentificacao("FECHAMENTO");

        List<CaixaFuncionario> caixas = new ArrayList<CaixaFuncionario>();
        caixas.add(sessionUtil.getCaixaFuncionario());
        sessionUtil.getCaixaLoja().setCaixas(caixas);

        Fachada fachada = new Fachada();
        fachada.save(sessionUtil.getCaixaFuncionario());
        fachada.save(sessionUtil.getCaixaLoja());
    }

    /**
     * Metodo chamado ao iniciar o frame. retorna -1 se o valor passado para o 
     * caixa for um valor invalido
     * @return 
     */
    private int inicializarCaixa() {
        SessionUtil sessionUtil = SessionUtil.getInstance();

        //Pegando os valores do ultimo caixa loja aberto
        Long lasId = repositoryDao.findLastId(new CaixaLoja());
        sessionUtil.getCaixaLoja().setId(lasId);
        sessionUtil.setCaixaLoja((CaixaLoja) repositoryDao.find(sessionUtil.getCaixaLoja()));

        //Solicitando o valor do caixa Funcionario
        BigDecimal temp = new BigDecimal("0.00");
        temp = sessionUtil.getCaixaLoja().getValor();
        String valor = GuiUtil.getMoedaSemFormatacao(String.valueOf(JOptionPane.showInputDialog(
                "Valor Atual: "
                + GuiUtil.getMoedaFormatada(String.valueOf(temp))
                + "\nTecle ENTER para Aceitar o valor Atual.",GuiUtil.getMoedaFormatada(String.valueOf(temp)))));

        
        valor = valor.replace(",", ".");
        int i = isDigit(valor);

        if (valor.length() == 0) {
            sessionUtil.getCaixaFuncionario().setValor(sessionUtil.getCaixaLoja().getValor());
        } else if (i == -1) {
            JOptionPane.showMessageDialog(null, "O valor não pode conter letras", "valor Inválido", JOptionPane.ERROR_MESSAGE);
            return -1;
        } else {
            BigDecimal valorInicial = new BigDecimal(valor);
            if (valorInicial.compareTo(new BigDecimal("0.00")) == -1) {
                JOptionPane.showMessageDialog(null, "O valor não pode ser negativo", "valor Inválido", JOptionPane.ERROR_MESSAGE);
                return -1;
            }
            sessionUtil.getCaixaFuncionario().setValor(valorInicial);
        }

        sessionUtil.getCaixaFuncionario().setColaborador(SessionUtil.getInstance().getColaborador());
        sessionUtil.getCaixaFuncionario().setIdentificacao("ABERTURA");
        sessionUtil.getCaixaFuncionario().setData(DataUtil.pegarDataAtualDoSistema());

        //SALVANDO A ABERTURA DE CAIXA DO FUNCIONARIO
        fachada = new Fachada();
        fachada.save(sessionUtil.getCaixaFuncionario());

        //PEGANDO OS ULTIMOS DADOS DO CAIXA DA LOJA
        sessionUtil.getCaixaLoja().setData(DataUtil.pegarDataAtualDoSistema());
        sessionUtil.getCaixaLoja().setIdentificacao("ABERTURA");

        //SALVANDO A ABERTURA DE CAIXA DA LOJA
        fachada.save(sessionUtil.getCaixaLoja());

        return 0;
    }

    /**
     *
     * @param Um String
     * @return -1 se existir letras e 0 se existir apenas numeros
     */
    private int isDigit(String valor) {
        int l = 0;
        valor = valor.replace(".", "");
        for (int k = 0; k < valor.length(); k++) {
            if (!Character.isDigit(valor.charAt(k))) {
                l = 1;
                break;
            }
        }

        if (l == 1) {
            JOptionPane.showMessageDialog(null, "Digite Apenas Números", "Erro", JOptionPane.ERROR_MESSAGE);
            txtQuantidade.setText("1");
            txtQuantidade.requestFocus();
            txtQuantidade.selectAll();
            return -1;
        }

        return 0;
    }

    /**
     * Configurando as teclas de atalhos do FORM
     */
    private void cadastrarTeclasDeAtalhos() {
        InputMap inputMap = this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "RemoverItem");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "AddItem");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0), "PesquisarItem");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0), "Sair");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "Confirmar");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0), "Limpar");

        this.getRootPane().setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, inputMap);
        this.getRootPane().getActionMap().put("Sair", new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                btnSair.doClick();
            }
        });

        this.getRootPane().getActionMap().put("Limpar", new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                btnLimpar.doClick();
            }
        });

        this.getRootPane().getActionMap().put("Confirmar", new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                btnConfimar.doClick();
            }
        });

        this.getRootPane().getActionMap().put("PesquisarItem", new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                produto = new Produto();
                produto.setId(Long.valueOf(JOptionPane.showInputDialog(null, "Digite o código do produto")));
                if (produto.getId() == 0) {
                    JOptionPane.showMessageDialog(null, "Código Inválido", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    produto = (Produto) repositoryDao.find(produto);
                    if (produto == null) {
                        JOptionPane.showMessageDialog(null, "Produto não encontrado", "Erro", JOptionPane.ERROR_MESSAGE);
                        txtCodigoDeBarras.requestFocus();
                        txtCodigoDeBarras.selectAll();
                    } else {
                        txtCodigoDeBarras.setText(produto.getCodigoBarras());
                        txtCodigoDeBarras.requestFocus();
                        txtCodigoDeBarras.selectAll();
                    }
                }
            }
        });

        this.getRootPane().getActionMap().put("AddItem", new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                btnAddItem.doClick();
            }
        });

        this.getRootPane().getActionMap().put("RemoverItem", new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent arg0) {
                btnRemoveItem.doClick();
            }
        });
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlDados = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtCodigoDeBarras = new javax.swing.JTextField();
        txtQuantidade = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtValorItem = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        txtCategoria = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtDescricao = new javax.swing.JTextField();
        txtUnidadeMedida = new javax.swing.JTextField();
        txtValorTotal = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        txtDesconto = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        txtValorUnitario = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        pnlBotoes = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        pnlBotoes1 = new javax.swing.JPanel();
        btnConfimar = new javax.swing.JToggleButton();
        btnRemoveItem = new javax.swing.JToggleButton();
        btnLimpar = new javax.swing.JToggleButton();
        btnSair = new javax.swing.JToggleButton();
        btnAddItem = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaPedidos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("VENDA");
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Qtde");

        txtCodigoDeBarras.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txtCodigoDeBarras.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCodigoDeBarras.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCodigoDeBarrasFocusLost(evt);
            }
        });

        txtQuantidade.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txtQuantidade.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtQuantidade.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtQuantidadeFocusLost(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Cód. Barras");

        txtValorItem.setForeground(new java.awt.Color(255, 0, 51));
        txtValorItem.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance())));
        txtValorItem.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtValorItem.setDisabledTextColor(new java.awt.Color(255, 0, 51));
        txtValorItem.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Valor Item");

        txtCategoria.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        txtCategoria.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCategoria.setDisabledTextColor(new java.awt.Color(255, 0, 51));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Descrição");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Categoria");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Uni. Medida");

        txtDescricao.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txtDescricao.setDisabledTextColor(new java.awt.Color(255, 0, 51));

        txtUnidadeMedida.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        txtUnidadeMedida.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtUnidadeMedida.setDisabledTextColor(new java.awt.Color(255, 0, 51));

        txtValorTotal.setForeground(new java.awt.Color(255, 0, 51));
        txtValorTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtValorTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtValorTotal.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        txtValorTotal.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 28)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 0, 51));
        jLabel7.setText("VALOR TOTAL:");

        txtDesconto.setForeground(new java.awt.Color(255, 0, 51));
        txtDesconto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance())));
        txtDesconto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDesconto.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txtDesconto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDescontoFocusLost(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Desconto");

        txtValorUnitario.setForeground(new java.awt.Color(255, 0, 51));
        txtValorUnitario.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance())));
        txtValorUnitario.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtValorUnitario.setDisabledTextColor(new java.awt.Color(255, 0, 51));
        txtValorUnitario.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Valor Unitário");

        javax.swing.GroupLayout pnlDadosLayout = new javax.swing.GroupLayout(pnlDados);
        pnlDados.setLayout(pnlDadosLayout);
        pnlDadosLayout.setHorizontalGroup(
            pnlDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDadosLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addGap(85, 85, 85)
                .addComponent(jLabel2)
                .addGap(110, 110, 110)
                .addComponent(jLabel8)
                .addGap(45, 45, 45)
                .addComponent(jLabel11)
                .addGap(38, 38, 38)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlDadosLayout.createSequentialGroup()
                .addGroup(pnlDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlDadosLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlDadosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlDadosLayout.createSequentialGroup()
                                .addComponent(txtQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCodigoDeBarras, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtDesconto, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14)
                                .addComponent(txtValorUnitario, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtValorItem, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlDadosLayout.createSequentialGroup()
                                .addGroup(pnlDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlDadosLayout.createSequentialGroup()
                                        .addGap(279, 279, 279)
                                        .addComponent(jLabel4))
                                    .addGroup(pnlDadosLayout.createSequentialGroup()
                                        .addGap(72, 72, 72)
                                        .addComponent(jLabel5))
                                    .addComponent(txtCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnlDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtUnidadeMedida, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDadosLayout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(76, 76, 76))))
                            .addComponent(txtDescricao))))
                .addGap(17, 17, 17))
        );
        pnlDadosLayout.setVerticalGroup(
            pnlDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDadosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel8)
                    .addComponent(jLabel11))
                .addGap(8, 8, 8)
                .addGroup(pnlDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodigoDeBarras, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtValorItem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDesconto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtValorUnitario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5))
                .addGroup(pnlDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDadosLayout.createSequentialGroup()
                        .addComponent(txtCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlDadosLayout.createSequentialGroup()
                        .addGap(0, 3, Short.MAX_VALUE)
                        .addComponent(txtUnidadeMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(pnlDadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(11, 11, 11))
        );

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Remover Item(F1) - AddItem(F2) - Pesquisar(F3) - Sair(F4) - Confirmar(F5) - Limpar(F7)");

        javax.swing.GroupLayout pnlBotoesLayout = new javax.swing.GroupLayout(pnlBotoes);
        pnlBotoes.setLayout(pnlBotoesLayout);
        pnlBotoesLayout.setHorizontalGroup(
            pnlBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBotoesLayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlBotoesLayout.setVerticalGroup(
            pnlBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBotoesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnConfimar.setBackground(new java.awt.Color(255, 0, 51));
        btnConfimar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnConfimar.setForeground(new java.awt.Color(255, 0, 51));
        btnConfimar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ok40x40.png"))); // NOI18N
        btnConfimar.setToolTipText("CONFIRMAR");
        btnConfimar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnConfimar.setBorderPainted(false);
        btnConfimar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnConfimar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfimarActionPerformed(evt);
            }
        });

        btnRemoveItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/remove_item40x40.png"))); // NOI18N
        btnRemoveItem.setToolTipText("REMOVER ITEM");
        btnRemoveItem.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnRemoveItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveItemActionPerformed(evt);
            }
        });

        btnLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/clear40X40.png"))); // NOI18N
        btnLimpar.setToolTipText("LIMPAR");
        btnLimpar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        btnSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exit40x40.png"))); // NOI18N
        btnSair.setToolTipText("SAIR");
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });

        btnAddItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add_item40x40.png"))); // NOI18N
        btnAddItem.setToolTipText("Adicionar Item");
        btnAddItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddItemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlBotoes1Layout = new javax.swing.GroupLayout(pnlBotoes1);
        pnlBotoes1.setLayout(pnlBotoes1Layout);
        pnlBotoes1Layout.setHorizontalGroup(
            pnlBotoes1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBotoes1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAddItem, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemoveItem, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnConfimar, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );
        pnlBotoes1Layout.setVerticalGroup(
            pnlBotoes1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnRemoveItem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnLimpar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnSair, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnConfimar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnAddItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tabelaPedidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tabelaPedidos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlDados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlBotoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnlBotoes1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 627, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlDados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(pnlBotoes1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRemoveItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveItemActionPerformed

        removerItem();

    }//GEN-LAST:event_btnRemoveItemActionPerformed

    /**
     *
     * @param qtde - um inteiro com a quantidade desejada
     * @param produto - o produto para comparar o estoue
     * @return - um boolean com false se nao ha estoque e true se tem estoque
     */
    private Boolean validarEstoqueDoProduto(int qtde, Produto produto) {
        Boolean temEstoque = true;
        
        Set<String> keys = mapEstoque.keySet();
        
        Boolean contem = keys.contains(produto.getCodigoBarras());
        Map<Integer, Integer> mapQtde= new HashMap<>();
        
        if(contem){
            mapQtde = mapEstoque.get(produto.getCodigoBarras());
            int foiComprado = mapQtde.get(produto.getQuantidadeEstoque());
            int estoque = produto.getQuantidadeEstoque();
            mapQtde.put(estoque, mapQtde.get(estoque)+qtde);
            mapEstoque.put(produto.getCodigoBarras(),mapQtde);
            qtde+=foiComprado;
            
            
        } else {
            mapQtde.put(produto.getQuantidadeEstoque(), qtde);
            mapEstoque.put(produto.getCodigoBarras(), mapQtde);
        }

        if (qtde > produto.getQuantidadeEstoque()) {
            temEstoque = false;
        }

        return temEstoque;
    }

    private void txtCodigoDeBarrasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCodigoDeBarrasFocusLost

        produto = new Produto();
        //verifica se foi digitado alguma coisa 
        if (txtCodigoDeBarras.getText().equals("")) {
            return;
        }

        try {
            produto.setCodigoBarras(txtCodigoDeBarras.getText());
        } catch (NullPointerException e) {
            txtDescricao.setText("");
            txtDescricao.requestFocus();
            return;
        }

        produto = repositoryDao.findProdutoByCodigoBarras(produto);

        //se o produto nao foi localizado
        if (produto == null) {
            JOptionPane.showMessageDialog(null, "Código de Barras Inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            txtCodigoDeBarras.setText("");
            txtCodigoDeBarras.requestFocus();
            return;
        }

        //Nao ha estoque disponivel?
        if (!validarEstoqueDoProduto(Integer.valueOf(txtQuantidade.getText()), produto)) {
            JOptionPane.showMessageDialog(null, produto.getDescricao() + "\n"
                    + "Estoque do Produto: " + produto.getQuantidadeEstoque()
                    + "\nProduto com estoque inferior a quantidade desejada!", "Erro", JOptionPane.ERROR_MESSAGE);
            txtCodigoDeBarras.requestFocus();
            txtCodigoDeBarras.setText("");
            txtCodigoDeBarras.selectAll();
            
            Map<Integer, Integer> mapProduto = new HashMap<>();
            mapProduto=mapEstoque.get(produto.getCodigoBarras());
            mapProduto.put(produto.getQuantidadeEstoque(), mapProduto.get(produto.getQuantidadeEstoque())-(Integer.valueOf(txtQuantidade.getText())));
            mapEstoque.put(produto.getCodigoBarras(), mapProduto);
            
            return;
        }

        preencherDadosNaView();

        BigDecimal valorUnitario = new BigDecimal(GuiUtil.getMoedaSemFormatacao(txtValorUnitario.getText()));
        int quantidade = Integer.valueOf(txtQuantidade.getText());
        BigDecimal temp = new BigDecimal(quantidade);
        valorUnitario = valorUnitario.multiply(temp);
        txtValorItem.setText(GuiUtil.getMoedaFormatada(String.valueOf(valorUnitario)));
        valorTotal = valorTotal.add(valorUnitario);

        txtValorTotal.setText(GuiUtil.getMoedaFormatada(String.valueOf(valorTotal)));

        txtDesconto.requestFocus();
        txtDesconto.selectAll();


    }//GEN-LAST:event_txtCodigoDeBarrasFocusLost

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        // TODO add your handling code here:
        int option = JOptionPane.showConfirmDialog(null, "Deseja Fechar a Tela de Vendas?", "AVISO",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            fecharCaixa();
            this.dispose();
        } else {
            this.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
        }


    }//GEN-LAST:event_btnSairActionPerformed

    private void txtQuantidadeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQuantidadeFocusLost
        //verifica se é numero
        if (txtQuantidade.getText().length() < 1) {
            JOptionPane.showMessageDialog(null, "A quantidade Minima deve ser 1", "Erro", JOptionPane.ERROR_MESSAGE);
            txtQuantidade.setText("1");
            txtQuantidade.requestFocus();
            txtQuantidade.selectAll();
            return;
        }
        String valor = txtQuantidade.getText();
        int l = 0;
        for (int k = 0; k < valor.length(); k++) {
            if (!Character.isDigit(valor.charAt(k))) {
                l = 1;
                break;
            }
        }

        if (l == 1) {
            JOptionPane.showMessageDialog(null, "Digite Apenas Números", "Erro", JOptionPane.ERROR_MESSAGE);
            txtQuantidade.setText("1");
            txtQuantidade.requestFocus();
            txtQuantidade.selectAll();
            return;
        }
        int quantidade = Integer.valueOf(txtQuantidade.getText());
        if (quantidade < 1) {
            JOptionPane.showMessageDialog(null, "A quantidade Minima deve ser 1", "Erro", JOptionPane.ERROR_MESSAGE);
            txtQuantidade.setText("1");
            txtQuantidade.requestFocus();
            txtQuantidade.selectAll();
            return;
        }

    }//GEN-LAST:event_txtQuantidadeFocusLost

    private void txtDescontoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDescontoFocusLost
        // TODO add your handling code here:
        BigDecimal desconto = new BigDecimal(GuiUtil.getMoedaSemFormatacao(txtDesconto.getText()));
        BigDecimal valorUnitario = new BigDecimal(GuiUtil.getMoedaSemFormatacao(txtValorUnitario.getText()));

        int quantidade = Integer.valueOf(txtQuantidade.getText());

        BigDecimal temp = new BigDecimal(quantidade);
        valorUnitario = valorUnitario.multiply(temp);

        if (desconto.compareTo(valorUnitario) == 1) {
            JOptionPane.showMessageDialog(null, "O Desconto não pode ser maior que o valor Total!", "Erro", JOptionPane.ERROR_MESSAGE);
            txtDesconto.setText(GuiUtil.getMoedaFormatada("0.00"));
            txtDesconto.requestFocus();
            txtDesconto.selectAll();
            return;
        } else {
            valorTotal = valorTotal.subtract(desconto);
            valorUnitario = valorUnitario.subtract(desconto);
            txtValorItem.setText(GuiUtil.getMoedaFormatada(String.valueOf(valorUnitario)));

            if (txtDescricao.getText().length() > 0) {
                addItem();
            } else {
                btnAddItem.requestFocus();
            }
        }
    }//GEN-LAST:event_txtDescontoFocusLost

    private void btnAddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddItemActionPerformed
        // TODO add your handling code here:
        if (txtDescricao.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Escolha o Produto Primeiro!", "Erro", JOptionPane.ERROR_MESSAGE);
            txtCodigoDeBarras.requestFocus();
            txtCodigoDeBarras.selectAll();
            return;
        }
        addItem();

    }//GEN-LAST:event_btnAddItemActionPerformed

    private void addItem() {

        itemPedido = new ItemPedido();
        itemPedido.setProduto(produto);
        BigDecimal desconto = new BigDecimal(GuiUtil.getMoedaSemFormatacao(txtDesconto.getText()));
        itemPedido.setDesconto(desconto);
        itemPedido.setQuantidade(Integer.valueOf(txtQuantidade.getText()));

        BigDecimal valorItem = new BigDecimal(GuiUtil.getMoedaSemFormatacao(txtValorItem.getText()));

        itemPedido.setValor(valorItem);
        itens.add(itemPedido);

        pedido.setItens(itens);

        itemPedido = new ItemPedido();
        produto = new Produto();

        preencherTabelas();

        limparComponentes();

        txtQuantidade.setText("1");
        txtQuantidade.requestFocus();
        txtQuantidade.selectAll();

        txtValorTotal.setText(GuiUtil.getMoedaFormatada(String.valueOf(valorTotal)));
    }

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        // TODO add your handling code here:
        limparComponentes();
        txtValorTotal.setText(GuiUtil.getMoedaFormatada("0.00"));
        valorTotal = new BigDecimal(GuiUtil.getMoedaSemFormatacao("0.00"));
        txtQuantidade.setText("1");
        txtQuantidade.requestFocus();
        mapEstoque.clear();

        itens.clear();
        pedido.setItens(itens);
        preencherTabelas();

    }//GEN-LAST:event_btnLimparActionPerformed

    private void btnConfimarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfimarActionPerformed

        //verifica se a tabela esta vazia
        if (tabelaPedidos.getRowCount() < 1) {
            JOptionPane.showMessageDialog(null, "Pedido sem Itens", "Erro", JOptionPane.ERROR_MESSAGE);
            txtQuantidade.requestFocus();
            txtQuantidade.selectAll();
            return;
        }

        //pegar data atual do sistema para o pedido
        pedido.setDataPedido(DataUtil.pegarDataAtualDoSistema());

        //pegando valor total do pedido
        BigDecimal valorTotalPedido = new BigDecimal(0);
        for (ItemPedido i : pedido.getItens()) {
            valorTotalPedido = valorTotalPedido.add(i.getValor());
        }
        pedido.setValor(valorTotalPedido);

        //setando o status do Pedido
        PedidoStatus pedidoStatus = new PedidoStatus();
        //ID 2 = Aguardando Pagamento
        pedidoStatus.setId(2L);
        pedidoStatus = (PedidoStatus) repositoryDao.find(pedidoStatus);
        pedido.setPedidoStatus(pedidoStatus);

        SessionUtil.getInstance().setPedido(pedido);

        //Abrir Form Para Pagamento
        FrmPagamento frmPagamento = new FrmPagamento(this, true);
        frmPagamento.setResizable(false);
        frmPagamento.setLocationRelativeTo(null);
        frmPagamento.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        frmPagamento.setVisible(true);


    }//GEN-LAST:event_btnConfimarActionPerformed

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained

    }//GEN-LAST:event_formFocusGained

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated

        if(podeFecharFrame){
            this.dispose();
        }
        //Limpa as vendas
        if (SessionUtil.getInstance().isFlagLimparVenda() == true) {
            txtDesconto.setText(GuiUtil.getMoedaFormatada("0.00"));
            pedido = new Pedido();
            txtValorTotal.setText(GuiUtil.getMoedaFormatada("0.00"));
            valorTotal = new BigDecimal(GuiUtil.getMoedaSemFormatacao("0.00"));
            txtQuantidade.setText("1");
            txtQuantidade.requestFocus();

            itens.clear();
            pedido.setItens(itens);
            preencherTabelas();

            SessionUtil.getInstance().setFlagLimparVenda(false);
        }
    }//GEN-LAST:event_formWindowActivated

    private void removerItem() {
        if (tabelaPedidos.getSelectedRow() != -1) {

            if (tabelaPedidos.getRowCount() == 1) {
                ((PedidoTableModel) tabelaPedidos.getModel()).removeRow(tabelaPedidos.getSelectedRow());
                limparComponentes();
                pedido.getItens().clear();
                itens.clear();
                txtValorTotal.setText(GuiUtil.getMoedaFormatada("0.00"));
                mapEstoque.clear();
                
            } else {

                //Atualizando o valor total do campo total
                BigDecimal b = new BigDecimal(0);
                b = (BigDecimal) tabelaPedidos.getValueAt(tabelaPedidos.getSelectedRow(), 5);
                BigDecimal tempValorTotal = new BigDecimal(GuiUtil.getMoedaSemFormatacao(txtValorTotal.getText()));
                tempValorTotal = tempValorTotal.subtract(b);
                valorTotal = tempValorTotal;
                txtValorTotal.setText(GuiUtil.getMoedaFormatada(String.valueOf(tempValorTotal)));

                //removendo a quantidade do MapEstoque
                Produto temp = new Produto();
                temp.setCodigoBarras(String.valueOf(tabelaPedidos.getValueAt(tabelaPedidos.getSelectedRow(), 1)));
                temp = repositoryDao.findProdutoByCodigoBarras(temp);
                int qtde = ((Integer)tabelaPedidos.getValueAt(tabelaPedidos.getSelectedRow(), 0));
                
                Map<Integer, Integer> mapProduto = new HashMap<>();
                mapProduto = mapEstoque.get(temp.getCodigoBarras());
                mapProduto.put(temp.getQuantidadeEstoque(), mapProduto.get(temp.getQuantidadeEstoque())-qtde);
                mapEstoque.put(temp.getCodigoBarras(), mapProduto);
                
                //remover a linha clicada
                ((PedidoTableModel) tabelaPedidos.getModel()).removeRow(tabelaPedidos.getSelectedRow());

                ItemPedido item = null;
                Produto p = null;
                List<ItemPedido> listaItens = new ArrayList<ItemPedido>();
                for (int i = 0; i < tabelaPedidos.getRowCount(); i++) {

                    item = new ItemPedido();
                    p = new Produto();
                    item.setQuantidade((Integer) tabelaPedidos.getValueAt(i, 0));

                    p.setCodigoBarras(String.valueOf(tabelaPedidos.getValueAt(i, 1)));
                    p = repositoryDao.findProdutoByCodigoBarras(p);
                    if (p == null) {
                        JOptionPane.showMessageDialog(null, "Erro ao remover produto", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    item.setProduto(p);
                    BigDecimal valorItem = new BigDecimal("0.00");
                    valorItem = (BigDecimal) tabelaPedidos.getValueAt(i, 5);
                    item.setValor(valorItem);
                    item.setDesconto((BigDecimal) tabelaPedidos.getValueAt(i, 3));
                    listaItens.add(item);

                   
                }

                itens.clear();
                itens = listaItens;

                pedido.getItens().clear();
                pedido.setItens(itens);
                
                preencherTabelas();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Pedido sem Itens", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
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
            java.util.logging.Logger.getLogger(FrmVenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmVenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmVenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmVenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrmVenda dialog = new FrmVenda(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAddItem;
    private javax.swing.JToggleButton btnConfimar;
    private javax.swing.JToggleButton btnLimpar;
    private javax.swing.JToggleButton btnRemoveItem;
    private javax.swing.JToggleButton btnSair;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlBotoes;
    private javax.swing.JPanel pnlBotoes1;
    private javax.swing.JPanel pnlDados;
    private javax.swing.JTable tabelaPedidos;
    private javax.swing.JTextField txtCategoria;
    private javax.swing.JTextField txtCodigoDeBarras;
    private javax.swing.JFormattedTextField txtDesconto;
    private javax.swing.JTextField txtDescricao;
    private javax.swing.JTextField txtQuantidade;
    private javax.swing.JTextField txtUnidadeMedida;
    private javax.swing.JFormattedTextField txtValorItem;
    private javax.swing.JFormattedTextField txtValorTotal;
    private javax.swing.JFormattedTextField txtValorUnitario;
    // End of variables declaration//GEN-END:variables

    @Override
    public void ativarParaCadastro(Boolean bool) {
        txtValorItem.setEnabled(bool);
        txtValorTotal.setEnabled(bool);
        txtCategoria.setEnabled(bool);
        txtUnidadeMedida.setEnabled(bool);
        txtDescricao.setEnabled(bool);
        txtValorUnitario.setEnabled(bool);
    }

    @Override
    public void inicializarCampoPesquisa() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void preencherTabelas() {
        List<EntidadeDominio> itens = new ArrayList<EntidadeDominio>();
        for (ItemPedido i : pedido.getItens()) {
            itens.add(i);
        }
        PedidoTableModel pedidoTableModel = new PedidoTableModel(itens);
        tabelaPedidos = GuiUtil.configurarTabelas(tabelaPedidos);
        tabelaPedidos.setModel(pedidoTableModel);
        TableRowSorter<PedidoTableModel> sorter = new TableRowSorter<PedidoTableModel>(pedidoTableModel);
        tabelaPedidos.setRowSorter(sorter);
    }

    @Override
    public void ativarComponentes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void desativarComponentes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void preencherDadosNaView() {

        txtCodigoDeBarras.setText(produto.getCodigoBarras());
        txtValorUnitario.setText(GuiUtil.getMoedaFormatada(String.valueOf(produto.getPreco())));
        txtDescricao.setText(produto.getDescricao());
        txtCategoria.setText(produto.getCategoria().getCategoria());
        txtUnidadeMedida.setText(produto.getUnidadeDeMedida().getUnidadeDeMedida());

    }

    @Override
    public void capturarDados() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void limparComponentes() {

        txtQuantidade.setText("1");
        txtQuantidade.selectAll();
        txtCodigoDeBarras.setText("");
        txtDesconto.setText(GuiUtil.getMoedaFormatada("0.00"));
        txtValorItem.setText(GuiUtil.getMoedaFormatada("0.00"));
        txtDescricao.setText("");
        txtCategoria.setText("");
        txtUnidadeMedida.setText("");
        txtValorUnitario.setText(GuiUtil.getMoedaFormatada("0.00"));

    }

    @Override
    public int validarCampos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Resultado salvar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Resultado editar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
