package org.displaytag.exception;

/**
 * Exception thrown by column decorators. If a decorator need to throw a checked exception this should be nested in
 * a DecoratorException.
 * @author fgiust
 * @version $Revision$ ($Author$)
 */
public class DecoratorException extends BaseNestableJspTagException
{

    /**
     * Constructor for DecoratorException.
     * @param source Class where the exception is generated
     * @param message message
     */
    public DecoratorException(Class source, String message)
    {
        super(source, message);
    }

    /**
     * Constructor for DecoratorException.
     * @param source Class where the exception is generated
     * @param message message
     * @param cause previous exception
     */
    public DecoratorException(Class source, String message, Throwable cause)
    {
        super(source, message, cause);
    }

    /**
     * @see org.displaytag.exception.BaseNestableJspTagException#getSeverity()
     */
    public SeverityEnum getSeverity()
    {
        return SeverityEnum.ERROR;
    }

}
