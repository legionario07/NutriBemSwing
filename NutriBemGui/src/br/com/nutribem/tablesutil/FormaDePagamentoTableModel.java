/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nutribem.tablesutil;

import br.com.nutribem.dominio.FormaDePagamento;
import br.com.nutribem.dominio.EntidadeDominio;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PauLinHo
 */
public class FormaDePagamentoTableModel extends TabelaModelo {
    
    private List<EntidadeDominio> dados;

    private String[] colunas = new String[]{"ID", "Forma De Pagamento"};

    public FormaDePagamentoTableModel(List<EntidadeDominio> lista) {
        dados = new ArrayList<EntidadeDominio>();
        dados = lista;
    }

    public void addRow(EntidadeDominio e) {
        dados.add(e);
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
        FormaDePagamento cat = (FormaDePagamento) lista.get(linha);
        switch (coluna) {
            case 0:
                return cat.getId();
            case 1:
                return cat.getForma();
        }
        return null;
    }
}
