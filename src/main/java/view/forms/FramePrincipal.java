package view.forms;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import controller.statics.ArquivosConfig;
import controller.statics.ConexaoSerial;
import controller.threads.ControleSerial;
import controller.threads.MonitoraBotoes;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import model.ListaCores;
import model.Musica;
import model.UserDefConfig;
import model.ValorSerial;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;

import static controller.statics.ArquivosConfig.CAMINHO_PASTA_SAMPLERS;

/**
 *
 * @author rafael
 */
public final class FramePrincipal extends javax.swing.JFrame {

    /**
     * Creates new form FramePrincipal
     */
    public static String nomeBtnPressionado;
    String portaCom = "";

    public final ArrayList<Button> arrayBtnMusicas = new ArrayList<>();
    public final ArrayList<Button> arrayBtnSamplers = new ArrayList<>();
    public ArrayList<Musica> arrayMusicas;

    MonitoraBotoes threadMonitoraBotoes;

    SerialPort portaSerial;

    UserDefConfig userDefConfig;

    static FrameTerminal frameTerminal;

    public static Font fonteMenu = new java.awt.Font("Courier 10 Pitch", 0, 10);
    public static Font fonteBotoes = new java.awt.Font("Courier 10 Pitch", 1, 10);

    ValorSerial valorSerial = new ValorSerial();

    ControleSerial controleSerial;

    public FramePrincipal() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, IOException {

        initComponents();

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setIconImage(new ImageIcon(getClass().getResource("/icons/SamplerinoIcon.png")).getImage());
        configuraFontes();
        gerarArrayMusicasPainelMusicas();
        monitorBotoes();
        recuperarDefUsuario();
        setBounds(new Rectangle(300, 300));
        setLocationRelativeTo(null);
        iniciaTerminalErros();
        System.gc();
    }

    private void configuraFontes() {

        btnLoop.setFont(fonteMenu);
        btnExecutar.setFont(fonteMenu);
        btnPlay.setFont(fonteMenu);
        btnStop.setFont(fonteMenu);
        menuNovo.setFont(fonteMenu);
        menuConfig.setFont(fonteMenu);
        menuSobre.setFont(fonteMenu);
        itemAtualizar.setFont(fonteMenu);
        itemSamplerino.setFont(fonteMenu);
        itemMenuAutoMode.setFont(fonteMenu);
        itemMenuLogger.setFont(fonteMenu);
        itemMenuNovaMusica.setFont(fonteMenu);
        itemMenuNovoSampler.setFont(fonteMenu);
        itemMenuNovoSair.setFont(fonteMenu);

    }

    private void gerarArrayMusicasPainelMusicas() {
        ArquivosConfig.validacaoArquivosConfig();
        criaArrayMusicasDoXML();
        geraPainelDeSamplers();
        ArquivosConfig.criaArquivoSave(arrayMusicas);
        atualizaPainelMusicas();
    }

    public void recuperarDefUsuario() {
        userDefConfig = ArquivosConfig.recuperaUserDef();
        itemMenuAutoMode.setSelected(userDefConfig.isConexaoAutomatica());
        itemMenuAutoMode.updateUI();
    }

    private void monitorBotoes() throws IOException {
        threadMonitoraBotoes = new MonitoraBotoes();
        threadMonitoraBotoes.setBtnAdicionarSampler(itemMenuNovoSampler);
        threadMonitoraBotoes.setPainelListaDeMusicas(painelListaDeMusicas);
        threadMonitoraBotoes.setBtnPlay(btnPlay);
        threadMonitoraBotoes.setBtnAtualizar(itemAtualizar);
        threadMonitoraBotoes.setBtnDescer(btnDescer);
        threadMonitoraBotoes.setBtnSubir(btnSubir);
        threadMonitoraBotoes.setBtnStop(btnStop);
        threadMonitoraBotoes.setBtnNovaMusica(itemMenuNovaMusica);
        threadMonitoraBotoes.setPainelListaDeSamplers(painelListaDeSamplers);
        threadMonitoraBotoes.setBtnLoop(btnLoop);
        threadMonitoraBotoes.setBtnExecutar(btnExecutar);
        threadMonitoraBotoes.setSerialReading(new ControleSerial(null));
        threadMonitoraBotoes.setPriority(Thread.MIN_PRIORITY);
        threadMonitoraBotoes.start();

    }

