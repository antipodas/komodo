/* Generated By:JJTree: Do not edit this line. BranchingStatement.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.teiid.query.sql.proc;

import org.komodo.spi.query.sql.proc.BranchingStatement;
import org.teiid.query.parser.TCLanguageVisitorImpl;
import org.teiid.query.parser.TeiidClientParser;

/**
 *
 */
public class BranchingStatementImpl extends StatementImpl implements BranchingStatement<TCLanguageVisitorImpl> {

    /**
     * Modes of branching
     */
    public enum BranchingMode {
        /**
         * Teiid specific - only allowed to target loops
         */
        BREAK,
        /**
         * Teiid specific - only allowed to target loops
         */
        CONTINUE,
        /**
         * ANSI - allowed to leave any block 
         */
        LEAVE
    }
    
    private String label;

    private BranchingMode mode = BranchingMode.BREAK;

    /**
     * @param p
     * @param id
     */
    public BranchingStatementImpl(TeiidClientParser p, int id) {
        super(p, id);
    }

    /**
     * Return the type for this statement, this is one of the types
     * defined on the statement object.
     */
    @Override
    public StatementType getType() {
        switch (mode) {
        case BREAK:
            return StatementType.TYPE_BREAK;
        case CONTINUE:
            return StatementType.TYPE_CONTINUE;
        case LEAVE:
            return StatementType.TYPE_LEAVE;
        }
        throw new AssertionError();
    }

    /**
     * @return label
     */
    public String getLabel() {
        return label;
    }
    
    /**
     * @param label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return branching mode
     */
    public BranchingMode getMode() {
        return mode;
    }

    /**
     * @param mode
     */
    public void setMode(BranchingMode mode) {
        this.mode = mode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.label == null) ? 0 : this.label.hashCode());
        result = prime * result + ((this.mode == null) ? 0 : this.mode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        BranchingStatementImpl other = (BranchingStatementImpl)obj;
        if (this.label == null) {
            if (other.label != null) return false;
        } else if (!this.label.equals(other.label)) return false;
        if (this.mode != other.mode) return false;
        return true;
    }

    /** Accept the visitor. **/
    @Override
    public void acceptVisitor(TCLanguageVisitorImpl visitor) {
        visitor.visit(this);
    }

    @Override
    public BranchingStatementImpl clone() {
        BranchingStatementImpl clone = new BranchingStatementImpl(this.parser, this.id);

        if(getLabel() != null)
            clone.setLabel(getLabel());
        if(getMode() != null)
            clone.setMode(getMode());

        return clone;
    }

}
/* JavaCC - OriginalChecksum=bdaeabb3c22da3651d61cf55bef53168 (do not edit this line) */