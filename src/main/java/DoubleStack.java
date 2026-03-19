import java.util.*;

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

	public void op (String s){
		double operandA; 
                double operandB;
		try{
			operandA = this.pop(); 
			operandB = this.pop();
		}catch (RuntimeException e){
			
			throw new RuntimeException("Too little arguments for operator: " + s);
		}	

		//if(!this.stEmpty()) throw new RuntimeException("Too many arguments for operator: " + s);	

		switch(s) {
			case "+": this.push(operandB + operandA); break;
			case "-": this.push(operandB - operandA); break;
			case "*": this.push(operandB * operandA); break;
			case "/": this.push(operandB / operandA); break;
			default: throw new RuntimeException("Unknown operator: " + s);
		}
	}

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

	public static double interpret (String pol){
		DoubleStack stack = new DoubleStack();
		String noDupSpaces = pol.replaceAll("\\s+", " ");
		String noTabs = noDupSpaces.replaceAll("\\t+", " ");
		String trim = noTabs.trim();
		String tokens[] = trim.split(" ");
		for(int i = 0; i < tokens.length; i++) {

			if(tokens[i].equals("+") || tokens[i].equals("-") || tokens[i].equals("/") || tokens[i].equals("*")){
				stack.op(tokens[i]);
			}
			else {
				try{
					stack.push(Double.parseDouble(tokens[i]));
				}catch (NumberFormatException e){
					throw new RuntimeException("Invalid format for: " + tokens[i]);
				}
			}

		}
		double ret = stack.pop();
		if(!stack.stEmpty()) throw new RuntimeException("Too many numbers");
		return ret;
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

