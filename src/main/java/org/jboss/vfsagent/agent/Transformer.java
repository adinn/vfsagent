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

import org.jboss.vfsagent.agent.adapter.VFSTransformClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * Agent transformer class for VFS
 */
public class Transformer implements ClassFileTransformer
{
    private Instrumentation inst;
    private String[] prefixes;

    public Transformer(Instrumentation inst, String[] prefixes)
    {
        this.inst = inst;
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        for (int i = 0; i < prefixes.length; i++) {
            if (className.startsWith(prefixes[i])) {
                return doTransform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
            }
        }
        return classfileBuffer;
    }

    public byte[] doTransform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        ClassReader reader = new ClassReader(classfileBuffer);
        ClassWriter writer = new ClassWriter(0);
        VFSTransformClassAdapter adapter = new VFSTransformClassAdapter(writer);
        reader.accept(adapter, 0);
        return writer.toByteArray();
    }
}
