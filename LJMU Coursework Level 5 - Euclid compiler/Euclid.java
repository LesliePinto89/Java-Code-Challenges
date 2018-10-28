/*
 * Euclid Language Skeleton Java program
 * You may use these .java files as a start for your coursework
 * All other coding should be your own (individual coursework)
 */
package euclid;

import java.io.IOException;
import java.util.Hashtable;

public class Euclid {
    public static final int MAX_NUM_TOKENS = 3000; // You may assume there will be no more than 3000 tokens
    
    public static Token[] tokenSequence = new Token[MAX_NUM_TOKENS];
    public static int currentToken=0;
    public static Hashtable <String,IdentToken> idents;
    public static boolean lexAnalysisSuccessful = true;
    
    public static void main(String[] args) {
        LexAnalyser lex = new LexAnalyser();
        Token nexttoken = new Token(TokenType.END_OF_LINE);
        do {
            try {
            	nexttoken = lex.scan();
                tokenSequence[currentToken] = nexttoken;
                if(nexttoken.returnType() == TokenType.NULL_TOKEN)
                    lexAnalysisSuccessful = false;
               
                nexttoken.print();
                currentToken++;
            }
            catch(IOException ex) {
                System.out.println("Error in lexical analysis of program.\n");
            }
        } while(nexttoken.returnType() != TokenType.END_OF_FILE);
        
       
        // Lexical analysis complete, now on to parsing..
        System.out.println("Lexical analysis successful.\n");
        System.out.println("--- Beginning Parsing ---");
        idents = lex.getIdentifiers();
        
	// This next declaration passes the sequence of tokens and hastable of identifiers to the parser
        Parser pars = new Parser(tokenSequence, idents);
        pars.prog();
        System.out.println("--- Ending Parsing ---");
    }
}
