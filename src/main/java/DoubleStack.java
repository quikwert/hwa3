import java.util.*;

/** Stack manipulation.
 * @since 1.8
 */
public class DoubleStack {
   private DoubleStackElement stackPointer; 



	public static void main (String[] argum) {
	   DoubleStack stack = new DoubleStack();
	   stack.push(1);
	   stack.push(2);
	   stack.push(3);
	   stack.push(4);
	   stack.push(5);
	   stack.toString();
	}

	DoubleStack() {
	   stackPointer = null;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return this; // TODO!!! Your code here!
	}

	public boolean stEmpty() {
		if(stackPointer == null) return true;
		return false; 
	}

	public void push (double a) {
		DoubleStackElement newElement;
		if(stackPointer == null){
			newElement = new DoubleStackElement(a, null);	
		}else {
			newElement = new DoubleStackElement(a, stackPointer.prevElement);
		}
		newElement.prevElement = stackPointer;
		stackPointer = newElement;
	}

	public double pop() throws EmptyStackException {
		if(stackPointer == null){
			throw new EmptyStackException();
		}
		double ret = stackPointer.value;
		stackPointer = stackPointer.prevElement;

		return ret;
	} // pop

	public void op (String s) {
	// TODO!!!
	}

	public double tos() {
		if(this.stackPointer == null)throw new EmptyStackException();
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
		strBuf.append(elToPrint.value);
		return strBuf;
	}

	public static double interpret (String pol) {
		return 0.; // TODO!!! Your code with full error handling here!
	}

	private class DoubleStackElement{
		DoubleStackElement prevElement;
		double value;
		DoubleStackElement(double value, DoubleStackElement prevElement){
			this.value = value;
			this.prevElement = prevElement;		
		}



		@Override
		public Object clone() throws CloneNotSupportedException{
			return this;	
		}

	}
}

