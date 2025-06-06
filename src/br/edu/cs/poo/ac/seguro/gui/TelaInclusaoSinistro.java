package br.edu.cs.poo.ac.seguro.gui;

import br.edu.cs.poo.ac.seguro.entidades.TipoSinistro;
import br.edu.cs.poo.ac.seguro.excecoes.ExcecaoValidacaoDados;
import br.edu.cs.poo.ac.seguro.mediators.DadosSinistro;
import br.edu.cs.poo.ac.seguro.mediators.SinistroMediator;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TelaInclusaoSinistro extends JFrame {

    private final SinistroMediator sinistroMediator = SinistroMediator.getInstancia();

    // Componentes da Tela
    private JTextField txtPlaca;
    private JTextField txtDataHoraSinistro; // Formato: dd/MM/yyyy HH:mm
    private JTextField txtUsuarioRegistro;
    private JTextField txtValorSinistro;
    private JComboBox<String> cmbTipoSinistro;

    private JButton btnIncluir;
    private JButton btnLimpar;

    public TelaInclusaoSinistro() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Inclusão de Sinistro");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Painel Principal ---
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Linha 0: Placa
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        pnlForm.add(new JLabel("Placa do Veículo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        txtPlaca = new JTextField(20);
        pnlForm.add(txtPlaca, gbc);

        // Linha 1: Data/Hora Sinistro
        gbc.gridy++; gbc.gridx = 0;
        pnlForm.add(new JLabel("Data/Hora Sinistro (dd/mm/aaaa hh:mm):"), gbc);
        gbc.gridx = 1;
        txtDataHoraSinistro = new JTextField();
        pnlForm.add(txtDataHoraSinistro, gbc);

        // Linha 2: Usuário Registro
        gbc.gridy++; gbc.gridx = 0;
        pnlForm.add(new JLabel("Usuário do Registro:"), gbc);
        gbc.gridx = 1;
        txtUsuarioRegistro = new JTextField();
        pnlForm.add(txtUsuarioRegistro, gbc);

        // Linha 3: Valor Sinistro
        gbc.gridy++; gbc.gridx = 0;
        pnlForm.add(new JLabel("Valor do Sinistro:"), gbc);
        gbc.gridx = 1;
        txtValorSinistro = new JTextField();
        pnlForm.add(txtValorSinistro, gbc);

        // Linha 4: Tipo de Sinistro
        gbc.gridy++; gbc.gridx = 0;
        pnlForm.add(new JLabel("Tipo de Sinistro:"), gbc);
        gbc.gridx = 1;
        cmbTipoSinistro = new JComboBox<>();
        popularComboTiposSinistro(); // Popula o JComboBox
        pnlForm.add(cmbTipoSinistro, gbc);

        add(pnlForm, BorderLayout.CENTER);

        // --- Painel de Botões ---
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnIncluir = new JButton("Incluir Sinistro");
        btnLimpar = new JButton("Limpar");

        pnlBotoes.add(btnIncluir);
        pnlBotoes.add(btnLimpar);
        add(pnlBotoes, BorderLayout.SOUTH);

        // --- Listeners ---
        btnLimpar.addActionListener(e -> limparCampos());

        btnIncluir.addActionListener(e -> {
            DadosSinistro dados = obterDadosDaTela();
            if (dados != null) {
                try {
                    String numeroSinistro = sinistroMediator.incluirSinistro(dados, LocalDateTime.now());
                    String mensagemSucesso = "Sinistro incluído com sucesso!\nAnote o número do sinistro: " + numeroSinistro; //
                    JOptionPane.showMessageDialog(this, mensagemSucesso, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    limparCampos();
                } catch (ExcecaoValidacaoDados ex) { //
                    String mensagensErro = ex.getMensagens().stream().collect(Collectors.joining("\n")); //
                    JOptionPane.showMessageDialog(this, "Erros de Validação:\n" + mensagensErro, "Erro", JOptionPane.ERROR_MESSAGE); //
                }
            }
        });
    }

    private void popularComboTiposSinistro() {
        List<String> nomesTipos = new ArrayList<>();
        for (TipoSinistro tipo : TipoSinistro.values()) {
            nomesTipos.add(tipo.getNome());
        }
        Collections.sort(nomesTipos); // Ordem alfabética crescente

        for (String nome : nomesTipos) {
            cmbTipoSinistro.addItem(nome);
        }
    }

    private void limparCampos() {
        txtPlaca.setText("");
        txtDataHoraSinistro.setText("");
        txtUsuarioRegistro.setText("");
        txtValorSinistro.setText("");
        if (cmbTipoSinistro.getItemCount() > 0) {
            cmbTipoSinistro.setSelectedIndex(0); // "Limpar" o combo significa mostrar nele o 1° elemento
        }
    }

    private DadosSinistro obterDadosDaTela() {
        String placa = txtPlaca.getText().trim();
        String dataHoraSinistroStr = txtDataHoraSinistro.getText().trim();
        String usuarioRegistro = txtUsuarioRegistro.getText().trim();
        String valorSinistroStr = txtValorSinistro.getText().trim();
        String nomeTipoSelecionado = (String) cmbTipoSinistro.getSelectedItem();

        LocalDateTime dataHoraSinistro = null;
        if (!dataHoraSinistroStr.isEmpty()) {
            try {
                // Ajuste o pattern conforme o formato que você espera do usuário. Ex: "dd/MM/yyyy HH:mm"
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                dataHoraSinistro = LocalDateTime.parse(dataHoraSinistroStr, formatter);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Data/Hora do Sinistro inválida. Use o formato dd/mm/aaaa hh:mm.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        double valorSinistro;
        try {
            valorSinistro = Double.parseDouble(valorSinistroStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor do sinistro inválido. Digite um número.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        int codigoTipoSinistro = -1;
        if (nomeTipoSelecionado != null) {
            for (TipoSinistro tipo : TipoSinistro.values()) {
                if (tipo.getNome().equals(nomeTipoSelecionado)) {
                    codigoTipoSinistro = tipo.getCodigo();
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um tipo de sinistro.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return null;
        }


        return new DadosSinistro(placa, dataHoraSinistro, usuarioRegistro, valorSinistro, codigoTipoSinistro);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaInclusaoSinistro().setVisible(true));
    }
}