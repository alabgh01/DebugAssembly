package vm252architecturespecifications;


import java.util.Scanner;
import java.lang.StringBuilder;

//import vm252utilities.*;


public class VM252ArchitectureSpecifications
{

    //
    // Public Class Constants
    //

        public static final int LOAD_OPCODE = 0;
        public static final int STORE_OPCODE = 1;
        public static final int ADD_OPCODE = 2;
        public static final int SUBTRACT_OPCODE = 3;
        public static final int JUMP_OPCODE = 4;
        public static final int JUMP_ON_ZERO_OPCODE = 5;
        public static final int JUMP_ON_POSITIVE_OPCODE = 6;
        public static final int SET_OPCODE = 14;
        public static final int INPUT_OPCODE = 60;
        public static final int OUTPUT_OPCODE = 61;
        public static final int NO_OP_OPCODE = 62;
        public static final int STOP_OPCODE = 63;

        public static final int numberOfMemoryBytes = 8192;

    //
    // Private Class Methods
    //

        //
        // Private Class Method
        //     short bytesToInteger(byte mostSignificantByte, byte leastSignificantByte)
        //
        // Purpose:
        //     Combines two bytes into a 16-bit signed integer value
        //
        // Formals:
        //     mostSignificantByte (in) - the byte containing the sign bit and
        //         most-significant 7 bits of the integer value
        //     leastSignificantByte (in) - the byte containing the least-significant 8 bits
        //         of the integer value
        //
        // Pre-conditions:
        //     none
        //
        // Post-conditions:
        //     none
        //
        // Returns:
        //     a 16-bit short corresponding to the concatenation of the two bytes
        //
        // Worst-case asymptotic runtime:
        //     O(1)
        //

        private static short bytesToInteger(
            byte mostSignificantByte,
            byte leastSignificantByte
            )
        {

            return
                ((short)
                    ((mostSignificantByte << 8 & 0xff00 | leastSignificantByte & 0xff)));

            }

        //
        // Private Class Method byte [] integerToBytes(short data)
        //
        // Purpose:
        //     Splits a 16-bit signed integer value into two bytes
        //
        // Formals:
        //     data (in) - the 16-bit signed integer value to be split
        //
        // Pre-conditions:
        //     none
        //
        // Post-conditions:
        //     none
        //
        // Returns:
        //     an array of bytes, with element 0 containing the sign bit and
        //         most-significant 7 bits of the integer value and element 1 containing
        //         the least-significant 8 bits of the integer value
        //
        //
        // Worst-case asymptotic runtime:
        //     O(1)
        //

        private static byte [] integerToBytes(short data)
        {

            byte [] dataBytes
                = {((byte) (data >> 8 & 0xff)),
                    ((byte) (data & 0xff))
                    };

            return dataBytes;

            }

        // Private Class Method short nextMemoryAddress(short address)
        //
        // Purpose:
        //     Determines the address of the next valid memory location following a given
        //     memory address
        //
        // Formals:
        //     address (in) - the address from which to determine the next valid address
        //
        // Pre-conditions:
        //     none
        //
        // Post-conditions:
        //     none
        //
        // Returns:
        //     the next valid memory address following address
        //
        // Worst-case asymptotic runtime:
        //     O(1)
        //

        private static short nextMemoryAddress(short address)
        {

            return ((short) ((address + 1) % numberOfMemoryBytes));

            }

        //
        // Private Class Method byte [] fetchBytePair(byte [] memory, short address)
        //
        // Purpose:
        //     Fetches a pair of sequential bytes from a simulated VM2525 memory
        //
        // Formals:
        //     memory (in) - the array of bytes representing the simulated VM2525 memory
        //     address (in) - the address from which to fetch the two sequential bytes
        //
        // Pre-conditions:
        //     memory is non-null
        //     0 <= address < numberOfMemoryBytes
        //
        // Post-conditions:
        //     none
        //
        // Returns:
        //     an array of two bytes corresponding to the two bytes at address
        //         address
        //
        // Worst-case asymptotic runtime:
        //     O(1)
        //

        public static byte [] fetchBytePair(byte [] memory, short address)
        {

            byte [] bytePair = { memory[ address ], memory[ nextMemoryAddress(address) ] };

            return bytePair;

            }

