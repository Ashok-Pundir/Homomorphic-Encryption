package homomorphicencryption;

import java.awt.event.*;
import java.sql.*;
import java.math.BigInteger;
import java.util.Random;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class Debit1 extends JFrame implements ActionListener 
{
	
	JPanel p1,p2;
	JTabbedPane tp;
	JButton sub1,sub2,back1,back2;
	JTextField accp,accs,amt1,amt2,acc2;
	private BigInteger lambda,n,nsquare,g;
	private String pp;
	
	public void GeneratingValues(BigInteger p, BigInteger q) 
	{
		n = p.multiply(q);
		nsquare = n.multiply(n);
		g = new BigInteger("2");
		lambda = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)).divide(
		p.subtract(BigInteger.ONE).gcd(q.subtract(BigInteger.ONE)));
	}
	
	public BigInteger Encryption(BigInteger m) 
	{
		BigInteger r = new BigInteger(512, new Random());
		//BigInteger r = new BigInteger("87051155521627615945126071128501996209711397670633062046550150379033194418167");
		return g.modPow(m, nsquare).multiply(r.modPow(n, nsquare)).mod(nsquare);
	}
	
	public BigInteger Decryption(BigInteger c) 
	{
		BigInteger u = g.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).modInverse(n);
		return c.modPow(lambda, nsquare).subtract(BigInteger.ONE).divide(n).multiply(u).mod(n);
	}

	
	Debit1()
        {
		accp=new JTextField(20);
		acc2=new JTextField(20);
		accs=new JTextField(20);
		amt1=new JTextField(20);
		amt2=new JTextField(20);
		sub1=new JButton("Submit");
		sub1.addActionListener(this);
		sub2=new JButton("Submit");
                back1=new JButton("Back");
                back2=new JButton("Back");
		sub2.addActionListener(this);
                back1.addActionListener(this);
                back2.addActionListener(this);
		
		p1=new JPanel(new GridLayout(4,2,20,20));
		p1.add(new JLabel(" Sender's Account No :"));
		p1.add(accs);
		p1.add(new JLabel(" Payee's Account No :"));
		p1.add(accp);
		p1.add(new JLabel(" Amount :"));
		p1.add(amt1);
		p1.add(back1);
		p1.add(sub1);
		
		p2=new JPanel(new GridLayout(3,2,20,20));
		p2.add(new JLabel("  Account No :"));
		p2.add(acc2);
		p2.add(new JLabel("  Amount :"));
		p2.add(amt2);
		p2.add(back2);
		p2.add(sub2);
		
		tp=new JTabbedPane();
		tp.addTab("Account Transfer", p1);
		tp.addTab("Cash", p2);
		
		setLayout(new FlowLayout());
		add(tp);
		setSize(650,400);
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		BigInteger id=null,wbal=null,fbal=null,newfbal=null,tempbal=null,newwbal=null,fmoney=null;
		int wmoney=0,fmoney2=0;
		try
		{  
			Class.forName("com.mysql.jdbc.Driver");  
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/java_project","root","root");
			Statement stmt=con.createStatement();  
			if(e.getSource()==back1)
                         {
                             Main m=new Main(new javax.swing.JFrame(), true);
                             setVisible(false);
                              m.setVisible(true);
                         } 
                        else if(e.getSource()==back2)
                         {
                             Main m=new Main(new javax.swing.JFrame(), true);
                             setVisible(false);
                             m.setVisible(true);
                         } 
                        else if(e.getSource()==sub1)
			{
				ResultSet rs=stmt.executeQuery("select pan from customer_details where Account_No="+accs.getText());
				rs.first();
				if(rs.getRow()!=1)
				{
					JOptionPane.showMessageDialog(this,"Wrong Sender's Account no.","Balance",JOptionPane.INFORMATION_MESSAGE);
					accs.setText("");
				}
				else
				{
                                                pp=rs.getString("pan");
						System.out.println(pp);
                                                rs=stmt.executeQuery("select * from keyval where pan='"+pp+"'");
                                                rs.first();
                                                GeneratingValues(new BigInteger(rs.getString(2)),new BigInteger(rs.getString(3)));
						
                                                rs=stmt.executeQuery("select pan from customer_details where Account_No="+accp.getText());
                                                rs.last();
                                                if(rs.getRow()!=1)
                                                {
                                                    JOptionPane.showMessageDialog(this,"Wrong Payee's Account no.","Balance",JOptionPane.INFORMATION_MESSAGE);
                                                    accp.setText("");
                                                }
                                                else
                                                {
						pp=rs.getString("pan");
						System.out.println(pp);
                                                
                                                rs=stmt.executeQuery("select * from balance");
						while(rs.next())
						{
						id=new BigInteger(rs.getString(1));
                                                System.out.println(Decryption(id));
						BigInteger account=new BigInteger(Decryption(id)+"");
						BigInteger tempacc=new BigInteger(accs.getText());
						if(account.compareTo(tempacc)==0)
						{
						wbal=new BigInteger(rs.getString(2));
						fbal=new BigInteger(rs.getString(3));
						break;
						}
						}
						System.out.println(Decryption(wbal)+"  "+Decryption(fbal));

						//float tempmoney=Float.parseFloat(amt1.getText())+0.00f;
                                                //String money=tempmoney+"";
                                                String money=amt1.getText();
                                                System.out.println(money);
						int strlen=money.length();
                                                System.out.println(strlen);
                                                System.out.println(money.charAt(1));
						char ch1='0';
                                                char ch2='0';
                                                if(money.charAt(strlen-3)=='.')
                                                {
                                                    ch1= money.charAt(strlen-2);
                                                    ch2= money.charAt(strlen-1);
                                                }
						else
						{
                                                    JOptionPane.showMessageDialog(this,"Wrong Format! Only two digits after the decimal.","Balance",JOptionPane.INFORMATION_MESSAGE);
                                                    System.exit(1);
                                                }
                                             
						String cha=""+ch1+ch2;
                                                System.out.println(cha);
						fmoney2=Integer.parseInt(cha);
						fmoney=new BigInteger(cha);
						float f=Float.parseFloat(money);
						wmoney=(int)f;

						if(wmoney>Integer.parseInt(Decryption(wbal)+""))
						{
						JOptionPane.showMessageDialog(this,"Insufficient Balance","Balance",JOptionPane.INFORMATION_MESSAGE);
						}
						else
						{
							newwbal=Subtract(wbal,Encryption(new BigInteger(wmoney+"")));
							if(fmoney2>Integer.parseInt(Decryption(fbal)+""))
							{
							tempbal=add(Encryption(new BigInteger("100")),fbal);
							System.out.println(Decryption(tempbal));
							newfbal=Subtract(tempbal,Encryption(fmoney));
							System.out.println(Decryption(newfbal));
							newwbal=Subtract(newwbal,Encryption(new BigInteger("1")));
							System.out.println(Decryption(newwbal));
							}
							else
							{
							newfbal=Subtract(fbal,Encryption(fmoney));
							System.out.println(Decryption(newfbal));
							}
						}
						
					
                                        	System.out.println(Decryption(newwbal)+"  "+Decryption(newfbal));
                                                
                                                BigInteger snewbal,snewfbal;
                                                snewbal=Decryption(newwbal);
                                                snewfbal=Decryption(newfbal);
                                                
                                        	String query="update balance set wbal=? where acc=?";
                                        	PreparedStatement pstmt=con.prepareStatement(query);
                                        	pstmt.setString(1, newwbal.toString());
                                                pstmt.setString(2, id.toString());
                                            	pstmt.executeUpdate();
				
                                        	query="update balance set fbal=? where acc=?";
                                        	pstmt=con.prepareStatement(query);
                                        	pstmt.setString(1, newfbal.toString());
                                        	pstmt.setString(2, id.toString());
                                                pstmt.executeUpdate();
                                        
                                        	rs=stmt.executeQuery("select * from keyval where pan='"+pp+"'");
                                        	rs.first();
                                        	GeneratingValues(new BigInteger(rs.getString(2)),new BigInteger(rs.getString(3)));
                                		rs=stmt.executeQuery("select * from balance");
                                            	while(rs.next())
                                            	{
                                        	id=new BigInteger(rs.getString(1));
                                                //System.out.println(Decryption(id)+"");
                                        	BigInteger account=new BigInteger(Decryption(id)+"");
                                        	BigInteger tempacc=new BigInteger(accp.getText());
                                        	if(account.compareTo(tempacc)==0)
                                        	{
                                        	wbal=new BigInteger(rs.getString(2));
                                        	fbal=new BigInteger(rs.getString(3));
                                        	break;
                                        	}
                                                }
                                        	newwbal=add(wbal,Encryption(new BigInteger(wmoney+"")));
                                        	newfbal=add(fbal,Encryption(fmoney));
					
                                        	query="update balance set wbal=? where acc=?";
                                            	pstmt=con.prepareStatement(query);
                                        	pstmt.setString(1, newwbal.toString());
                                        	pstmt.setString(2, id.toString());
                                        	pstmt.executeUpdate();
				
                                        	query="update balance set fbal=? where acc=?";
                                        	pstmt=con.prepareStatement(query);
                                        	pstmt.setString(1, newfbal.toString());
                                        	pstmt.setString(2, id.toString());
                                        	pstmt.executeUpdate();
					
                                        	rs=stmt.executeQuery("select t_id from value");
                                        	rs.first();
                                        	int t_id=rs.getInt(1);
					
                                        	query="update value set t_id=? where t_id=?";
                                        	pstmt=con.prepareStatement(query);
                                        	pstmt.setInt(1, (t_id+2));
                                        	pstmt.setInt(2, t_id);
                                        	pstmt.executeUpdate();
					
                                                query="insert into t_log values(?,?,?,?,?,?)";
                                                pstmt=con.prepareStatement(query);
                                                pstmt.setInt(1,(t_id));
                                                pstmt.setString(2,"Debit");
                                                pstmt.setString(3,wmoney+"."+fmoney2);
                                                pstmt.setInt(4,Integer.parseInt(accs.getText()));
                                                pstmt.setString(5,new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()));
                                                pstmt.setInt(6,0);
                                                
                                                pstmt.executeUpdate();
					
                                                query="insert into t_log values(?,?,?,?,?,?)";
                                                pstmt=con.prepareStatement(query);
                                                pstmt.setInt(1,(t_id+1));
                                                pstmt.setString(2,"Credit");
                                                pstmt.setString(3,wmoney+"."+fmoney2);
                                                pstmt.setInt(4,Integer.parseInt(accp.getText()));
                                                pstmt.setString(5,new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()));
                                                pstmt.setInt(6,0);
                                        	pstmt.executeUpdate();					
                                                
                                                 rs=stmt.executeQuery("select Email from customer_details where Account_No="+accs.getText());
                                                 String mail=""; 
                                                while(rs.next())
                                                {
                                                     mail=rs.getString(1);
                                                }
                                                SendEmail sm=new SendEmail();
                                                sm.mail(Integer.parseInt(accs.getText()),mail,"Debit",amt1.getText(),snewbal+"."+snewfbal,"");
                                                
                                                rs=stmt.executeQuery("select Email from customer_details where Account_No="+accp.getText());
                                                while(rs.next())
                                                {
                                                     mail=rs.getString(1);
                                                }
                                                
                                                sm.mail(Integer.parseInt(accp.getText()),mail,"Credit",amt1.getText(),Decryption(newwbal)+"."+Decryption(newfbal),"");
                                                JOptionPane.showMessageDialog(this,"Transaction Successfull !!","Balance",JOptionPane.INFORMATION_MESSAGE);
                                                accs.setText("");
                                                accp.setText("");
                                                amt1.setText("");
                                                }
                                                
                                                
				}	
			}
			
			else if(e.getSource()==sub2)
			{
                                        ResultSet rs=stmt.executeQuery("select pan from customer_details where Account_No="+acc2.getText());
                                        rs.last();
                                        if(rs.getRow()!=1)
                                        {
					JOptionPane.showMessageDialog(this,"Wrong Sender's Account no.","Balance",JOptionPane.INFORMATION_MESSAGE);
                            		accs.setText("");
                                    	}
                                	else
                                	{
                                        pp=rs.getString("pan");
					System.out.println(pp);
                                        rs=stmt.executeQuery("select * from keyval where pan='"+pp+"'");
                                        rs.first();
                                        GeneratingValues(new BigInteger(rs.getString(2)),new BigInteger(rs.getString(3)));
                                                
                                        rs=stmt.executeQuery("select * from balance");
					while(rs.next())
					{
					id=new BigInteger(rs.getString(1));
					BigInteger account=new BigInteger(Decryption(id)+"");
					BigInteger tempacc=new BigInteger(acc2.getText());
					if(account.compareTo(tempacc)==0)
					{
					wbal=new BigInteger(rs.getString(2));
					break;
					}
					
					}
					System.out.println(Decryption(wbal));
					int amount=0;
					String money=amt2.getText();
					try
					{
					amount=Integer.parseInt(money+"");
					if(amount>Integer.parseInt(Decryption(wbal)+""))
					{
						JOptionPane.showMessageDialog(this,"Insufficient Balance","Balance",JOptionPane.INFORMATION_MESSAGE);
						amt2.setText("");
					}
					else
					{
						newwbal=Subtract(wbal,Encryption(new BigInteger(amount+"")));
						
					
			
					System.out.println(Decryption(newwbal));
					
					String query="update balance set wbal=? where acc=?";
					PreparedStatement pstmt=con.prepareStatement(query);
					pstmt.setString(1, newwbal+"");
					pstmt.setString(2, id+"");
					pstmt.executeUpdate();
					
					rs=stmt.executeQuery("select t_id from value");
					rs.first();
					int t_id=rs.getInt(1);
					int nt=t_id+1;
					
					query="update value set t_id=? where t_id=?";
					pstmt=con.prepareStatement(query);
					pstmt.setInt(1, nt);
					pstmt.setInt(2, t_id);
					pstmt.executeUpdate();
					
					query="insert into t_log values(?,?,?,?,?,?)";
					pstmt=con.prepareStatement(query);
					pstmt.setInt(1,(t_id));
					pstmt.setString(2,"Debit");
					pstmt.setString(3,amount+"");
					pstmt.setInt(4,Integer.parseInt(acc2.getText()));
                                        pstmt.setString(5,new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()));
                                        pstmt.setInt(6,0);
					pstmt.executeUpdate();
                                        rs=stmt.executeQuery("select Email from customer_details where Account_No="+acc2.getText());
					String mail=""; 
                                                while(rs.next())
                                                {
                                                     mail=rs.getString(1);
                                                }
                                        SendEmail sm=new SendEmail();
                                        sm.mail(Integer.parseInt(acc2.getText()),mail,"Debit",amt2.getText(),Decryption(newwbal)+"","");
					
                                        JOptionPane.showMessageDialog(this,"Transaction Successfull !!","Balance",JOptionPane.INFORMATION_MESSAGE);
                                        }
                                        }
					catch(NumberFormatException nfe)
					{
					JOptionPane.showMessageDialog(this,"Wrong Amount! No floating point.","Balance",JOptionPane.INFORMATION_MESSAGE);
					amt2.setText("");
					}
                                        }
				}
                                        con.close();
		}catch(Exception ex)
		{
			JOptionPane.showMessageDialog(this,"Sorry for Inconvenience.[Database Error]","Balance",JOptionPane.INFORMATION_MESSAGE);
			System.out.println(ex);
		}
	}
	
	 public BigInteger Subtract(BigInteger m1,BigInteger m2)
	    {
	        BigInteger m2in=m2.modPow(new BigInteger("-1"),nsquare);
	        BigInteger sub=m1.multiply(m2in.mod(nsquare));
	        return sub;
	    }
	 
	 public BigInteger add(BigInteger m1,BigInteger m2)
	    {
	        BigInteger sum=m1.multiply(m2.mod(nsquare));
	        return sum;
	    }

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		//BigInteger p=new BigInteger("87051155521627615945126071128501996209711397670633062046550150379033194418167");
		//BigInteger q=new BigInteger("76883738083136012312372139006996661766766030694975628814935127558578572504397");
		//BigInteger p=new BigInteger("107378469332560560031989840066440309574379341693667637966677112802928189356871");
		//BigInteger q=new BigInteger("70174965326540477229797527662312936104796140786148709413990656891900892592927");
		
		new Debit1();
	}

}
