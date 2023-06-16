package FacialRecognision;

import java.io.File;

public class MethodAdder {
    
    public static void addMethod(String method, String classPath){

        //string contains a java method we want to add to the class
        String methodToAdd = method;

        //open the file
        File file = new File(classPath);
        //find the end of the file
        int fileLength = (int)file.length();
        //create a byte array of the file size
        byte[] fileData = new byte[fileLength];
        //read the file into the byte array
        try{
            java.io.DataInputStream dis = new java.io.DataInputStream(new java.io.FileInputStream(file));
            dis.readFully(fileData);
            dis.close();
        }catch(Exception e){
            System.out.println(e);
        }

        //create a string from the byte array
        String fileString = new String(fileData);
        //check if the method already exists in the file
        if(!MethodSyntaxValidator.isValidMethod(methodToAdd)){
            System.out.println("Method is not valid");
            return;
        }
        //first find the method name in the string
        String methodName = methodToAdd.substring(0, methodToAdd.indexOf("("));
        int methodIndex = fileString.indexOf(methodName);
        //if the method name is found, then the method already exists
        if(methodIndex != -1){
            System.out.println("Method already exists");
            return;
        }
        //find the index of the last bracket
        int lastBracket = fileString.lastIndexOf("}");
        //create a string of the file up to the last bracket
        String fileStart = fileString.substring(0, lastBracket);
        //create a string of the file after the last bracket
        String fileEnd = fileString.substring(lastBracket);
        //create a string of the file with the new method added
        String newFileString = fileStart + methodToAdd+ "\n" + fileEnd;

        //write the new file string to the file
        try{
            java.io.DataOutputStream dos = new java.io.DataOutputStream(new java.io.FileOutputStream(file));
            dos.writeBytes(newFileString);
            dos.close();
        }catch(Exception e){
            System.out.println(e);
        }
    

    }
    

    public static void main(String[] args) {
        String temp= "public ";
        String method = "void testMethod(){System.out.println(\"Hello World\");}";
        addMethod(temp+method,"src/main/java/FacialRecognision/test.java");
    }




}
