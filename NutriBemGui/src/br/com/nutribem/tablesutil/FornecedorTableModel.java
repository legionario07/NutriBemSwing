/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nutribem.tablesutil;

import br.com.nutribem.dominio.Fornecedor;
import br.com.nutribem.dominio.EntidadeDominio;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PauLinHo
 */
public class FornecedorTableModel extends TabelaModelo {

    private List<EntidadeDominio> dados;

    private String[] colunas = new String[]{"ID", "Nome", "Dt. Nasc.", "CPF", "Telefone Celular", "Telefone Fixo"};

    /**
     *
     * @param lista
     */
    public FornecedorTableModel(List<EntidadeDominio> lista) {
        dados = new ArrayList<EntidadeDominio>();
        dados = lista;
    }

    public void addRow(EntidadeDominio e) {
        getLinhas().add(e);
        this.fireTableDataChanged();
    }

    public String getColumnName(int num) {
        return this.colunas[num];
    }

    /**
     * @return the lista
     */
    public List<EntidadeDominio> getLinhas() {
        return dados;
    }

    /**
     * @param dados the lista to set
     */
    public void setDados(List<EntidadeDominio> dados) {
        this.dados = dados;
    }

    /**
     * @return the colunas
     */
    public String[] getColunas() {
        return colunas;
    }

    /**
     * @param colunas the colunas to set
     */
    public void setColunas(String[] colunas) {
        this.colunas = colunas;
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        List<EntidadeDominio> lista = getLinhas();
        Fornecedor objeto = (Fornecedor) lista.get(linha);
        switch (coluna) {
            case 0:
                return objeto.getId();
            case 1:
                return objeto.getNome();
            case 2:
                return objeto.getContato().getCelular();
            case 3:
                return objeto.getContato().getTelefoneComercial();

        }
        return null;
    }
}
