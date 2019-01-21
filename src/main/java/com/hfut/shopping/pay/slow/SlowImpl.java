package com.hfut.shopping.pay.slow;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.springframework.stereotype.Component;

@Component
public class SlowImpl {

	String begin = "package com.hfut.shopping.pay;\r\n" + 
			"\r\n" + 
			"import org.springframework.stereotype.Component;\r\n" + 
			"\r\n" + 
			"@Component\r\n" + 
			"public class PayImpl implements Pay{\r\n" + 
			"	@Override\r\n" + 
			"	public int getPay(int a, int b, int c) {\r\n" + 
			"		";

	String end = "\r\n" + 
			"	}\r\n" + 
			"\r\n" + 
			"}";

	String j1 = "D:\\mywork\\pay";
	String j2 = "\\PayImpl.java";
	String c1 = "D:\\mywork\\pay";
	String c2 = "\\PayImpl.class";

	String javaPlace;
	String classPlace;

	public byte[] getByte(String code) throws InterruptedException, IOException {
		FileInputStream fis = null;
		code = begin + code + end;
		long id = Thread.currentThread().getId();
		javaPlace = j1 + id + j2;
		classPlace = c1 + id + c2;
		File jfile = new File(javaPlace);
		File cfile = new File(classPlace);
		if (!jfile.getParentFile().exists()) {
		    jfile.getParentFile().mkdirs();
		}
		 jfile.createNewFile();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(jfile));
				ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			bw.write(code);
			bw.close();
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			compiler.run(null, null, null, javaPlace);
			fis = new FileInputStream(cfile);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = fis.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			baos.flush();
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			fis.close();
			cfile.delete();
			jfile.delete();
		}
		return null;
	}
}
