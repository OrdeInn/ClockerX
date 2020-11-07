package com.orderinn.clockerx.AlarmClock;

import com.orderinn.clockerx.AlarmClock.AlarmObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class ObjectSerializer {



    public static String serializeObject(Serializable obj){

        if(obj == null){
            return "";
        }
        try{
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(obj);
            out.close();

            byte[] bytes = byteOut.toByteArray();

            return stringFromByteArray(bytes);

        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }


    public static Object deserializeStringToArrayList(String str){

        if(str == null || str.length() == 0){
            return new ArrayList<AlarmObject>();
        }

        try{
            byte[] bytesToGet = byteArrayFromString(str);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(bytesToGet);
            ObjectInputStream in = new ObjectInputStream(byteIn);
            ArrayList<AlarmObject> arrayListToReturn = (ArrayList<AlarmObject>) in.readObject();

            in.close();

            return arrayListToReturn;




        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public static Object deserializeStringToAlarmObject(String str){

        if(str == null || str.length() == 0){
            return new AlarmObject(null, 0, null, 0, null, null, -1);
        }

        try{
            byte[] bytesToGet = byteArrayFromString(str);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(bytesToGet);
            ObjectInputStream in = new ObjectInputStream(byteIn);
            AlarmObject objectToReturn = (AlarmObject) in.readObject();

            in.close();

            return objectToReturn;

        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }





    private static String stringFromByteArray(byte[] bytes){
        String result = "";

        for(int i=0; i < bytes.length; i++){
            result += Byte.toString(bytes[i]) + "/";
        }
        return result;
    }


    private static byte[] byteArrayFromString(String result){


        String[] parsedString = result.split("/");
        byte[] bytes = new byte[parsedString.length];

        for(int i=0; i<parsedString.length; i++){

            byte b = (byte) Integer.parseInt(parsedString[i]);
            bytes[i] = b;
        }

        return bytes;
    }
}
