import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Comparator;

public class FileFreqWordsIterator implements Iterator<String>{
	
	private PriorityQueue<TreeNode> mostFrequent;
	private Hashtable<String,Integer> codeTable;
	private BufferedReader bf; 
	private String readWord;
	private int value;
	private ArrayList<String> listParts;
	private FileCharIterator iter;
	
	public Hashtable getCT(){
		return codeTable;
	}
	public ArrayList getAryList(){
		return listParts;
	}
	
	public FileFreqWordsIterator(String fileName, int n) {

		Comparator<TreeNode> comparator = new FreqComparator();
		mostFrequent = new PriorityQueue(11, comparator);
		codeTable = new Hashtable<String, Integer>();

		FileCharIterator myIter = new FileCharIterator(fileName);
		
		StringBuilder word = new StringBuilder("");
		while(myIter.hasNext()){
			String nextChar = myIter.next();
			while (!nextChar.equals("00001010") && !nextChar.equals("00100000")){
				word.append(nextChar);	
				if (!myIter.hasNext()) {
					 break;
				}
				nextChar = myIter.next();
			}
			if(codeTable.containsKey(word.toString())){
				codeTable.put(word.toString(), codeTable.get(word.toString()) + 1);
			}
			else{
				codeTable.put(word.toString(), 1);
			}
			word = new StringBuilder("");
		}	
		if (n > codeTable.keySet().size()) {
			System.err.println("Chosen too many words!!!");
			System.exit(1);
		}
		for(Map.Entry<String, Integer> entry: codeTable.entrySet()){
			if(entry.getKey().length() >= 2){
				mostFrequent.add(new TreeNode(entry.getKey(), entry.getValue()));
			}
		}
		codeTable.clear();
		while(codeTable.size() < n){
			TreeNode node = mostFrequent.poll();
			codeTable.put((String) node.myChar, (Integer) node.myFreq);
		}
		listParts = new ArrayList<String>();
		iter = new FileCharIterator(fileName);
		if (iter.hasNext()) {
			readWord = iter.next();
		}
		while (true) {
		    StringBuilder buildWord = new StringBuilder("");
			while(!readWord.equals("00001010") && !readWord.equals("00100000")){
				buildWord.append(readWord);
				if (iter.hasNext()) {
					readWord = iter.next();
				}
				else {
					break;
				}
			}
			if (buildWord.toString().isEmpty() && (readWord.equals("00001010") || readWord.equals("00100000"))) {
				listParts.add(readWord);
			}
			if (!buildWord.toString().isEmpty()) {
				if(codeTable.containsKey(buildWord.toString())){
					listParts.add(buildWord.toString());
				}
				else{
					String original = buildWord.toString();
					while(original.length() > 8){
						listParts.add(original.substring(0,8));
						original = original.substring(8);
					}
					listParts.add(original);
				}
				if((readWord.equals("00001010") || readWord.equals("00100000"))) {
					listParts.add(readWord);
				}
			}
			if (!iter.hasNext()) {
				break;
			}
			readWord = iter.next();
		}
	}
	
	@Override
	public boolean hasNext() {
		if(listParts.size() != 0){
			return true;
		}
		return false;
	}

	@Override
	public String next() {
		
		return listParts.remove(0);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException(
                "FileFreqWordsIterator does not delete from files.");
		
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

	public class FreqComparator implements Comparator<TreeNode>
	{
	    @Override
	    public int compare(TreeNode x, TreeNode y)
	    {
	        if (x.myFreq < y.myFreq)
	        {
	            return 1;
	        }
	        if (x.myFreq > y.myFreq)
	        {
	            return -1;
	        }
	        return 0;
	    }
	}

}