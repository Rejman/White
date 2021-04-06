package Utils;



import java.io.*;


public abstract class Serialize {

    public static final String path = "settings.ini";

    public static Settings loadSettings(){
        Settings settings;
        // Deserialization
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(file);
            // Method for deserialization of object
            settings = (Settings) in.readObject();

            in.close();
            file.close();
            System.out.println("settings loaded");
            return settings;
        }
        catch(Exception ex){
            Settings newSettings = new Settings();
            saveSettings(newSettings);
            return newSettings;
        }

    }

    public static void saveSettings(Settings settings){
        // Serialization
        try
        {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(file);
            //Method for serialization of object
            out.writeObject(settings);
            out.close();
            file.close();
            System.out.println("zapisano");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
