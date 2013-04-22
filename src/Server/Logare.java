/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;
import java.io.IOException;
import org.apache.log4j.*;

/**
 *
 * @author Andrei
 */

public class Logare {
   
    
    public static Logger initLogger(String filename) {
        
        Logger logger = Logger.getLogger(Logare.class); 
        PropertyConfigurator.configure("log4j.properties");
        SimpleLayout layout = new SimpleLayout();    
        FileAppender appender;    
        
        try {
            appender = new FileAppender(layout, filename,false);
            logger.addAppender(appender);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return logger;
    }
    
    
}
