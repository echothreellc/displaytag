package org.displaytag.decorator;

import junit.framework.TestCase;


/**
 * Test case for AutolinkColumnDecorator.
 * @author Fabrizio Giustina
 * @version $Revision$ ($Author$)
 */
public class AutolinkColumnDecoratorTest extends TestCase
{

    /**
     * instantiate a new test.
     * @param name test name
     */
    public AutolinkColumnDecoratorTest(String name)
    {
        super(name);
    }

    /**
     * Test for [952129] column:autolink throwing exception.
     */
    public void testLongTextWithLink()
    {
        String linked = new AutolinkColumnDecorator()
            .decorate("A large string of text. Foo bar. Foo bar. Foo bar. Foo bar. "
                + "Foo bar. Foo bar. Foo bar. Foo bar. Foo bar. Foo bar. Foo bar. "
                + "Foo bar. Foo bar. Foo bar. Foo bar. Foo bar. http://foo.bar.");

        assertEquals("A large string of text. Foo bar. Foo bar. Foo bar. Foo bar. "
            + "Foo bar. Foo bar. Foo bar. Foo bar. Foo bar. Foo bar. Foo bar. "
            + "Foo bar. Foo bar. Foo bar. Foo bar. Foo bar. <a href=\"http://foo.bar.\">http://foo.bar.</a>", linked);
    }

    /**
     * Test for [952129] column:autolink throwing exception.
     */
    public void testLongTextWithEmail()
    {
        String linked = new AutolinkColumnDecorator()
            .decorate("A large string of text. Foo bar. Foo bar. Foo bar. Foo bar. "
                + "Foo bar. Foo bar. Foo bar. Foo bar. Foo bar. Foo bar. Foo bar. "
                + "Foo bar. Foo bar. Foo bar. Foo bar. Foo bar. foo@bar.com.");

        assertEquals("A large string of text. Foo bar. Foo bar. Foo bar. Foo bar. "
            + "Foo bar. Foo bar. Foo bar. Foo bar. Foo bar. Foo bar. Foo bar. "
            + "Foo bar. Foo bar. Foo bar. Foo bar. Foo bar. <a href=\"mailto:foo@bar.com.\">foo@bar.com.</a>", linked);
    }

    /**
     * Test for [952132 ] autolink garbling urls.
     */
    public void testGarbledUrl()
    {
        String linked = new AutolinkColumnDecorator().decorate("http://foo.bar cat http://stoat");

        assertEquals(
            "<a href=\"http://foo.bar\">http://foo.bar</a> cat <a href=\"http://stoat\">http://stoat</a>",
            linked);
    }

    /**
     * Test simple link.
     */
    public void testSimpleLink()
    {
        String linked = new AutolinkColumnDecorator().decorate("http://foo.bar");

        assertEquals("<a href=\"http://foo.bar\">http://foo.bar</a>", linked);
    }

    /**
     * Test simple email.
     */
    public void testSimpleEmail()
    {
        String linked = new AutolinkColumnDecorator().decorate("foo@bar.com");
        assertEquals("<a href=\"mailto:foo@bar.com\">foo@bar.com</a>", linked);
    }

    /**
     * Test simple link plus dot.
     */
    public void testSimpleLinkPlusDot()
    {
        String linked = new AutolinkColumnDecorator().decorate("http://foo.bar .");
        assertEquals("<a href=\"http://foo.bar\">http://foo.bar</a> .", linked);
    }

}