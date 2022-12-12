package generic;
import java.io.*;
import java.io.FileInputStream;
import generic.Operand.OperandType;
import java.io.FileOutputStream;


public class Simulator {
		
	static FileInputStream inputcodeStream = null;

	public static void setupSimulation(String assemblyProgramFile)
	{	
		int firstCodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, firstCodeAddress);
		ParsedProgram.printState();
	}
 	
	public static String convert5(String num){
		String temp1 = num; 
		for(int i = 5; i > num.length(); i--){
			String temp2 = "0";
			String temp3 = temp2.concat(temp1);
			temp1 = temp3;
		}
		return temp1;
	}
	public static String convert17(String num){
		String temp1 = num; 
		for(int i = 17; i > num.length(); i--){
			String temp2 = "0";
			String temp3 = temp2.concat(temp1);
			temp1 = temp3;
		}
		return temp1;
	}
	public static String convert22(String num){
		String temp1 = num; 
		for(int i = 22; i > num.length(); i--){
			String temp2 = "0";
			String temp3 = temp2.concat(temp1);
			temp1 = temp3;
		}
		return temp1;
	}

	public static String fill8(String num){
		String temp1 = num; 
		for(int i = 8; i > num.length(); i--){
			String temp2 = "0";
			String temp3 = temp2.concat(temp1);
			temp1 = temp3;
		}
		return temp1;
	}
	
	public static void assemble(String objectProgramFile)
	{
		//TODO your assembler code
		//1. open the objectProgramFile in binary mode
		try
		{	
			FileOutputStream myWriter1 = new FileOutputStream(objectProgramFile);
			DataOutputStream myWriter = new DataOutputStream(myWriter1);
			
			int data = ParsedProgram.firstCodeAddress;//----------------firstCodeAddress
			myWriter.writeInt(data);
			String bin;
			for(int i=0; i<ParsedProgram.data.size();i++){//------------------------------data
				data = ParsedProgram.data.get(i);
				myWriter.writeInt(data);
			}

			for(int i = 0; i <ParsedProgram.code.size(); i++) {
				String str = ParsedProgram.code.get(i).operationType.name();//------------opcode
				data = Instruction.OperationType.valueOf(str).ordinal();
				bin = Integer.toBinaryString(data);
				bin = convert5(bin);//---------------------------------converting to 5 digit bin
				
				String finalString = bin;
			
					if(data == 29)
					{	
						// //System.out.println(finalString);
						bin = "000000000000000000000000000";
						finalString = finalString.concat(bin);
						// //System.out.println("This is end Final string: " + finalString);
						myWriter.writeInt(Integer.parseUnsignedInt(finalString, 2));
						continue;	
					}
					else if(data == 24)
					{
						int destOp    = ParsedProgram.code.get(i).getDestinationOperand().getOperandType().ordinal();
						if(destOp == 2)
						{
							//For only label
							String t  = ParsedProgram.code.get(i).getDestinationOperand().getLabelValue();
							int rd = ParsedProgram.symtab.get(t);
							rd = rd -i -1;
							bin = Integer.toBinaryString(rd);
							if(rd>=0){
								if(bin.length()!=32){
									bin = convert22(bin);
								}
								if(bin.length()==32){
									bin = bin.substring(10, bin.length()-1);
								}
							}
							else{
								if(bin.length()==32){
									bin = bin.substring(10, bin.length());
								}
							}

							//-------------------------label
							finalString = finalString.concat("00000");
							finalString = finalString.concat(bin);
							myWriter.writeInt(Integer.parseUnsignedInt(finalString, 2));
							continue;
						}
						if(destOp == 1)
						{
							//For only imm
							int rd = ParsedProgram.code.get(i).getDestinationOperand().getValue();
							bin = Integer.toBinaryString(rd);//-------------------------label
							bin = convert22(bin);
							finalString = finalString.concat("00000");
							finalString = finalString.concat(bin);
							myWriter.writeInt(Integer.parseUnsignedInt(finalString, 2));
							continue;
						}
					}
					int sourceOp1 = ParsedProgram.code.get(i).getSourceOperand1().getOperandType().ordinal();
					int sourceOp2 = ParsedProgram.code.get(i).getSourceOperand2().getOperandType().ordinal();
					int destOp    = ParsedProgram.code.get(i).getDestinationOperand().getOperandType().ordinal();
					if((sourceOp1 + sourceOp2 + destOp)==0)//-----------------------R3-TYPE
					{
						int rs1 = ParsedProgram.code.get(i).getSourceOperand1().getValue();
						int rs2 = ParsedProgram.code.get(i).getSourceOperand2().getValue();
						int rd = ParsedProgram.code.get(i).getDestinationOperand().getValue();

						bin = Integer.toBinaryString(rs1);//-------------------------rs1
						bin = convert5(bin);
						finalString = finalString.concat(bin);

						bin = Integer.toBinaryString(rs2);//-------------------------rs2
						bin = convert5(bin);
						finalString = finalString.concat(bin);

						bin = Integer.toBinaryString(rd);//-------------------------rd
						bin = convert5(bin);
						finalString = finalString.concat(bin);
						bin = "000000000000";
						finalString = finalString.concat(bin);

						myWriter.writeInt(Integer.parseUnsignedInt(finalString, 2));
					}

					else if ((sourceOp1==0) && (sourceOp2==1) && (destOp==0))//-------R2I-TYPE
					{
						int rs1 = ParsedProgram.code.get(i).getSourceOperand1().getValue();
						int rs2 = ParsedProgram.code.get(i).getSourceOperand2().getValue();
						int rd = ParsedProgram.code.get(i).getDestinationOperand().getValue();

						bin = Integer.toBinaryString(rs1);//-------------------------rs1
						bin = convert5(bin);
						finalString = finalString.concat(bin);
						
						bin = Integer.toBinaryString(rd);//-------------------------rd
						bin = convert5(bin);
						finalString = finalString.concat(bin);
						
						bin = Integer.toBinaryString(rs2);//-------------------------imm
						bin = convert17(bin);
						finalString = finalString.concat(bin);
						myWriter.writeInt(Integer.parseUnsignedInt(finalString, 2));
						
					}

					else if ((sourceOp1==0) && (sourceOp2==2) && (destOp==0))//-------R2I-TYPE for load
					{
						int rs1 = ParsedProgram.code.get(i).getSourceOperand1().getValue();
						String t= ParsedProgram.code.get(i).getSourceOperand2().getLabelValue();
						int rd  = ParsedProgram.code.get(i).getDestinationOperand().getValue();

						int rs2 = ParsedProgram.symtab.get(t);//---------------------label conversion

						bin = Integer.toBinaryString(rs1);//-------------------------rs1
						bin = convert5(bin);
						finalString = finalString.concat(bin);
						
						bin = Integer.toBinaryString(rd);//-------------------------rd
						bin = convert5(bin);
						finalString = finalString.concat(bin);
						
						bin = Integer.toBinaryString(rs2);//-------------------------label
						bin = convert17(bin);
						finalString = finalString.concat(bin);

						myWriter.writeInt(Integer.parseUnsignedInt(finalString, 2));
					}

					else if ((sourceOp1==0) && (sourceOp2==0) && (destOp==2))//-------R2I-TYPE for beq example
					{
						int rs1 = ParsedProgram.code.get(i).getSourceOperand1().getValue();
						int rs2 = ParsedProgram.code.get(i).getSourceOperand2().getValue();
						String t  = ParsedProgram.code.get(i).getDestinationOperand().getLabelValue();
						int rd = ParsedProgram.symtab.get(t);//---------------------label conversion
						
						bin = Integer.toBinaryString(rs1);//-------------------------rs1
						bin = convert5(bin);
						finalString = finalString.concat(bin);

						bin = Integer.toBinaryString(rs2);//-------------------------rs2
						bin = convert5(bin);
						finalString = finalString.concat(bin);

						rd = rd -i-1;
						if(rd>=0){
							bin = Integer.toBinaryString(rd);//-------------------------label
							if(bin.length()!=32){
								bin = convert17(bin);

							}
							if(bin.length()==32){
								bin = bin.substring(15, bin.length()-1);
							}
						}
						else{
							bin = Integer.toBinaryString(rd);//-------------------------label

							if(bin.length()==32){
								bin = bin.substring(15, bin.length());
							}
						}

						finalString = finalString.concat(bin);
						myWriter.writeInt(Integer.parseUnsignedInt(finalString, 2));
					}
					
				
			}

			myWriter.close();
		}
		catch(IOException ex){
			//System.out.println("An error occurred.");
			ex.printStackTrace();
		}

	}	
}
