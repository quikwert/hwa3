import java.util.*;
import java.util.function.Supplier;

/** Stack manipulation.
 * @since 1.8
 */
public class DoubleStack {
   private DoubleStackElement stackPointer; 



	public static void main (String[] argum) throws Exception{
	   DoubleStack stack1 = new DoubleStack();
	   stack1.push(1);
	   stack1.push(2);
	   stack1.push(3);
	   stack1.push(4);
	   stack1.push(5);

	   DoubleStack stack2 = (DoubleStack)stack1.clone();
	   stack2.push(2);
	
	   System.out.println(DoubleStack.interpret("3 2 *      3 +  \t\t \t 4 /"));
	   System.out.println(DoubleStack.interpret("3543"));
	   
	   System.out.println(DoubleStack.interpret("1. -10. 4. 8. 3. - + * +"));

	   System.out.println( stack1.toString() );
	   System.out.println( stack2.toString() );

	   System.out.print(stack1.equals(stack2));
	}

	DoubleStack() {
	   stackPointer = null;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		if(this.stackPointer == null){
			return new Stack();
		}

		DoubleStackElement origTop = this.stackPointer;
		DoubleStackElement newTop = new DoubleStackElement(origTop.value, null);

		DoubleStackElement currentOrig = origTop.prevElement;
		DoubleStackElement currentNew  = newTop;

		while(currentOrig != null) {
			DoubleStackElement newNode = new DoubleStackElement(currentOrig.value, null);
			currentNew.prevElement = newNode;

			currentNew = newNode;
			currentOrig = currentOrig.prevElement;
		}
		DoubleStack copy = new DoubleStack();
		copy.stackPointer = newTop;

		return copy;
	}

		
	public boolean stEmpty() {
		if(stackPointer == null) return true;
		return false; 
	}

	public void push (double a) {
		DoubleStackElement newElement;
		newElement = new DoubleStackElement(a, stackPointer);	
		stackPointer = newElement;
	}

	public double pop(){
		if(stackPointer == null){
			throw new RuntimeException();
		}
		double ret = stackPointer.value;
		stackPointer = stackPointer.prevElement;

		return ret;
	} // pop


	public double tos() {
		if(this.stackPointer == null)throw new RuntimeException("Stack is Empty");
		return stackPointer.value;
   		 
	}

	@Override
	public boolean equals (Object o) {
		DoubleStackElement el = stackPointer;
		DoubleStackElement obj = ((DoubleStack)o).stackPointer;	

		while (true){
			if( el == null && obj == null) return true; 
			if( el == null || obj == null) return false;
			if( el.value != obj.value) return false;
			el = el.prevElement;
			obj = obj.prevElement;

		}
	}

	@Override
	public String toString() {
		return _to_String(stackPointer,new StringBuffer()).toString();
	}
	private StringBuffer _to_String(DoubleStackElement elToPrint ,StringBuffer strBuf){
		if(elToPrint == null) return strBuf;	
		strBuf = _to_String(elToPrint.prevElement, strBuf);
		strBuf.append(elToPrint.value).append("\n");
		return strBuf;
	}

	public void op (String token){
		Operation operation = OperationRegistry.get(token);
		operation.execute(this);
	}
	public static double interpret (String pol){
		DoubleStack stack = new DoubleStack();
		String noDupSpaces = pol.replaceAll("\\s+", " ");
		String noTabs = noDupSpaces.replaceAll("\\t+", " ");
		String trim = noTabs.trim();
		String tokens[] = trim.split(" ");
		for(String token: tokens) {

			if(isNumber(token)){
				stack.push(Double.parseDouble(token));
			}
			else {
				stack.op(token);
			}

		}
		double ret = stack.pop();
		if(!stack.stEmpty()) throw new RuntimeException("Too many numbers");
		return ret;
	}

