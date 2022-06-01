import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class DisplayMachinePanel extends JPanel
{
    private static final int OUR_DEFAULT_WIDTH = 600;
    private static final int OUR_DEFAULT_HEIGHT = 300;


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
    // Ctors
    //

    public DisplayMachinePanel()
    {
        this(null);
    }

    public DisplayMachinePanel(ObservableVM252Machine machine)
    {

        //
        // create hybrid view-controller panel for display machine states
        //

        MachineStateViewAndController displayState = new MachineStateViewAndController(machine);

        //
        // create hybrid view-controller panel for display memory byte
        //

        MemoryByteViewAndController displayMemory= new MemoryByteViewAndController(machine);

        setPanel(new JPanel());
        getPanel().setBackground(new Color(51, 255, 255));

        //
        // add panels to container
        //

        setLayout(null);

        displayState.setBounds(0,0,300, 300);
        add(displayState);

        displayMemory.setBounds(400, 0, 300, 300);
        add(displayMemory);


    }
}

