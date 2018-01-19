/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.statics;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import model.Musica;
import model.UserDefConfig;
import view.forms.FramePrincipal;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rafael
 */
public final class ArquivosConfig {

    File saveFile;

    public static final String CAMINHO_ARQUIVO_SAVE = "/SamplerinoFiles/ConfigFiles/";
    public static final String CAMINHO_PASTA_SAMPLERS = "/SamplerinoFiles/ConfigFiles/Samplers/";
    static File config;

    public static String getCaminhoPastaUsuario() {
        return new JFileChooser().getCurrentDirectory().toString();
    }

    public static void validacaoArquivosConfig() {
        try {
            if (!new File(getCaminhoPastaUsuario() + CAMINHO_ARQUIVO_SAVE).isDirectory()) {
                new File(getCaminhoPastaUsuario() + CAMINHO_ARQUIVO_SAVE).mkdirs();
            }
            if (!new File(getCaminhoPastaUsuario() + CAMINHO_PASTA_SAMPLERS).isDirectory()) {
                new File(getCaminhoPastaUsuario() + CAMINHO_PASTA_SAMPLERS).mkdirs();
            }
        } catch (Exception e) {
            FramePrincipal.insereErroTerminal(e);
        }
    }

    public static void escreverArquivo(final File arquivo, final String data){
        try {
            PrintWriter writer = new PrintWriter(arquivo);
            writer.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>");
            writer.println(data);
            writer.flush();
        } catch (FileNotFoundException e) {
            FramePrincipal.insereErroTerminal(e);
        }
    }

    public static void criaArquivoSave(List<Musica> arrayMusicas) {
        XStream xStream = new XStream(new DomDriver());
        xStream.alias("Musica", Musica.class);
        File saveFile = new File(getCaminhoPastaUsuario() + CAMINHO_ARQUIVO_SAVE + "save.xml");
        escreverArquivo(saveFile, xStream.toXML(arrayMusicas));
    }

    public static void criaArquivoUserDef(UserDefConfig userDefConfig) {
        XStream xStream = new XStream(new DomDriver());
        xStream.alias("UserDefConfig", UserDefConfig.class);
        File saveFile = new File(getCaminhoPastaUsuario() + CAMINHO_ARQUIVO_SAVE + "UserDefConfig.xml");
        escreverArquivo(saveFile, xStream.toXML(userDefConfig));
    }

    public static ArrayList<Musica> criaArrayMusicasDoXML() {
        try {
            FileReader reader = new FileReader(new JFileChooser().getCurrentDirectory() + CAMINHO_ARQUIVO_SAVE + "save.xml");
            XStream xStream = new XStream(new DomDriver());
            xStream.processAnnotations(Musica.class);
            return validaArquivoSaveLocalmente((ArrayList<Musica>) xStream.fromXML(reader));
        } catch (FileNotFoundException e) {
            FramePrincipal.insereErroTerminal(e);
            return new ArrayList<Musica>();
        }
    }

    public static UserDefConfig recuperaUserDef() {

        UserDefConfig userDefConfig = new UserDefConfig();
        try {
            File file = new File(new JFileChooser().getCurrentDirectory() + CAMINHO_ARQUIVO_SAVE + "UserDefConfig.xml");
            if (file.exists()){
                FileReader reader = new FileReader(file);
                XStream xStream = new XStream(new DomDriver());
                xStream.processAnnotations(UserDefConfig.class);
                userDefConfig = (UserDefConfig) xStream.fromXML(reader);
            }
        } catch (FileNotFoundException e) {
            FramePrincipal.insereErroTerminal(e);
        }
        return userDefConfig;
    }

