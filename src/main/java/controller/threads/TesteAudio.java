/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.threads;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import controller.statics.ConexaoSerial;
import view.forms.FramePrincipal;

/**
 *
 * @author Rafael
 */
public final class TesteAudio extends Thread {

    @Override
    public void run() {

        AudioInputStream din = null;
        for (int x = 0; x < 5; x++) {
            try {
                AudioInputStream in = AudioSystem.getAudioInputStream(new File(getClass().getResource("/audio/TesteAudio.wav").getPath()));
                AudioFormat baseFormat = in.getFormat();
                AudioFormat decodedFormat = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
                        baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
                        false);
                din = AudioSystem.getAudioInputStream(decodedFormat, in);
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
                SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
                ConexaoSerial.inicializaLeituraSerial(din, decodedFormat, line);

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                FramePrincipal.insereErroTerminal(e);
            } finally {
                if (din != null) {
                    try {
                        din.close();
                    } catch (IOException e) {
                        FramePrincipal.insereErroTerminal(e);
                    }
                }
            }
        }
    }
}
