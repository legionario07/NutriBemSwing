/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nutribem.tablesutil;

import br.com.nutribem.dominio.CaixaLoja;
import br.com.nutribem.dominio.EntidadeDominio;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PauLinHo
 */
public class RelatorioCaixasTableModel extends TabelaModelo {

    private List<EntidadeDominio> dados;

    private String[] colunas = new String[]{"ID", "Data Pagamento", "Valor", "Identificação", "Qtde Retiradas", "Qtde Pagamentos"};

    /**
     *
     * @param lista
     */
    public RelatorioCaixasTableModel(List<EntidadeDominio> lista) {
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
        CaixaLoja obj = (CaixaLoja) lista.get(linha);
        switch (coluna) {
            case 0:
                return obj.getId();
            case 1:
                return obj.getData();
            case 2:
                return obj.getValor();
            case 3:
                return obj.getIdentificacao();
            case 4:
                return obj.getRetiradas().size();
            case 5:
                return obj.getEntradas().size();

        }
        return null;
    }
}
