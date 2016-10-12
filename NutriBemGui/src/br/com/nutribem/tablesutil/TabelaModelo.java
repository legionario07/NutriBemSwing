/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nutribem.tablesutil;

import br.com.nutribem.dominio.EntidadeDominio;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public abstract class TabelaModelo extends AbstractTableModel {
    
    private List linhas;
    private String[] colunas;
    
    public TabelaModelo(List linhas){
         linhas = new ArrayList<EntidadeDominio>();
        setLinhas(linhas);
    }
    
    public TabelaModelo(){
        
    }
    
    public TabelaModelo(List linhas, String[] colunas){
        linhas = new ArrayList<EntidadeDominio>();
        setLinhas(linhas);
        setColunas(colunas);
    }


    @Override
    public int getRowCount() {
        return getLinhas().size();
    }

    @Override
    public int getColumnCount() {
        return getColunas().length;
    }
    
    @Override
    public String getColumnName(int numColuna){
        return getColunas()[numColuna];
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

    /**
     * @return the linhas
     */
    public List getLinhas() {
        return linhas;
    }

    /**
     * @param linhas the linhas to set
     */
    public void setLinhas(List linhas) {
        this.linhas = linhas;
    }

    
}
