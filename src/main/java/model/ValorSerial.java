/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import view.forms.FramePrincipal;

/**
 *
 * @author rafael
 */
public class ValorSerial {

    protected int valor = -1;
    private boolean available;

    public synchronized int getValor() {
        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
                FramePrincipal.insereErroTerminal(e);
            }
        }
        available = false;
        notifyAll();
        return valor;
    }

    public synchronized void setValor(int valor) {
        while (available) {
            try {
                wait();
            } catch (InterruptedException e) {
                FramePrincipal.insereErroTerminal(e);
            }
        }
        available = true;
        this.valor = valor;
        notifyAll();
    }
}