        //
        // Private Class Method
        //     void storeBytePair(
        //         byte [] memory,
        //         short address,
        //         byte byte0,
        //         byte byte1
        //         )
        //
        // Purpose:
        //     Stores a pair of bytes into two sequential bytes in a simulated VM2525 memory
        //
        // Formals:
        //     memory (in-out) - the array of bytes representing the simulated VM2525 memory
        //     address (in) - the address to which to store the two sequential bytes
        //     byte0 (in) - the first byte to store to memory
        //     byte1 (in) - the second byte to store
        //
        // Pre-conditions:
        //     memory is non-null
        //     0 <= address < numberOfMemoryBytes
        //
        // Post-conditions:
        //     memory[ address ] == byte0
        //     memory[ next memory address after address ] == byte1
        //
        // Returns:
        //     an array of two bytes corresponding to the two bytes at memory address
        //         address
        //
        // Worst-case asymptotic runtime:
        //     O(1)
        //

        private static void storeBytePair(
            byte [] memory,
            short address,
            byte byte0,
            byte byte1
            )
        {

            memory[ address ] = byte0;
            memory[ nextMemoryAddress(address) ] = byte1;

            }

        //
        // Public Class Method
        //     short fetchIntegerValue(byte [] memory, short address)
        //
        // Purpose:
        //     Fetches a 16-bit signed integer from a simulated VM2525 memory
        //
        // Formals:
        //     memory (in) - the array of bytes representing the simulated VM2525 memory
        //     address (in) - the address from which to fetch the two sequential bytes
        //         collectively holding the 16-bit integer
        //
        // Pre-conditions:
        //     memory is non-null
        //     0 <= address < numberOfMemoryBytes
        //
        // Post-conditions:
        //     none
        //
        // Returns:
        //     a 16-bit short corresponding to the concatenation of the two bytes at address
        //         address
        //
        // Worst-case asymptotic runtime:
        //     O(1)
        //

        public static short fetchIntegerValue(byte [] memory, short address)
        {

            byte [] integerBytes = fetchBytePair(memory, address);

            return bytesToInteger(integerBytes[ 0 ], integerBytes[ 1 ]);

            }

        //
        // Public Class Method
        //     void storeIntegerValue(byte [] memory, short address, short dataValue)
        //
        // Purpose:
        //     Stores a 16-bit signed integer to a simulated VM2525 memory
        //
        // Formals:
        //     memory (in-out) - the array of bytes representing the simulated VM2525 memory
        //     address (in) - the address to which to store the two sequential bytes
        //         collectively holding the 16-bit integer
        //     statValue (in) - the 16-bit integer to store
        //
        // Pre-conditions:
        //     memory is non-null
        //     0 <= address < numberOfMemoryBytes
        //
        // Post-conditions:
        //     memory[ address ] = the sign bit and most-significant 7 bits of
        //         dataValue and element 1 containing
        //     memory[ next memory address after address ] = the least-significant
        //         8 bits of dataValue
        //
        // Returns:
        //     none
        //
        // Worst-case asymptotic runtime:
        //     O(1)
        //

        public static byte[] storeIntegerValue(
            byte [] memory,
            short address,
            short dataValue
            )
        {

            byte [] dataBytes = integerToBytes(dataValue);

            memory[ address ] = dataBytes[ 0 ];
            memory[ nextMemoryAddress(address) ] = dataBytes[ 1 ];

            return memory;

            }

        //
        // Public Class Method int instructionSize(int opcode)
        //
        // Purpose:
        //     Gives the size (in bytes) of an instruction having a given opcode
        //
        // Formals:
        //     opcode (in) - the opcode of the instruction whose size is to be determined
        //
        // Pre-conditions:
        //     none
        //
        // Post-conditions:
        //     none
        //
        // Returns:
        //     2, if opcode corresponds to an instruction whose encoding requires 2 bytes
        //     1, otherwise
        //
        // Worst-case asymptotic runtime:
        //     O(1)
        //

        public static int instructionSize(int opcode)
        {

            switch (opcode) {

                case LOAD_OPCODE,
                    SET_OPCODE,
                    STORE_OPCODE,
                    ADD_OPCODE,
                    SUBTRACT_OPCODE,
                    JUMP_OPCODE,
                    JUMP_ON_ZERO_OPCODE,
                    JUMP_ON_POSITIVE_OPCODE -> {

                    return 2;

                    }

                default -> {

                    return 1;

                    }

                }

            }

    //
    // Public Class Methods
    //

