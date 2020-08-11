import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

public class Menu implements ActionListener {
	JFrame frame = new JFrame("Word Counter");
	JPanel panel = new JPanel();
	JButton addFile = new JButton();
	JButton count = new JButton();
	static JLabel l;
	JTextArea resultsText;
	File[] files;
	
	ExecutorService executor;
	
	public void createUI() {
		frame.setLayout(new GridLayout(3,3));
		frame.setBounds(10,10,400,400);
		frame.setVisible(true);
		frame.add(panel);
		panel.add(addFile);
		panel.add(count);
		resultsText = new JTextArea("Word Count:",3,3);
		JScrollPane theScroller = new JScrollPane(resultsText,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addFile.setText("Add File");
		count.setText("Count Words");
		
		frame.add(theScroller);
		l = new JLabel("No File Selected");
		panel.add(l);
	
		frame.pack();
		
		addFile.addActionListener(this);
		count.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource()==addFile) {
			JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			j.setMultiSelectionEnabled(true);
			int r = j.showSaveDialog(null); 
			if (r == JFileChooser.APPROVE_OPTION) { 
				files = j.getSelectedFiles();
				String message="";
				for (int i = 0; i < files.length; i++) {
					String tempFilename=files[i].getName();	
					message+=tempFilename+": \n";
				}
					resultsText.setText(message);
					l.setText("File(s) Added");
				}
				else 
	                l.setText("the user cancelled the operation"); 
	        } 
		else if(evt.getSource()==count) {
		this.executor=Executors.newFixedThreadPool(files.length); 
		@SuppressWarnings("unchecked")
		Future<Data>[] allResults=new Future[files.length];
		for (int i = 0; i < files.length; i++) {
			File nextFile=files[i];
			allResults[i]=this.executor.submit(new ReadFile(nextFile));
		}
		this.executor.shutdown();
		try {
			executor.awaitTermination(2000,TimeUnit.MILLISECONDS);
			String message="";	
			for (int i = 0; i < allResults.length; i++) {
				Future<Data> temp=allResults[i];
				if(temp.isDone()) { 
            		Data bothDataFields = temp.get();
            		String tempFilename=bothDataFields.getFilename();
            		int tempWordCount=bothDataFields.getWordCount();
            		message+=tempFilename+": "+tempWordCount+"\n";
            	}
			}
			resultsText.setText(message);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}	
	}
	
	}
}











