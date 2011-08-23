/*
* JBoss, Home of Professional Open Source
* Copyright 20011 Red Hat and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*
* @authors Andrew Dinn
*/
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
