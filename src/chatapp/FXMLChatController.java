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

/**
 *
 * @author angel
 */
public class FXMLChatController implements Initializable {
   
    final String host = "localhost";
    private int clientNo = 0;
    
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
                Platform.runLater(()->
                messagesTA.appendText("Server at " + new Date() + '\n'));
                Socket socket = serverSocket.accept();
                
                DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
                
                while(true){
                   // Socket socket = serverSocket.accept();
                    
                    messagesTA.appendText(socket.toString() + " Client local port: " + 
                            socket.getRemoteSocketAddress() + '\n');
                   
                    String name = inputFromClient.readUTF();
                    String message = inputFromClient.readUTF();
                    
                    outputToClient.writeUTF("Name:  " + name);
                    outputToClient.writeUTF("Says: " + message);
                    
                    new HandleAClient(socket);
                 
                    Platform.runLater(()->{
                        messagesTA.appendText("Name: " + name + '\n');
                        messagesTA.appendText("Says: " + message + '\n');
                    });
                    
                    
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
                
               // new Thread(new HandleAClient(socket)).start();
            }
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }
} 
