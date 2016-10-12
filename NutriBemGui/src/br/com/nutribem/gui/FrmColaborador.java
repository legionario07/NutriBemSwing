/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nutribem.gui;

import br.com.nutribem.dao.RepositoryDao;
import br.com.nutribem.dominio.Colaborador;
import br.com.nutribem.dominio.Endereco;
import br.com.nutribem.dominio.EntidadeDominio;
import br.com.nutribem.dominio.Permissao;
import br.com.nutribem.dominio.enums.SexoType;
import br.com.nutribem.fachada.Fachada;
import br.com.nutribem.fachada.Resultado;
import br.com.nutribem.gui.util.GuiUtil;
import br.com.nutribem.gui.util.TableUtil;
import br.com.nutribem.tablesutil.ColaboradorTableModel;
import br.com.nutribem.utils.CepUtil;
import br.com.nutribem.utils.EncryptMD5Util;
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
public class FrmColaborador extends javax.swing.JDialog implements IForm {

    private RepositoryDao repositoryDao = null;
    private Colaborador colaborador;
    private Fachada fachada;
    private Resultado resultado;
    private List<EntidadeDominio> colaboradores;
    private List<EntidadeDominio> listaPermissoes;

    private ImageIcon IMG_EDITAR = new ImageIcon(getClass().getResource("/images/edit_edited.png"));
    private ImageIcon IMG_SALVAR = new ImageIcon(getClass().getResource("/images/save_edited.png"));

    /**
     * Creates new form FrmLoja
     */
    public FrmColaborador(java.awt.Frame parent, boolean modal) {

        super(parent, modal);
        initComponents();

        repositoryDao = new RepositoryDao();
        fachada = new Fachada();
        colaborador = new Colaborador();

        colaboradores = new ArrayList<EntidadeDominio>();

        this.txtId.setEnabled(false);
        ativarParaCadastro(false);

        iniciarCombos();

        colaboradores = new ArrayList<EntidadeDominio>();
        colaboradores = GuiUtil.inicializarDadosNasTabelas(new Colaborador());

        preencherTabelas();

        inicializarCampoPesquisa();
    }

    private void iniciarCombos() {

        Vector<SexoType> sexos = new Vector<SexoType>();
        for (SexoType s : SexoType.values()) {
            sexos.add(s);
        }
        DefaultComboBoxModel model = new DefaultComboBoxModel(sexos);
        cmbSexo.setModel(model);

        listaPermissoes = new ArrayList<EntidadeDominio>();
        listaPermissoes = repositoryDao.findAll(new Permissao());
        Vector<Permissao> permissoes = new Vector<Permissao>();
        for (EntidadeDominio p : listaPermissoes) {
            if (p instanceof Permissao) {
                permissoes.add((Permissao) p);
            }
        }

        DefaultComboBoxModel cmbPermissoes = new DefaultComboBoxModel(permissoes);
        cmbPermissao.setModel(cmbPermissoes);

    }

