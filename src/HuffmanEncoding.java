import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
//write ERRORRSSSSS

public class HuffmanEncoding<K, V> {
	private Hashtable<String,Integer> codeTable;
	private Hashtable<String, StringBuilder> byteCodeTable;
	private TreeNode huffmanTree;
	private Hashtable<String, String> byteDeCodeTable;
	
	public static void main(String[] args){
		HuffmanEncoding huff = new HuffmanEncoding();
		if (args[0].equals("encode")) {
			huff.encode(args[1], args[2]);
		}
		if (args[0].equals("encode2")) {
			huff.encode2(args[1], args[2], Integer.parseInt(args[3]));
		}
		if (args[0].equals("decode")) {
			huff.decode(args[1], args[2]);
		}
	}
	
	public TreeNode getHuffmanTree(){
		return huffmanTree;
	}
	public Hashtable<String, StringBuilder> getByteCodeTable(){
		return this.byteCodeTable;
	}
	public Hashtable<String, Integer> getCodeTable(){
		return this.codeTable;
	}

	public void findFrequencyTable(String fileName){
		codeTable = new Hashtable<String, Integer>();
		FileCharIterator myIter = new FileCharIterator(fileName);
		codeTable.put("EOF", 1);
		while(myIter.hasNext()){
			String next = myIter.next();
			if(codeTable.containsKey(next)){
				codeTable.put(next, codeTable.get(next) + 1);
			}
			else{
				codeTable.put(next, 1);
			}
		}
		
	}
	public Hashtable<String, Integer> getFreqTable(){
		return this.codeTable;
	}
	
	public ArrayList<TreeNode> nodeSorter(){
		
		ArrayList<TreeNode> toReturn = new ArrayList<TreeNode>();
		for(String key: codeTable.keySet()){
			if(toReturn.size() == 0){
				toReturn.add(new TreeNode(key, codeTable.get(key)));
			}
			else if(toReturn.get(toReturn.size()-1).myFreq <= codeTable.get(key)){
				toReturn.add(new TreeNode(key,codeTable.get(key)));
			}
			else{
				int i = 0;
				int limit = toReturn.size();
				while(i < limit){
					if(toReturn.get(i).myFreq > codeTable.get(key)){
						toReturn.add(i, new TreeNode(key,codeTable.get(key)));
						break;
					}
					i++;
				}
			}
		}
		return toReturn;
	}
	
	public void buildTree(){
		ArrayList<TreeNode> builtTree = nodeSorter();
		while(builtTree.size() != 1){
			TreeNode a = builtTree.remove(0);
			TreeNode b = builtTree.remove(0);
			
			builtTree.add(new TreeNode(null, a.myFreq + b.myFreq, a, b));
		}
		huffmanTree = builtTree.get(0);
	}
	

