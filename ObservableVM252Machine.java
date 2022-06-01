import vm252architecturespecifications.VM252ArchitectureSpecifications;

class ObservableVM252Machine extends SimpleObservable
{
    private short myACC;
    private short myPC;
    private byte [] myMemory;
    private boolean suppressPcIncrement;
    private boolean lastInstructionCausedHalt;
    private String [] myDisplayContents;
    private int myExecutingSpeed;
    private boolean myPauseStatus;
    private short myBreakPoint;
    private short myInput;
    private boolean inputReady = false;
    private String nextInst;
    private String humanReadableInstructions;

    //
    // Accessors
    //
    public String getNextInst()
    {
        return nextInst;
    }

    public String getHumanReadableInstructions()
    {
        return humanReadableInstructions;
    }

    public short getACCValue()
    {
        return myACC;
    }

    public short getPCValue()
    {
        return myPC;
    }

    public short getInputValue()
    {
        return myInput;
    }


    public byte [] getMemoryValue()
    {
        return myMemory;
    }

    public String [] getDisplayContents()
    {
        return myDisplayContents;
    }

    private boolean getSuppressPcStatus()
    {
        return suppressPcIncrement;
    }

    public boolean getHaltStatus()
    {
        return lastInstructionCausedHalt;
    }

    public int getExecutingSpeed()
    {
        return myExecutingSpeed;
    }

    public boolean getPauseStatus()
    {
        return myPauseStatus;
    }

    public short getBreakPoint()
    {
        return myBreakPoint;
    }

    public boolean getInputReady()
    {
        return inputReady;
    }

    //
    // Mutators
    //

    public void setNextInst(String other)
    {
        nextInst = other;
        announceChange();
    }

    public void setACCValue(short other)
    {
        myACC = other;
        announceChange();
    }

    public void setPCValue(short other)
    {
        myPC = other;
        announceChange();
    }

    public void setInputValue(short other)
    {
        myInput = other;
        announceChange();
    }

    public void setMemoryValue(byte [] other)
    {
        myMemory = other;
        announceChange();
    }

    public void setDisplayContents(String [] other)
    {
        myDisplayContents = other;
        announceChange();
    }

    public void resetDisplayContents()
    {
        String [] contents = {""};
        myDisplayContents = contents;
    }

    private void setSuppressPcStatus(boolean other)
    {
        suppressPcIncrement = other;
    }

    public void setHalt(boolean other)
    {
       lastInstructionCausedHalt = other;
    }

    public void setExecutingSpeed(int other)
    {
        myExecutingSpeed = other;
    }

    public void setPauseStatus(boolean other)
    {
        myPauseStatus = other;
    }

    public void setBreakPoint(short other)
    {
        myBreakPoint = other;
    }

    public void setInputReady(boolean other)
    {
        inputReady = other;
    }

    private void setHumanReadableInstructions(String other)
    {
        humanReadableInstructions = other;
    }

    //
    // Ctors
    //

    ObservableVM252Machine()
    {
        super();

        String [] welcomeContents = {"Welcome to VM252 debugger"};

        setACCValue((short)0);
        setPCValue((short)0);
        setMemoryValue(new byte [8192]);
        setDisplayContents(welcomeContents);
        setExecutingSpeed(500);
        setPauseStatus(false);
        setBreakPoint((short)8192);
        setNextInst(VM252ArchitectureSpecifications.instructionToString(getMemoryValue(), getPCValue()));
        setHumanReadableInstructions(VM252ArchitectureSpecifications.humanReadableInstructions(getMemoryValue()));
    }

    ObservableVM252Machine(byte [] programEncoded)
    {
        super();

        byte [] memory = new byte[ 8192 ];
        String [] welcomeContents = {""};

        setSuppressPcStatus(false);
        setHalt(false);
        setACCValue((short)0);
        setPCValue((short) 0);
        setMemoryValue(VM252ArchitectureSpecifications.addProgramToMemory(memory, programEncoded));
        setDisplayContents(welcomeContents);
        setExecutingSpeed(500);
        setPauseStatus(false);
        setBreakPoint((short)8192);
        setNextInst(VM252ArchitectureSpecifications.instructionToString(getMemoryValue(), getPCValue()));
        setHumanReadableInstructions(VM252ArchitectureSpecifications.humanReadableInstructions(getMemoryValue()));
    }

