/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nutribem.gui.util;

import java.util.regex.PatternSyntaxException;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author PauLinHo
 */
public abstract class TableUtil {
    
    /**
     * 
     * @param sorter
     * @param texto - Texto digitado pelo usuario na view
     */
    public static void filterTable(TableRowSorter<? extends TableModel> sorter, String texto) {
        
        if (texto.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            try {
                sorter.setRowFilter(
                        RowFilter.regexFilter("(?i)"+ texto));
            } catch (PatternSyntaxException pse) {
                JOptionPane.showMessageDialog(null,"Erro", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