	private static boolean isNumber(String token){
		try{
			Double.parseDouble(token);
			return true;
		}catch (NumberFormatException e){
			return false;
		}	
	}

	private class DoubleStackElement{
		private DoubleStackElement prevElement;
		private double value;
		DoubleStackElement(double value, DoubleStackElement prevElement){
			this.value = value;
			this.prevElement = prevElement;		
		}

	}
}


interface Operation{
	public void execute (DoubleStack stack);
}

class OperationRegistry {
	private static final Map<String, Supplier<Operation>> operations =
	    Map.of(
		"+", AddOperation::new,	
		"-", SubtractOperation::new,	
		"*", MultiplyOperation::new,	
		"/", DivideOperation::new,	
		"swap", SwapOperation::new,	
		"dup", DupOperation::new,	
		"rot", RotOperation::new,	
		"drop", DropOperation::new	
	    );

    public static Operation get(String token) {
        Supplier<Operation> supplier = operations.get(token);
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown operation: " + token);
        }
        return supplier.get();
    }
}

class AddOperation implements Operation{
	@Override	
	public void execute (DoubleStack stack){
		double operandA;
		double operandB;
		try{
			operandA = stack.pop();
			operandB = stack.pop();

		}catch(RuntimeException e){
			throw new RuntimeException("Not enough operands for: +");	
		}			
		stack.push(operandA + operandB);
	}
}

class SubtractOperation implements Operation{
	@Override	
	public void execute (DoubleStack stack){
			
		double operandA;
		double operandB;
		try{
			operandB = stack.pop();
			operandA = stack.pop();

		}catch(RuntimeException e){
			throw new RuntimeException("Not enough operands for: +");	
		}			
		stack.push(operandA - operandB);
	}
}

class MultiplyOperation implements Operation{
	@Override	
	public void execute (DoubleStack stack){
		double operandA;
		double operandB;
		try{
			operandA = stack.pop();
			operandB = stack.pop();

		}catch(RuntimeException e){
			throw new RuntimeException("Not enough operands for: +");	
		}			
		stack.push(operandA * operandB);
			
	}
}

class DivideOperation implements Operation{
	@Override	
	public void execute (DoubleStack stack){
		double operandA;
		double operandB;
		try{
			operandB = stack.pop();
			operandA = stack.pop();

		}catch(RuntimeException e){
			throw new RuntimeException("Not enough operands for: +");	
		}			
			
		stack.push(operandA / operandB);
	}
}

class SwapOperation implements Operation{
	@Override	
	public void execute (DoubleStack stack){
		double operandA;
		double operandB;
		try{
			operandB = stack.pop();
			operandA = stack.pop();

			stack.push(operandB);
			stack.push(operandA);
		}catch(RuntimeException e){
			throw new RuntimeException("Error: not enough numbers for operation 'Swap', at least 2 numbers must be present on the stack");
		}	
	}
}

class DupOperation implements Operation{
	@Override	
	public void execute (DoubleStack stack){
		double operandA;
		try{
			operandA = stack.tos();
			stack.push(operandA);
		}catch(RuntimeException e){
			throw new RuntimeException("Error: Stack is empty");
		}	
	}
}

class RotOperation implements Operation{
	@Override	
	public void execute (DoubleStack stack){
			
		double operandA;
		double operandB;
		double operandC;
		try{
			operandC = stack.pop();
			operandB = stack.pop();
			operandA = stack.pop();

			stack.push(operandA);
			stack.push(operandC);
			stack.push(operandB);
			
		}catch(RuntimeException e){
			throw new RuntimeException("Error: not enough numbers for operation 'ROT', at least 3 numbers must be present on the stack");
		}	
	}
}

class DropOperation implements Operation{
	@Override	
	public void execute (DoubleStack stack){
			
		double operandA;
		try{
			operandA = stack.pop();
		}catch(RuntimeException e){
			throw new RuntimeException("Error: Stack is empty");
		}	
	}
}
