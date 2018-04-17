package algebraicPetriNet;

public class Substitution {

    public Symbol symbol;
    public Term term;
    private boolean isIdentity;

    public Substitution(Symbol symbol,Term term){
        this.symbol=symbol;
        this.term=term;
    }
    
    //empty substitution for non-unifiable terms
    public Substitution(){
        
    }

    public Substitution(boolean isIdentity) {
      this.isIdentity = isIdentity;
    }

    public String toString(){
        if(this.isIdentity) {
           return "<identity>";
        }
        if(this.symbol == null){
            return "none";
        }
        String s = symbol.getName();
        s = s + " -> ";
        s = s + this.term.toString();
        return s;
    }
}
