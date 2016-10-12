/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nutribem.tablesutil;

import br.com.nutribem.dominio.Produto;
import br.com.nutribem.dominio.EntidadeDominio;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PauLinHo
 */
public class ProdutoTableModel extends TabelaModelo {

    private List<EntidadeDominio> dados;

    private String[] colunas = new String[]{"ID", "Cód. Barras", "Descrição", "Dt. Val.", "Preço", "Qtde Est.", "Unidade"};

    /**
     *
     * @param lista
     */
    public ProdutoTableModel(List<EntidadeDominio> lista) {
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
        Produto objeto = (Produto) lista.get(linha);
        switch (coluna) {
            case 0:
                return objeto.getId();
            case 1:
                return objeto.getCodigoBarras();
            case 2:
                return objeto.getDescricao();
            case 3:
                return objeto.getDataDeValidade();
            case 4:
                return objeto.getPreco();
            case 5:
                return objeto.getQuantidadeEstoque();
            case 6:
                return objeto.getUnidadeDeMedida().getUnidadeDeMedida();

        }
        return null;
    }
}
