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
    static Logger logger = Logger.getLogger(Logare.class); 
    
    public static Logger initLogger(String filename) {
        
        Logger logger = Logger.getLogger(Logare.class); 
        PropertyConfigurator.configure(filename);
        SimpleLayout layout = new SimpleLayout();    
        FileAppender appender;    
        
        try {
            appender = new FileAppender(layout,"user1.log",false);
            logger.addAppender(appender);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return logger;
    }
    
    
}
