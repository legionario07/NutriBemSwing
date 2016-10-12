/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nutribem.gui;

import br.com.nutribem.dao.RepositoryDao;
import br.com.nutribem.dominio.Categoria;
import br.com.nutribem.dominio.Produto;
import br.com.nutribem.dominio.EntidadeDominio;
import br.com.nutribem.dominio.Fornecedor;
import br.com.nutribem.dominio.UnidadeDeMedida;
import br.com.nutribem.fachada.Fachada;
import br.com.nutribem.fachada.Resultado;
import br.com.nutribem.gui.util.GuiUtil;
import br.com.nutribem.gui.util.TableUtil;
import br.com.nutribem.tablesutil.ProdutoTableModel;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author PauLinHo
 */
public class FrmProduto extends javax.swing.JDialog implements IForm {

    private RepositoryDao repositoryDao = null;
    private Produto produto;
    private Fachada fachada;
    private Resultado resultado;
    private List<EntidadeDominio> produtos;
    private List<EntidadeDominio> categorias;
    private List<EntidadeDominio> unidadesMedidas;
    private List<EntidadeDominio> fornecedores;

    private ImageIcon IMG_EDITAR = new ImageIcon(getClass().getResource("/images/edit_edited.png"));
    private ImageIcon IMG_SALVAR = new ImageIcon(getClass().getResource("/images/save_edited.png"));

    /**
     * Creates new form FrmLoja
     */
    public FrmProduto(java.awt.Frame parent, boolean modal) {

        super(parent, modal);
        initComponents();

        repositoryDao = new RepositoryDao();
        fachada = new Fachada();
        produto = new Produto();

        produtos = new ArrayList<EntidadeDominio>();

        //desativando componentes ao iniciar
        this.txtId.setEnabled(false);
        
        ativarParaCadastro(false);

        iniciarCombos();

        produtos = new ArrayList<EntidadeDominio>();
        produtos = GuiUtil.inicializarDadosNasTabelas(new Produto());

        preencherTabelas();

        inicializarCampoPesquisa();
    }

    private void iniciarCombos() {

        Vector<Categoria> vectorCategorias = new Vector<Categoria>();

        categorias = new ArrayList<EntidadeDominio>();
        categorias = repositoryDao.findAll(new Categoria());
        for (EntidadeDominio c : categorias) {
            if (c instanceof Categoria) {
                vectorCategorias.add((Categoria) c);
            }
        }
        DefaultComboBoxModel model = new DefaultComboBoxModel(vectorCategorias);
        cmbCategoria.setModel(model);

        unidadesMedidas = new ArrayList<EntidadeDominio>();
        unidadesMedidas = repositoryDao.findAll(new UnidadeDeMedida());
        Vector<UnidadeDeMedida> vectorUnidades = new Vector<UnidadeDeMedida>();
        for (EntidadeDominio u : unidadesMedidas) {
            if (u instanceof UnidadeDeMedida) {
                vectorUnidades.add((UnidadeDeMedida) u);
            }
        }
        model = new DefaultComboBoxModel(vectorUnidades);
        cmbUnidadeMedida.setModel(model);

        fornecedores = new ArrayList<EntidadeDominio>();
        fornecedores = repositoryDao.findAll(new Fornecedor());
        Vector<Fornecedor> vectorFornecedores = new Vector<Fornecedor>();
        for (EntidadeDominio f : fornecedores) {
            if (f instanceof Fornecedor) {
                vectorFornecedores.add((Fornecedor) f);
            }
        }
        model = new DefaultComboBoxModel(vectorFornecedores);
        cmbFornecedor.setModel(model);

    }