	public void createByteCodeTable(TreeNode t, String progress) {
		if (t == null) {
			return;
		}
		if (t.myChar != null) {
			byteCodeTable.put(t.myChar, new StringBuilder(progress));
		}
		createByteCodeTable(t.myLeft, progress + "0");
		createByteCodeTable(t.myRight, progress + "1");
	}

	
	public StringBuilder writeCodetable(){
		StringBuilder toRtn = new StringBuilder();
		for(String item: byteCodeTable.keySet()){
			toRtn.append(item + "," + byteCodeTable.get(item).toString() + "\n");
		}
		toRtn.append("\n");
		return toRtn;
	}
	
	
	public void encode(String filename, String outputFileName){
		encodeSetUp(filename);
		try{
			FileWriter fw = new FileWriter(outputFileName, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(writeCodetable().toString());
			bw.close();
		}
		catch(FileNotFoundException e){
			System.err.print("No such file!");
			System.exit(1);
		}
		catch(IOException e){
			System.err.print("IOException while reading from file!");
			System.exit(1);
		}
		FileOutputHelper.writeBinStrToFile(encodeHelper(filename).toString(), outputFileName);
	}
	
	public void encodeSetUp(String filename){
		findFrequencyTable(filename);
		buildTree();
		byteCodeTable = new Hashtable();
		this.createByteCodeTable(huffmanTree, "");
	}
	
	public StringBuilder encodeHelper(String filename){
		StringBuilder outputStr = new StringBuilder("");
		FileCharIterator myIter = new FileCharIterator(filename);
		while(myIter.hasNext()){
			String next = myIter.next();
			String value = byteCodeTable.get(next).toString();
			outputStr.append(value);
		}
		outputStr.append(byteCodeTable.get("EOF").toString());
		int remainder = 0;
		int length = outputStr.length();
		while ((length + remainder) % 8 != 0) {
			remainder++;
		}
		while(remainder > 0){
			outputStr.append("0");
			remainder--;
		}
		outputStr.append("00001010");
		return outputStr;
	}	
	
	
	public void encode2(String filename, String outputFileName, int n) {
		encodeSetUp2(filename, n);
		try{
			FileWriter fw = new FileWriter(outputFileName);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(writeCodetable().toString());
			bw.close();
		}
		catch(FileNotFoundException e){
			System.err.print("No such file!");
			System.exit(1);
		}
		catch(IOException e){
			System.err.print("IOException while reading from file!");
			System.exit(1);
		}
		FileOutputHelper.writeBinStrToFile(encodeHelper2(filename, n).toString(), outputFileName);
	}
	
	public void encodeSetUp2(String filename, int n){
		findFrequencyTableWords(filename, n);
		buildTree();
		byteCodeTable = new Hashtable();
		this.createByteCodeTable(huffmanTree, "");
	}
	
	public void findFrequencyTableWords(String fileName, int n){
		codeTable = new Hashtable<String, Integer>();
		FileFreqWordsIterator myIter = new FileFreqWordsIterator(fileName, n);
		codeTable.put("EOF", 1);
		while(myIter.hasNext()){
			String next = myIter.next();
			if(codeTable.containsKey(next)){
				codeTable.put(next, codeTable.get(next) + 1);
			}
			else{
				codeTable.put(next, 1);
			}
		}
	}
	
	public StringBuilder encodeHelper2(String filename, int n) {
		StringBuilder outputStr = new StringBuilder("");
		FileFreqWordsIterator iter = new FileFreqWordsIterator(filename, n);
		while (iter.hasNext()) {
			String next = iter.next();
			String value = byteCodeTable.get(next).toString();
			outputStr.append(value);
		}
		outputStr.append(byteCodeTable.get("EOF").toString());
		int remainder = 0;
		int length = outputStr.length();
		while ((length + remainder) % 8 != 0) {
			remainder++;
		}
		while(remainder > 0){
			outputStr.append("0");
			remainder--;
		}
		outputStr.append("00001010");
		return outputStr;
	}
	


	public void decode (String encodedFile, String filename){
		File f = new File(encodedFile);
		byteDeCodeTable = new Hashtable<String, String>();
		try{
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String keyCode = br.readLine();
			while(!keyCode.isEmpty()){
				int index = keyCode.indexOf(",");
				String key = keyCode.substring(0, index);
				String code = keyCode.substring(index + 1);
				byteDeCodeTable.put(code, key);
				keyCode = br.readLine();
			}
			br.close();
			FileCharIterator myIter = new FileCharIterator(encodedFile);
			while (myIter.hasNext()) {
				String next = myIter.next();
				if (next.equals("00001010")) {
					String after = myIter.next();
					if (after.equals("00001010")) {
						break;
					}
				}
			}
			StringBuilder longString = new StringBuilder("");
			StringBuilder testKey = new StringBuilder("");
			StringBuilder outputStr = new StringBuilder("");
			longString.append(myIter.next());
			testKey.append(longString.charAt(0));
			longString = longString.deleteCharAt(0);
			while (true) {
				while(byteDeCodeTable.get(testKey.toString()) == null){
					if(longString.length() == 1){
						if(myIter.hasNext()){
							String val = myIter.next();
							longString.append(val);
						}
					}
					testKey.append(longString.charAt(0));
					longString = longString.deleteCharAt(0);
				}
				if (byteDeCodeTable.get(testKey.toString()).equals("EOF")) {
					break;
				}
				outputStr.append(byteDeCodeTable.get(testKey.toString()));
				testKey = new StringBuilder("");
				testKey.append(longString.charAt(0));
				longString = longString.deleteCharAt(0);
				if (longString.length() == 0) {
					if(myIter.hasNext()){
						String val = myIter.next();
						longString.append(val);
					}
				}
			}
			FileOutputHelper.writeBinStrToFile(outputStr.toString(), filename);
		}	
		catch(FileNotFoundException e){
			System.err.print("File was not found.");
			System.exit(1);
		}
		catch(IOException e){
			System.err.print("IOException while creating file");
			System.exit(1);
		}
		
	}
	
	private class TreeNode {
		public String myChar;
		public Integer myFreq;
		public TreeNode myLeft;
		public TreeNode myRight;

		public TreeNode(String item, Integer freq) {
			myChar = item;
			myFreq = freq;
			myLeft = myRight = null;
		}
		
		public TreeNode(String item, Integer freq, TreeNode left, TreeNode right) {
			myChar = item;
			myFreq = freq;
			myLeft = left;
			myRight = right;
		}
		
	}
	
}
