import java.awt.*;
import java.util.Scanner;
import java.awt.event.*;
import javax.swing.*;

public class ButtonController extends JPanel
{
    private JPanel myPanel;
    private ObservableVM252Machine myModel;

    // toolbar
    private JToolBar tb;

    // buttons
    private JButton hHelp, nCommand, qCommand, rCommand, stop, resume, increaseSpeed, decreaseSpeed ;
    private JLabel toolbarLabel, baLabel;
    private JTextField textFieldForba;

    //
    // Accessors
    //

    private JPanel getPanel()
    {
        return myPanel;
    }

    private ObservableVM252Machine getModel()
    {
        return myModel;
    }

    //
    // Mutators
    //

    private void setPanel(JPanel other)
    {
        myPanel = other;
    }

    private void setModel(ObservableVM252Machine other)
    {
        myModel = other;
    }

    //
    // Ctors
    //

    public ButtonController()
    {
        this(null);
    }

    public ButtonController(ObservableVM252Machine initialModel)
    {
        setModel(initialModel);

        // create a toolbar
        tb = new JToolBar();

        // Create buttons

        toolbarLabel = new JLabel("Toolbar ");
        nCommand = new JButton(" n ");
        qCommand = new JButton(" q ");
        rCommand = new JButton(" r ");
        baLabel = new JLabel(" ba: ");
        textFieldForba = new JTextField("", 10);
        stop = new JButton("Pause");
        resume = new JButton("Resume");
        increaseSpeed = new JButton("Increase Speed");
        decreaseSpeed = new JButton ("Decrease Speed");
        hHelp = new JButton(" Help ");

        // add the help functionality
        // to print what all the other commands do
        // using an eventlistenr

        hHelp.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent e){
                String [] helpContents = {"ba MA = Set a breakpoint at address MA",
                "help = Print this help message",
                "n = Execute next machine instruction",
                "q = Quit",
                "r = Run machine until error occurs or stop instruction is executed"
                };
		        getModel().setDisplayContents(helpContents);
            }});

        // add the quit functionality
        // quit the program up clicking on the quit button
        // using an event listener for this function

        ActionListener quitActListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // close the current JFrame
                ((JFrame)myPanel.getTopLevelAncestor()).dispose();
                // Create a new File Chooser
                ObjectFileChooser newFile = new ObjectFileChooser();
                // Run the FileChooser to create a new JFrame out of the new chosen file
                newFile.ObjectFileChooser();

                // the file chooser will open
                // if no file was choosen to open
                // the program will close
            }
        };

        qCommand.addActionListener(quitActListener);

        //
        // Add action listener for r command button
        //

        RunButtonActionListener runListener = new RunButtonActionListener();
        rCommand.addActionListener(runListener);

        //
        // Add action listener for n command button
        //

        RunStepListener runStepListener = new RunStepListener();
        nCommand.addActionListener(runStepListener);

        //
        // Add action listener for increase speed and decrease speed command
        //

        ChangeSpeedListener changeSpeedListener = new ChangeSpeedListener();
        increaseSpeed.addActionListener(changeSpeedListener);
        decreaseSpeed.addActionListener(changeSpeedListener);

        //
        // Add action listener for stop and resume command
        //

        ChangeRunningStatus changeRunningStatus = new ChangeRunningStatus();
        stop.addActionListener(changeRunningStatus);
        resume.addActionListener(changeRunningStatus);

        //
        // Add action Listener for ba text field
        //

        setBreakPointListener baListener = new setBreakPointListener();
        textFieldForba.addActionListener(baListener);

        // Add buttons to toolbar

        tb.setFloatable(false);
        tb.add(toolbarLabel);
        tb.add(nCommand);
        tb.add(qCommand);
        tb.add(rCommand);
        tb.add(baLabel);
        tb.add(textFieldForba);
        tb.add(stop);
        tb.add(resume);
        tb.add(increaseSpeed);
        tb.add(decreaseSpeed);
        tb.addSeparator(new Dimension(5, 1));
        tb.add(hHelp);


        setPanel(new JPanel());
        getPanel().add(tb);

        // add color to the background of the toolbar
        // to sdistinguich it from other part

        tb.setBackground(new Color(200,200,200));


        // Add the panel to the container

        add(getPanel());
    }

    private class setBreakPointListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            try
            {
                short breakPointPosition = Short.valueOf(textFieldForba.getText());
                if(breakPointPosition > 8191 || breakPointPosition < 0)
                {
                    getModel().setDisplayContents(new String[] {"No address" + breakPointPosition});
                    getModel().resetDisplayContents();
                }else
                {
                    getModel().setBreakPoint(breakPointPosition);
                    getModel().setDisplayContents(new String[] {"set breakpoint at address " + breakPointPosition});
                }
            }catch(NumberFormatException err)
            {
                getModel().setDisplayContents(new String [] {"Not a valid input. ba value must be a number"});
                getModel().resetDisplayContents();

            }

        }
    }

    private class RunStepListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            StepExecutionThread runStepThread = new StepExecutionThread();
            runStepThread.start();
        }
    }
    private class RunButtonActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            ExecutionThread runThread = new ExecutionThread();
            runThread.start();
        }
    }

    private class ChangeSpeedListener implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            if (event.getSource() == increaseSpeed)
            {
                int currentSpeed = getModel().getExecutingSpeed();
                getModel().setExecutingSpeed(currentSpeed < 0 ? 0 : (currentSpeed - 500));
            }else if (event.getSource() == decreaseSpeed)
            {
                int currentSpeed = getModel().getExecutingSpeed();
                getModel().setExecutingSpeed(currentSpeed + 500);
            }else
                ; // do nothing
        }
    }

    private class ChangeRunningStatus implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            if (event.getSource() == stop)
                getModel().setPauseStatus(true);
            else if (event.getSource() == resume)
                getModel().setPauseStatus(false);
            else
                ; //do nothing
        }
    }

    private class ExecutionThread extends Thread{
        @Override
        public void run()
        {
            //
            // execute obj file in another thread
            //
            boolean hitBreakPoint = false;
            if(getModel().getHaltStatus())
            {
                getModel().setDisplayContents(new String [] {"Program stopped"});
            }else
            {
                while(!getModel().getHaltStatus() && !hitBreakPoint)
                {
                    if(getModel().getPauseStatus())
                        ; // do nothing
                else if (getModel().getBreakPoint() == getModel().getPCValue())
                    {
                        getModel().runProgram();
                        getModel().setDisplayContents(new String [] {"Hit breakpoint at address " + getModel().getBreakPoint() });
                        hitBreakPoint = true;
                    }else
                        getModel().runProgram();

                    try{
                        Thread.sleep(getModel().getExecutingSpeed());
                    }catch(Exception e){}
                }
            }
        }
    }

    private class StepExecutionThread extends Thread{
        @Override
        public void run()
        {
            //
            // execute obj file in another thread
            //
            if(getModel().getHaltStatus())
            {
                getModel().setDisplayContents(new String [] {"Program stopped"});
            }else
            {
                getModel().runProgram();
            }
        }
    }
}

