/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.statics;

import controller.threads.ControleSerial;
import javax.swing.JPanel;

/**
 *
 * @author Rafael
 */
public final class ValidacaoBotoes {
        
    public static boolean validateBtnPlay(ControleSerial serialReading) {
        return checkStatusThread(serialReading);
    }

    public static boolean validateBtnAdicionarSampler(ControleSerial serialReading, JPanel painel) {
        return painel.getComponentCount() > 0
                && checkStatusThread(serialReading);

    }
    public static boolean validateBtnStop(ControleSerial serialReading) {
        return (serialReading.isAlive()
                && !checkStatusThread(serialReading));
    }

    public static boolean validateBtnAtualizar(ControleSerial serialReading) {
        return (checkStatusThread(serialReading));
    }

    public static boolean validateBtnNovaMusica(ControleSerial serialReading) {
        return checkStatusThread(serialReading);
    }

    public static boolean validateBtnSubirDescer(ControleSerial serialReading) {
        return checkStatusThread(serialReading);
    }

    public static boolean checkStatusThread(ControleSerial serialReading) {
        return ((serialReading.getState().toString().equals("NEW")) || (serialReading.getState().toString().equals("TERMINATED")));
    }
}
