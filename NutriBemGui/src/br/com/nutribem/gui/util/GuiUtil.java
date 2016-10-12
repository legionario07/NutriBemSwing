/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nutribem.gui.util;

import br.com.nutribem.dao.RepositoryDao;
import br.com.nutribem.dominio.EntidadeDominio;
import br.com.nutribem.dominio.enums.SexoType;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

/**
 *
 * @author PauLinHo
 */
public class GuiUtil {

    private static RepositoryDao repositoryDao = null;

    public static JTable configurarTabelas(JTable jtable) {

        jtable.getTableHeader().setReorderingAllowed(false);
        jtable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return jtable;
    }

    /**
     *
     * @param entidade
     * @return Uma lista de Entidade Dominio ou null se não encontrar
     */
    public static List<EntidadeDominio> inicializarDadosNasTabelas(EntidadeDominio entidade) {

        repositoryDao = new RepositoryDao();

        return repositoryDao.findAll(entidade);

    }

    /**
     * Recebe um valor da view e converte para o formato moeda brasil para Armazenar em BigDecimal
     * @param formatado
     * @return 
     */
    public static String getMoedaFormatada(String formatado){
        
        
        String semFormatacao = formatado.replace("R$", "").replace(" ", "").replace(",",".");
        BigDecimal valor = new BigDecimal(semFormatacao);
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        formatado = nf.format(valor);
        
        return formatado;
        
    }
    
    
    /**
     * Recebe um valor em String da View  e retira a formatacao para armazenar em BigDecimal
     * @param formatado
     * @return 
     */
    public static String getMoedaSemFormatacao(String formatado){
        
        String semFormatacao = formatado.replace("R$", "").replace(" ", "").replace(".","").replace(",",".");
        
        return semFormatacao;
        
    }
    
    /**
     * recebe uma Lista com os componentes da view e verifica se tem dados
     * preenchidos ou não
     *
     * @param componentes
     * @return
     */
    public static int validarDadosNaView(List<JComponent> componentes) {

        int i = 0;

        for (JComponent j : componentes) {
            if (j instanceof JTextField) {
                if (((JTextField) j).getText().length() == 0) {
                    ++i;
                }
            }
            if (j instanceof JFormattedTextField) {
                String valor = ((JFormattedTextField) j).getText();
                int l = 1;
                for (int k = 0; k < valor.length(); k++) {
                    if (Character.isDigit(valor.charAt(k))) {
                        l = 0;
                        break;
                    }
                }

                if (l == 1) {
                    ++i;
                }

            } else if (j instanceof JComboBox) {
                int x = 1;
                if (((JComboBox) j).getSelectedItem().getClass().getSimpleName().equals("SexoType")) {
                    for (SexoType s : SexoType.values()) {
                        if (((JComboBox) j).getSelectedItem() == s) {
                            x = 0;
                        }
                    }
                } else {
                    if(((JComboBox) j).getSelectedItem()!=null){
                        x=0;
                    }
                }

                if (x == 1) {
                    ++i;
                }
            }
        }

        return i;
    }

}
