/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Ivy
 */
public class AttachmentManager {
    private static String workingDirectory = null;
    
    public static String GetWorkingDirectory(){
        if (workingDirectory != null)
            return workingDirectory;
        
        try{
        String root = System.getProperty("user.dir") + File.separatorChar + "Faceboot";
        if (!Files.exists(Paths.get(root)))
            Files.createDirectory(Paths.get(root));
        
        root += File.separatorChar + "Attachments";
        if (!Files.exists(Paths.get(root)))
            Files.createDirectory(Paths.get(root));
        
        workingDirectory = root;
        return root;
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean WriteFile(String filename, byte[] data){
        try{
            String finalPath = GetWorkingDirectory() + File.separatorChar + filename;
            Files.write(Paths.get(finalPath), data);
            return true;
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public static byte[] GetFile(String filename){
        try{
            String finalPath = GetWorkingDirectory() + File.separatorChar + filename;
            return Files.readAllBytes(Paths.get(finalPath));
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
