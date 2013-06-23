package com.e2info.helloasm;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.commons.io.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.TraceClassVisitor;

/**
 * License: MIT http://opensource.org/licenses/MIT
 * Copyright (c) 2013 mitsuaki kuawhara <quwahara@gmail.com>
 */
public class HelloAsmApp 
{
    public static void main( String[] args ) throws IOException
    {
        String name = "HelloAsm";
        int flag = ClassWriter.COMPUTE_MAXS;
        ClassWriter cw = new ClassWriter(flag);
        cw.visit(Opcodes.V1_5,
                Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER,
                name,
                null,
                "java/lang/Object",
                null
                );

        cw.visitSource(name + ".java", null);
                
        {
            MethodVisitor mv;
            mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL,
                    "java/lang/Object",
                    "<init>",
                    "()V");
            mv.visitInsn(Opcodes.RETURN);
            // we need this call to take effect ClassWriter.COMPUTE_MAXS flag.
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        
        {
            MethodVisitor mv;
            mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
                    "main",
                    "([Ljava/lang/String;)V",
                    null,
                    null);
            mv.visitCode();
            mv.visitFieldInsn(Opcodes.GETSTATIC,
                    "java/lang/System",
                    "out",
                    "Ljava/io/PrintStream;");
            mv.visitLdcInsn("hello ASM");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    "java/io/PrintStream",
                    "println",
                    "(Ljava/lang/String;)V");
            mv.visitInsn(Opcodes.RETURN);
            // we need this call to take effect ClassWriter.COMPUTE_MAXS flag.
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        
        cw.visitEnd();
        
        // build binary
        byte[] bin = cw.toByteArray();

        // save asm trace for human readable
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            new ClassReader(bin).accept(new TraceClassVisitor(pw), 0);
            File f = new File(name + ".txt");
            FileUtils.writeStringToFile(f, sw.toString());
        }
        
        // save as calss file
        {
            File f = new File(name + ".class");
            FileUtils.writeByteArrayToFile(f, bin);
        }
        
    }
}
