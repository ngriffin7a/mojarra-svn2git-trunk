/* Generated By:JJTree: Do not edit this line. AstPlus.java */

package com.sun.faces.el.impl;

public class AstPlus extends AbstractNode
{
    public AstPlus(int id)
    {
        super(id);
    }

    /** Accept the visitor. * */
    public Object jjtAccept(JsfParserVisitor visitor, Object data)
            throws javax.faces.el.EvaluationException
    {
        return visitor.visit(this, data);
    }
}