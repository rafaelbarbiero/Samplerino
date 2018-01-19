/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.statics;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.TooManyListenersException;
import model.ValorSerial;
import view.forms.FramePrincipal;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author rafael
 */
public final class ConexaoSerial implements SerialPortEventListener {

    SerialPort serialPort;
    ValorSerial valorSerial;

    public ConexaoSerial(ValorSerial valorSerial) {
        this.valorSerial = valorSerial;

    }

    public SerialPort conectarSerial(String portId) {
        try {

            serialPort = (SerialPort) CommPortIdentifier.getPortIdentifier(portId).open("Comm", 9600);
            serialPort.setSerialPortParams(9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
            return serialPort;
        } catch (NoSuchPortException | PortInUseException | UnsupportedCommOperationException | TooManyListenersException e) {
            FramePrincipal.insereErroTerminal(e);
            return null;
        }
    }

    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    @Override
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                valorSerial.setValor(serialPort.getInputStream().read());                
            } catch (Exception e) {
                FramePrincipal.insereErroTerminal(e);
            }
        }
    }

    public static void inicializaLeituraSerial(AudioInputStream din, AudioFormat decodedFormat, SourceDataLine line) throws LineUnavailableException, IOException {
        if (line != null) {
            line.open(decodedFormat);
            byte[] data = new byte[4096];
            line.start();

            int nBytesRead;
            while ((nBytesRead = din.read(data, 0, data.length)) != -1) {
                line.write(data, 0, nBytesRead);
            }

            line.drain();
            line.stop();
            line.close();
            din.close();
        }
    }
}
