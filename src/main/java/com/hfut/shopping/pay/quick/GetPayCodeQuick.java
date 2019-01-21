package com.hfut.shopping.pay.quick;

import java.util.ArrayList;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import com.hfut.shopping.javaFileUtil.CharSequenceJavaFileObject;
import com.hfut.shopping.javaFileUtil.ClassFileManager;

public class GetPayCodeQuick {
	private static JavaCompiler compiler;
	
	static String begin = "package com.hfut.shopping.pay;\r\n" + 
			"\r\n" + 
			"import org.springframework.stereotype.Component;\r\n" + 
			"\r\n" + 
			"@Component\r\n" + 
			"public class PayImpl implements Pay{\r\n" + 
			"	@Override\r\n" + 
			"	public int getPay(int a, int b, int c) {\r\n" + 
			"		";

	static String end = "\r\n" + 
			"	}\r\n" + 
			"\r\n" + 
			"}";
	
	static {
		compiler = ToolProvider.getSystemJavaCompiler(); 
	}
	public static byte[] getCode(String code) {
		code=begin+code+end;
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		ClassFileManager fileManager = new ClassFileManager(compiler.getStandardFileManager(diagnostics, null, null));
		List<JavaFileObject> jfiles = new ArrayList<JavaFileObject>();
        jfiles.add(new CharSequenceJavaFileObject("com.hfut.shopping.pay.PayImpl", code));
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, jfiles);
        boolean success = task.call();
        String error = null;
        System.out.println(code);
        for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
            error = error + compilePrint(diagnostic);
        }
        if(success) {
        	return  fileManager.getJavaClassObject().getBytes();
        }
		return null;
	}
	
	private static String compilePrint(Diagnostic<?> diagnostic) {
        System.out.println("Code:" + diagnostic.getCode());
        System.out.println("Kind:" + diagnostic.getKind());
        System.out.println("Position:" + diagnostic.getPosition());
        System.out.println("Start Position:" + diagnostic.getStartPosition());
        System.out.println("End Position:" + diagnostic.getEndPosition());
        System.out.println("Source:" + diagnostic.getSource());
        System.out.println("Message:" + diagnostic.getMessage(null));
        System.out.println("LineNumber:" + diagnostic.getLineNumber());
        System.out.println("ColumnNumber:" + diagnostic.getColumnNumber());
        StringBuffer res = new StringBuffer();
        res.append("Code:[" + diagnostic.getCode() + "]\n");
        res.append("Kind:[" + diagnostic.getKind() + "]\n");
        res.append("Position:[" + diagnostic.getPosition() + "]\n");
        res.append("Start Position:[" + diagnostic.getStartPosition() + "]\n");
        res.append("End Position:[" + diagnostic.getEndPosition() + "]\n");
        res.append("Source:[" + diagnostic.getSource() + "]\n");
        res.append("Message:[" + diagnostic.getMessage(null) + "]\n");
        res.append("LineNumber:[" + diagnostic.getLineNumber() + "]\n");
        res.append("ColumnNumber:[" + diagnostic.getColumnNumber() + "]\n");
        return res.toString();
    }
}
