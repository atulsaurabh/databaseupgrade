package com.technoride.server.databaseupgrade.utils;

import com.technoride.server.databaseupgrade.mode.Mode;

import java.io.*;

public class EncodeDecodeMode
{
    public void encodeMode(Mode mode)
    {
        try {
            String filePath = getClass().getResource("/config/").getPath();
            File file = new File(filePath+"mode.ser");
            if(!file.exists())
                file.createNewFile();
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(
                    new FileOutputStream(
                            new File(getClass().getResource("/config/mode.ser").getPath())
                    ));

            objectOutputStream.writeObject(mode);
            objectOutputStream.flush();
            objectOutputStream.close();
        }
        catch (FileNotFoundException f)
        {
            f.printStackTrace();
        }
        catch (IOException io)
        {
            io.printStackTrace();
        }
    }


    public Mode decodeMode()
    {
        try {
            ObjectInputStream objectInputStream=new ObjectInputStream(
                    new FileInputStream(new File(getClass().getResource("/config/mode.ser").getPath())));
            Mode mode=(Mode)objectInputStream.readObject();
            return mode;
        }
        catch (FileNotFoundException file)
        {
            file.printStackTrace();
        }catch (IOException io)
        {
            io.printStackTrace();
        }
        catch (ClassNotFoundException cnf)
        {
            cnf.printStackTrace();
        }

        return new Mode();

    }
}
