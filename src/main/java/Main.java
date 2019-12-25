import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class Main
{
	// Khởi tạo AST của files .java
	public static ASTVisitorMod parse(char[] str)
	{
		ASTParser parser = ASTParser.newParser(AST.JLS3); 
		parser.setSource(str);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		// Kiểm tra các vấn đề khi biên dịch các Units trong các files được cung cấp.
		IProblem[] problems = cu.getProblems();		
		for (IProblem problem : problems) {
			// Bỏ qua một vài lỗi bởi các phiên bản khác nhau.
			if (problem.getID() == 1610613332) 		 // 1610613332 = Syntax error, chú thích chỉ khả dụng khi source level là 5.0
				continue;
			else if (problem.getID() == 1610613329) // 1610613329 = Syntax error, các loại tham số chỉ khả dụng khi source level là 5.0
				continue;
			else if (problem.getID() == 1610613328) // 1610613328 = Syntax error, vòng lặp 'for each' chỉ khả dụng khi source level là 5.0
                continue;
            else {
            	// Dừng biên dịch nếu
    	        System.out.println("Vấn đề biên dịch Units " + problem.getMessage() + " \t ở dòng "+problem.getSourceLineNumber() + "\t Problem ID="+ problem.getID());
    	        
    	        System.out.println("Chương trình sẽ dừng lại ngay bây giờ!");
    	        System.exit(1);
            }
	    }

		// Visit các nút (nodes) của AST được khởi tạo.
		ASTVisitorMod visitor = new ASTVisitorMod();
		cu.accept(visitor);
	    return visitor;
	}

	// Phân tích cú pháp (parse files) trong mảng char.
	public static char[] ReadFileToCharArray(String filePath) throws IOException
	{
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));		
		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return  fileData.toString().toCharArray();	
	}

	// Phân tích cú các files trong một thư mục để liệt kê các mảng char.
	public static List<char[]> ParseFilesInDir(List<String> JavaFiles) throws IOException
	{
		if (JavaFiles.isEmpty()) {
			System.out.println("Không có file mã nguồn java nào trong thư mục cung cấp");
			System.exit(0);
		}
		List<char[]> FilesRead= new ArrayList<char []>();
		for(int i = 0; i < JavaFiles.size(); i++) {
			System.out.println("Đang đọc file: "+ JavaFiles.get(i));
			FilesRead.add(ReadFileToCharArray(JavaFiles.get(i)));
		}
		return FilesRead;
	}

	// Lấy tất cả các files .java trong thư mục và thư mục con.
	public static List<String> retrieveFiles(String directory)
	{
		List<String> Files = new ArrayList<String>();
		File dir = new File(directory);
		if (!dir.isDirectory()) {
			 System.out.println("Đường dẫn cung cấp không hợp lệ");
			 System.exit(1);
		}
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				Files.addAll(retrieveFiles(file.getAbsolutePath()));
			}
			if (file.getName().endsWith((".java"))) {
				Files.add(file.getAbsolutePath());
			}
		}
		return Files;
	}

	public static void main(String[] args) throws IOException
	{
		String DirName=null;
		Scanner user_input = new Scanner( System.in );
		System.out.print("Nhập vào đường dẫn: ");
		DirName = user_input.next( );
		user_input.close();		
		System.out.println("Đường dẫn là: " + DirName);

		// Lấy tất cả các files .java trong thư mục và thư mục con.
		List<String> JavaFiles=retrieveFiles(DirName);
		// Phân tích cú các files trong một thư mục để liệt kê các mảng char.
		List<char[]> FilesRead=ParseFilesInDir(JavaFiles);

		ASTVisitorMod ASTVisitorFile;
		int DistinctOperators=0;
		int DistinctOperands=0;
		int TotalOperators=0;
		int TotalOperands=0;
		int OperatorCount=0;
		int OperandCount=0;

		// Khởi tạo AST của từng file java, visit các nút khác nhau để lấy số lượng các toán tử và toán hạng
		// Lấy số lượng các toán tử duy nhất, toán hạng duy nhất,
		// Tính tổng số toán tử, toán hạng cho từng file .java trong thư mục.
		// Tổng từng tham số từ các file khác nhau để được sử dụng trong hàm tính độ phức tạm Halstead Metrics
		for(int i = 0; i < FilesRead.size(); i++) {
			System.out.println("AST đang phân tích cú pháp cho : " + JavaFiles.get(i));
			ASTVisitorFile = parse(FilesRead.get(i));
			DistinctOperators += ASTVisitorFile.oprt.size();
			DistinctOperands += ASTVisitorFile.names.size();
			OperatorCount = 0;

			for (int f : ASTVisitorFile.oprt.values()) {				
				OperatorCount += f;
			}
			TotalOperators += OperatorCount;

			OperandCount = 0;
			for (int f : ASTVisitorFile.names.values()) {
				OperandCount += f;
			}
			TotalOperands += OperandCount;
			
			System.out.println("Số toán tử duy nhất trong file .java này là: " + ASTVisitorFile.oprt.size());
			System.out.println("Số toán hạng duy nhất trong file .java này là: " + ASTVisitorFile.names.size());
			System.out.println("Tổng số toán tử trong file .java này là: " + OperatorCount);
			System.out.println("Tổng số toán hạng trong file .java này là: " + OperandCount);
			System.out.println("\n");
		}
		
		System.out.println("\n");
		System.out.println("Tổng số toán tử duy nhất trong thư mục này là: " + DistinctOperators);
		System.out.println("Tổng số toán hạng duy nhất trong thư mục này là: " + DistinctOperands);
		System.out.println("Tổng số toán tử được sử dụng trong thư mục này là: " + TotalOperators);
		System.out.println("Tổng số toán hạng được sử dụng trong thư mục này là: " + TotalOperands);

		System.out.println("\n");
		System.out.println("###### Thông số độ phức tạp Halstead Metrics ######");
		HalsteadMetrics hal = new HalsteadMetrics();

		// Tính toán các giá trị Halstead Metrics
		hal.setParameters(DistinctOperators, DistinctOperands, TotalOperators, TotalOperands);
		hal.getVocabulary();
		hal.getProglen();
		hal.getCalcProgLen();
		hal.getVolume();
		hal.getDifficulty();
		hal.getEffort();
		hal.getTimeReqProg();
		hal.getTimeDelBugs();
	}
}