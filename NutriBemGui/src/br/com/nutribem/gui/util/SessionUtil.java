/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nutribem.gui.util;

import br.com.nutribem.dominio.CaixaFuncionario;
import br.com.nutribem.dominio.CaixaLoja;
import br.com.nutribem.dominio.Colaborador;
import br.com.nutribem.dominio.Pedido;

/**
 *
 * @author PauLinHo
 */
public class SessionUtil {
    
    private static SessionUtil instance = null;
    
    private Colaborador colaborador;
    private Pedido pedido;
    private CaixaLoja caixaLoja;
    private CaixaFuncionario caixaFuncionario;
    private boolean flagLimparVenda;

    
    private SessionUtil(){
        colaborador = new Colaborador();
        pedido = new Pedido();
        caixaLoja= new CaixaLoja();
        caixaFuncionario = new CaixaFuncionario();
        flagLimparVenda = false;
    }
    
    public static synchronized SessionUtil getInstance() {
		if (instance == null)
			instance = new SessionUtil();

		return instance;
	}
    
    /**
     * @param instance the instance to set
     */
    public void setInstance(SessionUtil instance) {
        SessionUtil.instance = instance;
    }

    /**
     * @return the colaborador
     */
    public Colaborador getColaborador() {
        return colaborador;
    }

    /**
     * @param colaborador the colaborador to set
     */
    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    /**
     * @return the pedido
     */
    public Pedido getPedido() {
        return pedido;
    }

    /**
     * @param pedido the pedido to set
     */
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    /**
     * @return the caixaLoja
     */
    public CaixaLoja getCaixaLoja() {
        return caixaLoja;
    }

    /**
     * @param caixaLoja the caixaLoja to set
     */
    public void setCaixaLoja(CaixaLoja caixaLoja) {
        this.caixaLoja = caixaLoja;
    }

    /**
     * @return the caixaFuncionario
     */
    public CaixaFuncionario getCaixaFuncionario() {
        return caixaFuncionario;
    }

    /**
     * @param caixaFuncionario the caixaFuncionario to set
     */
    public void setCaixaFuncionario(CaixaFuncionario caixaFuncionario) {
        this.caixaFuncionario = caixaFuncionario;
    }

    /**
     * @return the flagLimparVenda
     */
    public boolean isFlagLimparVenda() {
        return flagLimparVenda;
    }

    /**
     * @param flagLimparVenda the flagLimparVenda to set
     */
    public void setFlagLimparVenda(boolean flagLimparVenda) {
        this.flagLimparVenda = flagLimparVenda;
    }
    
}
