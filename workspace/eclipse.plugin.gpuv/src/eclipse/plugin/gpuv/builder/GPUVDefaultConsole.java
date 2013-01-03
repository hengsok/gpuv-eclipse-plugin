package eclipse.plugin.gpuv.builder;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
/**
 * 
 * Gain access to current console for printing stderr or stdout
 * Implemented using the Singleton pattern.
 * 
 */
public class GPUVDefaultConsole {
    private static GPUVDefaultConsole instance = new GPUVDefaultConsole();
    
    // stream to output message to console
    private MessageConsoleStream consoleStream;

    private MessageConsole console;

    
    /**
     * Output a message to the console.
     */
    public static void printToConsole(String msg) {
        instance.getConsoleStream().println(msg);
        
    }
    
    /**
     * Return the console. Instantiate if necessary.
     * @return the output console
     */
    private MessageConsole getConsole() {
        if (console == null) {
            console = new MessageConsole("GPUVerify", null);
            IConsoleManager mgr = ConsolePlugin.getDefault().getConsoleManager();
            mgr.addConsoles(new IConsole[] { console });
        }
        return console;
    }
    
    /**
     * Return the console output stream. Initialise it if this is the first time.
     */
    private MessageConsoleStream getConsoleStream() {
        if (consoleStream == null) {
            consoleStream = getConsole().newMessageStream();
        }
        return consoleStream;
    }
    
}
