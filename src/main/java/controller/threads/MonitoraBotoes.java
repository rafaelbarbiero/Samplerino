/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.threads;

import controller.statics.ValidacaoBotoes;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import view.forms.FramePrincipal;

/**
 *
 * @author Rafael
 */
public class MonitoraBotoes extends Thread {

    private JMenuItem btnAtualizar, btnNovaMusica, btnAdicionarSampler;
    private JButton btnPlay, btnStop, btnSubir, btnDescer, btnLoop, btnExecutar;
    private ControleSerial serialReading;
    private JPanel panelListaDeSamplers, painelListaDeMusicas;

    JComboBox jComboBox;

    public MonitoraBotoes() {
        this.setPriority(Thread.MIN_PRIORITY);
    }

    @Override
    public void run() {

        while (true) {
            btnAdicionarSampler.setEnabled(ValidacaoBotoes.validateBtnAdicionarSampler(serialReading, painelListaDeMusicas));
            btnPlay.setEnabled(ValidacaoBotoes.validateBtnPlay(serialReading));
            btnStop.setEnabled(ValidacaoBotoes.validateBtnStop(serialReading));
            btnSubir.setEnabled(ValidacaoBotoes.validateBtnSubirDescer(serialReading));
            btnDescer.setEnabled(ValidacaoBotoes.validateBtnSubirDescer(serialReading));
            btnAtualizar.setEnabled(ValidacaoBotoes.validateBtnAtualizar(serialReading));
            btnNovaMusica.setEnabled(ValidacaoBotoes.validateBtnNovaMusica(serialReading));
            btnLoop.setEnabled(ValidacaoBotoes.validateBtnPlay(serialReading));
            btnExecutar.setEnabled(ValidacaoBotoes.validateBtnPlay(serialReading));

            btnPlay.updateUI();
            btnStop.updateUI();
            btnSubir.updateUI();
            btnDescer.updateUI();
            btnAtualizar.updateUI();
            btnNovaMusica.updateUI();
            btnLoop.updateUI();
            btnExecutar.updateUI();

            try {
                MonitoraBotoes.sleep(1000);
            } catch (InterruptedException e) {
                FramePrincipal.insereErroTerminal(e);
            }
        }
    }

    public void setBtnLoop(JButton btnLoop) {
        this.btnLoop = btnLoop;
    }

    public void setBtnExecutar(JButton btnExecutar) {
        this.btnExecutar = btnExecutar;
    }

    public void setPanelListaDeSamplers(JPanel panelListaDeSamplers) {
        this.panelListaDeSamplers = panelListaDeSamplers;
    }

    public void setPainelListaDeMusicas(JPanel painelListaDeMusicas) {
        this.painelListaDeMusicas = painelListaDeMusicas;
    }

    public void setBtnAdicionarSampler(JMenuItem btnAdicionarSampler) {
        this.btnAdicionarSampler = btnAdicionarSampler;
    }

    public void setBtnPlay(JButton btnConectar) {
        this.btnPlay = btnConectar;
    }

    public void setBtnStop(JButton btnStop) {
        this.btnStop = btnStop;
    }

    public void setBtnSubir(JButton btnSubir) {
        this.btnSubir = btnSubir;
    }

    public void setBtnDescer(JButton btnDescer) {
        this.btnDescer = btnDescer;
    }

    public void setBtnAtualizar(JMenuItem btnAtualizar) {
        this.btnAtualizar = btnAtualizar;
    }

    public void setBtnNovaMusica(JMenuItem btnNovaMusica) {
        this.btnNovaMusica = btnNovaMusica;
    }

    public void setSerialReading(ControleSerial serialReading) {
        this.serialReading = serialReading;
    }

    public void setPainelListaDeSamplers(JPanel painelListaDeSamplers) {
        this.panelListaDeSamplers = painelListaDeSamplers;
    }

    public void setjComboBox(JComboBox<String> jComboBox) {
        this.jComboBox = jComboBox;
    }
}
