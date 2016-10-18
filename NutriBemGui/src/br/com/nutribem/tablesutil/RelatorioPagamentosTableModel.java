/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nutribem.tablesutil;

import br.com.nutribem.dominio.EntidadeDominio;
import br.com.nutribem.dominio.Pagamento;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PauLinHo
 */
public class RelatorioPagamentosTableModel extends TabelaModelo {

    private List<EntidadeDominio> dados;

    private String[] colunas = new String[]{"ID", "Data Pagamento", "Valor Total","Valor Pago", "Forma de Pag.",  "Total de Itens"};

    /**
     *
     * @param lista
     */
    public RelatorioPagamentosTableModel(List<EntidadeDominio> lista) {
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
        Pagamento obj = (Pagamento) lista.get(linha);
        switch (coluna) {
            case 0:
                return obj.getId();
            case 1:
                return obj.getDataPagamento();
            case 2:
                return obj.getPedido().getValor();
            case 3:
                return obj.getValorPago();
            case 4:
                return obj.getFormaDePagamento().getForma();
            case 5:
                return obj.getPedido().getItens().size();
        }
        return null;
    }
}