    public void runProgram()
    {
        // Scanner input = new Scanner(System.in);

        //
        // Let opcode = the operation code portion of the instruction stored
        //     at address programCounter
        // Let operand = the operand portion (if any) of the instruction
        //     stored at address programCounter
        //

        byte [] encodedInstruction
            = VM252ArchitectureSpecifications.fetchBytePair(getMemoryValue(), getPCValue());

        int [] decodedInstruction
            = VM252ArchitectureSpecifications.decodedInstructionComponents(encodedInstruction);
        int opcode = decodedInstruction[ 0 ];

        short operand
            = decodedInstruction.length == 2
                ? ((short) (decodedInstruction[ 1 ]))
                : 0;

        setSuppressPcStatus(false);

        //
        // Simulate execution of a VM252 instruction represented by opcode
        //     (and for instructions that have an operand, operand), altering
        //     accumulator, programCounter, and memory, as required
        // Let supressPcIncrement = true iff any kind of jump instruction was
        //     executed, a STOP instruction was executed, or a failed INPUT
        //     instruction was executed
        //

            switch (opcode) {

                case VM252ArchitectureSpecifications.LOAD_OPCODE -> {

                    resetDisplayContents();
                    setACCValue(VM252ArchitectureSpecifications.fetchIntegerValue(getMemoryValue(), operand));
                    setDisplayContents(new String [] {"Addr " + getPCValue() + ": " + "LOAD " + operand});

                    }

                case VM252ArchitectureSpecifications.SET_OPCODE -> {

                    resetDisplayContents();
                    setACCValue(operand);
                    setDisplayContents(new String [] {"Addr " + getPCValue() + ": " + "SET " + operand});

                    }

                case VM252ArchitectureSpecifications.STORE_OPCODE -> {

                    resetDisplayContents();
                    byte [] newMemory = VM252ArchitectureSpecifications.storeIntegerValue(getMemoryValue(), operand, getACCValue());
                    setMemoryValue(newMemory);
                    setDisplayContents(new String [] {"Addr " + getPCValue() + ": " + "STORE " + operand});

                    }

                case VM252ArchitectureSpecifications.ADD_OPCODE -> {

                    resetDisplayContents();
                    setACCValue((short)(getACCValue() + VM252ArchitectureSpecifications.fetchIntegerValue(getMemoryValue(), operand)));
                    setDisplayContents(new String [] {"Addr " + getPCValue() + ": " + "ADD " + operand});

                    }

                case VM252ArchitectureSpecifications.SUBTRACT_OPCODE -> {

                    resetDisplayContents();
                    setACCValue((short)(getACCValue() - VM252ArchitectureSpecifications.fetchIntegerValue(getMemoryValue(), operand)));
                    setDisplayContents(new String [] {"Addr " + getPCValue() + ": " + "SUBTRACT " + operand});

                    }

                case VM252ArchitectureSpecifications.JUMP_OPCODE -> {

                    setDisplayContents(new String [] {"Addr " + getPCValue() + ": " + "JUMP " + operand});
                    resetDisplayContents();
                    setPCValue(operand);
                    setSuppressPcStatus(true);

                    }

                case VM252ArchitectureSpecifications.JUMP_ON_ZERO_OPCODE -> {

                    resetDisplayContents();
                    setDisplayContents(new String [] {"Addr " + getPCValue() + ": " + "JUMPZ " + operand});
                    if (getACCValue() == 0) {
                        resetDisplayContents();
                        setPCValue(operand);
                        setSuppressPcStatus(true);
                        }
                    }

                case VM252ArchitectureSpecifications.JUMP_ON_POSITIVE_OPCODE -> {

                    resetDisplayContents();
                    setDisplayContents(new String [] {"Addr " + getPCValue() + ": " + "JUMPP " + operand});
                    resetDisplayContents();
                    if (getACCValue() > 0) {
                        setPCValue(operand);
                        setSuppressPcStatus(true);
                        }
                    }

                case VM252ArchitectureSpecifications.INPUT_OPCODE -> {

                        resetDisplayContents();

                        setDisplayContents(new String [] {"Addr " + getPCValue() + ": " + "Running INPUT"});
                        while (!getInputReady())
                            resetDisplayContents();

                        setACCValue(getInputValue());
                        setDisplayContents(new String[] {"Addr " + getPCValue() + ": " + "Set Input value to " + getInputValue()});

                        setInputReady(false);

                    }

                case VM252ArchitectureSpecifications.OUTPUT_OPCODE -> {

                    String [] output = {"Addr " + getPCValue() + ": " + "OUTPUT: " + getACCValue()};
                    setDisplayContents(output);

                    }

                case VM252ArchitectureSpecifications.NO_OP_OPCODE -> {

                    ; // do nothing

                    }

                case VM252ArchitectureSpecifications.STOP_OPCODE -> {

                    setHalt(true);
                    String [] stopMessage = {"Addr " + getPCValue() + ": " + "Program Stops"};
                    setDisplayContents(stopMessage);
                    resetDisplayContents();
                    }

                }

            //
            // Increment the program counter to contain the address of the next
            //     instruction to execute, unless the program counter was already
            //     adjusted or the program is not continuing
            //

            if (! getHaltStatus() && ! getSuppressPcStatus())
            {

                resetDisplayContents();
                setPCValue(
                    (short)
                        ((getPCValue() + VM252ArchitectureSpecifications.instructionSize(opcode))
                            % VM252ArchitectureSpecifications.numberOfMemoryBytes)
                        );
            }
            if (!getHaltStatus())
                setNextInst(VM252ArchitectureSpecifications.instructionToString(getMemoryValue(), getPCValue()));
        }

}
