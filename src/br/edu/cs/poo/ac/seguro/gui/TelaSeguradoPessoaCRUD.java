package br.edu.cs.poo.ac.seguro.gui;

import br.edu.cs.poo.ac.seguro.entidades.Endereco;
import br.edu.cs.poo.ac.seguro.entidades.SeguradoPessoa;
import br.edu.cs.poo.ac.seguro.mediators.SeguradoPessoaMediator;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TelaSeguradoPessoaCRUD extends JFrame {

    private SeguradoPessoaMediator seguradoPessoaMediator = SeguradoPessoaMediator.getInstancia();

    private enum EstadoTela {
        INICIAL, NOVO, VISUALIZACAO, ALTERACAO
    }

    // Componentes da Tela
    private JTextField txtCpf;
    private JTextField txtNome;
    private JTextField txtDataNascimento;
    private JTextField txtRenda;
    private JTextField txtBonus;
    private JTextField txtLogradouro;
    private JTextField txtNumero;
    private JTextField txtComplemento;
    private JTextField txtCep;
    private JTextField txtCidade;
    private JTextField txtEstado;
    private JTextField txtPais;

    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnBuscar;
    private JButton btnAlterar;
    private JButton btnExcluir;
    private JButton btnLimpar;

    public TelaSeguradoPessoaCRUD() {
        initComponents();
        configurarEstado(EstadoTela.INICIAL);
    }

    private void initComponents() {
        setTitle("CRUD de Segurado Pessoa");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(contentPanel, BorderLayout.CENTER);

        // --- Grupo de Dados Pessoais ---
        JPanel pnlDadosPessoais = new JPanel(new GridBagLayout());
        pnlDadosPessoais.setBorder(new TitledBorder("Dados Pessoais"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 5, 4, 5); // Aumentando o espaçamento vertical
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Linha 0: CPF
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        pnlDadosPessoais.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.9;
        txtCpf = new JTextField(20);
        pnlDadosPessoais.add(txtCpf, gbc);

        // Linha 1: Nome
        gbc.gridy++; gbc.gridx = 0;
        pnlDadosPessoais.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        txtNome = new JTextField();
        pnlDadosPessoais.add(txtNome, gbc);

        // Linha 2: Nascimento
        gbc.gridy++; gbc.gridx = 0;
        pnlDadosPessoais.add(new JLabel("Nascimento (dd/mm/aaaa):"), gbc);
        gbc.gridx = 1;
        txtDataNascimento = new JTextField();
        pnlDadosPessoais.add(txtDataNascimento, gbc);

        // Linha 3: Renda
        gbc.gridy++; gbc.gridx = 0;
        pnlDadosPessoais.add(new JLabel("Renda:"), gbc);
        gbc.gridx = 1;
        txtRenda = new JTextField();
        pnlDadosPessoais.add(txtRenda, gbc);

        // Linha 4: Bônus
        gbc.gridy++; gbc.gridx = 0;
        pnlDadosPessoais.add(new JLabel("Bônus:"), gbc);
        gbc.gridx = 1;
        txtBonus = new JTextField();
        txtBonus.setEditable(false);
        pnlDadosPessoais.add(txtBonus, gbc);

        contentPanel.add(pnlDadosPessoais);

        // --- Grupo de Endereço ---
        JPanel pnlEndereco = new JPanel(new GridBagLayout());
        pnlEndereco.setBorder(new TitledBorder("Endereço"));

        // Linha 0: Logradouro
        gbc.gridy = 0; gbc.gridx = 0; gbc.weightx = 0.1;
        pnlEndereco.add(new JLabel("Logradouro:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.9;
        txtLogradouro = new JTextField();
        pnlEndereco.add(txtLogradouro, gbc);

        // Linha 1: Número e Complemento
        gbc.gridy++; gbc.gridx = 0; gbc.weightx = 0.1;
        pnlEndereco.add(new JLabel("Número:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4;
        txtNumero = new JTextField();
        pnlEndereco.add(txtNumero, gbc);

        gbc.gridx = 2; gbc.weightx = 0.1;
        pnlEndereco.add(new JLabel("Complemento:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4;
        txtComplemento = new JTextField();
        pnlEndereco.add(txtComplemento, gbc);

        // Linha 2: CEP e País
        gbc.gridy++; gbc.gridx = 0; gbc.weightx = 0.1;
        pnlEndereco.add(new JLabel("CEP:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4;
        txtCep = new JTextField();
        pnlEndereco.add(txtCep, gbc);

        gbc.gridx = 2; gbc.weightx = 0.1;
        pnlEndereco.add(new JLabel("País:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4;
        txtPais = new JTextField();
        pnlEndereco.add(txtPais, gbc);

        // Linha 3: Cidade e Estado
        gbc.gridy++; gbc.gridx = 0; gbc.weightx = 0.1;
        pnlEndereco.add(new JLabel("Cidade:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.4;
        txtCidade = new JTextField();
        pnlEndereco.add(txtCidade, gbc);

        gbc.gridx = 2; gbc.weightx = 0.1;
        pnlEndereco.add(new JLabel("Estado (UF):"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.4;
        txtEstado = new JTextField();
        pnlEndereco.add(txtEstado, gbc);

        contentPanel.add(pnlEndereco);

        // --- Painel de Botões ---
        JPanel pnlBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnNovo = new JButton("Novo");
        btnSalvar = new JButton("Salvar");
        btnBuscar = new JButton("Buscar");
        btnAlterar = new JButton("Alterar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar");

        pnlBotoes.add(btnNovo);
        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnBuscar);
        pnlBotoes.add(btnAlterar);
        pnlBotoes.add(btnExcluir);
        pnlBotoes.add(btnLimpar);
        add(pnlBotoes, BorderLayout.SOUTH);

        // --- Comportamentos dos Botões (Listeners) ---
        btnNovo.addActionListener(e -> {
            limparCampos();
            configurarEstado(EstadoTela.NOVO);
        });

        btnLimpar.addActionListener(e -> {
            limparCampos();
            configurarEstado(EstadoTela.INICIAL);
        });

        btnBuscar.addActionListener(e -> {
            String cpf = txtCpf.getText().trim();
            SeguradoPessoa segurado = seguradoPessoaMediator.buscarSeguradoPessoa(cpf);
            if (segurado != null) {
                popularTela(segurado);
                configurarEstado(EstadoTela.VISUALIZACAO);
            } else {
                JOptionPane.showMessageDialog(this, "Segurado não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                limparCampos();
                configurarEstado(EstadoTela.INICIAL);
            }
        });

        btnAlterar.addActionListener(e -> configurarEstado(EstadoTela.ALTERACAO));

        btnExcluir.addActionListener(e -> {
            int resposta = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir este segurado?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                String cpf = txtCpf.getText().trim();
                String msg = seguradoPessoaMediator.excluirSeguradoPessoa(cpf);
                if (msg == null) {
                    JOptionPane.showMessageDialog(this, "Segurado excluído com sucesso!");
                    limparCampos();
                    configurarEstado(EstadoTela.INICIAL);
                } else {
                    JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnSalvar.addActionListener(e -> {
            SeguradoPessoa segurado = obterDadosDaTela();
            if (segurado != null) {
                boolean isInclusao = !btnAlterar.isEnabled();
                String msg = isInclusao ? seguradoPessoaMediator.incluirSeguradoPessoa(segurado) : seguradoPessoaMediator.alterarSeguradoPessoa(segurado);

                if (msg == null) {
                    JOptionPane.showMessageDialog(this, "Operação realizada com sucesso!");
                    // Repopula a tela para garantir que dados calculados (bônus) estejam corretos
                    SeguradoPessoa seguradoSalvo = seguradoPessoaMediator.buscarSeguradoPessoa(segurado.getCpf());
                    if (seguradoSalvo != null) {
                        popularTela(seguradoSalvo);
                    }
                    configurarEstado(EstadoTela.VISUALIZACAO);
                } else {
                    JOptionPane.showMessageDialog(this, msg, "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void configurarEstado(EstadoTela estado) {
        // Campos editáveis
        boolean camposEditaveis = (estado == EstadoTela.NOVO || estado == EstadoTela.ALTERACAO);

        txtNome.setEditable(camposEditaveis);
        txtDataNascimento.setEditable(camposEditaveis);
        txtRenda.setEditable(camposEditaveis);
        txtLogradouro.setEditable(camposEditaveis);
        txtNumero.setEditable(camposEditaveis);
        txtComplemento.setEditable(camposEditaveis);
        txtCep.setEditable(camposEditaveis);
        txtCidade.setEditable(camposEditaveis);
        txtEstado.setEditable(camposEditaveis);
        txtPais.setEditable(camposEditaveis);

        txtCpf.setEditable(estado == EstadoTela.INICIAL || estado == EstadoTela.NOVO);

        // Habilitação de botões
        btnNovo.setEnabled(estado == EstadoTela.INICIAL || estado == EstadoTela.VISUALIZACAO);
        btnSalvar.setEnabled(estado == EstadoTela.NOVO || estado == EstadoTela.ALTERACAO);
        btnBuscar.setEnabled(estado == EstadoTela.INICIAL);
        btnAlterar.setEnabled(estado == EstadoTela.VISUALIZACAO);
        btnExcluir.setEnabled(estado == EstadoTela.VISUALIZACAO);
        btnLimpar.setEnabled(true);
    }

    private void limparCampos() {
        // Itera sobre todos os componentes e limpa os JTextFields
        for (Component panel : getContentPane().getComponents()) {
            if (panel instanceof JPanel) {
                for (Component group : ((JPanel) panel).getComponents()) {
                    if (group instanceof JPanel) {
                        for(Component field : ((JPanel)group).getComponents()){
                            if(field instanceof JTextField){
                                ((JTextField)field).setText("");
                            }
                        }
                    }
                }
            }
        }
    }

    private void popularTela(SeguradoPessoa segurado) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        txtCpf.setText(segurado.getCpf());
        txtNome.setText(segurado.getNome());
        txtDataNascimento.setText(segurado.getDataNascimento() != null ? segurado.getDataNascimento().format(dtf) : "");
        txtRenda.setText(String.valueOf(segurado.getRenda()));
        txtBonus.setText(segurado.getBonus() != null ? segurado.getBonus().toPlainString() : "0.00");

        if (segurado.getEndereco() != null) {
            Endereco end = segurado.getEndereco();
            txtLogradouro.setText(end.getLogradouro());
            txtNumero.setText(end.getNumero());
            txtComplemento.setText(end.getComplemento());
            txtCep.setText(end.getCep());
            txtCidade.setText(end.getCidade());
            txtEstado.setText(end.getEstado());
            txtPais.setText(end.getPais());
        }
    }

    private SeguradoPessoa obterDadosDaTela() {
        String cpf = txtCpf.getText().trim();
        String nome = txtNome.getText().trim();
        String dataNascStr = txtDataNascimento.getText().trim();
        String rendaStr = txtRenda.getText().trim();
        String logradouro = txtLogradouro.getText().trim();
        String numero = txtNumero.getText().trim();
        String complemento = txtComplemento.getText().trim();
        String cep = txtCep.getText().trim();
        String cidade = txtCidade.getText().trim();
        String estado = txtEstado.getText().trim();
        String pais = txtPais.getText().trim();

        LocalDate dataNascimento = null;
        if (!dataNascStr.isEmpty()) {
            try {
                dataNascimento = LocalDate.parse(dataNascStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Formato de data de nascimento inválido. Use dd/mm/aaaa.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        double renda = 0.0;
        if (!rendaStr.isEmpty()) {
            try {
                renda = Double.parseDouble(rendaStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Formato de renda inválido. Use um número.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        BigDecimal bonus = BigDecimal.ZERO;
        if (!txtBonus.getText().isEmpty()) {
            try {
                bonus = new BigDecimal(txtBonus.getText());
            } catch (NumberFormatException e) {
                // Ignora. O bônus é apenas para exibição
            }
        }

        Endereco end = new Endereco(logradouro, cep, numero, complemento, pais, estado, cidade);
        return new SeguradoPessoa(nome, end, dataNascimento, bonus, cpf, renda);
    }

    public static void main(String[] args) {
        // Garante que a UI seja criada na Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> new TelaSeguradoPessoaCRUD().setVisible(true));
    }
}