        //
        // Public Class Method byte [] encodedInstructionBytes(int opcode, int operand)
        //
        // Purpose:
        //     Creates the binary encoding of a VM252 instruction
        //
        // Formals:
        //     opcode (in) - the integer operation code for the instruction to be encoded
        //     operand (in) - the integer operand for the instruction to be encoded;
        //         will be ignored for certain opcodes
        //
        // Pre-conditions:
        //     none
        //
        // Post-conditions:
        //     none
        //
        // Returns:
        //     a byte-array of 1 or two elements, where element 0 contains the
        //         operation code and (for opcodes that require an operand) most-significant
        //         bits of the operand, and element 1 (for opcodes that require an operand)
        //         contains the least-significant bits of the operand
        //
        // Worst-case asymptotic runtime:
        //     O(1)
        //

        public static byte [] encodedInstructionBytes(int opcode, int operand)
        {

            byte [] instruction;

            switch (opcode) {

                //
                // Cases for instructions encoded with a 3-bit opcode and a 13-bit unsigned
                // operand
                //

                    case LOAD_OPCODE :
                    case STORE_OPCODE :
                    case ADD_OPCODE :
                    case SUBTRACT_OPCODE :
                    case JUMP_OPCODE :
                    case JUMP_ON_ZERO_OPCODE :
                    case JUMP_ON_POSITIVE_OPCODE : {

                        instruction = new byte[ 2 ];

                        //
                        // Let instruction[ 0 ] = the 3-bit opcode and the 5 most-significant
                        //     bits of the operand
                        // Let instruction[ 1 ] = the 8 least-significant bits of the operand
                        //

                            instruction[ 0 ] = ((byte) (opcode << 5 | operand >> 8 & 0x1f));
                            instruction[ 1 ] = ((byte) (operand & 0xff));

                        break;

                        }

                //
                // Cases for instructions encoded with a 4-bit opcode and a 12-bit signed
                // operand
                //

                    case SET_OPCODE : {

                        instruction = new byte[ 2 ];

                        //
                        // Let instruction[ 0 ] = the 4-bit opcode and the 5 most-significant
                        //     bits of the operand
                        // Let instruction[ 1 ] = the 8 least-significant bits of the operand
                        //

                            instruction[ 0 ] = ((byte) (opcode << 4 | operand >> 8 & 0xf));
                            instruction[ 1 ] = ((byte) (operand & 0xff));

                        break;

                        }

                //
                // Cases for instructions encoded with a 6-bit opcode and no operand
                //

                    case INPUT_OPCODE :
                    case OUTPUT_OPCODE :
                    case NO_OP_OPCODE :
                    case STOP_OPCODE : {

                        instruction = new byte[ 1 ];

                        //
                        // Let instruction[ 0 ] = the 6-bit opcode and 2 0 bits
                        //

                            instruction[ 0 ] = ((byte) (opcode << 2 & 0xff));

                        break;

                        }

                //
                // Case for invalid opcode
                //

                    default :

                        instruction = null;

                }

            return instruction;

            }

        //
        // Public Class Method int [] decodedInstructionComponents(byte [] instructionBytes)
        //
        // Purpose:
        //     Decodes the binary encoding of a VM252 instruction
        //
        // Formals:
        //     instructionBytes (in) - the 1- or 2-byte binary encoding
        //         of a VM252 instruction
        //
        // Pre-conditions:
        //     none
        //
        // Post-conditions:
        //     none
        //
        // Returns:
        //     an int-array of 1 or two elements, where element 0 contains the
        //         operation code, and element 1 (for opcodes that require an operand)
        //         contains the operand
        //
        // Worst-case asymptotic runtime:
        //     O(1)
        //