    public static int criaPainelAdicaoSamplers(final String musica, final File[] samplers) {
        JOptionPane frameSamplersSelecionados = new JOptionPane();
        JPanel panel = new JPanel();
        for (int x = 0; x < samplers.length; x++) {
            JRadioButton radioButton = new JRadioButton();
            radioButton.setName(String.valueOf(x));
            radioButton.setEnabled(false);
            radioButton.setText(samplers[x].getName());
            panel.add(radioButton);
        }
        if (samplers.length < 5) {
            frameSamplersSelecionados.setSize(new Dimension(200, 200));
        } else {
            frameSamplersSelecionados.setSize(new Dimension((samplers.length * 35), samplers.length * 50));
        }
        frameSamplersSelecionados.setLayout(new GridLayout(samplers.length + 2, 1));
        return JOptionPane.showConfirmDialog(null, panel.getComponents(), "Confirmar Samplers", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    public static Musica criaArquivosEPastas(String nomeMusica, File[] samplers) {

        File path = new File(ArquivosConfig.getCaminhoPastaUsuario() + CAMINHO_PASTA_SAMPLERS + "/" + nomeMusica);
        path.mkdir();
        FileChannel origem;
        FileChannel destino;

        ArrayList<File> samplerFinal = new ArrayList<File>();
        for (File sampler : samplers) {
            try {
                String urlNomeArquivo = path + "/" + sampler.getName();
                origem = new FileInputStream(sampler).getChannel();
                destino = new FileOutputStream(urlNomeArquivo).getChannel();
                origem.transferTo(0, origem.size(), destino);
                origem.close();
                destino.close();
                File arquivo1 = new File(urlNomeArquivo);
                File arquivo2 = new File(path + "/" + nomeMusica + "_" + sampler.getName());
                arquivo1.renameTo(arquivo2);
                samplerFinal.add(arquivo2);
            } catch (IOException e) {
                FramePrincipal.insereErroTerminal(e);
                return null;
            }
        }
        Musica musica = new Musica();
        musica.setNomeMusica(nomeMusica);
        musica.getSamplers().addAll(samplerFinal);
        return musica;

    }

    public static File insereNovoSampler(String nomeMusica, File sampler) {

        File path = new File(ArquivosConfig.getCaminhoPastaUsuario() + CAMINHO_PASTA_SAMPLERS + "/" + nomeMusica);
        FileChannel origem;
        FileChannel destino;
        if (path.isDirectory()) {
            try {
                String urlNomeArquivo = path + "/" + sampler.getName();
                origem = new FileInputStream(sampler).getChannel();
                destino = new FileOutputStream(urlNomeArquivo).getChannel();
                origem.transferTo(0, origem.size(), destino);
                origem.close();
                destino.close();
                File arquivo1 = new File(urlNomeArquivo);
                File arquivo2 = null;
                if (new File(path + "/" + nomeMusica + "_" + sampler.getName()).exists()) {
                    for (int x = 1; x <= 20; x++) {
                        if (!new File(path + "/" + nomeMusica + "_" + x + sampler.getName()).exists()) {
                            arquivo2 = new File(path + "/" + nomeMusica + "_" + x + sampler.getName());
                            break;
                        }
                    }
                } else {
                    arquivo2 = new File(path + "/" + nomeMusica + "_" + sampler.getName());
                }
                arquivo1.renameTo(arquivo2);
                return arquivo2;
            } catch (IOException e) {
                FramePrincipal.insereErroTerminal(e);
                return null;
            }

        } else {
            FramePrincipal.insereErroTerminal(new Exception("O Diretório com a música " + nomeMusica + " não existe!"));
            return null;
        }
    }

    public static void removeMusicas(ArrayList<Musica> arrayMusica) {
        for (int x = 0; x < arrayMusica.size(); x++) {
            if (!new File(ArquivosConfig.getCaminhoPastaUsuario() + CAMINHO_PASTA_SAMPLERS + arrayMusica.get(x).getNomeMusica()).exists()) {
                arrayMusica.remove(x);
            }
        }
        arrayMusica.trimToSize();
    }

    public static void removeSamplers(ArrayList<Musica> arrayMusica) {
        for (int x = 0; x < arrayMusica.size(); x++) {
            for (int y = 0; y < arrayMusica.get(x).getSamplers().size(); y++) {
                if (!new File(arrayMusica.get(x).getSamplers().get(y).toString()).exists()) {
                    arrayMusica.get(x).getSamplers().remove(y);
                }
            }
        }
        arrayMusica.trimToSize();
        criaArquivoSave(arrayMusica);

    }

    public static ArrayList<Musica> validaArquivoSaveLocalmente(ArrayList<Musica> arrayMusica) {
        removeMusicas(arrayMusica);
        removeSamplers(arrayMusica);
        return arrayMusica;
    }
}
