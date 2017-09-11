import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.TreeMap;

public class GenerateJavaCode_Example {

	public static TreeMap<String, String> map = new TreeMap<String, String>();

	public void readFromFile() {
		System.out.println(getClass().getClassLoader().getResourceAsStream("GenerateJavaCode.properties"));
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					getClass().getClassLoader().getResourceAsStream(
                            "GenerateJavaCode.properties")));
			String line = br.readLine();
			while (line != null) {
				String value = line.substring(0, line.indexOf('='));
				String key = value.substring(value.indexOf(".")+1);
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
				fileText.append("\n public final static String " + key + "=\"" + map.get(key) + "\";" );
			}
			fileText.append("\n}");
			file.write(fileText.toString());
			file.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		GenerateJavaCode_Example m = new GenerateJavaCode_Example();
		m.readFromFile();
		m.writeToFile();
//		System.out.println(GeneratedClass.MESSAGE_ALL);
//		System.out.println(GeneratedClass.MESSAGE_BILLING);

	}
}
