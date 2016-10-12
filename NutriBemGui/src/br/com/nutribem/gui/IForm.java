/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nutribem.gui;

import br.com.nutribem.fachada.Resultado;

/**
 *
 * @author PauLinHo
 */
public interface IForm {
    /**
     * Recebe um Boolean, e seta o Atribuo setEnabled dos componentes da VIew
     * @param bool 
     */
    void ativarParaCadastro(Boolean bool);
    void inicializarCampoPesquisa();
    void preencherTabelas();
    void ativarComponentes();
    void desativarComponentes();
    void preencherDadosNaView();
    void capturarDados();
    void limparComponentes();
    int validarCampos();
    
    Resultado salvar();
    Resultado editar();
    
}
