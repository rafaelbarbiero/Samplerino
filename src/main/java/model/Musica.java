/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author rafael
 */
@XStreamAlias("Musica")
public class Musica {

    @XStreamAlias("nomeMusica")
    String nomeMusica;
    
    @XStreamAlias("samplers")
    ArrayList<File> samplers = new ArrayList<>();

    public ArrayList<File> getSamplers() {
        return samplers;
    }

    public String getNomeMusica() {
        return nomeMusica;
    }

    public void setNomeMusica(String nomeMusica) {
        this.nomeMusica = nomeMusica;
    }
}
