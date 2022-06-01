import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FunctionButtonsPanel extends JPanel
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

    public FunctionButtonsPanel()
    {
        this(null);
    }

    public FunctionButtonsPanel(ObservableVM252Machine machine)
    {

        //
        // Create button controller
        //

        ButtonController buttons = new ButtonController(machine);

        setPanel(new JPanel());
        getPanel().setSize(OUR_DEFAULT_WIDTH, OUR_DEFAULT_HEIGHT);
        getPanel().add(buttons);

        add(getPanel());

    }
}