        public static int [] decodedInstructionComponents(byte [] instructionBytes)
        {

            //
            // If instruction bytes is not a valid binary encoding of a VM252 instruction,
            //     return a null array
            //

                if (instructionBytes == null || instructionBytes.length > 2)

                    return null;

            //
            // Otherwise, return an array holding the opcode and (for some opcodes) operand
            //    from the encoded instruction
            //

                else {

                    int [] instructionComponents = null;

                    switch (instructionBytes[ 0 ] >> 5 & 0x7) {

                        //
                        // If the 3 most-significant bits of the byte holding the encoded
                        // opcode denote an instruction having a 3-bit opcode and a 13-bit
                        // unsigned integer operand, return an array holding those two values
                        //

                            case LOAD_OPCODE :
                            case STORE_OPCODE :
                            case ADD_OPCODE :
                            case SUBTRACT_OPCODE :
                            case JUMP_OPCODE :
                            case JUMP_ON_ZERO_OPCODE :
                            case JUMP_ON_POSITIVE_OPCODE : {

                                if (instructionBytes.length == 2) {

                                    instructionComponents = new int [ 2 ];

                                    //
                                    // Let instructionComponents[ 0 ]
                                    //     = the 3 most-significant bits of
                                    //         instructionBytes[ 0 ]
                                    // Let instructionComponents[ 1 ]
                                    //     = the 5 least-significant bits of
                                    //         instructionBytes[ 0 ] concatenated with
                                    //         instructionBytes[ 1 ]


                                        instructionComponents[ 0 ]
                                            = instructionBytes[ 0 ] >> 5 & 0x7;
                                        instructionComponents[ 1 ]
                                            = instructionBytes[ 0 ] << 8 & 0x1f00
                                                | instructionBytes[ 1 ] & 0xff;

                                    }

                                break;

                                }

                        default :

                            switch (instructionBytes[ 0 ] >> 4 & 0xf) {

                                //
                                // If the 4 most-significant bits of the byte holding the
                                // encoded opcode denote an instruction having a 4-bit opcode
                                // and a 12-bit signed integer operand, return an array
                                // holding those two values
                                //

                                    case SET_OPCODE : {

                                        if (instructionBytes.length == 2) {

                                            instructionComponents = new int [ 2 ];

                                            //
                                            // Let instructionComponents[ 0 ]
                                            //     = the 4 most-significant bits of
                                            //         instructionBytes[ 0 ]
                                            // Let instructionComponents[ 1 ]
                                            //     = the 4 least-significant bits of
                                            //         instructionBytes[ 0 ] concatenated
                                            //         with instructionBytes[ 1 ]


                                                instructionComponents[ 0 ]
                                                    = instructionBytes[ 0 ] >> 4 & 0xf;
                                                instructionComponents[ 1 ]
                                                    = instructionBytes[ 0 ] << 28 >> 20
                                                        | instructionBytes[ 1 ] & 0xff;

                                            }

                                        break;

                                    }

                                //
                                // Otherwise, the 6 most-significant bits of the byte holding
                                // the encoded necessarily opcode denote an instruction
                                // having a 6-bit opcode and no operand, so return an array
                                // holding the opcode only
                                //

                                    default : {

                                        instructionComponents = new int [ 1 ];

                                        instructionComponents[ 0 ]
                                            = instructionBytes[ 0 ] >> 2 & 0x3f;

                                        }

                                }

                        }

                return instructionComponents;

                }

            }

        //
        // Public Class Method void runProgram(byte [] program)
        //
        // Purpose:
        //     Simulates the execution of a VM252 program whose binary encoding is found in
        //     an array of bytes
        //
        // Formals:
        //     program (in) - the array of bytes containing the binary encoding of the
        //         VM252 program whose execution is to be simulated
        //
        // Pre-conditions:
        //     none
        //
        // Post-conditions:
        //     input may have been read from the standard input stream
        //     output may have been written to the standard output stream
        //
        // Returns:
        //     an array of bytes, with element 0 containing the sign bit and
        //         most-significant 7 bits of the integer value and element 1 containing
        //         the least-significant 8 bits of the integer value
        //
        //
        // Worst-case asymptotic runtime:
        //     O(1
        //         + max(
        //             (the # number of simulated VM252 instruction executions),
        //             (the number of invalid user-inputs entered)
        //             )
        //         )
        //
        public static byte[] addProgramToMemory(byte[] memory, byte[] program)
        {
                //
                // Let memory[ 0 ... numberOfMemoryBytes-1 ] =
                //     the bytes of the program whose execution is to be simulated, followed,
                //     to the end of memory, 0-initialized bytes
                //

                    for (int loadAddress = 0; loadAddress < program.length; ++ loadAddress)
                            //
                            // Loop invariant:
                            //     memory[ 0 ... loadAddres-1 ] has been assignment
                            //         program[ 0 ... loadAddress-1 ]
                            //
                        memory[ loadAddress ] = program[ loadAddress ];

                    for (int loadAddress = program.length;
                            loadAddress < numberOfMemoryBytes;
                            ++ loadAddress)
                            //
                            // Loop invariant:
                            //     Each byte in
                            //         memory[ program.length ... numberOfMemoryBytes-1 ] has
                            //         been assigned 0
                            //
                        memory[ loadAddress ] = 0;
                    return memory;
        }