    @Override
    public void inicializarCampoPesquisa() {
        txtLocaliza.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                TableUtil.filterTable((TableRowSorter<ProdutoTableModel>) tabelaProdutos.getRowSorter(), txtLocaliza.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                TableUtil.filterTable((TableRowSorter<ProdutoTableModel>) tabelaProdutos.getRowSorter(), txtLocaliza.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                TableUtil.filterTable((TableRowSorter<ProdutoTableModel>) tabelaProdutos.getRowSorter(), txtLocaliza.getText());
            }
        });
    }

    @Override
    public void preencherTabelas() {

        ProdutoTableModel produtoTableModel = new ProdutoTableModel(produtos);
        tabelaProdutos = GuiUtil.configurarTabelas(tabelaProdutos);
        tabelaProdutos.setModel(produtoTableModel);
        TableRowSorter<ProdutoTableModel> sorter = new TableRowSorter<ProdutoTableModel>(produtoTableModel);
        tabelaProdutos.setRowSorter(sorter);

    }

    @Override
    public void ativarComponentes() {
        this.btnEditar.setEnabled(true);
        this.btnExcluir.setEnabled(true);
    }

    @Override
    public void desativarComponentes() {
        this.btnEditar.setEnabled(false);
        this.btnExcluir.setEnabled(false);
    }

    @Override
    public void preencherDadosNaView() {

        txtId.setText(String.valueOf(produto.getId()));
        txtDescricao.setText(produto.getDescricao());
        txtCodigoBarras.setText(produto.getCodigoBarras());
        txtQuantidadeEstoque.setText(String.valueOf(produto.getQuantidadeEstoque()));
        txtQuantidadeMinima.setText(String.valueOf(produto.getQuantidadeMinima()));
        txtPreco.setText(GuiUtil.getMoedaFormatada(String.valueOf(produto.getPreco())));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        txtDtFabricacao.setText(sdf.format(produto.getDataDeFabricacao()));
        txtDtVencimento.setText(sdf.format(produto.getDataDeValidade()));

        txtLote.setText(produto.getLote());
        cmbCategoria.setSelectedItem(produto.getCategoria());
        cmbUnidadeMedida.setSelectedItem(produto.getUnidadeDeMedida());
        cmbFornecedor.setSelectedItem(produto.getFornecedores().get(0));

    }

    @Override
    public void capturarDados() {

        if (txtId.getText().length() > 0) {
            produto.setId(Long.valueOf(txtId.getText()));
        }

        produto.setDescricao(txtDescricao.getText().toUpperCase());
        produto.setCodigoBarras(txtCodigoBarras.getText().toUpperCase());
        produto.setQuantidadeEstoque(Integer.valueOf(txtQuantidadeEstoque.getText()));
        produto.setQuantidadeMinima(Integer.valueOf(txtQuantidadeMinima.getText()));
        produto.setPreco(new BigDecimal(GuiUtil.getMoedaSemFormatacao(txtPreco.getText())));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            produto.setDataDeFabricacao(sdf.parse(txtDtFabricacao.getText()));
            produto.setDataDeValidade(sdf.parse(txtDtVencimento.getText()));
        } catch (ParseException ex) {
            Logger.getLogger(FrmColaborador.class.getName()).log(Level.SEVERE, null, ex);
        }

        produto.setLote(txtLote.getText());
        produto.setCategoria((Categoria) cmbCategoria.getSelectedItem());
        produto.setUnidadeDeMedida((UnidadeDeMedida) cmbUnidadeMedida.getSelectedItem());

        List<Fornecedor> fornecedores = new ArrayList<Fornecedor>();
        fornecedores.add((Fornecedor) cmbFornecedor.getSelectedItem());
        produto.setFornecedores(fornecedores);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlBotoes = new javax.swing.JPanel();
        btnNovo = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        txtId = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtDescricao = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtQuantidadeEstoque = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        txtCodigoBarras = new javax.swing.JTextField();
        txtQuantidadeMinima = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtDtFabricacao = new javax.swing.JFormattedTextField();
        jLabel24 = new javax.swing.JLabel();
        txtDtVencimento = new javax.swing.JFormattedTextField();
        jLabel25 = new javax.swing.JLabel();
        txtLote = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        cmbCategoria = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        btnAddCategoria = new javax.swing.JButton();
        cmbUnidadeMedida = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        btnAddUnidadeDeMedida = new javax.swing.JButton();
        cmbFornecedor = new javax.swing.JComboBox<>();
        jLabel29 = new javax.swing.JLabel();
        txtPreco = new javax.swing.JFormattedTextField();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        txtLocaliza = new javax.swing.JTextField();
        pnScrollCategoria = new javax.swing.JScrollPane();
        tabelaProdutos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gerenciar Colaborador");

        pnlBotoes.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "Opções"));
        pnlBotoes.setLayout(new java.awt.GridLayout(1, 0));

        btnNovo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/new_edited.png"))); // NOI18N
        btnNovo.setToolTipText("NOVO");
        btnNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoActionPerformed(evt);
            }
        });
        pnlBotoes.add(btnNovo);

        btnEditar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit_edited.png"))); // NOI18N
        btnEditar.setToolTipText("EDITAR");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        pnlBotoes.add(btnEditar);

        btnExcluir.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete_edited.png"))); // NOI18N
        btnExcluir.setToolTipText("EXCLUIR");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });
        pnlBotoes.add(btnExcluir);

        btnLimpar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/clear_edited.png"))); // NOI18N
        btnLimpar.setToolTipText("LIMPAR");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });
        pnlBotoes.add(btnLimpar);

        btnSair.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/exit_edited.png"))); // NOI18N
        btnSair.setToolTipText("SAIR");
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });
        pnlBotoes.add(btnSair);

        txtId.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel19.setText("Código:");

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setText("Descrição*:");

        txtDescricao.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtDescricao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescricaoActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel22.setText("Qtde Estoque*:");

        txtQuantidadeEstoque.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtQuantidadeEstoque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQuantidadeEstoqueActionPerformed(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel37.setText("Cod. Barras:");

        txtCodigoBarras.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtCodigoBarras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoBarrasActionPerformed(evt);
            }
        });

        txtQuantidadeMinima.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtQuantidadeMinima.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQuantidadeMinimaActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel23.setText("Qtde Mínima:");

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel26.setText("Preço*:");

        txtDtFabricacao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        txtDtFabricacao.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel24.setText("Data Fabricação:");

        txtDtVencimento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        txtDtVencimento.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel25.setText("Data Vencimento:");

        txtLote.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtLote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLoteActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel21.setText("Lote:");

        cmbCategoria.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel27.setText("Categoria:");

        btnAddCategoria.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnAddCategoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add18x18.png"))); // NOI18N
        btnAddCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddCategoriaActionPerformed(evt);
            }
        });

        cmbUnidadeMedida.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel28.setText("Un. Medida*:");

        btnAddUnidadeDeMedida.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnAddUnidadeDeMedida.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/add18x18.png"))); // NOI18N
        btnAddUnidadeDeMedida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddUnidadeDeMedidaActionPerformed(evt);
            }
        });

        cmbFornecedor.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel29.setText("Fornecedor*:");

        txtPreco.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getCurrencyInstance())));
        txtPreco.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtPreco.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPrecoFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel20)
                    .addComponent(jLabel22)
                    .addComponent(jLabel19)
                    .addComponent(jLabel24)
                    .addComponent(jLabel21)
                    .addComponent(jLabel27)
                    .addComponent(jLabel29))
                .addGap(10, 10, 10)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCodigoBarras, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtDescricao)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtDtFabricacao, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                            .addComponent(txtQuantidadeEstoque, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtLote, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addGap(10, 10, 10)
                                .addComponent(txtQuantidadeMinima, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel26))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDtVencimento, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                            .addComponent(txtPreco)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbCategoria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbFornecedor, javax.swing.GroupLayout.Alignment.TRAILING, 0, 150, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbUnidadeMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAddUnidadeDeMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel37)
                    .addComponent(txtCodigoBarras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtQuantidadeEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(txtQuantidadeMinima, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(txtPreco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDtFabricacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24)
                    .addComponent(txtDtVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(btnAddCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbUnidadeMedida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27)
                            .addComponent(jLabel28))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29)))
                    .addComponent(btnAddUnidadeDeMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "Localizar"));

        txtLocaliza.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtLocaliza.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                txtLocalizaCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtLocalizaInputMethodTextChanged(evt);
            }
        });

        pnScrollCategoria.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        tabelaProdutos.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tabelaProdutos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaProdutosMouseClicked(evt);
            }
        });
        pnScrollCategoria.setViewportView(tabelaProdutos);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnScrollCategoria, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(txtLocaliza, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(txtLocaliza, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnScrollCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 104, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 23, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBotoes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 579, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 568, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        // TODO add your handling code here:

        limparComponentes();

        this.btnNovo.setEnabled(false);
        this.btnEditar.setIcon(IMG_SALVAR);
        this.btnEditar.setToolTipText("SALVAR");
        this.btnEditar.setEnabled(true);

        ativarParaCadastro(true);

    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // TODO add your handling code here:
        resultado = new Resultado();

        if (btnEditar.getIcon().equals(IMG_SALVAR)) {
            resultado = salvar();
        } else {
            resultado = editar();
        }

        if (resultado.getEntidades().isEmpty()) {
            JOptionPane.showMessageDialog(null, resultado.getMsg(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        } else {

            limparComponentes();
            ativarParaCadastro(false);

            JOptionPane.showMessageDialog(null, resultado.getMsg());

            produtos = GuiUtil.inicializarDadosNasTabelas(new Produto());
            preencherTabelas();

        }

    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        // TODO add your handling code here:

        if (txtId.getText().length() == 0) {
            return;
        }

        int option = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja EXCLUIR?", "AVISO",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

        if (option == JOptionPane.OK_CANCEL_OPTION) {
            return;
        }

        produto = new Produto();
        produto.setId(Long.valueOf(txtId.getText()));

        resultado = new Resultado();
        resultado = fachada.delete(produto);

        if (resultado.getEntidades().isEmpty()) {
            JOptionPane.showMessageDialog(null, resultado.getMsg(), "Erro", JOptionPane.ERROR_MESSAGE);
        } else {
            limparComponentes();

            produtos = GuiUtil.inicializarDadosNasTabelas(new Produto());
            preencherTabelas();

            btnEditar.setEnabled(false);
            btnExcluir.setEnabled(false);
            btnNovo.setEnabled(true);

            ativarParaCadastro(false);
        }

    }//GEN-LAST:event_btnExcluirActionPerformed

    public void ativarParaCadastro(Boolean bool) {

        txtDescricao.setEnabled(bool);
        txtCodigoBarras.setEnabled(bool);
        txtQuantidadeEstoque.setEnabled(bool);
        txtQuantidadeMinima.setEnabled(bool);
        txtPreco.setEnabled(bool);
        txtDtFabricacao.setEnabled(bool);
        txtDtVencimento.setEnabled(bool);
        txtLote.setEnabled(bool);
        cmbCategoria.setEnabled(bool);
        cmbUnidadeMedida.setEnabled(bool);
        cmbFornecedor.setEnabled(bool);
        btnAddCategoria.setEnabled(bool);
        btnAddUnidadeDeMedida.setEnabled(bool);

    }

    @Override
    public void limparComponentes() {

        txtId.setText("");
        txtDescricao.setText("");
        txtCodigoBarras.setText("");
        txtQuantidadeEstoque.setText("");
        txtQuantidadeMinima.setText("");
        txtPreco.setText("");
        txtDtFabricacao.setText("");
        txtDtVencimento.setText("");
        txtLote.setText("");
        cmbCategoria.setSelectedItem(new Categoria());
        cmbUnidadeMedida.setSelectedItem(new Categoria());
        cmbFornecedor.setSelectedItem(new Categoria());

    }

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        // TODO add your handling code here:

        limparComponentes();

        this.txtId.setEnabled(false);

        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
        btnNovo.setEnabled(true);

        ativarParaCadastro(false);

    }//GEN-LAST:event_btnLimparActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnSairActionPerformed

    private void txtDescricaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescricaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescricaoActionPerformed

    private void txtLocalizaInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtLocalizaInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLocalizaInputMethodTextChanged

    private void txtLocalizaCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtLocalizaCaretPositionChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLocalizaCaretPositionChanged

    private void tabelaProdutosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaProdutosMouseClicked
        // TODO add your handling code here:
        produto = new Produto();
        produto.setId(Long.valueOf(String.valueOf(tabelaProdutos.getValueAt(tabelaProdutos.getSelectedRow(), 0))));

        produto = (Produto) repositoryDao.find(produto);
        preencherDadosNaView();

        this.btnExcluir.setEnabled(true);
        this.btnEditar.setIcon(IMG_EDITAR);
        this.btnEditar.setToolTipText("EDITAR");
        this.btnEditar.setEnabled(true);

        ativarParaCadastro(true);

    }//GEN-LAST:event_tabelaProdutosMouseClicked

    private void txtQuantidadeEstoqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuantidadeEstoqueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQuantidadeEstoqueActionPerformed

    private void txtCodigoBarrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoBarrasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoBarrasActionPerformed

    private void txtQuantidadeMinimaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuantidadeMinimaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQuantidadeMinimaActionPerformed

    private void txtLoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLoteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLoteActionPerformed

    private void btnAddCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddCategoriaActionPerformed
        // TODO add your handling code here:
        Categoria categoria = new Categoria();
        categoria.setCategoria(JOptionPane.showInputDialog(null, "Digite a Nova Categoria").toUpperCase());
        if(categoria.getCategoria().length()==0){
             JOptionPane.showMessageDialog(null, "Categoria Inválida", "Categoria Inválida", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        resultado = new Resultado();
        resultado = fachada.save(categoria);
        
         if (resultado.getEntidades().isEmpty()) {
            JOptionPane.showMessageDialog(null, resultado.getMsg(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        } else {

             JOptionPane.showMessageDialog(null, "Categoria adicionada com Sucesso","Adicionado",JOptionPane.INFORMATION_MESSAGE);
             iniciarCombos();

        }
        
    }//GEN-LAST:event_btnAddCategoriaActionPerformed

    private void btnAddUnidadeDeMedidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddUnidadeDeMedidaActionPerformed
        // TODO add your handling code here:
        UnidadeDeMedida unidadeDeMedida = new UnidadeDeMedida();
        unidadeDeMedida.setUnidadeDeMedida(JOptionPane.showInputDialog(null, "Digite a Nova Unidade De Medida").toUpperCase());
        if (unidadeDeMedida.getUnidadeDeMedida().length() == 0) {
            JOptionPane.showMessageDialog(null, "Unidade De Medida Inválida", "Unidade De Medida Inválida", JOptionPane.ERROR_MESSAGE);
            return;
        }

        resultado = new Resultado();
        resultado = fachada.save(unidadeDeMedida);

        if (resultado.getEntidades().isEmpty()) {
            JOptionPane.showMessageDialog(null, resultado.getMsg(), "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        } else {

            JOptionPane.showMessageDialog(null, "Unidade De Medida adicionada com Sucesso", "Adicionado", JOptionPane.INFORMATION_MESSAGE);
            iniciarCombos();
        }
        
    }//GEN-LAST:event_btnAddUnidadeDeMedidaActionPerformed

    private void txtPrecoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrecoFocusLost
        
        txtPreco.setText(GuiUtil.getMoedaFormatada(txtPreco.getText()));
        
    }//GEN-LAST:event_txtPrecoFocusLost

    @Override
    public Resultado salvar() {

        if (validarCampos() > 0) {
            JOptionPane.showMessageDialog(null, "Há Campos Vazios", "Campos Vazios", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        capturarDados();

        return fachada.save(produto);
    }

    @Override
    public Resultado editar() {

        produto = new Produto();
        produto.setId(Long.valueOf(txtId.getText()));
        produto = (Produto) repositoryDao.find(produto);

        if (validarCampos() > 0) {
            JOptionPane.showMessageDialog(null, "Há Campos Vazios", "Campos Vazios", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        capturarDados();

        return fachada.update(produto);

    }

    @Override
    public int validarCampos() {

        List<JComponent> componentes = new ArrayList<JComponent>();
        componentes.add(txtDescricao);
        componentes.add(txtQuantidadeEstoque);
        componentes.add(cmbCategoria);
        componentes.add(cmbUnidadeMedida);
        componentes.add(txtPreco);
        componentes.add(cmbFornecedor);

        return GuiUtil.validarDadosNaView(componentes);
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
            java.util.logging.Logger.getLogger(FrmLoja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmLoja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmLoja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmLoja.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrmProduto dialog = new FrmProduto(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAddCategoria;
    private javax.swing.JButton btnAddUnidadeDeMedida;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSair;
    private javax.swing.JComboBox<String> cmbCategoria;
    private javax.swing.JComboBox<String> cmbFornecedor;
    private javax.swing.JComboBox<String> cmbUnidadeMedida;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane pnScrollCategoria;
    private javax.swing.JPanel pnlBotoes;
    private javax.swing.JTable tabelaProdutos;
    private javax.swing.JTextField txtCodigoBarras;
    private javax.swing.JTextField txtDescricao;
    private javax.swing.JFormattedTextField txtDtFabricacao;
    private javax.swing.JFormattedTextField txtDtVencimento;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtLocaliza;
    private javax.swing.JTextField txtLote;
    private javax.swing.JFormattedTextField txtPreco;
    private javax.swing.JTextField txtQuantidadeEstoque;
    private javax.swing.JTextField txtQuantidadeMinima;
    // End of variables declaration//GEN-END:variables

}
