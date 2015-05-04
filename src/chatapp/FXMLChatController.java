/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author angel
 */
public class FXMLChatController implements Initializable {
   
    final String host = "localhost";
    private int clientNo = 0;
    
    @FXML
    private TextField nameTF;
    @FXML
    private TextField textTF;
    @FXML
    public TextArea messagesTA;
    
   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        server();
        
        
    }    
    
    public void server(){
        
        new Thread( () -> {
            try{
                ServerSocket serverSocket = new ServerSocket(8001);
                messagesTA.appendText("Server at" + new Date() + '\n');
                
                while(true){
                    Socket socket = serverSocket.accept();
                    
                    clientNo++;
                }
                
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }).start();
        
    }
       
}

class HandleAClient implements Runnable{
    private Socket socket;
    public TextArea messagesTA;
    
    public HandleAClient(Socket socket){
        this.socket = socket;
    }
    
    public void run(){
        try{
            
            DataInputStream inputFromClient = new DataInputStream(
                socket.getInputStream());
            DataOutputStream outputToClient = new DataOutputStream(
                socket.getOutputStream());
            
            while (true){
                String message = inputFromClient.readUTF();
                
                outputToClient.writeUTF(message);
                
                Platform.runLater(()->{ 
                    messagesTA.appendText(message);
                });
                
                new Thread(new HandleAClient(socket)).start();
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
}
