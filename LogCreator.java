package homomorphicencryption;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.sql.Connection;
import javax.swing.*;

public class LogCreator extends JFrame implements ActionListener 
{
	FileWriter fileWriter;
	JTextField acc;
	JButton sub,email;
	JTextArea log;
	
	LogCreator()
	{
		acc=new JTextField(20);
		log=new JTextArea(10,70);
		sub=new JButton("Submit");
		sub.addActionListener(this);
                email=new JButton("Email");
		email.addActionListener(this);
		email.setEnabled(false);
                
		JPanel p1=new JPanel(new GridLayout(3,2,20,20));
		p1.add(new JLabel("Account No."));
		p1.add(acc);
		p1.add(new JLabel(" "));
		p1.add(sub);
                p1.add(new JLabel(" "));
		p1.add(email);
                
		
		setLayout(new FlowLayout());
		add(p1);
		add(log);
		
		setSize(800,400);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/java_project","root","root");
			Statement stmt=con.createStatement();
			if(arg0.getSource()==sub)
                        {
                            ResultSet rs=stmt.executeQuery("select * from t_log where t_account="+acc.getText());
			fileWriter = new FileWriter(new File("C:\\Users\\Ashok Pundir\\Documents\\NetBeansProjects\\HomomorphicEncryption\\Transections.txt"));
			fileWriter.write("Transaction Details For Account:"+acc.getText());
			fileWriter.append(System.lineSeparator());
			fileWriter.append("T_id		T_type		T_ammount		T_time");
			fileWriter.append(System.lineSeparator());
			fileWriter.append("________________________________________________________________________________________");
			fileWriter.append(System.lineSeparator());
			String temp="Id	Type		Amount		Time\n";
			while(rs.next())
			{
				if(rs.getRow()==0)
					JOptionPane.showMessageDialog(this,"No Transaction For mentioned account Number","Transaction",JOptionPane.INFORMATION_MESSAGE);
				else
				{
					if(rs.getInt(6)==0)
					{
						fileWriter.append(""+rs.getInt(1));
						fileWriter.append("		"+rs.getString(2));
						fileWriter.append("		"+rs.getString(3));
						fileWriter.append("			"+rs.getTimestamp(5));
						fileWriter.append(System.lineSeparator());
						temp=temp+rs.getInt(1)+"	"+rs.getString(2)+"		"+rs.getString(3)+"		"+rs.getString(5)+"\n";
					}
				}
			}
			fileWriter.flush();
			fileWriter.close();
			log.setText(temp);
                        email.setEnabled(true);
                        }
                        else if(arg0.getSource()==email)
                        {
                            ResultSet rs=stmt.executeQuery("select Email from customer_details where Account_No="+acc.getText());
                            String mail=""; 
                            while(rs.next())
                            {
                                mail=rs.getString(1);
                            }
                            SendEmail sm=new SendEmail();
                            sm.mail(Integer.parseInt(acc.getText()), mail, "log", "0", "","C:\\Users\\Ashok Pundir\\Documents\\NetBeansProjects\\HomomorphicEncryption\\Transections.txt");
                        }
                        con.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	public static void main(String[] args) 
	{
		new LogCreator();
	}

}
