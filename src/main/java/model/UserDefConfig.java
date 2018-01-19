/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *
 * @author rafael
 */
@XStreamAlias("UserDefConfig")
public class UserDefConfig {

    @XStreamAlias("autoMode")
    boolean conexaoAutomatica = false;

    public boolean isConexaoAutomatica() {
        return conexaoAutomatica;
    }

    public void setConexaoAutomatica(boolean autoMode) {
        this.conexaoAutomatica = autoMode;
    }
}
