import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client extends JFrame {

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    //Delcare Components
    private JLabel heading=new JLabel("Client Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Robot",Font.PLAIN,20);

    //constructor
    public Client() {
        try {
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1", 8778);
            System.out.println("connection done.");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();

            startReading();
            //startWriting();

        } catch (Exception e) {
            // TODO: handle exception

        }
    }

    private void handleEvents() {

        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                //System.out.println("Key released"+e.getKeyCode());

                if(e.getKeyCode()==10){
                   // System.out.println("you have pressed enter button");
                   String contentToSend=messageInput.getText();
                   messageArea.append("Me : "+contentToSend+"\n");
                   out.println(contentToSend);
                   out.flush();
                   messageInput.setText("");
                   messageInput.requestFocus();
                }
                
            }
            
        });
    }

    private void createGUI(){
        //gui code...
        this.setTitle("Client Messager[END]");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("p1.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        
        //messageInput.setHorizontalAlignment(SwingConstants.RIGHT);

        //frame ka layout set karenge
        this.setLayout(new BorderLayout());

        //adding the components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);


        this.setVisible(true);
    }


    
    //start reading [Method]
    public void startReading() {
        // thread-read
        Runnable r1 = () -> {

            System.out.println("reader started..");

            try {

                while (true) {

                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    //System.out.println("Server : " + msg);
                    messageArea.append("Server : " + msg+"\n");

                }
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println("Connection closed");
            }

        };

        new Thread(r1).start();
    }

    

    //start Writing [Method]
    public void startWriting() {
        // thread-data user
        Runnable r2 = () -> {
            System.out.println("writer started..");

            try {

                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

                    String content = br1.readLine();

                    out.println(content);
                    out.flush();

                    if (content.equals("exit")) {
                        socket.close();
                        break;
                    }

                }

                System.out.println("Connection closed");

            } catch (Exception e) {
                e.printStackTrace();
            }

        };

        new Thread(r2).start();
    }

    
    public static void main(String[] args) {

        System.out.println("this is client...");
        new Client();
    }

}
