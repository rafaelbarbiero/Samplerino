package controller.threads;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Button;
import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JPanel;
import model.Musica;
import model.ValorSerial;
import view.forms.FramePrincipal;

/**
 *
 * @author Rafael
 */
public class ControleSerial extends Thread {

    AudioExecDataLine execDataLine;
    ArrayList<AudioExecDataLine> arrayDataLine = new ArrayList<>();
    ArrayList<Musica> arrayMusicas;
    JPanel painelDeMusicas, painelDeSamplers;
    private final ValorSerial ValorSerial;

    public ControleSerial(ValorSerial valorSerial) {
        this.ValorSerial = valorSerial;

    }

    @Override
    public void run() {
        while (true) {
            String system = System.getProperties().getProperty("os.name");
            for (Musica musica : arrayMusicas) {
                for (File arquivoSampler : musica.getSamplers()) {
                    for (Component component : painelDeMusicas.getComponents()) {
                        Button button = (Button) component;
                        new RealcePaineis(painelDeSamplers, painelDeMusicas, button, musica, arquivoSampler).start();
                    }
                    if (system.contains("Windows")) {

                    } else {
                        if (ValorSerial.getValor() == 1) {
                            execDataLine = new AudioExecDataLine();
                            execDataLine.setSampler(arquivoSampler);
                            execDataLine.start();
                            arrayDataLine.add(execDataLine);
                        }
                    }

                }
            }
        }
    }

    public synchronized void close() {
        try {
            for (AudioExecDataLine dataLine : arrayDataLine) {
                dataLine.stop();
            }
        } catch (Exception e) {
            FramePrincipal.insereErroTerminal(e);
        }
    }

    public JPanel getPainelDeMusicas() {
        return painelDeMusicas;
    }

    public void setPainelDeMusicas(JPanel painelDeMusicas) {
        this.painelDeMusicas = painelDeMusicas;
    }

    public JPanel getPainelDeSamplers() {
        return painelDeSamplers;
    }

    public void setPainelDeSamplers(JPanel painelDeSamplers) {
        this.painelDeSamplers = painelDeSamplers;
    }

    public ArrayList<Musica> getArrayMusicas() {
        return arrayMusicas;
    }

    public void setArrayMusicas(ArrayList<Musica> arrayMusicas) {
        this.arrayMusicas = arrayMusicas;
    }
}
