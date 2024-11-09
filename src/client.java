import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class client {
    private static final String SERVER_ADDRESS = "10.84.83.113"; // Server's IP address
    private static final int SERVER_PORT = 12345;
    private static final int WIDTH =Toolkit.getDefaultToolkit().getScreenSize().width ;
    private static final int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    private static final int FPS = 60;

    // Game state variables
    ArrayList<Character> charactersOnField = new ArrayList<>();

    // Client Only Variables
    public int counter = 0;
    public int spriteNum;

    private static Socket socket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static int playerId;

    public static void main(String[] args) {
        client client = new client();
        client.start();
    }

    // Start the client
    public void start() {
        try {
            // Establish a connection to the server
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // Receive player ID from server
            playerId = (Integer) in.readObject();
            System.out.println("Connected as Player " + playerId);

            // Set up the game window
            JFrame frame = new JFrame("Game Title");//SET TITLE
            frame.setSize(WIDTH, HEIGHT);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new GamePanel());
            frame.setVisible(true);

            // Start listening for server messages
            new Thread(new ServerListener()).start();

            // Handle key events for paddle movement
            frame.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    String action = null;
                    //CHANGE AS NEEDED
                    if (e.getKeyCode() == KeyEvent.VK_UP) {
                        action = "UP";
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        action = "DOWN";
                    }

                    // Send player action to the server
                    if (action != null) {
                        try {//SEND PLAYER ACTION
                            out.writeObject(new PlayerAction(playerId));
                            out.flush();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Thread to listen for game state updates from the server
    private class ServerListener implements Runnable {
        @Override
        public void run() {
            


            try {
                while (true) {
                    // Receive GameState object from the server
                    GameState gameState = (GameState) in.readObject();

                    // Update game state variables
                    charactersOnField = gameState.charactersOnField;

                    //update local animations
                    counter++;
                    if(counter > 10){
                        if(spriteNum == 1){
                            spriteNum = 2;
                        }
                        else if(spriteNum == 2){
                            spriteNum = 1;
                        }
                        counter = 0;
                    }

                    // Trigger a repaint on the game window
                    SwingUtilities.invokeLater(() -> {
                        JFrame.getFrames()[0].repaint();
                    });
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    // Game panel to render paddles, ball, and scores
    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;

            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, WIDTH, HEIGHT); // Clear the background

            // Draw Game

            // Draw Characters
            for(Character character : charactersOnField){
                BufferedImage image = null;
                if(character.direction == 1){
                    if(spriteNum == 1){
                        image = character.left1;
                    }
                    if(spriteNum == 2){
                        image = character.left2;
                    }
                }
                if(character.direction == 2){
                    if(spriteNum == 1){
                        image = character.right1;
                    }
                    if(spriteNum == 2){
                        image = character.right2;
                    }
                }
                int dimm = character.size * character.scale;
                g2.drawImage(image, character.x, character.y, dimm, dimm, null);
            }
            

        }
    }
}