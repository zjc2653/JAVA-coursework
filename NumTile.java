import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;



public class NumTile extends JTextField {
	
	/**
	 *  NumTile is an extension of JTextField, which is a JComponent that accepts text input from the user.
	 */

	private static final long serialVersionUID = 1L;
	private static int width = 100, height = 100;
	
	/**
	 *  Sets the size, font, text alignment, opacity, and Document of the JTextField.
	 *  The Document functions as a reader for the text input, which can be set up to have certain constraints.
	 */
	
	public NumTile() {
		setPreferredSize(new Dimension(width,height));
		setFont(new Font("Verdana", Font.BOLD, 32));
		setHorizontalAlignment(JTextField.CENTER);
		setDocument(new JTextFieldLimit(1));
		setOpaque(true);
	}
	
	/**
	 * Sets the text in the number tile manually.
	 * @param n = String input for number tile.
	 */
	
	public void setNum(String n) {
		setText(n);
	}
	
	/**
	 * 
	 * @return current text in the number tile text field.
	 */
	public String getNum() {
		return getText();
	}
	
	/**
	 * 
	 * Acts as an input reader for the number tile.
	 * Restricts the number of characters that can be entered to a given limit, in this case 1.
	 *
	 */
	
	private class JTextFieldLimit extends PlainDocument {
		  /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int limit;
		
		  JTextFieldLimit(int limit) {
		    super();
		    this.limit = limit;
		  }

		  public void insertString(int offset, String str, javax.swing.text.AttributeSet attr) throws BadLocationException {
			char c = str.charAt(0);
			if (Character.isLetter(c))
				return;
		    if ((getLength() + str.length()) <= limit) {
		      super.insertString(offset, str, attr);
		    }
		  }
		}
}
