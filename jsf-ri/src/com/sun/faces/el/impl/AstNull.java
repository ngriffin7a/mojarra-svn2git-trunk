/* Generated By:JJTree: Do not edit this line. AstNull.java */

package com.sun.faces.el.impl;

public class AstNull extends AbstractConstantNode
{
    public AstNull(int id)
    {
        super(id);
    }

    /** Accept the visitor. * */
    public Object jjtAccept(JsfParserVisitor visitor, Object data)
            throws javax.faces.el.EvaluationException
    {
        return visitor.visit(this, data);
    }
    
    /* (non-Javadoc)
     * @see com.sun.faces.el.impl.AbstractConstantNode#getConstantValue()
     */
    public Object getConstantValue()
    {
        return null;
    }
}