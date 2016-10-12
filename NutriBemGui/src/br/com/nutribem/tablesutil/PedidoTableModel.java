/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nutribem.tablesutil;

import br.com.nutribem.dominio.EntidadeDominio;
import br.com.nutribem.dominio.ItemPedido;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PauLinHo
 */
public class PedidoTableModel extends TabelaModelo {

    private List<EntidadeDominio> dados;

    private String[] colunas = new String[]{"Qtde", "Cod. Barras", "Produto", "Desconto", "Valor", "Valor Total"};

    /**
     *
     * @param lista
     */
    public PedidoTableModel(List<EntidadeDominio> lista) {
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
    
    public void removeRow(int linha){
        getLinhas().remove(linha);
        fireTableRowsDeleted(linha, linha);
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
        ItemPedido objeto = (ItemPedido) lista.get(linha);
        switch (coluna) {
            case 0:
                return objeto.getQuantidade();
            case 1:
                return objeto.getProduto().getCodigoBarras();
            case 2:
                return objeto.getProduto().getDescricao();
            case 3:
                return objeto.getDesconto();
            case 4:
                return objeto.getProduto().getPreco();
            case 5:
                return objeto.getValor();
        }
        return null;
    }
}
