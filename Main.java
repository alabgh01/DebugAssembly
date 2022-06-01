import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Scanner;
import vm252utilities.VM252Utilities;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Main
{
    public static void main(String [] commandLineArguments)
    {

        EventQueue.invokeLater(
            () ->
            {

                // call the file chooser method from the file chooser class
                ObjectFileChooser File = new ObjectFileChooser();
                File.ObjectFileChooser();
            }
        );

    }

}

class ObjectFileChooser extends JFileChooser
{
    public void ObjectFileChooser()
    {
        // Create a new File chooser from JFileChooser()
        JFileChooser fileChooser = new JFileChooser();

        // Adding filter to the file chooser
        // to only allow choices of files of type vm252 obj
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Opcode Files", "vm252obj");
        fileChooser.setFileFilter(filter);

        // Specify the location to be the current directory for the file chooser search
        fileChooser.setCurrentDirectory(new File("./vm252obj_examples"));

        // assign the result from file choices to response
        // it is int because it will be 0 if a file was chosen
        // it will be 1 if no file was choosen, and user clicked x or cancel
        int response = fileChooser.showOpenDialog(null);

        // if there was a file chosen to open,
        // get the file name from file chooser
        // get the object code from the choosen file
        // then feed that into the program
        if(response == JFileChooser.APPROVE_OPTION) {
            String file = fileChooser.getSelectedFile().getPath();
            byte [] program = VM252Utilities.readObjectCodeFromObjectFile(file);
            ProgramFrame frame = new ProgramFrame(program);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
        // if no file was choosen
        // exit the program
        else{
            System.exit(0);
        }
    }
}


class ProgramFrame extends JFrame
{

    private static final int OUR_DEFAULT_WIDTH = 800;
    private static final int OUR_DEFAULT_HEIGHT = 900;

    private JPanel myPanel;

    //
    // Accessors
    //

    private JPanel getPanel()
    {
        return myPanel;
    }

    //
    // Mutators
    //

    private void setPanel(JPanel other)
    {
        myPanel = other;
    }

    //
    //Ctors
    //


    public ProgramFrame(byte[] program)
    {
        setTitle("VM252 Debugger");
        setSize(OUR_DEFAULT_WIDTH, OUR_DEFAULT_HEIGHT);

        //
        // Create Model Object
        //

        ObservableVM252Machine machine = new ObservableVM252Machine(program);

        //
        // Create function buttons Pannel
        //
        FunctionButtonsPanel buttonsPanel = new FunctionButtonsPanel(machine);

        //
        // Create display machine pannel
        //

        DisplayMachinePanel displayPanel = new DisplayMachinePanel(machine);

        //
        // Create display instruction Pannel
        //

        DisplayInstructionPanel instructionPanel = new DisplayInstructionPanel(machine);

        //
        // Create display running Pannel
        //

        DisplayRunningPanel runningPanel = new DisplayRunningPanel(machine);

        //
        // ADD Panel to programe frame
        //

        setPanel(new JPanel());
        getPanel().setLayout(null);

        buttonsPanel.setBounds(0, 0, 800, 100);
        getPanel().add(buttonsPanel);

        displayPanel.setBounds(0, 100, 800, 200);
        getPanel().add(displayPanel);

        instructionPanel.setBounds(0, 300, 800, 200);
        getPanel().add(instructionPanel);

        runningPanel.setBounds(0, 500, 800, 300);
        getPanel().add(runningPanel);

        add(getPanel());
    }
}
