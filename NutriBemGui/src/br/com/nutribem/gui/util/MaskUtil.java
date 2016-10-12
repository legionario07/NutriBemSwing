/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nutribem.gui.util;

import br.com.nutribem.gui.FrmLoja;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author PauLinHo
 */
public class MaskUtil {

    private static MaskFormatter mask = null;

    public static JFormattedTextField getMaskTelefone() {
        try {
            mask = new MaskFormatter("(##)####-####");

        } catch (ParseException ex) {
            Logger.getLogger(FrmLoja.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new javax.swing.JFormattedTextField(mask);
    }

    public static JFormattedTextField getMaskCelular() {
        try {
            mask = new MaskFormatter("(##)#####-####");

        } catch (ParseException ex) {
            Logger.getLogger(FrmLoja.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new javax.swing.JFormattedTextField(mask);
    }

    public static JFormattedTextField getMaskCPF() {
        try {
            mask = new MaskFormatter("###.###.###-##");

        } catch (ParseException ex) {
            Logger.getLogger(FrmLoja.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new javax.swing.JFormattedTextField(mask);
    }

    public static JFormattedTextField getMaskCNPJ() {

        try {
            mask = new MaskFormatter("##.###.###/####-##");

        } catch (ParseException ex) {
            Logger.getLogger(FrmLoja.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new javax.swing.JFormattedTextField(mask);
    }

    public static JFormattedTextField getMaskDataNascimento() {

        try {
            mask = new MaskFormatter("##/##/####");

        } catch (ParseException ex) {
            Logger.getLogger(FrmLoja.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new javax.swing.JFormattedTextField(mask);
    }

    public static JFormattedTextField getMaskCEP() {

        try {
            mask = new MaskFormatter("#####-###");

        } catch (ParseException ex) {
            Logger.getLogger(FrmLoja.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new javax.swing.JFormattedTextField(mask);
    }

}
