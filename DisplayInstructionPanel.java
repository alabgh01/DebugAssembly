import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JList;

public class DisplayInstructionPanel extends JPanel implements SimpleObserver
{
    private static final int OUR_DEFAULT_WIDTH = 500;
    private static final int OUR_DEFAULT_HEIGHT = 200;

    private JPanel myPanel;
    private ObservableVM252Machine mySubject;
    private String [] myContents;
    private JTextArea displayBox;

    //
    // Accessors
    //

    private JPanel getPanel()
    {
        return myPanel;
    }

    private ObservableVM252Machine getSubject()
    {
        return mySubject;
    }

    private String [] getContents()
    {
        return myContents;
    }

    private JTextArea getDisplayBox()
    {
        return displayBox;
    }

    //
    // Mutators
    //

    private void setPanel(JPanel other)
    {
        myPanel = other;
    }

    private void setContents(String [] other)
    {
        myContents = other;
    }

    private void setSubject(ObservableVM252Machine other)
    {
        if (getSubject() != null)
            getSubject().detach(this);

        mySubject = other;

        if (getSubject() != null)
            getSubject().attach(this);

    }

    private void setDisplayBox(JTextArea other)
    {
        displayBox = other;
    }

    //
    // Ctors
    //

    public DisplayInstructionPanel()
    {
        this(null);
    }

    public DisplayInstructionPanel(ObservableVM252Machine machine)
    {
        setSubject(machine);
        setContents(getSubject().getDisplayContents());

        setPanel(new JPanel());


        //
        // Initially display the model's display contents
        //

        setDisplayBox(new JTextArea(getSubject().getHumanReadableInstructions(), 10, 1));
        getDisplayBox().setBounds(150, 25, OUR_DEFAULT_WIDTH, OUR_DEFAULT_HEIGHT);
        getDisplayBox().setLineWrap(true);
        getDisplayBox().setEditable(false);


        JScrollPane scroll = new JScrollPane(getDisplayBox(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(150, 25, OUR_DEFAULT_WIDTH, OUR_DEFAULT_HEIGHT);

        setLayout(null);
        add(scroll);

    }

    @Override
    public void update()
    {
        ; // do nothing
    }
}

