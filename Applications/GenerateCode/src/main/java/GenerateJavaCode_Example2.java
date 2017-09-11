import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.TreeMap;


public class GenerateJavaCode_Example2 {

	public static TreeMap<String, String> map = new TreeMap<String, String>();

	public void readFromFile() {
		System.out.println(getClass().getClassLoader().getResourceAsStream("GenerateJavaCode.properties"));
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					getClass().getClassLoader().getResourceAsStream(
                            "GenerateJavaCode.properties")));
			String line = br.readLine();
			while (line != null) {
				String key = line.substring(0, line.indexOf('='));
				String value = line.substring(line.indexOf("=")+1);
				System.out.println("key: \t" + key + " \tvalue: \t" + value);
				map.put(key, value);
				line = br.readLine();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeToFile() {
		String className = "GeneratedClass";
		String classPackage = this.getClass().getPackage().getName();
		
		String path1 = "D:/Dropbox/Java/workspace/LexxStudySpring/src/main/java/com/lexx/generateCode/"
				+ className + ".java";

		try {
			
			String path = new File(".").getCanonicalPath();
			File fullFileName = new File(path + "/src/main/java/" + classPackage.replace(".", "/") + "/" + className + ".java" );
			
			System.out.println(fullFileName);
		
			StringBuilder fileText = new StringBuilder();
			
			PrintWriter file = new PrintWriter(fullFileName, "UTF-8");
			fileText.append( getClass().getPackage() + ";");  
			fileText.append("\npublic class " + className + " {  ");
			for (String key : map.keySet()) {
				fileText.append("\n public final static String " + key.substring(key.indexOf(".")+1) + "=\"" + key + "\";" + "\t//" + map.get(key) );
				System.out.println("public final static String " + key.substring(key.indexOf(".")+1) + "=\"" + key + "\";" + "\t//" + map.get(key) );
			}
			fileText.append("\n}");
			file.write(fileText.toString());
			file.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		GenerateJavaCode_Example2 m = new GenerateJavaCode_Example2();
		m.readFromFile();
		m.writeToFile();
		short aa=1123;
		System.out.println("aa="+String.valueOf(aa)+" aa=" + Short.toString(aa));
//		System.out.println(GeneratedClass.MESSAGE_ALL);
//		System.out.println(GeneratedClass.MESSAGE_BILLING);

	}
}
