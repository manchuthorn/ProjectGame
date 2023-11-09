/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pacman;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Model extends JPanel implements ActionListener {
    private Dimension d;
    private final Font smallFont = new Font("Arial", Font.BOLD, 14);
    private boolean inGame = false;
    private boolean dying = false;
    private final int BLOCK_SIZE = 24;
    private final int N_BLOCKS = 15;
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
    private final int MAX_GHOSTS = 12;
    private final int PACMAN_SPEED = 6;
    private int N_GHOSTS = 6;
    private int lives, score;
    private int[] dx, dy;
    private int[] ghost_x, ghost_y, ghost_dx, ghost_dy, ghostSpeed;
    private Image heart, ghost;
    private Image up, down, left, right;
    private int pacman_x, pacman_y, pacmand_x, pacmand_y;
    private int req_dx, req_dy;
    private int currentLevel = 1;
    private boolean gameOver = false;
    private int gameOverScore;

    private final short levelData[] = {
    	19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
        17, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        25, 24, 24, 24, 28, 0, 17, 16, 16, 16, 16, 16, 16, 16, 20,
        0,  0,  0,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 20,
        19, 18, 18, 18, 18, 18, 16, 16, 16, 16, 24, 24, 24, 24, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
        17, 16, 16, 16, 24, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
        17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 18, 18, 18, 18, 20,
        17, 24, 24, 28, 0, 25, 24, 24, 16, 16, 16, 16, 16, 16, 20,
        21, 0,  0,  0,  0,  0,  0,   0, 17, 16, 16, 16, 16, 16, 20,
        17, 18, 18, 22, 0, 19, 18, 18, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        25, 24, 24, 24, 26, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28
    };
    private final int validSpeeds[] = {1, 2, 3, 4, 6, 8};
    private final int maxSpeed = 6;
    private int currentSpeed = 3;
    private short[] screenData;
    private Timer timer;
    private int timeLeft; // New variable to keep track of time left (in seconds)
    private Timer countdownTimer; // New Timer for the countdown
    

    public Model() {
        loadImages();
        initVariables();
        addKeyListener(new TAdapter());
        setFocusable(true);
        
        JButton level1Button = new JButton("Level 1");
        JButton level2Button = new JButton("Level 2");
        // Level 1
        level1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentLevel = 1; 
                startGame(); 
                level1Button.setVisible(false);
                level2Button.setVisible(false);
            }
        });
        // Level 2
        level2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentLevel = 2; 
                startGame(); 
                level1Button.setVisible(false);
                level2Button.setVisible(false);
            }
        });
        // Arrange buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(level1Button);
        buttonPanel.add(level2Button);
        add(buttonPanel);
        // create Layout Manager
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        // Set the position and size of the button.
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(level1Button, c);
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(level2Button, c);
        initGame();
        // Initialize the timer with 2 minutes (120 seconds)
        timeLeft = 120;
        // Create a Timer with a 1-second delay that counts down the time
        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;

                if (timeLeft < 0) {
                    timeLeft = 0;
                    // Handle game over or timeout logic here
                }
            }
        });
        // Start the countdown timer
        countdownTimer.start();
    }
    private void startGame() {
        lives = 3;
        score = 0;
        initLevel();
        N_GHOSTS = 6;
        currentSpeed = 3;
        inGame = true;
        timer.restart();
    }
    
    private void loadImages(){
        down = new ImageIcon("C:\\Users\\MJAm\\Desktop\\character\\down.gif").getImage();
        up = new ImageIcon("C:\\Users\\MJAm\\Desktop\\character\\up.gif").getImage();
        left = new ImageIcon("C:\\Users\\MJAm\\Desktop\\character\\left.gif").getImage();
        right = new ImageIcon("C:\\Users\\MJAm\\Desktop\\character\\right.gif").getImage();
        ghost = new ImageIcon("C:\\Users\\MJAm\\Desktop\\character\\ghost.gif").getImage();
        heart = new ImageIcon("C:\\Users\\MJAm\\Desktop\\character\\heart.png").getImage();
    }
    
    private void initVariables() {
    screenData = new short[N_BLOCKS * N_BLOCKS];
    d = new Dimension(400, 400);
    ghost_x = new int[MAX_GHOSTS];
    ghost_dx = new int[MAX_GHOSTS];
    ghost_y = new int[MAX_GHOSTS];
    ghost_dy = new int[MAX_GHOSTS];
    ghostSpeed = new int[MAX_GHOSTS];
    dx = new int[4];
    dy = new int[4];
    timer = new Timer(40, this);
    timer.start();
    }

    private void playGame(Graphics2D g2d) {
        if (dying) {
            death();
        } else {
            movePacman();
            drawPacman(g2d);
            moveGhosts(g2d);
            checkMaze();
        }
        // Calculate the width of the time string to center it
        FontMetrics fontMetrics = g2d.getFontMetrics(smallFont);
        int timeStringWidth = fontMetrics.stringWidth("Time: " + timeLeft + " seconds");
        // Calculate the x-coordinate for centering the time string
        int centerX = (SCREEN_SIZE - timeStringWidth) / 2;
        // Create a blue rectangle background for the time
        g2d.setColor(new Color(0, 0, 255, 128)); // Blue color with alpha transparency
        g2d.fillRect(centerX - 5, SCREEN_SIZE + 5, timeStringWidth + 10, 25);
        // Draw the time string on top of the blue rectangle
        g2d.setColor(new Color(255, 255, 255));
        g2d.drawString("Time: " + timeLeft + " seconds", centerX, SCREEN_SIZE + 20);
        // Display the score and heart images as you were doing before
        drawScore(g2d);
    }
    
    // Add a method to check if the game is over due to a timeout
    private boolean isGameOver() {
        return timeLeft <= 0;
    }
    private void showIntroScreen(Graphics2D g2d) {
 
    	//String start = "Press SPACE to start";
        String start = "";
        g2d.setColor(Color.yellow);
        g2d.drawString(start, (SCREEN_SIZE)/4, 150);
    }

    private void drawScore(Graphics2D g) {
        g.setFont(smallFont);
        g.setColor(new Color(5, 181, 79));
        String s = "Score: " + score;
        g.drawString(s, SCREEN_SIZE / 2 + 96, SCREEN_SIZE + 16);

        for (int i = 0; i < lives; i++) {
            g.drawImage(heart, i * 28 + 8, SCREEN_SIZE + 1, this);
        }
    }

    private void checkMaze() {
        int i = 0;
        boolean finished = true;
        while (i < N_BLOCKS * N_BLOCKS && finished) {
            if ((screenData[i]) != 0) {
                finished = false;
            }
            i++;
        }
        if (finished) {
            score += 50;
            if (N_GHOSTS < MAX_GHOSTS) {
                N_GHOSTS++;
            }
            if (currentSpeed < maxSpeed) {
                currentSpeed++;
            }
            initLevel();
        }
    }

    private void death() {
    	lives--;
        if (lives == 0) {
            inGame = false;
            gameOverScore = score; // Set the game over score
        // Additional code to handle game over logic here
        }
        continueLevel();
    }

    private void moveGhosts(Graphics2D g2d) {
        int pos;
        int count;
        for (int i = 0; i < N_GHOSTS; i++) {
            if (ghost_x[i] % BLOCK_SIZE == 0 && ghost_y[i] % BLOCK_SIZE == 0) {
                pos = ghost_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (ghost_y[i] / BLOCK_SIZE);
                count = 0;
                if ((screenData[pos] & 1) == 0 && ghost_dx[i] != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }
                if ((screenData[pos] & 2) == 0 && ghost_dy[i] != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }
                if ((screenData[pos] & 4) == 0 && ghost_dx[i] != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }
                if ((screenData[pos] & 8) == 0 && ghost_dy[i] != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }
                if (count == 0) {
                    if ((screenData[pos] & 15) == 15) {
                        ghost_dx[i] = 0;
                        ghost_dy[i] = 0;
                    } else {
                        ghost_dx[i] = -ghost_dx[i];
                        ghost_dy[i] = -ghost_dy[i];
                    }
                } else {
                    count = (int) (Math.random() * count);
                    if (count > 3) {
                        count = 3;
                    }
                    ghost_dx[i] = dx[count];
                    ghost_dy[i] = dy[count];
                }
            }
            ghost_x[i] = ghost_x[i] + (ghost_dx[i] * ghostSpeed[i]);
            ghost_y[i] = ghost_y[i] + (ghost_dy[i] * ghostSpeed[i]);
            drawGhost(g2d, ghost_x[i] + 1, ghost_y[i] + 1);
            if (pacman_x > (ghost_x[i] - 12) && pacman_x < (ghost_x[i] + 12)
                    && pacman_y > (ghost_y[i] - 12) && pacman_y < (ghost_y[i] + 12)
                    && inGame) {
                dying = true;
            }
        }
    }

    private void drawGhost(Graphics2D g2d, int x, int y) {
    	g2d.drawImage(ghost, x, y, this);
        }

    private void movePacman() {
        int pos;
        short ch;
        if (pacman_x % BLOCK_SIZE == 0 && pacman_y % BLOCK_SIZE == 0) {
            pos = pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE);
            ch = screenData[pos];
            if ((ch & 16) != 0) {
                screenData[pos] = (short) (ch & 15);
                score++;
            }
            if (req_dx != 0 || req_dy != 0) {
                if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)
                        || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
                        || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
                        || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
                    pacmand_x = req_dx;
                    pacmand_y = req_dy;
                }
            }
            // Check for standstill
            if ((pacmand_x == -1 && pacmand_y == 0 && (ch & 1) != 0)
                    || (pacmand_x == 1 && pacmand_y == 0 && (ch & 4) != 0)
                    || (pacmand_x == 0 && pacmand_y == -1 && (ch & 2) != 0)
                    || (pacmand_x == 0 && pacmand_y == 1 && (ch & 8) != 0)) {
                pacmand_x = 0;
                pacmand_y = 0;
            }
        } 
        pacman_x = pacman_x + PACMAN_SPEED * pacmand_x;
        pacman_y = pacman_y + PACMAN_SPEED * pacmand_y;
    }

    private void drawPacman(Graphics2D g2d) {

        if (req_dx == -1) {
        	g2d.drawImage(left, pacman_x + 1, pacman_y + 1, this);
        } else if (req_dx == 1) {
        	g2d.drawImage(right, pacman_x + 1, pacman_y + 1, this);
        } else if (req_dy == -1) {
        	g2d.drawImage(up, pacman_x + 1, pacman_y + 1, this);
        } else {
        	g2d.drawImage(down, pacman_x + 1, pacman_y + 1, this);
        }
    }

    private void drawMaze(Graphics2D g2d) {
        short i = 0;
        int x, y;
        for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {
                g2d.setColor(new Color(0,72,251));
                g2d.setStroke(new BasicStroke(5));             
                if ((levelData[i] == 0)) { 
                	g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                 }
                if ((screenData[i] & 1) != 0) { 
                    g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);
                }
                if ((screenData[i] & 2) != 0) { 
                    g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y);
                }
                if ((screenData[i] & 4) != 0) { 
                    g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }
                if ((screenData[i] & 8) != 0) { 
                    g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1,
                            y + BLOCK_SIZE - 1);
                }
                if ((screenData[i] & 16) != 0) { 
                    g2d.setColor(new Color(255,255,255));
                    g2d.fillOval(x + 10, y + 10, 6, 6);
               }
                i++;
            }
        }
    }

    private void initGame() {

    	lives = 3;
        score = 0;
        initLevel();
        N_GHOSTS = 6;
        currentSpeed = 3;
    }

    private void initLevel() {

        int i;
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            screenData[i] = levelData[i];
        }

        continueLevel();
    }

    private void continueLevel() {
    	int dx = 1;
        int random;
        for (int i = 0; i < N_GHOSTS; i++) {
        ghost_y[i] = 4 * BLOCK_SIZE; //start position
        ghost_x[i] = 4 * BLOCK_SIZE;
        ghost_dy[i] = 0;
        ghost_dx[i] = dx;
        dx = -dx;
        // Increase ghost speed in stage 1
        if (currentLevel == 1) {
            ghostSpeed[i] = 2; // speed in stage 1
        } else{
        // Increase ghost speed in stage 2
        if (currentLevel == 2) {
            ghostSpeed[i] = 6; // speed in stage 2
        } else {
            random = (int) (Math.random() * (currentSpeed + 1));
            if (random > currentSpeed) {
                random = currentSpeed;
            }
            ghostSpeed[i] = validSpeeds[random];}}
    }
        pacman_x = 7 * BLOCK_SIZE;  //start position
        pacman_y = 11 * BLOCK_SIZE;
        pacmand_x = 0;	//reset direction move
        pacmand_y = 0;
        req_dx = 0;   // reset direction controls
        req_dy = 0;
        dying = false;
    }

    private void gameOver(Graphics2D g2d) {
    String gameOverMsg = "Game Over";
    String scoreMsg = "Your Score: " + gameOverScore;

    Font bigFont = new Font("Helvetica", Font.BOLD, 36);
    FontMetrics fontMetrics = g2d.getFontMetrics(bigFont);

    int msgWidth = fontMetrics.stringWidth(gameOverMsg);
    int msgHeight = fontMetrics.getHeight();

    int x = (SCREEN_SIZE - msgWidth) / 2;
    int y = (SCREEN_SIZE - msgHeight) / 2;

    g2d.setColor(Color.white);
    g2d.setFont(bigFont);
    g2d.drawString(gameOverMsg, x, y);

    int scoreX = (SCREEN_SIZE - fontMetrics.stringWidth(scoreMsg)) / 2;
    int scoreY = y + msgHeight + 20;

    g2d.drawString(scoreMsg, scoreX, scoreY);
}

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);
        drawMaze(g2d);
        drawScore(g2d);
        if (inGame) {
            playGame(g2d);
        } else {
            showIntroScreen(g2d);
        }
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }


    //controls
    class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (inGame) {
                if (key == KeyEvent.VK_A) {
                    req_dx = -1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_D) {
                    req_dx = 1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_W) {
                    req_dx = 0;
                    req_dy = -1;
                } else if (key == KeyEvent.VK_S) {
                    req_dx = 0;
                    req_dy = 1;
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    inGame = false;
                } 
        }
}}

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
		
	}