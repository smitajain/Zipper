import java.io.*;
import java.util.*;
import java.nio.file.*;

public class Zipper{
	private StringBuilder TOC;
	private int byteStart;
	private TreeNode myRoot;
	private ArrayList<TreeNode> nodesList;
	
	private Hashtable<String, String> directoryMap;
	private Hashtable<String, String> fileMap;
	private String compFileName;
	
	
	public Zipper(){ 
		TOC = new StringBuilder("");
		byteStart = 0;
		myRoot = null; // i initialized myRoot to be null;
		nodesList = new ArrayList<TreeNode>();
	}
	
	public static void main(String[] args){
		Zipper zipp = new Zipper();
		if (args[0].equals("zipper")){ 
			zipp.zipper(args[1], args[2]);
		}
		else if (args[0].equals("unzipper")){ 
			zipp.unzipper(args[1], args[2]);
		}
	}
	public String getTOC(){
		return TOC.toString();
	}
	//Check if it works for file
	public void zipper(String DirectoryToZip, String outputFileName){
		myRoot = new TreeNode(new File(DirectoryToZip), new StringBuilder(DirectoryToZip.toString()));
		if(myRoot.myFile.isDirectory()){
			getAllFiles(myRoot.myFile, myRoot);
		}
			traverseTree();
		makeTOC();
		try { 
			FileWriter fw = new FileWriter(outputFileName);
			BufferedWriter bw = new BufferedWriter(fw);
			System.out.println(TOC.toString());
			bw.write(TOC.toString());
			bw.write("\n");
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
		for (TreeNode node : nodesList) {
			if (node.myFile.isFile()) {
				HuffmanEncoding huff = new HuffmanEncoding();
				huff.encode(node.myFile.toString(), outputFileName);
			}
		}
		
		
	}
	public void getAllFiles(File directoryName, TreeNode t){ 
		File [] listOfFiles = directoryName.listFiles();
		for (File f : listOfFiles){
			t.addFiletoDir(directoryName, f);
		}
		for (TreeNode tree : t.myChildren){
			if (tree.myFile.isDirectory()){ 
				getAllFiles(tree.myFile, tree);
			}
		}
		// IF THIS DOESNT WORK, THE ISSUE MAY BE BECUASE I JUST PASS IN 
		// "f.toString()" <-- make sure this works correctly 
		
		//ALSO, CAN CAN FILES IN SUBDIRECTORIES HAVE THE SAME NAME AS FILES IN 
		//OUTTER DIRECTORIES? (CAUSE THEN HASHMAP WILL CAUSE TROUBLE)
	}
	
	public void traverseTree() {
		Queue<TreeNode> myQueue = new LinkedList<TreeNode>();
		myQueue.add(myRoot);
		while (!myQueue.isEmpty()) {
			TreeNode curr = myQueue.poll();
			nodesList.add(curr);
			if (curr.myChildren != null) {
				for (TreeNode child : curr.myChildren) {
					myQueue.add(child);
				}
			}
		}
		
	}
	
	public void makeTOC() {
		for (TreeNode node : nodesList) {
			if (node.myFile.isDirectory()) {
				TOC.append(node.myPathway + "," + -1);
			}
			else {
				TOC.append(node.myPathway + "," + byteStart);
				HuffmanEncoding huff = new HuffmanEncoding();
				huff.encodeSetUp(node.myFile.toString());
				StringBuilder codeMap = huff.writeCodetable();
				StringBuilder encodedFile = huff.encodeHelper(node.myFile.toString());
				byteStart = codeMap.length() + (encodedFile.length() / 8);
			}
			TOC.append("\n");
		}
	}
	
	
	

	public void unzipper(String filename, String place){
		compFileName = filename;
		unzipToMap(filename, place);
		createDirs(place);
		try {
			createTempFiles();
		} catch (IOException e) {
			System.err.print("Error in creating temporary");
			System.exit(1);
		}
	}
	
	
	 public void unzipToMap (String totalFile, String outputPlace){
		try{
			fileMap = new Hashtable<String, String>();
			directoryMap = new Hashtable<String, String>();
			File f = new File(totalFile);
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String lineOfText = br.readLine();
			System.out.println(lineOfText);
			while (!lineOfText.isEmpty()){
				String[] parts = lineOfText.split(",");
				String filePath = parts[0]; 
				String bytePlace = parts[1];
				int byteLoc = Integer.parseInt(bytePlace);
				System.out.println("byte place is:"+ bytePlace);
				if (byteLoc == -1){
					//System.out.println("HERERER");
					directoryMap.put(filePath, bytePlace);
				} else{
					fileMap.put(filePath, bytePlace);
				}
				lineOfText = br.readLine();
			}
			br.close();
			//System.out.println("HERE: " + fileMap);
		}
		catch(IOException e){
			System.err.print("IOException while creating file");
			System.exit(1);
		}
	 }
	 
	 
	 
	 public void createDirs(String output){
		 System.out.println("output file is "+ output);
		 File newDir = new File(output); 
		 //newDir.mkdir();
		 newDir.mkdirs();
		 Set<String> paths = directoryMap.keySet();
		 System.out.println("DIRECTORY: " + directoryMap);
		 for (String key : paths){
			 System.out.println("key is "+key);
			File newDir1 = new File(output.toString(), key);
			newDir1.mkdirs();
		 }
	 }
	 
	 //IF INPUT IS JUST FILE
	 
	 
	 
//	 public String getCompressedString(String key, File f){
//		 int index = Integer.parseInt(fileMap.get(key));
//		 int byteLocation = lengthOfTOC + index;
//		 int i = 0;
//		 while (i < byteLocation){
//			 if (myIter.next().equals("00001010") && myIter.next().equals("00001010")){
//				 
//			 }
//			 
//		 }
//		 return 
//	 }
		 
	 public String findKeytoVal(int index, Hashtable<String, String> map){
		 for (String key : map.keySet()){
			 if (Integer.parseInt(map.get(key)) == index){
				 return key;
			 }
		 }
		 return null;
	 }
	 
	 public void createTempFiles() throws IOException {
			ArrayList<Integer> indexes = new ArrayList<Integer>();
			for (String key : fileMap.keySet()) {
				indexes.add(Integer.parseInt(fileMap.get(key)));
			}
			System.out.println("Indices are:" + indexes);
			FileCharIterator myIter = new FileCharIterator(compFileName);
			//			 FileReader fr = new FileReader(compFileName);
			//			 BufferedReader br = new BufferedReader(fr);
			//			 String line = br.readLine();
			//			 while (!line.isEmpty()) {
			//				 line = br.readLine();
			//			 }
			while (true) {
				if (myIter.next().equals("00001010") && myIter.next().equals("00001010")) {
					break; 
				}
			}
			int i = 0;
			int ptsVisited = 0;
			//			 try {
			//				 temp.createNewFile();
			//			 }
			//			 catch (IOException e) {
			//				 System.out.println("File can't be created!");
			//				 System.exit(1);
			//			 }
			File temp = new File("temp.txt");
			while (ptsVisited < indexes.size()) {
				temp = new File("temp.txt");
				temp.createNewFile();
				
				BufferedReader br = new BufferedReader(new FileReader(temp));
				System.out.println("This is temps contents AFTER INIT \n" + br.readLine() + "\n" + br.readLine());
				
				//System.out.println("OUTPUT STR IS: " + outputStr.toString());
				StringBuilder outputStr = new StringBuilder("");
				System.out.println("Entering the while loop now");
				//				 FileWriter fw = new FileWriter(temp);
				//				 BufferedWriter bw = new BufferedWriter(fw);
				String keyOfNewFile = findKeytoVal(i, fileMap);
				System.out.println(keyOfNewFile);
				if (keyOfNewFile == null){
					System.err.println("Index is not in map ---> indexing/one off error");
				}
				i++;
				while(!indexes.contains(i)){
					//					String keyCode = br.readLine();
					//System.out.println(keyCode);
					//					while(!keyCode.isEmpty()){
					//						bw.write(keyCode);
					//						keyCode = br.readLine();
					//					}
					//					br.close();
					if(!myIter.hasNext()){
						break;
					}
					outputStr.append(myIter.next());
					i++;
				}
				
				// FIX TO YO 1 AM PROBLEM
				// like a new line or something is in between each to-be-decoded chunk that needs to be skipped for proper codemap header reading
				
				myIter.next();
				System.out.println(outputStr.substring(0, 256));
				
				br = new BufferedReader(new FileReader(temp));
				System.out.println("This is temps contents BEFORREEEE\n" + br.readLine() + "\n" + br.readLine());
				br.close();
				
				FileOutputHelper.writeBinStrToFile(outputStr.toString(), temp.toString());
				
				br = new BufferedReader(new FileReader(temp));
				System.out.println("This is temps contents\n" + br.readLine() + "\n" + br.readLine());
				br.close();
				
				System.out.println("I is: " + i);
				//				bw.close();
				//System.out.println("about to decode");
				HuffmanEncoding<String, String> huffy = new HuffmanEncoding<String, String>();
				System.out.println(temp + "arrived before destination creation");
				File f = new File(keyOfNewFile);
				f.createNewFile();
				huffy.decode(temp.toString(), f.getPath());
				ptsVisited++;
				/*try {
						PrintWriter writer = new PrintWriter(temp.toString());
						writer.close();
					}
					catch (FileNotFoundException e) {
						System.err.println("File not found!");
						System.exit(1);
					}*/
				Files.delete(temp.toPath());
			}
			
		}
	
	
	private class TreeNode {
		//probably no ERRORS here anyway 
		private File myFile;
		private StringBuilder myPathway;
		private ArrayList<TreeNode> myChildren;
		
		public TreeNode(File name, StringBuilder pathway) {
			myFile = name;
		    myPathway = pathway;
			myChildren = new ArrayList<TreeNode>();
		}
		/*public TreeNode(File name, StringBuilder pathway, ArrayList<TreeNode> children) {
			myFile = name;
			myPathway = pathway;
			myChildren = children;
//			myCodeMap = null;
//			myEncoded = null;
//			this.byteCount = myCodeMap.length() + (myEncoded.length() / 8);
		}*/

		
		
		public void addFiletoDir(File directory, File fileName) {
            if (myFile.equals(directory)) {
                TreeNode child = new TreeNode(fileName, new StringBuilder(fileName.toString()));
                myChildren.add(child);
            }
        }
		
	}
	
	
}
