import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Sudoku {
    class Tile extends JButton {
        int r;
        int c;
        Tile(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    int boardWidth = 600;
    int boardHeight = 650;
String[] puzzle = {
                    "--74916-5",
                    "2---6-3-9",
                    "-----7-1-",
                    "-586----4",
                    "--3----9-",
                    "--62--187",
                    "9-4-7---2",
                    "67-83----",
                    "81--45---"
                    };

        String[] solution = {
                    "387491625",
                    "241568379",
                    "569327418",
                    "758619234",
                    "123784596",
                    "496253187",
                    "934176852",
                    "675832941",
                    "812945763"
                    };


    JFrame frame = new JFrame("Sudoku");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    BackgroundPanel boardPanel;
    JPanel buttonsPanel = new JPanel();

    JButton numSelected = null;
    int errors = 0;

    Sudoku() {
        BufferedImage backgroundImg = null;
        try {
            backgroundImg = ImageIO.read(getClass().getResource("/resources/sudoku_bg.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        boardPanel = new BackgroundPanel(backgroundImg);
        boardPanel.setLayout(new GridLayout(9, 9));

        frame.setSize(boardWidth, boardHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Courier New", Font.BOLD, 30));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("SODOKU - BYTE WAVE : 0");

        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        setupTiles();
        frame.add(boardPanel, BorderLayout.CENTER);

        buttonsPanel.setLayout(new GridLayout(1, 9));
        setupButtons();
        frame.add(buttonsPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    void setupTiles() {
        Color brown = new Color(139, 69, 19);

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Tile tile = new Tile(r, c);
                char tileChar = puzzle[r].charAt(c);
                tile.setFont(new Font("Courier New", Font.BOLD, 20));

                if (tileChar != '-') {
                    tile.setText(String.valueOf(tileChar));
                    tile.setBackground(brown);
                    tile.setForeground(Color.white);
                    tile.setOpaque(true);
                    tile.setContentAreaFilled(true);
                } else {
                    tile.setText("");
                    tile.setOpaque(false);
                    tile.setContentAreaFilled(false);
                    tile.setForeground(Color.black);
                    tile.setBorderPainted(true);
                }

                // Add grid borders
                if ((r == 2 && c == 2) || (r == 2 && c == 5) || (r == 5 && c == 2) || (r == 5 && c == 5)) {
                    tile.setBorder(BorderFactory.createMatteBorder(1, 1, 5, 5, Color.black));
                } else if (r == 2 || r == 5) {
                    tile.setBorder(BorderFactory.createMatteBorder(1, 1, 5, 1, Color.black));
                } else if (c == 2 || c == 5) {
                    tile.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 5, Color.black));
                } else {
                    tile.setBorder(BorderFactory.createLineBorder(Color.black));
                }

                tile.setFocusable(false);
                boardPanel.add(tile);

                tile.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Tile clickedTile = (Tile) e.getSource();
                        int r = clickedTile.r;
                        int c = clickedTile.c;

                        if (numSelected != null) {
                            if (!clickedTile.getText().equals("")) {
                                return;
                            }

                            String numSelectedText = numSelected.getText();
                            String tileSolution = String.valueOf(solution[r].charAt(c));

                            if (tileSolution.equals(numSelectedText)) {
                                clickedTile.setText(numSelectedText);
                                clickedTile.setForeground(Color.black);

                                // Verificar se o jogador ganhou
                                boolean win = true;
                                Component[] tiles = boardPanel.getComponents();
                                for (Component comp : tiles) {
                                    if (comp instanceof Tile tileCheck) {
                                        String currentText = tileCheck.getText();
                                        if (currentText.isEmpty()) {
                                            win = false;
                                            break;
                                        }
                                        int rr = tileCheck.r;
                                        int cc = tileCheck.c;
                                        if (!currentText.equals(String.valueOf(solution[rr].charAt(cc)))) {
                                            win = false;
                                            break;
                                        }
                                    }
                                }

                                if (win) {
                                    JOptionPane.showMessageDialog(frame,
                                        "🎉 Parabéns! Você completou o Sudoku!",
                                        "Vitória!",
                                        JOptionPane.INFORMATION_MESSAGE);
                                }

                            } else {
                                errors += 1;
                                textLabel.setText("Sudoku: " + errors);
                                JOptionPane.showMessageDialog(frame,
                                    "Ei presta atenção, você consegue. Eu estou aqui.",
                                    "Erro",
                                    JOptionPane.WARNING_MESSAGE);
                            }
                        }
                    }
                });
            }
        }
    }

    void setupButtons() {
        Color terraClara = new Color(222, 184, 135);

        for (int i = 1; i < 10; i++) {
            JButton button = new JButton();
            button.setFont(new Font("Courier New", Font.BOLD, 20));
            button.setText(String.valueOf(i));
            button.setFocusable(false);
            button.setBackground(Color.white);
            button.setForeground(Color.black);
            buttonsPanel.add(button);

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton selected = (JButton) e.getSource();
                    if (numSelected != null) {
                        numSelected.setBackground(Color.white);
                    }
                    numSelected = selected;
                    numSelected.setBackground(terraClara);
                }
            });
        }
    }

    public static void main(String[] args) {
        new Sudoku();
    }
}

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(Image image) {
        this.backgroundImage = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.50f));
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            g2d.dispose();
        }
    }
}
