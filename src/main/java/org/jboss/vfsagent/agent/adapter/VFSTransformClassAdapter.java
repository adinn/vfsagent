package org.jboss.vfsagent.agent.adapter;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Class adapter to modify calls to instance method VirtualFile.getHandle to statoc method VFSHelper.getHandle
 */
public class VFSTransformClassAdapter extends ClassAdapter
{
    private static String CLASS_NAME_VIRTUAL_FILE = "Lorg/jboss/vfs/VirtualFile;";
    private static String CLASS_NAME_VIRTUAL_FILE_HANDLER = "Lorg/jboss/vfs/VirtualFileHandler;";

    private static String METHOD_NAME_GET_HANDLER = "getHandler";
    private static String METHOD_DESC_GET_HANDLER = "()" + CLASS_NAME_VIRTUAL_FILE_HANDLER ;

    private static String CLASS_NAME_VIRTUAL_FILE_HELPER= "Lorg/jboss/vfsagent/helper/VirtualFileHelper;";


    public VFSTransformClassAdapter(ClassVisitor cv)
    {
        super(cv);
    }

    public MethodVisitor visitMethod(
        final int access,
        final String name,
        final String desc,
        final String signature,
        final String[] exceptions)
    {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        return new VFSTransformMethodAdapter(mv);
    }

    public class VFSTransformMethodAdapter extends MethodAdapter
    {
        public VFSTransformMethodAdapter(MethodVisitor mv)
        {
            super(mv);
        }

        public void visitMethodInsn(int opcode, String owner, String name, String desc) {
	    if (opcode == Opcodes.INVOKEVIRTUAL &&
                owner.equals(CLASS_NAME_VIRTUAL_FILE) &&
                name.equals(METHOD_NAME_GET_HANDLER) &&
                desc.equals(METHOD_DESC_GET_HANDLER))
            {
                super.visitMethodInsn(Opcodes.INVOKESTATIC,
                                      CLASS_NAME_VIRTUAL_FILE_HELPER,
                                      name,
                                      desc);
            } else {
                super.visitMethodInsn(opcode, owner, name, desc);
            }
        }
    }
}