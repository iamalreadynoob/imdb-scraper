package org.home;

import java.io.*;
import java.util.ArrayList;

public class FileCommunication
{

    protected static ArrayList<String> read(String path)
    {
        ArrayList<String> lines = new ArrayList<>();

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(new File(path)));

            String line;
            while ((line = reader.readLine()) != null) lines.add(line);
        }
        catch (IOException e){e.printStackTrace();}

        return lines;
    }

    protected static void write(ArrayList<String> lines, String path)
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path)));

            for (int i = 0; i < lines.size(); i++)
            {
                if (i == 0) writer.write(lines.get(i));
                else writer.write("\n" + lines.get(i));
            }

            writer.close();
        }
        catch (IOException e) {e.printStackTrace();}
    }

}
