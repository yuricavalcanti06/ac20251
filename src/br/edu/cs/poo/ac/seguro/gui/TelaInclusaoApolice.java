package br.edu.cs.poo.ac.seguro.gui;

import br.edu.cs.poo.ac.seguro.entidades.CategoriaVeiculo;
import br.edu.cs.poo.ac.seguro.mediators.ApoliceMediator;
import br.edu.cs.poo.ac.seguro.mediators.DadosVeiculo;
import br.edu.cs.poo.ac.seguro.mediators.RetornoInclusaoApolice;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TelaInclusaoApolice extends JFrame {

    private final ApoliceMediator apoliceMediator = ApoliceMediator.getInstancia();

    // Componentes da Tela
    private JTextField txtCpfCnpj;
    private JTextField txtPlaca;
    private JTextField txtAno;
    private JTextField txtValorMaximoSegurado;
    private JComboBox<String> cmbCategoria;

    private JButton btnIncluir;
    private JButton btnLimpar;

    public TelaInclusaoApolice() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Inclusão de Apólice");
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

        // Linha 0: CPF/CNPJ
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        pnlForm.add(new JLabel("CPF ou CNPJ do Proprietário:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        txtCpfCnpj = new JTextField(20);
        pnlForm.add(txtCpfCnpj, gbc);

        // Linha 1: Placa
        gbc.gridy++; gbc.gridx = 0;
        pnlForm.add(new JLabel("Placa do Veículo:"), gbc);
        gbc.gridx = 1;
        txtPlaca = new JTextField();
        pnlForm.add(txtPlaca, gbc);

        // Linha 2: Ano
        gbc.gridy++; gbc.gridx = 0;
        pnlForm.add(new JLabel("Ano:"), gbc);
        gbc.gridx = 1;
        txtAno = new JTextField();
        pnlForm.add(txtAno, gbc);

        // Linha 3: Valor Máximo Segurado
        gbc.gridy++; gbc.gridx = 0;
        pnlForm.add(new JLabel("Valor Máximo Segurado:"), gbc);
        gbc.gridx = 1;
        txtValorMaximoSegurado = new JTextField();
        pnlForm.add(txtValorMaximoSegurado, gbc);

        // Linha 4: Categoria
        gbc.gridy++; gbc.gridx = 0;
        pnlForm.add(new JLabel("Categoria do Veículo:"), gbc);
        gbc.gridx = 1;
        cmbCategoria = new JComboBox<>();
        popularComboCategorias(); // Popula o JComboBox
        pnlForm.add(cmbCategoria, gbc);

        add(pnlForm, BorderLayout.CENTER);

        // --- Painel de Botões ---
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnIncluir = new JButton("Incluir Apólice");
        btnLimpar = new JButton("Limpar");

        pnlBotoes.add(btnIncluir);
        pnlBotoes.add(btnLimpar);
        add(pnlBotoes, BorderLayout.SOUTH);

        // --- Listeners ---
        btnLimpar.addActionListener(e -> limparCampos());

        btnIncluir.addActionListener(e -> {
            DadosVeiculo dados = obterDadosDaTela();
            if (dados != null) {
                RetornoInclusaoApolice retorno = apoliceMediator.incluirApolice(dados);
                if (retorno.getMensagemErro() != null) {
                    JOptionPane.showMessageDialog(this, retorno.getMensagemErro(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                } else {
                    String mensagemSucesso = "Apólice incluída com sucesso!\nAnote o número da apólice: " + retorno.getNumeroApolice();
                    JOptionPane.showMessageDialog(this, mensagemSucesso, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    limparCampos();
                }
            }
        });
    }

    private void popularComboCategorias() {
        List<String> nomesCategorias = new ArrayList<>();
        for (CategoriaVeiculo cat : CategoriaVeiculo.values()) {
            nomesCategorias.add(cat.getNome());
        }
        Collections.sort(nomesCategorias); // Ordem alfabética crescente

        for (String nome : nomesCategorias) {
            cmbCategoria.addItem(nome);
        }
    }

    private void limparCampos() {
        txtCpfCnpj.setText("");
        txtPlaca.setText("");
        txtAno.setText("");
        txtValorMaximoSegurado.setText("");
        if (cmbCategoria.getItemCount() > 0) {
            cmbCategoria.setSelectedIndex(0); // "Limpar" o combo significa mostrar nele o 1° elemento
        }
    }

    private DadosVeiculo obterDadosDaTela() {
        String cpfOuCnpj = txtCpfCnpj.getText().trim();
        String placa = txtPlaca.getText().trim();
        String anoStr = txtAno.getText().trim();
        String valorMaximoStr = txtValorMaximoSegurado.getText().trim();
        String nomeCategoriaSelecionada = (String) cmbCategoria.getSelectedItem();

        int ano;
        try {
            ano = Integer.parseInt(anoStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ano inválido. Digite um número inteiro.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        BigDecimal valorMaximoSegurado;
        try {
            valorMaximoSegurado = new BigDecimal(valorMaximoStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor máximo segurado inválido. Digite um número.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        int codigoCategoria = -1;
        for (CategoriaVeiculo cat : CategoriaVeiculo.values()) {
            if (cat.getNome().equals(nomeCategoriaSelecionada)) {
                codigoCategoria = cat.getCodigo();
                break;
            }
        }

        return new DadosVeiculo(cpfOuCnpj, placa, ano, valorMaximoSegurado, codigoCategoria);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaInclusaoApolice().setVisible(true));
    }
}