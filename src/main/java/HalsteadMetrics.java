// Class này để tính toán tất cả số liệu độ phức tạp Halstead Metrics
public class HalsteadMetrics
{
	public int DistOperators, DistOperands, TotOperators, TotOperands;	
	private int Vocabulary = 0;
	private int Proglen = 0;
	private double CalcProgLen = 0;
	private double Volume = 0;
	private double Difficulty = 0;
	private double Effort = 0;
	private double TimeReqProg = 0;
	private double TimeDelBugs = 0;

	// Khởi tạo các biến trong hàm tạo
	public HalsteadMetrics()
	{
		DistOperators = 0;
		DistOperands = 0;
		TotOperators = 0;
		TotOperands = 0;
	}

	// Thiết lập gán các params
	public void setParameters(int DistOprt, int DistOper, int TotOprt, int TotOper)
	{
		DistOperators = DistOprt;
		DistOperands = DistOper;
		TotOperators = TotOprt;
		TotOperands = TotOper;
	}

	// Tính toán từ vựng (Vocabulary) chương trình
	public int getVocabulary()
	{
		Vocabulary = DistOperators + DistOperands;
		System.out.println("Số lượng từ vựng trong chương trình là: " + Vocabulary);
		return Vocabulary;
	}

	// Tính toán độ dài (Length) chương trình
	public int getProglen()
	{
		Proglen = TotOperators + TotOperands;
		System.out.println("Độ dài chương trình là: " + Proglen);
		return Proglen;
	}

	// Tính toán độ dài chương trình ước tính được tính toán (Calculated estimated program length)
	public double getCalcProgLen()
	{
		CalcProgLen = DistOperators*(Math.log(DistOperators) / Math.log(2)) + DistOperands*(Math.log(DistOperands) / Math.log(2));
		System.out.println("Độ dài chương trình được tính toán là: " + CalcProgLen);
		return CalcProgLen;
	}

	// Tính toán độ lớn chương trình
	public double getVolume()
	{
		Volume = (TotOperators + TotOperands) * (Math.log(DistOperators + DistOperands)/Math.log(2));
		System.out.println("Độ lớn chương trình là: " + Volume);
		return Volume;
	}

	// Tính toán độ khó (Difficulty) chương trình
	public double getDifficulty()
	{
		Difficulty = (DistOperators / 2) * (TotOperands / (double)DistOperands);
		System.out.println("Độ khó chương trình là: " + Difficulty);
		return Difficulty;
	}

	// Tính toán công sức (Effort) viết chương trình
	public double getEffort()
	{
		Effort = ((DistOperators / 2) * (TotOperands / (double)DistOperands)) * ((TotOperators + TotOperands) * (Math.log(DistOperators + DistOperands) / Math.log(2)));
		System.out.println("Công sức viết chương trình là: " + Effort);
		return Effort;
	}

	// Tình toán thời gian (Time) cần thiết để viết chương trình
	public double getTimeReqProg()
	{
		TimeReqProg = (((DistOperators / 2) * (TotOperands / (double)DistOperands)) * ((TotOperators + TotOperands) * (Math.log(DistOperators + DistOperands) / Math.log(2)))) / 18;
		if (TimeReqProg > 3600) {
			System.out.println("Thời gian cần thiết để viết chương trình là: " + TimeReqProg / 3600 + " giờ");
		} else if (TimeReqProg < 3600 && TimeReqProg > 60) {
			System.out.println("Thời gian cần thiết để viết chương trình là: "+ TimeReqProg / 60 + " phút");
		} else {
			System.out.println("Thời gian cần thiết để viết chương trình là: "+ TimeReqProg + " giây");
		}
		return TimeReqProg;
	}

	// Tính toán số lượng lỗi (Delivered bugs) trong chương trình
	public double getTimeDelBugs()
	{
		TimeDelBugs = ((TotOperators + TotOperands) * (Math.log(DistOperators + DistOperands)/Math.log(2))) / 3000;
		System.out.println("Số lỗi tồn tại là: " + TimeDelBugs);
		return TimeDelBugs;
	}	
}