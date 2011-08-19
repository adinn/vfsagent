package org.jboss.vfsagent.agent;

import java.lang.instrument.Instrumentation;

/**
 * Agent main class for vfs agent
 *
 * Run the program with -javaagent:/path/to/vfsagent.jar=org.my.package,org.your.package.org.other.package
 * The agent will replace scan classes in the supplied packages and replace calls to VirtualFile.getHandler
 * with calls to VirtualFileHelper.getHandler
 */
public class Main
{
    public void premain(String args, Instrumentation inst)
            throws Exception
    {
        if (args == null || args.length()  == 0) {
            throw new Exception("agent must be provided with comma separated list of prefixes");
        }
        String[] prefixes;

        if (args.contains(",")) {
            prefixes = args.split(",");
        } else {
            prefixes = new String[] {args};
        }
        Transformer transformer = new Transformer(inst, prefixes);
        inst.addTransformer(transformer);
    }
}
