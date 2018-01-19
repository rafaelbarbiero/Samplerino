/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.threads;

import java.awt.Button;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JPanel;
import model.Musica;
import view.forms.FramePrincipal;

/**
 *
 * @author rafael
 */
public class RealcePaineis extends Thread {

    protected JPanel painelDeSamplers, painelDeMusicas;
    protected Button botaoMusica;
    protected Musica musica;
    protected File sampler;

    public RealcePaineis(JPanel painelDeSamplers, JPanel painelDeMusicas, Button botaoMusica, Musica musica, File sampler) {
        this.painelDeMusicas = painelDeMusicas;
        this.painelDeSamplers = painelDeSamplers;
        this.botaoMusica = botaoMusica;
        this.musica = musica;
        this.sampler = sampler;
    }

    @Override
    public void run() {
        if (botaoMusica.getLabel().equals(musica.getNomeMusica())) {
            botaoMusica.setBackground(Color.RED);
            botaoMusica.setForeground(Color.YELLOW);
            botaoMusica.setEnabled(true);
            ArrayList<File> listaDeSamplers = musica.getSamplers();
            painelDeSamplers.removeAll();
            painelDeSamplers.setLayout(new GridLayout(listaDeSamplers.size(), 1));
            for (File arquivoSampler : listaDeSamplers) {
                Button botaoSampler = new Button(arquivoSampler.getName());
                if (botaoSampler.getLabel().equals(sampler.getName())) {
                    botaoSampler.setFont(FramePrincipal.fonteBotoes);
                    botaoSampler.setBackground(Color.RED);
                    botaoSampler.setForeground(Color.YELLOW);
                    botaoSampler.setEnabled(true);
                } else {
                    botaoSampler.setFont(FramePrincipal.fonteBotoes);
                    botaoSampler.setBackground(new Button().getBackground());
                    botaoSampler.setEnabled(false);
                }
                painelDeSamplers.add(botaoSampler);
                painelDeSamplers.updateUI();
                painelDeMusicas.updateUI();
            }
        } else {
            botaoMusica.setBackground(new Button().getBackground());
            botaoMusica.setEnabled(false);
        }

    }

}