        public static String humanReadableInstructions(byte[] memory)
        {
            String instructionString = "";
            short programCounter = (short) 0;
            boolean endOfCode = false;
            boolean supressStatus = false;

            while(!endOfCode)
            {
                byte [] encodedInstruction
                    = fetchBytePair(memory, programCounter);

                int [] decodedInstruction
                    = decodedInstructionComponents(encodedInstruction);
                int opcode = decodedInstruction[ 0 ];

                short operand
                    = decodedInstruction.length == 2
                        ? ((short) (decodedInstruction[ 1 ]))
                        : 0;

                supressStatus = false;
                switch (opcode) {

                    case LOAD_OPCODE -> {
                        instructionString = instructionString + "[Addr " + programCounter + "] " + "LOAD" + operand + "\n";
                        }

                    case SET_OPCODE -> {
                        instructionString = instructionString + "[Addr " + programCounter + "] " + "SET" + operand + "\n";
                        }

                    case STORE_OPCODE -> {
                        instructionString = instructionString + "[Addr " + programCounter + "] " + "STORE" + operand + "\n";
                        }

                    case ADD_OPCODE -> {
                        instructionString = instructionString + "[Addr " + programCounter + "] " + "Add" + operand + "\n";
                        }

                    case SUBTRACT_OPCODE -> {
                        instructionString = instructionString + "[Addr " + programCounter + "] " + "Subtract" + operand + "\n";
                        }

                    case JUMP_OPCODE -> {
                        instructionString = instructionString + "[Addr " + programCounter + "] " + "Jump" + operand + "\n";
                        }

                    case JUMP_ON_ZERO_OPCODE -> {
                        instructionString = instructionString + "[Addr " + programCounter + "] " + "Jumpz" + operand + "\n";
                        }

                    case JUMP_ON_POSITIVE_OPCODE -> {
                        instructionString = instructionString + "[Addr " + programCounter + "] " + "Jumpp" + operand + "\n";
                        }

                    case INPUT_OPCODE -> {
                        instructionString = instructionString + "[Addr " + programCounter + "] " + "Input" + "\n";
                        }

                    case OUTPUT_OPCODE -> {
                        instructionString = instructionString + "[Addr " + programCounter + "] " + "Output" + "\n";
                        }

                    case NO_OP_OPCODE -> {
                        instructionString = instructionString + "[Addr " + programCounter + "] " + "Noop" + "\n";
                        }

                    case STOP_OPCODE -> {
                        instructionString = instructionString + "[Addr " + programCounter + "] " + "Stop" + "\n";
                        endOfCode = true;
                        }

                    }
                if (!supressStatus)
                {
                    programCounter =
                        (short)
                            ((programCounter + instructionSize(opcode))
                                % numberOfMemoryBytes);
                }

            }
            return instructionString;
        }

        public static String instructionToString(byte[] memory, short programCounter)
        {
            String instructionString = "";
            byte [] encodedInstruction
                = fetchBytePair(memory, programCounter);

            int [] decodedInstruction
                = decodedInstructionComponents(encodedInstruction);
            int opcode = decodedInstruction[ 0 ];

            short operand
                = decodedInstruction.length == 2
                    ? ((short) (decodedInstruction[ 1 ]))
                    : 0;
            switch (opcode) {

                case LOAD_OPCODE -> {
                    instructionString = "LOAD" + operand;
                    }

                case SET_OPCODE -> {
                    instructionString = "SET" + operand;
                    }

                case STORE_OPCODE -> {
                    instructionString = "STORE" + operand;
                    }

                case ADD_OPCODE -> {
                    instructionString = "Add" + operand;
                    }

                case SUBTRACT_OPCODE -> {
                    instructionString = "Subtract" + operand;
                    }

                case JUMP_OPCODE -> {
                    instructionString = "Jump" + operand;
                    }

                case JUMP_ON_ZERO_OPCODE -> {
                    instructionString = "Jumpz" + operand;
                    }

                case JUMP_ON_POSITIVE_OPCODE -> {
                    instructionString = "Jumpp" + operand;
                    }

                case INPUT_OPCODE -> {
                    instructionString = "Input";
                    }

                case OUTPUT_OPCODE -> {
                    instructionString = "Output";
                    }

                case NO_OP_OPCODE -> {
                    instructionString = "Noop";
                    }

                case STOP_OPCODE -> {
                    instructionString = "Stop";
                    }

                }
            return instructionString;

        }

    }
