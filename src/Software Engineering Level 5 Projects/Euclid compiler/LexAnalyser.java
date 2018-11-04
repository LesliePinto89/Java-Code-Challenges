package euclid;

import java.io.IOException;
import java.util.Hashtable;

public class LexAnalyser {
	public int linenumber = 1;
	private char peek = ' ';
	private Hashtable<String, IdentToken> identifiers = new Hashtable<String, IdentToken>();
	private Hashtable<String, KeywordToken> reservedkeywords = new Hashtable<String, KeywordToken>();

	void addIdentifier(IdentToken aToken) {
		identifiers.put(aToken.identifierName, aToken);
	}

	public LexAnalyser() {
		// When a LexAnalyser object is created, populate the reservedkeywords
		// hashtable with all keywords
		reservedkeywords.put("WHILE", new KeywordToken("WHILE"));
		reservedkeywords.put("IF", new KeywordToken("IF"));
		reservedkeywords.put("THEN", new KeywordToken("THEN"));
		reservedkeywords.put("ENDIF", new KeywordToken("ENDIF"));
		reservedkeywords.put("ENDWHILE", new KeywordToken("ENDWHILE"));
		reservedkeywords.put("INT", new KeywordToken("INT"));
		reservedkeywords.put("PRINT", new KeywordToken("PRINT"));
		reservedkeywords.put("IS", new KeywordToken("IS"));
	}

	public void checkForComments() throws IOException {
		if (peek == '/') {
			peek = (char) System.in.read();

			// Check for single comment
			if (peek == '/') {
				do {
					peek = (char) System.in.read();
				} while (peek != '\r'); // allows white spaces in comments to
										// allow reach end of line
										// can be change to
										// character.isLetterOrDigit to retain
										// no white spaces, but this will
										// make the lookhead stop before
										// reaching the line end if a space is
										// encountered

				for (;; peek = (char) System.in.read()) {
					if (peek == '\r') {
						linenumber = linenumber + 1;
						peek = (char) System.in.read();
						continue;
					} else
						break;
				}
				System.out.print("Single line comment detected\n");
			}
			// check for multi comment
			else if (peek == '*') {
				do {
					peek = (char) System.in.read();
					if (peek == '*') {
						peek = (char) System.in.read();
						if (peek == '/') {
							for (;; peek = (char) System.in.read()) {
								if (peek == ' ' || peek == '/' || peek == '\t' || peek == '\r')
									continue;
								else if (peek == '\n') {
									linenumber = linenumber + 1;
								} else {
									break;
								}
							}
							System.out.print("Multi-line comment detected\n");
							break;
						} else if (peek == '\r') {
							System.out.println("Invalid comment\n");
						} else {
							continue;
						}
					}
				} while (peek != '\r'); // allows comments to have spaces in
										// them
			} else {
				System.out.println("Syntax Error");
			}
		}
	}

	public Token scan() throws IOException {
		for (;; peek = (char) System.in.read()) {
			if (peek == ' ' || peek == '\t' || peek == '\r')
				continue;
			else if (peek == '\n') {
				linenumber = linenumber + 1;
			} else
				break;
		}
		checkForComments();

		// Check for digit characters / token
		if (Character.isDigit(peek)) {
			int value = 0;
			do {
				value = 10 * value + Character.digit(peek, 10);
				peek = (char) System.in.read();
			} while (Character.isDigit(peek));

			// The value variable stores the integer value of the token
			return new NumToken(value);
		}

		// Check for letter characters / token
		else if (Character.isLetter(peek)) {
			StringBuffer astringbuffer = new StringBuffer();
			do {
				astringbuffer.append(peek);
				peek = (char) System.in.read();
			} while (Character.isLetterOrDigit(peek));

			// Store next lexeme as a string
			String storedfromstringbuffer = astringbuffer.toString();

			// Check to see if string is a keyword from hashtable
			KeywordToken word = (KeywordToken) reservedkeywords.get(storedfromstringbuffer);
			if (word != null) {
				return word;
			}

			else {
				IdentToken newIdentifier = new IdentToken(storedfromstringbuffer);
				identifiers.put(storedfromstringbuffer, new IdentToken (storedfromstringbuffer));
				return newIdentifier;
			}
		}

		switch (peek) {
		case ('$'):
			peek = ' ';
			return new Token(TokenType.END_OF_FILE);

		case (';'):
			peek = ' ';
			return new Token(TokenType.END_OF_LINE);
		case ('('):
			peek = ' ';
			return new Token(TokenType.LBRACKET);
		case (')'):
			peek = ' ';
			return new Token(TokenType.RBRACKET);

		case ('+'):
			peek = ' ';
			return new OperatorToken(OperatorType.PLUS);

		// This "–" symbol from the spec example doesn't work as program input,
		// the above type does.
		case ('-'):
			peek = ' ';
			return new OperatorToken(OperatorType.MINUS);

		case ('*'):
			peek = ' ';
			return new OperatorToken(OperatorType.MULTIPLY);

		case ('%'):
			peek = ' ';
			return new OperatorToken(OperatorType.MODULUS);

		case ('>'):
			peek = ' ';
			return new OperatorToken(OperatorType.GREATER_THAN);

		case ('<'):
			peek = ' ';
			return new OperatorToken(OperatorType.LESS_THAN);
		}

		// If we have gotten to here, we have not matched any token so print an
		// error message and return a NULL_TOKEN
		System.out.println("Syntax Error");
		return new Token(TokenType.NULL_TOKEN);
	}

	public Hashtable<String, IdentToken> getIdentifiers() {
		return identifiers;
	}
}
