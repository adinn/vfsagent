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
