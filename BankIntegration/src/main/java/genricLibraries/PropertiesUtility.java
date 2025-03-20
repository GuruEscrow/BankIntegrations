package genricLibraries;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/*
 * This class contains all reusable methods to perform actions
 * on Properties file 
 * @author Guruprasad
 */
public class PropertiesUtility {

    private Properties property;

    /*
     * This method is used to initialize properties file
     * @param filePath
     */
    public void propertiesInit(String filePath) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            property = new Properties();
            property.load(fis);
//            System.out.println("Properties initialized: " + property);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
            property = new Properties(); // Initialize even if file doesn’t exist
        } catch (IOException e) {
            System.err.println("Error loading properties: " + e.getMessage());
            property = new Properties(); // Fallback to empty properties
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * This method is used to fetch data from properties file
     * @param key
     * @return
     */
    public String readData(String key) {
        if (property == null) {
//            System.err.println("Properties not initialized. Call propertiesInit first.");
            return null;
        }
        return property.getProperty(key);
    }

    /*
     * This method is used to write and save data to properties file
     * @param key
     * @param value
     * @param filePath
     */
    public synchronized void writeToProperties(String key, String value, String filePath) {
        if (property == null) {
//            System.err.println("Properties not initialized. Initializing now...");
            propertiesInit(filePath); // Auto-initialize if not done
        }

        // Load the latest file state before writing (optional, see note below)
        try (FileInputStream fis = new FileInputStream(filePath)) {
            property.load(fis); // Reload to avoid overwriting with stale data
        } catch (IOException e) {
            System.out.println("File not found or error reloading, proceeding with current state: " + e.getMessage());
        }

        // Update the property
        property.put(key, value);
//        System.out.println("Writing " + key + "=" + value);

        // Write to file
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            property.store(fos, "Updated on " + System.currentTimeMillis());
            fos.flush(); // Ensure data is written
//            System.out.println("File updated successfully");
        } catch (IOException e) {
            System.err.println("Error writing to properties file: " + e.getMessage());
        }
    }
}