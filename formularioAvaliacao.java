import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class formularioAvaliacao extends JFrame {

    private JTextField txtCodigo;
    private JTextField txtNome;
    private JTextField txtAreas;
    private JRadioButton radioFeminino;
    private JRadioButton radioMasculino;
    private JTextArea txtCurriculum;
    private JTable tabelaInteresses;
    private DefaultTableModel tableModel;

    private List<Candidato> candidatos = new ArrayList<>();
    private int indiceAtual = -1;

    public formularioAvaliacao() {
        super("Formulário de Avaliação");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLayout(new BorderLayout(10, 10));

        carregarDados();

        JMenuBar menuBar = criarMenuBar();
        setJMenuBar(menuBar);

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BoxLayout(painelPrincipal, BoxLayout.Y_AXIS));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        painelPrincipal.add(criarCampoComLinha("Código :", true));
        painelPrincipal.add(Box.createVerticalStrut(15));
        painelPrincipal.add(criarCampoComLinha("Nome :", false));
        painelPrincipal.add(Box.createVerticalStrut(15));
        painelPrincipal.add(criarGrupoSexo());
        painelPrincipal.add(Box.createVerticalStrut(15));
        painelPrincipal.add(new JSeparator(JSeparator.HORIZONTAL));
        painelPrincipal.add(Box.createVerticalStrut(15));
        painelPrincipal.add(criarAreaComTitulo("Curriculum Vitae", 5));
        painelPrincipal.add(Box.createVerticalStrut(15));
        painelPrincipal.add(new JSeparator(JSeparator.HORIZONTAL));
        painelPrincipal.add(Box.createVerticalStrut(15));
        painelPrincipal.add(criarCampoComLinha("Áreas", false));
        painelPrincipal.add(Box.createVerticalStrut(15));
        painelPrincipal.add(criarAreaComTitulo("Interesse :", 2));
        painelPrincipal.add(Box.createVerticalStrut(15));
        painelPrincipal.add(criarTabelaInteresses());
        painelPrincipal.add(Box.createVerticalStrut(20));
        painelPrincipal.add(criarBotoes());

        add(painelPrincipal, BorderLayout.CENTER);

        if (!candidatos.isEmpty()) {
            indiceAtual = 0;
            mostrarCandidato(candidatos.get(0));
        }
    }

    private JMenuBar criarMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menuArquivo = new JMenu("Arquivo");
        
        JMenuItem itemEnviar = new JMenuItem("Enviar");
        itemEnviar.addActionListener(e -> enviarFormulario());
        
        JMenu subMenuEmail = new JMenu("email");
        JMenuItem itemFicha = new JMenuItem("Ficha de Avaliação");
        itemFicha.addActionListener(e -> enviarEmail());
        subMenuEmail.add(itemFicha);
        
        JMenu subMenuSalvar = new JMenu("Salvar");
        JMenuItem itemImpressora = new JMenuItem("Impressora");
        itemImpressora.addActionListener(e -> imprimirFormulario());
        subMenuSalvar.add(itemImpressora);
        
        JMenuItem itemSair = new JMenuItem("Sair");
        itemSair.addActionListener(e -> System.exit(0));
        
        menuArquivo.add(itemEnviar);
        menuArquivo.add(subMenuEmail);
        menuArquivo.add(subMenuSalvar);
        menuArquivo.addSeparator();
        menuArquivo.add(itemSair);
        
        menuBar.add(menuArquivo);
        
        return menuBar;
    }

    private JPanel criarCampoComLinha(String rotulo, boolean isCodigo) {
        JPanel painel = new JPanel();
        painel.setLayout(new BorderLayout());
        
        JLabel label = new JLabel(rotulo);
        painel.add(label, BorderLayout.NORTH);
        
        JSeparator linha = new JSeparator(JSeparator.HORIZONTAL);
        painel.add(linha, BorderLayout.SOUTH);
        
        if (isCodigo) {
            txtCodigo = new JTextField(20);
            painel.add(txtCodigo, BorderLayout.CENTER);
        } else if (rotulo.equals("Nome :")) {
            txtNome = new JTextField(20);
            painel.add(txtNome, BorderLayout.CENTER);
        } else {
            txtAreas = new JTextField(20);
            painel.add(txtAreas, BorderLayout.CENTER);
        }
        
        return painel;
    }

    private JPanel criarGrupoSexo() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painel.add(new JLabel("Sexo :"));
        
        radioFeminino = new JRadioButton("Feminino");
        radioMasculino = new JRadioButton("Masculino");
        
        ButtonGroup grupoSexo = new ButtonGroup();
        grupoSexo.add(radioFeminino);
        grupoSexo.add(radioMasculino);
        
        painel.add(radioFeminino);
        painel.add(radioMasculino);
        
        return painel;
    }

    private JPanel criarAreaComTitulo(String titulo, int linhas) {
        JPanel painel = new JPanel();
        painel.setLayout(new BorderLayout());
        
        JLabel label = new JLabel(titulo);
        painel.add(label, BorderLayout.NORTH);
        
        txtCurriculum = new JTextArea(linhas, 30);
        txtCurriculum.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        painel.add(new JScrollPane(txtCurriculum), BorderLayout.CENTER);
        
        return painel;
    }

    private JScrollPane criarTabelaInteresses() {
        String[] colunas = {"Desenvolvedor", "Programação"};
        tableModel = new DefaultTableModel(colunas, 3);
        tabelaInteresses = new JTable(tableModel);
        tabelaInteresses.setPreferredScrollableViewportSize(new Dimension(450, 80));
        
        return new JScrollPane(tabelaInteresses);
    }

    private JPanel criarBotoes() {
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvarCandidato());
        
        JButton btnAnterior = new JButton("Anterior");
        btnAnterior.addActionListener(e -> mostrarAnterior());
        
        JButton btnProximo = new JButton("Proximo");
        btnProximo.addActionListener(e -> mostrarProximo());
        
        JButton btnNovo = new JButton("Novo");
        btnNovo.addActionListener(e -> novoCandidato());
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> limparCampos());
        
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnAnterior);
        painelBotoes.add(btnProximo);
        painelBotoes.add(btnNovo);
        painelBotoes.add(btnCancelar);
        
        return painelBotoes;
    }
    
    private void carregarDados() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("candidatos.dat"))) {
            candidatos = (List<Candidato>) ois.readObject();
        } catch (FileNotFoundException e) {

        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("candidatos.dat"))) {
            oos.writeObject(candidatos);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar dados: " + e.getMessage(), 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Candidato lerDadosFormulario() {
        Candidato candidato = new Candidato();
        candidato.setCodigo(txtCodigo.getText());
        candidato.setNome(txtNome.getText());
        candidato.setSexo(radioFeminino.isSelected() ? "Feminino" : 
                          radioMasculino.isSelected() ? "Masculino" : "");
        candidato.setCurriculum(txtCurriculum.getText());
        candidato.setAreas(txtAreas.getText());
        
        List<String[]> interesses = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String desenvolvedor = (String) tableModel.getValueAt(i, 0);
            String programacao = (String) tableModel.getValueAt(i, 1);
            interesses.add(new String[]{desenvolvedor, programacao});
        }
        candidato.setInteresses(interesses);
        
        return candidato;
    }
    
    private void preencherFormulario(Candidato candidato) {
        txtCodigo.setText(candidato.getCodigo());
        txtNome.setText(candidato.getNome());
        
        if (candidato.getSexo().equals("Feminino")) {
            radioFeminino.setSelected(true);
        } else if (candidato.getSexo().equals("Masculino")) {
            radioMasculino.setSelected(true);
        } else {
            radioFeminino.setSelected(false);
            radioMasculino.setSelected(false);
        }
        
        txtCurriculum.setText(candidato.getCurriculum());
        txtAreas.setText(candidato.getAreas());

        tableModel.setRowCount(0);
        for (String[] interesse : candidato.getInteresses()) {
            tableModel.addRow(interesse);
        }
    }
    
    private void limparCampos() {
        txtCodigo.setText("");
        txtNome.setText("");
        radioFeminino.setSelected(false);
        radioMasculino.setSelected(false);
        txtCurriculum.setText("");
        txtAreas.setText("");

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt("", i, 0);
            tableModel.setValueAt("", i, 1);
        }
        
        indiceAtual = -1;
    }
    
    private void salvarCandidato() {
        Candidato candidato = lerDadosFormulario();
        
        if (candidato.getCodigo().isEmpty() || candidato.getNome().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Código e Nome são obrigatórios!", 
                                          "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (indiceAtual >= 0 && indiceAtual < candidatos.size()) {
            candidatos.set(indiceAtual, candidato);
        } else {
            candidatos.add(candidato);
            indiceAtual = candidatos.size() - 1;
        }
        
        salvarDados();
        JOptionPane.showMessageDialog(this, "Candidato salvo com sucesso!", 
                                      "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarCandidato(Candidato candidato) {
        preencherFormulario(candidato);
    }
    
    private void mostrarAnterior() {
        if (candidatos.isEmpty()) return;
        
        if (indiceAtual > 0) {
            indiceAtual--;
            mostrarCandidato(candidatos.get(indiceAtual));
        } else {
            JOptionPane.showMessageDialog(this, "Primeiro registro", 
                                          "Navegação", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void mostrarProximo() {
        if (candidatos.isEmpty()) return;
        
        if (indiceAtual < candidatos.size() - 1) {
            indiceAtual++;
            mostrarCandidato(candidatos.get(indiceAtual));
        } else {
            JOptionPane.showMessageDialog(this, "Último registro", 
                                          "Navegação", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void novoCandidato() {
        limparCampos();
        txtCodigo.requestFocus();
    }
    
    private void enviarFormulario() {
        JOptionPane.showMessageDialog(this, "Formulário enviado com sucesso!", 
                                      "Envio", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void enviarEmail() {
        JOptionPane.showMessageDialog(this, "Ficha de avaliação enviada por e-mail!", 
                                      "E-mail", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void imprimirFormulario() {
        JOptionPane.showMessageDialog(this, "Enviando para impressora...", 
                                      "Impressão", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            formularioAvaliacao formulario = new formularioAvaliacao();
            formulario.setVisible(true);
        });
    }
    
    static class Candidato implements Serializable {
        private String codigo;
        private String nome;
        private String sexo;
        private String curriculum;
        private String areas;
        private List<String[]> interesses = new ArrayList<>();

        // Getters e Setters
        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }
        
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        
        public String getSexo() { return sexo; }
        public void setSexo(String sexo) { this.sexo = sexo; }
        
        public String getCurriculum() { return curriculum; }
        public void setCurriculum(String curriculum) { this.curriculum = curriculum; }
        
        public String getAreas() { return areas; }
        public void setAreas(String areas) { this.areas = areas; }
        
        public List<String[]> getInteresses() { return interesses; }
        public void setInteresses(List<String[]> interesses) { this.interesses = interesses; }
    }
}