    private void iniciaTerminalErros() {
        frameTerminal = new FrameTerminal();
        frameTerminal.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frameTerminal.setFont(fonteMenu);
        frameTerminal.getTxtLogger().setEditable(false);
        frameTerminal.setBounds(this.getX(), this.getY() + this.getHeight(), this.getWidth(), 150);
    }

    public static void insereErroTerminal(Exception e) {
        try {
            frameTerminal.getTxtLogger().append(new Date() + "\t" + e.getLocalizedMessage() + "\n");
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                frameTerminal.getTxtLogger().append(new Date() + "\t" + stackTraceElement + "\n");
            }
        } catch (Exception ex) {
            e.printStackTrace();
        }
    }

    private void criaArrayMusicasDoXML() {
        arrayMusicas = ArquivosConfig.criaArrayMusicasDoXML();
    }

    public void geraPainelDeSamplers() {

        if (!arrayMusicas.isEmpty()) {
            arrayBtnMusicas.clear();
            arrayBtnSamplers.clear();
            for (final Musica musica : arrayMusicas) {
                final Button botaoMusicas = new Button();
                botaoMusicas.setFont(fonteBotoes);
                botaoMusicas.setLabel(musica.getNomeMusica());
                botaoMusicas.setName(musica.getNomeMusica());
                botaoMusicas.setForeground(ListaCores.getColors().get((int) ((Math.random() * 10) + (Math.random() * 10))));
                botaoMusicas.addFocusListener(
                        new FocusListener() {

                            @Override
                            public void focusGained(FocusEvent e
                            ) {
                                botaoMusicas.setBackground(Color.GRAY);
                            }

                            @Override
                            public void focusLost(FocusEvent e
                            ) {
                                botaoMusicas.setBackground(new Button().getForeground());
                            }
                        }
                );
                botaoMusicas.addActionListener(
                        new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e
                            ) {
                                arrayBtnSamplers.clear();
                                btnLoop.setEnabled(true);
                                btnExecutar.setEnabled(true);
                                btnLoop.updateUI();
                                btnExecutar.updateUI();
                                nomeBtnPressionado = e.getActionCommand();

                                for (File sampler : musica.getSamplers()) {
                                    final Button botaoSamplers = new Button();
                                    botaoSamplers.setFont(botaoMusicas.getFont());
                                    botaoMusicas.setForeground(botaoMusicas.getForeground());
                                    botaoSamplers.setName(sampler.getName());
                                    botaoSamplers.setLabel(sampler.getName());
                                    botaoSamplers.addFocusListener(new FocusListener() {

                                        @Override
                                        public void focusGained(FocusEvent e) {
                                            botaoSamplers.setBackground(Color.GRAY);
                                        }

                                        @Override
                                        public void focusLost(FocusEvent e) {
                                            botaoSamplers.setBackground(new Button().getForeground());
                                        }
                                    });
                                    botaoSamplers.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            btnLoop.setEnabled(false);
                                            btnExecutar.setEnabled(false);
                                            btnLoop.updateUI();
                                            btnExecutar.updateUI();
                                            nomeBtnPressionado = e.getActionCommand();
                                        }
                                    });
                                    botaoSamplers.setForeground(botaoMusicas.getForeground());
                                    arrayBtnSamplers.add(botaoSamplers);
                                    atualizaPainelSamplers();
                                }
                            }
                        }
                );
                arrayBtnMusicas.add(botaoMusicas);
            }
        }
    }

    private void subirSamplers() {
        int tamanhoArrayMusicas = arrayMusicas.size();
        for (int y = 0; y < tamanhoArrayMusicas; y++) {
            int tamanhoArraySamplers = arrayMusicas.get(y).getSamplers().size();
            for (int z = 0; z < tamanhoArraySamplers; z++) {
                if ((arrayMusicas.get(y).getSamplers().get(z).getName().equals(nomeBtnPressionado)) && z > 0) {
                    Musica musica = arrayMusicas.get(y);
                    File file = arrayMusicas.get(y).getSamplers().get(z);
                    arrayMusicas.get(y).getSamplers().remove(z);
                    arrayMusicas.get(y).getSamplers().trimToSize();
                    arrayMusicas.get(y).getSamplers().add(z - 1, file);
                    Button botaoMusica = null;
                    for (Button jButton : arrayBtnMusicas) {
                        if (jButton.getLabel().equals(musica.getNomeMusica())) {
                            botaoMusica = jButton;
                        }
                    }
                    atualizaPainelSamplers(musica, botaoMusica);
                    break;
                }
            }
        }
        ArquivosConfig.criaArquivoSave(arrayMusicas);
    }

    private void descerSamplers() {
        int tamanhoArrayMusicas = arrayMusicas.size();
        for (int y = 0; y < tamanhoArrayMusicas; y++) {
            int tamanhoArraySamplers = arrayMusicas.get(y).getSamplers().size();
            for (int z = 0; z < tamanhoArraySamplers; z++) {
                if ((arrayMusicas.get(y).getSamplers().get(z).getName().equals(nomeBtnPressionado)) && z < tamanhoArraySamplers) {
                    File file = arrayMusicas.get(y).getSamplers().get(z);
                    Musica musica = arrayMusicas.get(y);
                    arrayMusicas.get(y).getSamplers().remove(z);
                    arrayMusicas.get(y).getSamplers().trimToSize();
                    if (z == arrayMusicas.get(y).getSamplers().size()) {
                        arrayMusicas.get(y).getSamplers().add(file);
                    } else {
                        arrayMusicas.get(y).getSamplers().add(z + 1, file);
                    }
                    Button botaoMusica = null;
                    for (Button jButton : arrayBtnMusicas) {
                        if (jButton.getLabel().equals(musica.getNomeMusica())) {
                            botaoMusica = jButton;
                        }
                    }
                    atualizaPainelSamplers(musica, botaoMusica);
                    break;
                }
            }
        }
        ArquivosConfig.criaArquivoSave(arrayMusicas);
    }

    private boolean subirMusicas() {

        for (int x = 0; x < arrayMusicas.size(); x++) {
            if (arrayMusicas.get(x).getNomeMusica().equals(nomeBtnPressionado) && x > 0) {
                Musica musica = arrayMusicas.get(x);
                arrayMusicas.remove(x);
                arrayMusicas.trimToSize();
                arrayMusicas.add(x - 1, musica);
                geraPainelDeSamplers();
                ArquivosConfig.criaArquivoSave(arrayMusicas);
                atualizaPainelMusicas();
                return true;
            }
        }
        return false;
    }

    private boolean descerMusicas() {
        for (int x = 0; x < arrayMusicas.size(); x++) {
            if (arrayMusicas.get(x).getNomeMusica().equals(nomeBtnPressionado) && x < arrayMusicas.size()) {
                Musica musica = arrayMusicas.get(x);
                arrayMusicas.remove(x);
                arrayMusicas.trimToSize();
                if (x == arrayMusicas.size()) {
                    arrayMusicas.add(musica);
                } else {
                    arrayMusicas.add(x + 1, musica);
                }
                geraPainelDeSamplers();
                ArquivosConfig.criaArquivoSave(arrayMusicas);
                atualizaPainelMusicas();
                return true;
            }
        }
        return false;
    }

    public void atualizaPainelMusicas() {
        painelListaDeMusicas.removeAll();
        painelListaDeMusicas.setLayout(new GridLayout(arrayBtnMusicas.size(), 1));
        for (Button arrayButtonsSampler : arrayBtnMusicas) {
            painelListaDeMusicas.add(arrayButtonsSampler);
        }
        painelListaDeMusicas.updateUI();
    }

    public void atualizaPainelSamplers() {
        painelListaDeSamplers.removeAll();
        painelListaDeSamplers.setLayout(new GridLayout(arrayBtnSamplers.size(), 1));
        for (Button arrayButtonsSampler : arrayBtnSamplers) {
            painelListaDeSamplers.add(arrayButtonsSampler);
        }
        painelListaDeSamplers.updateUI();
    }

    private void atualizaPainelSamplers(Musica musica, Button botaoMusica) {
        arrayBtnSamplers.clear();
        for (File sampler : musica.getSamplers()) {
            Button botaoSamplers = new Button();
            botaoSamplers.setFont(botaoMusica.getFont());
            botaoSamplers.setForeground(botaoMusica.getForeground());
            botaoSamplers.setName(sampler.getName());
            botaoSamplers.setLabel(sampler.getName());
            botaoSamplers.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    nomeBtnPressionado = e.getActionCommand();
                }
            });
            botaoSamplers.setForeground(botaoMusica.getForeground());
            arrayBtnSamplers.add(botaoSamplers);
        }
        painelListaDeSamplers.removeAll();
        painelListaDeSamplers.setLayout(new GridLayout(arrayBtnSamplers.size(), 1));
        for (Button arrayButtonsSampler : arrayBtnSamplers) {
            painelListaDeSamplers.add(arrayButtonsSampler);
        }
        painelListaDeSamplers.updateUI();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        painelPrincipal = new javax.swing.JPanel();
        painelControles = new javax.swing.JPanel();
        painelBtnPlay = new javax.swing.JPanel();
        btnPlay = new javax.swing.JButton();
        btnLoop = new javax.swing.JButton();
        painelBtnStop = new javax.swing.JPanel();
        btnExecutar = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();
        painelBtnMoveStatus = new javax.swing.JPanel();
        painelBtnMover = new javax.swing.JPanel();
        btnSubir = new javax.swing.JButton();
        btnDescer = new javax.swing.JButton();
        painelConteudoMusicasSamplers = new javax.swing.JPanel();
        painelListaDeMusicas = new javax.swing.JPanel();
        painelListaDeSamplers = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        menuNovo = new javax.swing.JMenu();
        itemMenuNovaMusica = new javax.swing.JMenuItem();
        itemMenuNovoSampler = new javax.swing.JMenuItem();
        itemMenuNovoSair = new javax.swing.JMenuItem();
        menuConfig = new javax.swing.JMenu();
        itemMenuLogger = new javax.swing.JRadioButtonMenuItem();
        itemAtualizar = new javax.swing.JMenuItem();
        itemMenuAutoMode = new javax.swing.JRadioButtonMenuItem();
        menuSobre = new javax.swing.JMenu();
        itemSamplerino = new javax.swing.JMenuItem();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Samplerino");
        setLocationByPlatform(true);
        setUndecorated(true);
        setType(java.awt.Window.Type.UTILITY);

        painelPrincipal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        painelPrincipal.setPreferredSize(new java.awt.Dimension(600, 600));

        painelControles.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        painelControles.setMinimumSize(new java.awt.Dimension(0, 0));
        painelControles.setLayout(new java.awt.GridLayout(1, 3));

        painelBtnPlay.setMinimumSize(new java.awt.Dimension(0, 0));
        painelBtnPlay.setLayout(new java.awt.GridLayout(1, 1));

        btnPlay.setFont(new java.awt.Font("FoughtKnight", 0, 18)); // NOI18N
        btnPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/play.png"))); // NOI18N
        btnPlay.setToolTipText("Conecta o programa ao Arduino através da porta Serial");
        btnPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayActionPerformed(evt);
            }
        });
        painelBtnPlay.add(btnPlay);

        btnLoop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/loop.png"))); // NOI18N
        btnLoop.setToolTipText("Executa essa música em Loop");
        btnLoop.setEnabled(false);
        btnLoop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoopActionPerformed(evt);
            }
        });
        painelBtnPlay.add(btnLoop);

        painelControles.add(painelBtnPlay);

        painelBtnStop.setMinimumSize(new java.awt.Dimension(0, 0));
        painelBtnStop.setLayout(new java.awt.GridLayout(1, 1));

        btnExecutar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/executar.png"))); // NOI18N
        btnExecutar.setToolTipText("Executa a partir daqui!");
        btnExecutar.setEnabled(false);
        btnExecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExecutarActionPerformed(evt);
            }
        });
        painelBtnStop.add(btnExecutar);

        btnStop.setFont(new java.awt.Font("FoughtKnight", 1, 18)); // NOI18N
        btnStop.setForeground(java.awt.Color.lightGray);
        btnStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/stop.png"))); // NOI18N
        btnStop.setToolTipText("Interrompe a execução dos Samplers e desconecta do Arduino");
        btnStop.setEnabled(false);
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed();
            }
        });
        painelBtnStop.add(btnStop);

        painelControles.add(painelBtnStop);

        painelBtnMoveStatus.setMinimumSize(new java.awt.Dimension(0, 0));
        painelBtnMoveStatus.setLayout(new java.awt.GridLayout(1, 1));

        painelBtnMover.setLayout(new java.awt.GridLayout(1, 2));

        btnSubir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/up.png"))); // NOI18N
        btnSubir.setToolTipText("Sobe a Musica ou o Sampler na lista");
        btnSubir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubirActionPerformed(evt);
            }
        });
        painelBtnMover.add(btnSubir);

        btnDescer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/down.png"))); // NOI18N
        btnDescer.setToolTipText("Desce a Musica ou o Sampler na lista");
        btnDescer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescerActionPerformed(evt);
            }
        });
        painelBtnMover.add(btnDescer);

        painelBtnMoveStatus.add(painelBtnMover);

        painelControles.add(painelBtnMoveStatus);

        painelConteudoMusicasSamplers.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        painelConteudoMusicasSamplers.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        painelConteudoMusicasSamplers.setMaximumSize(new java.awt.Dimension(86, 180));
        painelConteudoMusicasSamplers.setMinimumSize(new java.awt.Dimension(0, 0));
        painelConteudoMusicasSamplers.setLayout(new java.awt.GridLayout(1, 2));

        painelListaDeMusicas.setBorder(null);

        javax.swing.GroupLayout painelListaDeMusicasLayout = new javax.swing.GroupLayout(painelListaDeMusicas);
        painelListaDeMusicas.setLayout(painelListaDeMusicasLayout);
        painelListaDeMusicasLayout.setHorizontalGroup(
            painelListaDeMusicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );
        painelListaDeMusicasLayout.setVerticalGroup(
            painelListaDeMusicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 222, Short.MAX_VALUE)
        );

        painelConteudoMusicasSamplers.add(painelListaDeMusicas);

        painelListaDeSamplers.setBorder(null);

        javax.swing.GroupLayout painelListaDeSamplersLayout = new javax.swing.GroupLayout(painelListaDeSamplers);
        painelListaDeSamplers.setLayout(painelListaDeSamplersLayout);
        painelListaDeSamplersLayout.setHorizontalGroup(
            painelListaDeSamplersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );
        painelListaDeSamplersLayout.setVerticalGroup(
            painelListaDeSamplersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 222, Short.MAX_VALUE)
        );

        painelConteudoMusicasSamplers.add(painelListaDeSamplers);

        javax.swing.GroupLayout painelPrincipalLayout = new javax.swing.GroupLayout(painelPrincipal);
        painelPrincipal.setLayout(painelPrincipalLayout);
        painelPrincipalLayout.setHorizontalGroup(
            painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(painelConteudoMusicasSamplers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(painelControles, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        painelPrincipalLayout.setVerticalGroup(
            painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(painelControles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(painelConteudoMusicasSamplers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        menuNovo.setText("Novo");
        menuNovo.setFont(new java.awt.Font("Courier 10 Pitch", 0, 14)); // NOI18N

        itemMenuNovaMusica.setFont(new java.awt.Font("Courier 10 Pitch", 0, 12)); // NOI18N
        itemMenuNovaMusica.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Musica.png"))); // NOI18N
        itemMenuNovaMusica.setText("Musica");
        itemMenuNovaMusica.setToolTipText("Adicionar nova Música na coleção");
        itemMenuNovaMusica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuNovaMusicaActionPerformed(evt);
            }
        });
        menuNovo.add(itemMenuNovaMusica);

        itemMenuNovoSampler.setFont(new java.awt.Font("Courier 10 Pitch", 0, 12)); // NOI18N
        itemMenuNovoSampler.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Sampler.png"))); // NOI18N
        itemMenuNovoSampler.setText("+Sampler");
        itemMenuNovoSampler.setToolTipText("Adicionar novo Sampler em uma Musica ja existente");
        itemMenuNovoSampler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuNovoSamplerActionPerformed(evt);
            }
        });
        menuNovo.add(itemMenuNovoSampler);

        itemMenuNovoSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/exit.png"))); // NOI18N
        itemMenuNovoSair.setText("Sair");
        itemMenuNovoSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuNovoSairActionPerformed(evt);
            }
        });
        menuNovo.add(itemMenuNovoSair);

        menuBar.add(menuNovo);

        menuConfig.setText("Config");
        menuConfig.setFont(new java.awt.Font("Courier 10 Pitch", 0, 12)); // NOI18N

        itemMenuLogger.setText("Logger");
        itemMenuLogger.setToolTipText("Exibe o painel de erros");
        itemMenuLogger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuLoggerActionPerformed(evt);
            }
        });
        menuConfig.add(itemMenuLogger);

        itemAtualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Atualizar.png"))); // NOI18N
        itemAtualizar.setText("Atualizar");
        itemAtualizar.setToolTipText("Atualiza a lista de Músicas e Samplers após serem deletados durante a execução do programa");
        itemAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemAtualizarActionPerformed(evt);
            }
        });
        menuConfig.add(itemAtualizar);

        itemMenuAutoMode.setText("Conexão Automática");
        itemMenuAutoMode.setToolTipText("Ativa o modo de conexão automática, conectando diretamente ao Arduino sem perguntar o nome da porta serial!");
        itemMenuAutoMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemMenuAutoModeActionPerformed(evt);
            }
        });
        menuConfig.add(itemMenuAutoMode);

        menuBar.add(menuConfig);

        menuSobre.setText("Sobre");
        menuSobre.setToolTipText("Exibe informações do Software");
        menuSobre.setFont(new java.awt.Font("Courier 10 Pitch", 0, 12)); // NOI18N
        menuSobre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSobreActionPerformed(evt);
            }
        });

        itemSamplerino.setText("Samplerino");
        itemSamplerino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemSamplerinoActionPerformed(evt);
            }
        });
        menuSobre.add(itemSamplerino);

        menuBar.add(menuSobre);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(painelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(painelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnStopActionPerformed() {//GEN-FIRST:event_btnStopActionPerformed
        portaSerial.close();
        controleSerial.close();
        controleSerial.stop();
        gerarArrayMusicasPainelMusicas();
        painelListaDeSamplers.removeAll();
        painelListaDeSamplers.updateUI();
    }//GEN-LAST:event_btnStopActionPerformed

    private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayActionPerformed
        this.play(evt);
    }//GEN-LAST:event_btnPlayActionPerformed

    private void play(ActionEvent evt) {
        portaCom = "";
        try {
            Enumeration listaDePortas = CommPortIdentifier.getPortIdentifiers();
            ArrayList<String> arrayDePortas = new ArrayList();
            List portas = (List) CommPortIdentifier.getPortIdentifiers();
            while (listaDePortas.hasMoreElements()) {
                arrayDePortas.add(((CommPortIdentifier) listaDePortas.nextElement()).getName());
            }
            if (arrayDePortas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Parece não haver porta serial disponível ou conectada ao computador.", "Portas não disponíveis", JOptionPane.ERROR_MESSAGE);
            } else {
                if (userDefConfig.isConexaoAutomatica()) {
                    if (arrayDePortas.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Parece não haver porta serial disponível ou conectada ao computador.", "Portas não disponíveis", JOptionPane.ERROR_MESSAGE);
                    } else {
                        iniciarConexaoSerial(arrayDePortas.get(0));
                    }

                } else {
                    JPanel painelPortasCom = new JPanel();
                    ButtonGroup group = new ButtonGroup();
                    JRadioButton radioButton;
                    for (String porta : arrayDePortas) {
                        radioButton = new JRadioButton(porta);
                        radioButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                portaCom = e.getActionCommand();
                            }
                        });
                        painelPortasCom.add(radioButton);
                    }
                    if (JOptionPane.showConfirmDialog(this, painelPortasCom, "Selecione a porta para conectar", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                        if ("".equals(portaCom)) {
                            JOptionPane.showMessageDialog(this, "Selecione ao menos uma porta para conexão!", "Portas não selecionada", JOptionPane.ERROR_MESSAGE);
                            btnPlayActionPerformed(evt);
                        } else {
                            iniciarConexaoSerial(portaCom);
                        }
                    }
                }
            }
        } catch (UnsatisfiedLinkError | NoClassDefFoundError e) {
            FramePrincipal.insereErroTerminal(new Exception(e.getMessage()));
            e.printStackTrace();
        }
    }

    private void iniciarConexaoSerial(String porta) {
        portaSerial = new ConexaoSerial(valorSerial).conectarSerial(porta);
        controleSerial = new ControleSerial(valorSerial);
        controleSerial.setArrayMusicas(arrayMusicas);
        controleSerial.setPainelDeMusicas(painelListaDeMusicas);
        controleSerial.setPainelDeSamplers(painelListaDeSamplers);
        controleSerial.start();
        threadMonitoraBotoes.setSerialReading(controleSerial);
    }

    private void play() {
        portaCom = "";
        try {
            ArrayList<String> arrayDePortas = new ArrayList();
            Enumeration listaDePortas = CommPortIdentifier.getPortIdentifiers();
            while (listaDePortas.hasMoreElements()) {
                arrayDePortas.add(((CommPortIdentifier) listaDePortas.nextElement()).getName());

            }
            if (arrayDePortas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Parece não haver porta serial disponível ou conectada ao computador.", "Portas não disponíveis", JOptionPane.ERROR_MESSAGE);
            } else {
                if (userDefConfig.isConexaoAutomatica()) {
                    if (arrayDePortas.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Parece não haver porta serial disponível ou conectada ao computador.", "Portas não disponíveis", JOptionPane.ERROR_MESSAGE);
                    } else {
                        iniciarConexaoSerial(arrayDePortas.get(0));
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Para esta função, a opção de conexão automática deve estar marcada no menu Config --> Conexao Automática", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (HeadlessException e) {
            FramePrincipal.insereErroTerminal(e);
        }
    }

    private void itemMenuLoggerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuLoggerActionPerformed
        frameTerminal.setVisible(!frameTerminal.isShowing());
    }//GEN-LAST:event_itemMenuLoggerActionPerformed

    private void itemMenuNovaMusicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuNovaMusicaActionPerformed
        String nomeMusica = JOptionPane.showInputDialog(this, "Nova Música", "Qual é o nome da música?", JOptionPane.PLAIN_MESSAGE);
        if (!"".equals(nomeMusica)) {
            if (nomeMusica != null) {
                if (!new File(ArquivosConfig.getCaminhoPastaUsuario() + CAMINHO_PASTA_SAMPLERS + "/" + nomeMusica).isDirectory()) {
                    if (painelListaDeMusicas != null) {
                        JFileChooser listaDeSamplers = new JFileChooser();
                        listaDeSamplers.setDialogTitle("Selecione os arquivos de sampler");
                        listaDeSamplers.setMultiSelectionEnabled(true);
                        listaDeSamplers.setAcceptAllFileFilterUsed(true);
                        if (listaDeSamplers.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                            if (ArquivosConfig.criaPainelAdicaoSamplers(nomeMusica, listaDeSamplers.getSelectedFiles()) == JOptionPane.OK_OPTION) {
                                arrayMusicas.add(arrayMusicas.size(), ArquivosConfig.criaArquivosEPastas(nomeMusica, listaDeSamplers.getSelectedFiles()));
                                ArquivosConfig.criaArquivoSave(arrayMusicas);
                                criaArrayMusicasDoXML();
                                geraPainelDeSamplers();
                                atualizaPainelMusicas();
                                atualizaPainelSamplers();
                            }
                        }
                    }
                } else {
                    FramePrincipal.insereErroTerminal(new Exception("O Diretório com o nome da música já existe!"));
                    JOptionPane.showMessageDialog(null, "O Diretório com o nome da música já existe!", "Musica já existe", JOptionPane.ERROR_MESSAGE);
                    itemMenuNovaMusicaActionPerformed(evt);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "É obrigatório escolher o nome da música");
            itemMenuNovaMusicaActionPerformed(evt);
        }
    }//GEN-LAST:event_itemMenuNovaMusicaActionPerformed

    private void btnDescerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescerActionPerformed
        if (!descerMusicas()) {
            descerSamplers();
        }
    }//GEN-LAST:event_btnDescerActionPerformed

    private void btnSubirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubirActionPerformed
        if (!subirMusicas()) {
            subirSamplers();
        }
    }//GEN-LAST:event_btnSubirActionPerformed

    private void itemMenuNovoSamplerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuNovoSamplerActionPerformed
        nomeBtnPressionado = "";
        JRadioButton radioButton;
        ButtonGroup group = new ButtonGroup();
        JPanel jPanel = new JPanel();
        for (Component component : painelListaDeMusicas.getComponents()) {
            Button botaoMusicaInserirSampler = (Button) component;
            radioButton = new JRadioButton(botaoMusicaInserirSampler.getLabel());
            radioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    nomeBtnPressionado = e.getActionCommand();
                }
            });
            group.add(radioButton);
            jPanel.add(radioButton);
        }

        if (JOptionPane.showConfirmDialog(this, jPanel, "Escolha a música para adicionar o sampler!", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
            if (nomeBtnPressionado.equals("")) {
                JOptionPane.showMessageDialog(this, "Escolha em qual música o sampler será adicionado!", "Erro", JOptionPane.ERROR_MESSAGE);
                itemMenuNovoSamplerActionPerformed(evt);
            } else {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setApproveButtonText("OK");
                fileChooser.setMultiSelectionEnabled(false);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setDialogTitle("Selecione o sampler");
                if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    for (Musica musica : arrayMusicas) {
                        if (musica.getNomeMusica().equals(nomeBtnPressionado)) {
                            musica.getSamplers().add(musica.getSamplers().size(), ArquivosConfig.insereNovoSampler(nomeBtnPressionado, fileChooser.getSelectedFile()));
                        }
                    }
                }
                ArquivosConfig.criaArquivoSave(arrayMusicas);
                criaArrayMusicasDoXML();
                geraPainelDeSamplers();
                atualizaPainelMusicas();
                atualizaPainelSamplers();
            }

        }


    }//GEN-LAST:event_itemMenuNovoSamplerActionPerformed

    private void itemAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemAtualizarActionPerformed
        arrayMusicas = ArquivosConfig.criaArrayMusicasDoXML();
        ArquivosConfig.criaArquivoSave(arrayMusicas);
        geraPainelDeSamplers();
        atualizaPainelMusicas();
    }//GEN-LAST:event_itemAtualizarActionPerformed

    private void itemMenuAutoModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuAutoModeActionPerformed
        userDefConfig.setConexaoAutomatica(itemMenuAutoMode.isSelected());
        ArquivosConfig.criaArquivoUserDef(userDefConfig);
    }//GEN-LAST:event_itemMenuAutoModeActionPerformed

    private void menuSobreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSobreActionPerformed
        FrameAbout frameAbout = new FrameAbout();
        frameAbout.setVisible(true);
    }//GEN-LAST:event_menuSobreActionPerformed

    private void itemSamplerinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemSamplerinoActionPerformed
        final FrameAbout frameAbout = new FrameAbout();
        frameAbout.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                frameAbout.dispose();
            }
        });
        frameAbout.setVisible(true);
    }//GEN-LAST:event_itemSamplerinoActionPerformed

    private void itemMenuNovoSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemMenuNovoSairActionPerformed
        System.exit(0);
    }//GEN-LAST:event_itemMenuNovoSairActionPerformed

    private void btnLoopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoopActionPerformed
        ArrayList<Musica> arrayTemp = new ArrayList<>();
        for (Musica arrayMusica : arrayMusicas) {
            if (arrayMusica.getNomeMusica().equals(nomeBtnPressionado)) {
                arrayTemp.add(arrayMusica);
                play();
                break;
            }
        }
    }//GEN-LAST:event_btnLoopActionPerformed

    private void btnExecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExecutarActionPerformed
        ArrayList<Musica> arrayTemp = new ArrayList<>();
        for (int x = 0; x < arrayMusicas.size(); x++) {
            if (arrayMusicas.get(x).getNomeMusica().equals(nomeBtnPressionado)) {
                for (int y = x; y < arrayMusicas.size(); y++) {
                    arrayTemp.add(arrayMusicas.get(y));
                }
                play();
                break;
            }
        }
    }//GEN-LAST:event_btnExecutarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDescer;
    private javax.swing.JButton btnExecutar;
    private javax.swing.JButton btnLoop;
    private javax.swing.JButton btnPlay;
    private javax.swing.JButton btnStop;
    private javax.swing.JButton btnSubir;
    private javax.swing.JMenuItem itemAtualizar;
    private javax.swing.JRadioButtonMenuItem itemMenuAutoMode;
    private javax.swing.JRadioButtonMenuItem itemMenuLogger;
    private javax.swing.JMenuItem itemMenuNovaMusica;
    private javax.swing.JMenuItem itemMenuNovoSair;
    private javax.swing.JMenuItem itemMenuNovoSampler;
    private javax.swing.JMenuItem itemSamplerino;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuConfig;
    private javax.swing.JMenu menuNovo;
    private javax.swing.JMenu menuSobre;
    private javax.swing.JPanel painelBtnMoveStatus;
    private javax.swing.JPanel painelBtnMover;
    private javax.swing.JPanel painelBtnPlay;
    private javax.swing.JPanel painelBtnStop;
    private javax.swing.JPanel painelConteudoMusicasSamplers;
    private javax.swing.JPanel painelControles;
    public javax.swing.JPanel painelListaDeMusicas;
    public javax.swing.JPanel painelListaDeSamplers;
    private javax.swing.JPanel painelPrincipal;
    // End of variables declaration//GEN-END:variables

}