    @Override
    public void inicializarCampoPesquisa() {
        txtLocaliza.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                TableUtil.filterTable((TableRowSorter<ColaboradorTableModel>) tabelaColaboradores.getRowSorter(), txtLocaliza.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                TableUtil.filterTable((TableRowSorter<ColaboradorTableModel>) tabelaColaboradores.getRowSorter(), txtLocaliza.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                TableUtil.filterTable((TableRowSorter<ColaboradorTableModel>) tabelaColaboradores.getRowSorter(), txtLocaliza.getText());
            }
        });
    }

    @Override
    public void preencherTabelas() {

        ColaboradorTableModel colaboradorTableModel = new ColaboradorTableModel(colaboradores);
        tabelaColaboradores = GuiUtil.configurarTabelas(tabelaColaboradores);
        tabelaColaboradores.setModel(colaboradorTableModel);
        TableRowSorter<ColaboradorTableModel> sorter = new TableRowSorter<ColaboradorTableModel>(colaboradorTableModel);
        tabelaColaboradores.setRowSorter(sorter);

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

        txtId.setText(String.valueOf(colaborador.getId()));
        txtNome.setText(colaborador.getNome());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtDtNascimento.setText(sdf.format(colaborador.getDataDeNascimento()));
        txtCpf.setText(colaborador.getCpf());
        cmbSexo.setSelectedItem(colaborador.getSexo());

        txtCep.setText(colaborador.getEndereco().getCep());
        txtLogradouro.setText(colaborador.getEndereco().getLogradouro());
        txtNumero.setText(colaborador.getEndereco().getNumero());
        txtComplemento.setText(colaborador.getEndereco().getComplemento());
        txtBairro.setText(colaborador.getEndereco().getBairro());
        txtCidade.setText(colaborador.getEndereco().getCidade().getNome());

        txtTelefoneFixo.setText(colaborador.getContato().getTelefoneFixo());
        txtTelefoneComercial.setText(colaborador.getContato().getTelefoneComercial());
        txtCelular.setText(colaborador.getContato().getCelular());
        txtEmail.setText(colaborador.getContato().getEmail());

        txtLogin.setText(colaborador.getUsuario().getLogin());
        txtSenha.setText(colaborador.getUsuario().getSenha());
        cmbPermissao.setSelectedItem(colaborador.getUsuario().getPermissao());

    }

    @Override
    public void capturarDados() {

        if (txtId.getText().length() > 0) {
            colaborador.setId(Long.valueOf(txtId.getText()));
        }

        colaborador.setNome(txtNome.getText().toUpperCase());
        colaborador.setCpf(txtCpf.getText().toUpperCase());
        colaborador.setSexo((SexoType) cmbSexo.getSelectedItem());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            colaborador.setDataDeNascimento(sdf.parse(txtDtNascimento.getText()));
        } catch (ParseException ex) {
            Logger.getLogger(FrmColaborador.class.getName()).log(Level.SEVERE, null, ex);
        }

        colaborador.getEndereco().setCep(txtCep.getText());
        colaborador.getEndereco().setLogradouro(txtLogradouro.getText().toUpperCase());
        colaborador.getEndereco().setNumero(txtNumero.getText());
        colaborador.getEndereco().setComplemento(txtComplemento.getText().toUpperCase());
        colaborador.getEndereco().getCidade().setNome(txtCidade.getText().toUpperCase());

        colaborador.getContato().setTelefoneFixo(txtTelefoneFixo.getText());
        colaborador.getContato().setTelefoneComercial(txtTelefoneComercial.getText());
        colaborador.getContato().setCelular(txtCelular.getText());
        colaborador.getContato().setEmail(txtEmail.getText());

        colaborador.getUsuario().setLogin(txtLogin.getText());
        colaborador.getUsuario().setSenha(EncryptMD5Util.getEncryptMD5(txtSenha.getText()));
        colaborador.getUsuario().setPermissao((Permissao) cmbPermissao.getSelectedItem());

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
        txtNome = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtCpf = new javax.swing.JFormattedTextField();
        jLabel22 = new javax.swing.JLabel();
        txtDtNascimento = new javax.swing.JFormattedTextField();
        cmbSexo = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        txtCep = new javax.swing.JFormattedTextField();
        jLabel24 = new javax.swing.JLabel();
        txtLogradouro = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtNumero = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtComplemento = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txtBairro = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        txtCidade = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        txtTelefoneFixo = new javax.swing.JFormattedTextField();
        jLabel31 = new javax.swing.JLabel();
        txtTelefoneComercial = new javax.swing.JFormattedTextField();
        jLabel32 = new javax.swing.JLabel();
        txtCelular = new javax.swing.JFormattedTextField();
        jLabel33 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        txtLogin = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        txtSenha = new javax.swing.JPasswordField();
        jLabel36 = new javax.swing.JLabel();
        cmbPermissao = new javax.swing.JComboBox<>();
        jPanel12 = new javax.swing.JPanel();
        txtLocaliza = new javax.swing.JTextField();
        pnScrollCategoria = new javax.swing.JScrollPane();
        tabelaColaboradores = new javax.swing.JTable();

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
        jLabel20.setText("Nome*:");

        txtNome.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel21.setText("CPF*:");

        try {
            txtCpf.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCpf.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtCpf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCpfActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel22.setText("Dt. Nasc*:");

        try {
            txtDtNascimento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDtNascimento.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        cmbSexo.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel23.setText("Sexo*:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel22)
                    .addComponent(jLabel20)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel21))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(txtDtNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel23)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbSexo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(txtCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtDtNascimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel23)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        try {
            txtCep.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCep.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtCep.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCepFocusLost(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel24.setText("Cep*:");

        txtLogradouro.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtLogradouro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLogradouroActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel25.setText("Logradouro*:");

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel26.setText("Número:");

        txtNumero.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel27.setText("Complemento:");

        txtComplemento.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel28.setText("Bairro*:");

        txtBairro.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel29.setText("Cidade*:");

        txtCidade.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel30.setText("Telefone Fixo:");

        try {
            txtTelefoneFixo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtTelefoneFixo.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel31.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel31.setText("Telefone Comercial:");

        try {
            txtTelefoneComercial.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtTelefoneComercial.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel32.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel32.setText("Celular*:");

        try {
            txtCelular.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)#####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCelular.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel33.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel33.setText("Email:");

        txtEmail.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel34.setText("Login*:");

        txtLogin.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel35.setText("Senha:");

        txtSenha.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel36.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel36.setText("Permissão*:");

        cmbPermissao.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

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

        tabelaColaboradores.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tabelaColaboradores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaColaboradoresMouseClicked(evt);
            }
        });
        pnScrollCategoria.setViewportView(tabelaColaboradores);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(txtLocaliza, javax.swing.GroupLayout.PREFERRED_SIZE, 541, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(pnScrollCategoria, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(txtLocaliza, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnScrollCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(jLabel34)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addComponent(jLabel36)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmbPermissao, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
            .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(txtLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35)
                    .addComponent(txtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36)
                    .addComponent(cmbPermissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 301, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel33)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(txtTelefoneFixo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTelefoneComercial, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelefoneFixo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31)
                    .addComponent(txtTelefoneComercial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32)
                    .addComponent(txtCelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel27)
                    .addComponent(jLabel24)
                    .addComponent(jLabel25))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(txtComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(txtLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(txtCep, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
            .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(txtCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26)
                    .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(txtComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(txtBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29)
                    .addComponent(txtCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlBotoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

            colaboradores = GuiUtil.inicializarDadosNasTabelas(new Colaborador());
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

        colaborador = new Colaborador();
        colaborador.setId(Long.valueOf(txtId.getText()));

        resultado = new Resultado();
        resultado = fachada.delete(colaborador);

        if (resultado.getEntidades().isEmpty()) {
            JOptionPane.showMessageDialog(null, resultado.getMsg(), "Erro", JOptionPane.ERROR_MESSAGE);
        } else {
            limparComponentes();

            colaboradores = GuiUtil.inicializarDadosNasTabelas(new Colaborador());
            preencherTabelas();

            btnEditar.setEnabled(false);
            btnExcluir.setEnabled(false);
            btnNovo.setEnabled(true);

            ativarParaCadastro(false);
        }

    }//GEN-LAST:event_btnExcluirActionPerformed

    public void ativarParaCadastro(Boolean bool) {

        txtNome.setEnabled(bool);
        txtDtNascimento.setEnabled(bool);
        txtCpf.setEnabled(bool);

        cmbSexo.setEnabled(bool);
        cmbPermissao.setEnabled(bool);
        
        txtCep.setEnabled(bool);
        txtLogradouro.setEnabled(bool);
        txtNumero.setEnabled(bool);
        txtComplemento.setEnabled(bool);
        txtBairro.setEnabled(bool);
        txtCidade.setEnabled(bool);

        txtTelefoneFixo.setEnabled(bool);
        txtTelefoneComercial.setEnabled(bool);
        txtCelular.setEnabled(bool);
        txtEmail.setEnabled(bool);

        txtLogin.setEnabled(bool);
        txtSenha.setEnabled(bool);

    }

    @Override
    public void limparComponentes() {

        txtId.setText("");
        txtNome.setText("");
        txtDtNascimento.setText("");
        txtCpf.setText("");

        txtCep.setText("");
        txtLogradouro.setText("");
        txtNumero.setText("");
        txtComplemento.setText("");
        txtBairro.setText("");
        txtCidade.setText("");

        txtTelefoneFixo.setText("");
        txtTelefoneComercial.setText("");
        txtCelular.setText("");
        txtEmail.setText("");

        txtLogin.setText("");
        txtSenha.setText("");

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

    private void txtNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeActionPerformed

    private void txtCpfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCpfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCpfActionPerformed

    private void txtCepFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCepFocusLost
        // TODO add your handling code here:
        Endereco endereco = new Endereco();

        endereco = CepUtil.buscarCep(txtCep.getText());
        if (endereco == null) {
            JOptionPane.showMessageDialog(null, "Cep Invalido", "Cep Errado", JOptionPane.ERROR_MESSAGE);
        } else {
            txtLogradouro.setText(endereco.getLogradouro());
            txtBairro.setText(endereco.getBairro());
            txtCidade.setText(endereco.getCidade().getNome());

        }
        colaborador.setEndereco(endereco);
    }//GEN-LAST:event_txtCepFocusLost

    private void txtLogradouroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLogradouroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLogradouroActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtLocalizaInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtLocalizaInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLocalizaInputMethodTextChanged

    private void txtLocalizaCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtLocalizaCaretPositionChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLocalizaCaretPositionChanged

    private void tabelaColaboradoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaColaboradoresMouseClicked
        // TODO add your handling code here:
        colaborador = new Colaborador();
        colaborador.setId(Long.valueOf(String.valueOf(tabelaColaboradores.getValueAt(tabelaColaboradores.getSelectedRow(), 0))));

        colaborador = (Colaborador) repositoryDao.find(colaborador);
        preencherDadosNaView();

        this.btnExcluir.setEnabled(true);
        this.btnEditar.setIcon(IMG_EDITAR);
        this.btnEditar.setToolTipText("EDITAR");
        this.btnEditar.setEnabled(true);

        ativarParaCadastro(true);

    }//GEN-LAST:event_tabelaColaboradoresMouseClicked

    @Override
    public Resultado salvar() {

        if (validarCampos() > 0) {
            JOptionPane.showMessageDialog(null, "Há Campos Vazios", "Campos Vazios", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        capturarDados();

        return fachada.save(colaborador);
    }

    @Override
    public Resultado editar() {

        colaborador = new Colaborador();
        colaborador.setId(Long.valueOf(txtId.getText()));
        colaborador = (Colaborador) repositoryDao.find(colaborador);

        if (validarCampos() > 0) {
            JOptionPane.showMessageDialog(null, "Há Campos Vazios", "Campos Vazios", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        capturarDados();

        return fachada.update(colaborador);
        
    }

    @Override
    public int validarCampos() {

        List<JComponent> componentes = new ArrayList<JComponent>();
        componentes.add(txtNome);
        componentes.add(txtDtNascimento);
        componentes.add(txtCpf);
        componentes.add(txtCep);
        componentes.add(txtCidade);
        componentes.add(txtLogradouro);
        componentes.add(txtBairro);
        componentes.add(txtCelular);
        componentes.add(txtLogin);
        componentes.add(cmbPermissao);
        componentes.add(cmbSexo);

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
                FrmColaborador dialog = new FrmColaborador(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSair;
    private javax.swing.JComboBox<String> cmbPermissao;
    private javax.swing.JComboBox<String> cmbSexo;
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
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane pnScrollCategoria;
    private javax.swing.JPanel pnlBotoes;
    private javax.swing.JTable tabelaColaboradores;
    private javax.swing.JTextField txtBairro;
    private javax.swing.JFormattedTextField txtCelular;
    private javax.swing.JFormattedTextField txtCep;
    private javax.swing.JTextField txtCidade;
    private javax.swing.JTextField txtComplemento;
    private javax.swing.JFormattedTextField txtCpf;
    private javax.swing.JFormattedTextField txtDtNascimento;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtLocaliza;
    private javax.swing.JTextField txtLogin;
    private javax.swing.JTextField txtLogradouro;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JPasswordField txtSenha;
    private javax.swing.JFormattedTextField txtTelefoneComercial;
    private javax.swing.JFormattedTextField txtTelefoneFixo;
    // End of variables declaration//GEN-END:variables

}
